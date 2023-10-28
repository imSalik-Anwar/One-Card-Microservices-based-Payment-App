package com.service.profile.controller;

import com.service.profile.DTOs.requestDTOs.GuardianRequest;
import com.service.profile.DTOs.responseDTOs.GuardianResponse;
import com.service.profile.exceptions.InvalidRequestException;
import com.service.profile.service.Impl.RegistrationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    final RegistrationServiceImpl registrationService;
    @Autowired
    public RegistrationController(RegistrationServiceImpl registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/guardian")
    public ResponseEntity addGuardian(@RequestBody GuardianRequest request){
        try{
            GuardianResponse response = registrationService.addGuardian(request);
            return new ResponseEntity(response, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
