package com.service.profile.service;

import com.service.profile.DTOs.requestDTOs.AddUserRequest;
import com.service.profile.DTOs.requestDTOs.PasswordResetRequest;

public interface GuardianUtilityService {
    String forgotPassword(String email);

    String resetPassword(PasswordResetRequest request);

    String addChild(AddUserRequest request) throws Exception;

    String removeChild(String childName);
}
