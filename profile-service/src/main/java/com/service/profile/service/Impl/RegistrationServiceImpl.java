package com.service.profile.service.Impl;

import com.service.profile.DTOs.requestDTOs.GuardianRequest;
import com.service.profile.DTOs.responseDTOs.GuardianResponse;
import com.service.profile.converter.GuardianConverter;
import com.service.profile.exceptions.InvalidRequestException;
import com.service.profile.profiles.Guardian;
import com.service.profile.repository.GuardianRepository;
import com.service.profile.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    final GuardianRepository guardianRepository;
    @Autowired
    public RegistrationServiceImpl(GuardianRepository guardianRepository) {
        this.guardianRepository = guardianRepository;
    }

    @Override
    public GuardianResponse addGuardian(GuardianRequest request) throws RuntimeException{
        // validate request parameters
        Optional<Guardian> guardianOptional = guardianRepository.findByEmailOrContact(request.getEmail(), request.getContact());
        if(guardianOptional.isPresent()){
            throw new InvalidRequestException("Invalid input");
        }
        // create a new guardian
        Guardian guardian = GuardianConverter.fromGuardianRequestToGuardian(request);
        // save to guardian table
        Guardian savedGuardian = guardianRepository.save(guardian);
        // prepare response
        return GuardianConverter.fromGuardianToGuardianResponse(savedGuardian);
    }
}
