package com.service.profile.converter;

import com.service.profile.DTOs.requestDTOs.GuardianRequest;
import com.service.profile.DTOs.responseDTOs.GuardianResponse;
import com.service.profile.profiles.Guardian;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

public class GuardianConverter {
    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static Guardian fromGuardianRequestToGuardian(GuardianRequest request){
        return Guardian.builder()
                .name(request.getName())
                .email(request.getEmail())
                .contact(request.getContact())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .users(new ArrayList<>())
                .build();
    }

    public static GuardianResponse fromGuardianToGuardianResponse(Guardian guardian){
        return GuardianResponse.builder()
                .name(guardian.getName())
                .email(guardian.getEmail())
                .contact("*******"+guardian.getContact().substring(guardian.getContact().length()-3))
                .password("********")
                .username(guardian.getUsername())
                .children(guardian.getUsers().size())
                .build();
    }
}
