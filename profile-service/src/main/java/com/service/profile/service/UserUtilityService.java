package com.service.profile.service;

import com.service.profile.DTOs.requestDTOs.PasswordResetRequest;

public interface UserUtilityService {
    String forgotPassword(String email);

    String resetPassword(PasswordResetRequest request);
}
