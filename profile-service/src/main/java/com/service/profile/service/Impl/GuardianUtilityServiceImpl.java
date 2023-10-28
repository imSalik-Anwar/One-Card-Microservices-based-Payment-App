package com.service.profile.service.Impl;

import com.service.profile.DTOs.requestDTOs.AddUserRequest;
import com.service.profile.DTOs.requestDTOs.PasswordResetRequest;
import com.service.profile.converter.SystemDTOConverter;
import com.service.profile.enums.CardStatus;
import com.service.profile.exceptions.InvalidRequestException;
import com.service.profile.exceptions.UserNotFoundException;
import com.service.profile.profiles.Card;
import com.service.profile.profiles.Guardian;
import com.service.profile.profiles.User;
import com.service.profile.repository.CardRepository;
import com.service.profile.repository.GuardianRepository;
import com.service.profile.repository.UserRepository;
import com.service.profile.service.GuardianUtilityService;
import com.service.profile.systemDTOs.Bundle;
import com.service.profile.systemDTOs.CardDataTransfer;
import com.service.profile.systemDTOs.GuardianDataTransfer;
import com.service.profile.systemDTOs.UserDataTransfer;
import com.service.profile.utility.CardServiceSystemAPICalls;
import com.service.profile.utility.MailComposer;
import com.service.profile.utility.TransactionServiceSystemAPICalls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GuardianUtilityServiceImpl implements GuardianUtilityService {
    final GuardianRepository guardianRepository;
    final JavaMailSender javaMailSender;
    final UserRepository userRepository;
    final CardRepository cardRepository;
    final SecurityContextHolder securityContextHolder;
    final BCryptPasswordEncoder passwordEncoder;
    final RestTemplate restTemplate;
    @Autowired
    public GuardianUtilityServiceImpl(GuardianRepository guardianRepository, JavaMailSender javaMailSender,
                                      UserRepository userRepository, CardRepository cardRepository,
                                      SecurityContextHolder securityContextHolder, BCryptPasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.guardianRepository = guardianRepository;
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.securityContextHolder = securityContextHolder;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    @Override
    public String forgotPassword(String email) {
        Optional<Guardian> guardianOptional = Optional.ofNullable(guardianRepository.findByEmail(email));
        if(guardianOptional.isEmpty()){
            throw new InvalidRequestException("Invalid request");
        }
        Guardian guardian = guardianOptional.get();
        // generate a new password-reset-request-id and allocate it to the guardian
        String reqId = String.valueOf(UUID.randomUUID());
        guardian.setPasswordResetReqId(reqId);
        Guardian savedGuardian = guardianRepository.save(guardian);
        // email this reqId to the guardian asking them to login using email, reqId and new password
        SimpleMailMessage message = MailComposer.composeReqIdMailForForgotPasswordForGuardian(savedGuardian);
        javaMailSender.send(message);
        return "Check your email *****"+savedGuardian.getEmail().substring(savedGuardian.getEmail().length()-13)+" to complete the process";
    }

    @Override
    public String resetPassword(PasswordResetRequest request) {
        Optional<Guardian> guardianOptional = Optional.ofNullable(guardianRepository.findByEmail(request.getEmail()));
        if(guardianOptional.isEmpty()){
            throw new InvalidRequestException("No guardian found.");
        }
        Guardian guardian = guardianOptional.get();
        // check if sent reqId matches the saved reqId
        if(!request.getReqId().equals(guardian.getPasswordResetReqId())){
            throw new InvalidRequestException("Invalid request ID");
        }
        // if req ID is valid, update the password
         guardian.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // set req ID corresponding to the guardian null and save guardian
        guardian.setPasswordResetReqId(null);
        Guardian savedGuardian = guardianRepository.save(guardian);
        // send guardian to card service to align the cardDB with profileDB
        try{
            // Update DBs of other services
            CardServiceSystemAPICalls.updateGuardianPasswordInCardService(savedGuardian);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        // return message
        return "Your password was reset successfully";
    }

    @Override
    public String addChild(AddUserRequest request) throws Exception{
        // extract guardian from the authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            throw  new InvalidRequestException("Unauthorized input.");
        }
        String guardianUserName = authentication.getName();
        // get guardian
        Optional<Guardian> guardianOptional = Optional.ofNullable(guardianRepository.findByUsername(guardianUserName));
        if(guardianOptional.isEmpty()){
            throw  new InvalidRequestException("Guardian does not exist.");
        }
        Guardian guardian = guardianOptional.get();
        // same guardian can not add more than one child with same name
        List<User> users = guardian.getUsers();
        for(User user : users){
            if(user.getName().equals(request.getChildName())){
                throw new Exception("One parent can't have two children with the same name.");
            }
        }
        // create a user
        User user = User.builder()
                .name(request.getChildName())
                .contact(request.getChildContact())
                .email(request.getChildEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getChildUsername())
                .guardian(guardian)
                .build();
        // save user
        User savedUser = userRepository.save(user);
        // create a new card for the user
        Card card = Card.builder()
                .cardNumber(String.valueOf(UUID.randomUUID()))
                .balance(0)
                .cardLimit(25000)
                .cardStatus(CardStatus.ACTIVE)
                .user(savedUser)
                .build();
        // save card
        Card savedCard = cardRepository.save(card);
        // add relations
        savedUser.setCard(savedCard);
        guardian.getUsers().add(savedUser);
        Guardian savedGuardian = guardianRepository.save(guardian);
        // send entities to card service
        try{
            // save these entities in DBs of other services
            CardServiceSystemAPICalls.sendEntitiesToCardService(savedGuardian, savedUser, savedCard);
            TransactionServiceSystemAPICalls.sendEntitiesToCardService(savedGuardian, savedUser, savedCard);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return "Child added with card no: "+savedCard.getCardNumber();
    }

    @Override
    public String removeChild(String childName) {
        // extract Guardian from the authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            throw  new InvalidRequestException("Unauthorized input.");
        }
        String guardianUserName = authentication.getName();
        // get guardian
        Optional<Guardian> guardianOptional = Optional.ofNullable(guardianRepository.findByUsername(guardianUserName));
        if(guardianOptional.isEmpty()){
            throw  new InvalidRequestException("Guardian does not exist.");
        }
        Guardian guardian = guardianOptional.get();
        // check if childName exists, if yes, delete
        List<User> users = guardian.getUsers();
        User targetUser = null;
        for(User user : users){
            if(user.getName().equals(childName)){
                targetUser = user;
                break;
            }
        }
        if(targetUser == null){
            throw new UserNotFoundException("You have no child named "+childName);
        }
        // get user's name to send it along with GuardianDataTransfer
        String childUsername = targetUser.getUsername();
        users.remove(targetUser);
        userRepository.delete(targetUser);
        Guardian savedGuardian = guardianRepository.save(guardian);
        try{
            // update DBs of other services
            TransactionServiceSystemAPICalls.removeChildFromTransactionService(childUsername);
            CardServiceSystemAPICalls.removeChildFromCardService(savedGuardian, childUsername);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "The child and the corresponding card have been removed successfully";
    }
}
