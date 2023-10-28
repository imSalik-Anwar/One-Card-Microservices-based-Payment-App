package com.service.profile.controller;

import com.service.profile.DTOs.requestDTOs.PasswordResetRequest;
import com.service.profile.service.Impl.UserUtilityUtilityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserUtilityController {
    final UserUtilityUtilityServiceImpl userUtilityService;
    @Autowired
    public UserUtilityController(UserUtilityUtilityServiceImpl userUtilityService) {
        this.userUtilityService = userUtilityService;
    }
    // reset password request
    @PutMapping("/forgot-password/email/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable("email") String email){
        try{
            String response = userUtilityService.forgotPassword(email);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // reset password
    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request){
        try{
            String response = userUtilityService.resetPassword(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
