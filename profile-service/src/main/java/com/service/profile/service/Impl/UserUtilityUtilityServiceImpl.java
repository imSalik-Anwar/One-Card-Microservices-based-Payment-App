package com.service.profile.service.Impl;

import com.service.profile.DTOs.requestDTOs.PasswordResetRequest;
import com.service.profile.converter.SystemDTOConverter;
import com.service.profile.exceptions.InvalidRequestException;
import com.service.profile.exceptions.UserNotFoundException;
import com.service.profile.profiles.User;
import com.service.profile.repository.UserRepository;
import com.service.profile.service.UserUtilityService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserUtilityUtilityServiceImpl implements UserUtilityService {
    final UserRepository userRepository;
    final JavaMailSender javaMailSender;
    final BCryptPasswordEncoder passwordEncoder;
    final RestTemplate restTemplate;
    @Autowired
    public UserUtilityUtilityServiceImpl(UserRepository userRepository,
                                         JavaMailSender javaMailSender, BCryptPasswordEncoder passwordEncoder,
                                         RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    @Override
    public String forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException("User does not exists");
        }
        User user = userOptional.get();
        // generate a unique password-reset-req-ID and assign to the user
        String passwordResetReqID = String.valueOf(UUID.randomUUID());
        user.setPasswordResetReqId(passwordResetReqID);
        User savedUser = userRepository.save(user);
        // email this reqId to the user asking them to login using email, reqId and new password
        SimpleMailMessage message = MailComposer.composeReqIdMailForForgotPasswordForUser(savedUser);
        javaMailSender.send(message);
        return "Check your email *****"+savedUser.getEmail().substring(savedUser.getEmail().length()-13)+" to complete the process";
    }

    @Override
    public String resetPassword(PasswordResetRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if(userOptional.isEmpty()){
            throw new UserNotFoundException("User does not exists");
        }
        User user = userOptional.get();
        // check if sent reqId matches the saved reqId
        if(!user.getPasswordResetReqId().equals(request.getReqId())){
            throw new InvalidRequestException("Invalid request ID");
        }
        // if req ID is valid, update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // set req ID corresponding to the guardian null and save guardian
        user.setPasswordResetReqId(null);
        User savedUser = userRepository.save(user);
        try{
            // update password in DBs of other services
            TransactionServiceSystemAPICalls
                    .updateUserPasswordInTransactionService(savedUser);
            CardServiceSystemAPICalls.updateUserPasswordInCardService(savedUser);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        // return message
        return "Your password was reset successfully";
    }
}
