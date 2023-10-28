package com.service.profile.service;

import com.service.profile.DTOs.requestDTOs.GuardianRequest;
import com.service.profile.DTOs.responseDTOs.GuardianResponse;

public interface RegistrationService {
    GuardianResponse addGuardian(GuardianRequest request);
}
