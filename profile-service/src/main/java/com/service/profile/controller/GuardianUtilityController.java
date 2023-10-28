package com.service.profile.controller;

import com.service.profile.DTOs.requestDTOs.AddUserRequest;
import com.service.profile.DTOs.requestDTOs.PasswordResetRequest;
import com.service.profile.service.Impl.GuardianUtilityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guardian")
public class GuardianUtilityController {
    final GuardianUtilityServiceImpl guardianUtilityService;
    @Autowired
    public GuardianUtilityController(GuardianUtilityServiceImpl guardianUtilityService) {
        this.guardianUtilityService = guardianUtilityService;
    }

    // forgot password API
    @PutMapping("/forgot-password/email/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable("email") String email){
        try{
            String response = guardianUtilityService.forgotPassword(email);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // reset password
    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request){
        try{
            String response = guardianUtilityService.resetPassword(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // add child API
    @PostMapping("/add-child")
    public ResponseEntity<String> addChild(@RequestBody AddUserRequest request){
        try{
            String response = guardianUtilityService.addChild(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // delete child
    @DeleteMapping("/remove-child")
    public ResponseEntity<String> removeChild(@RequestParam("childName") String childName){
        try{
            String response = guardianUtilityService.removeChild(childName);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // get details of all the children with their cards
}
