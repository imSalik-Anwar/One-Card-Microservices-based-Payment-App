package com.service.profile.DTOs.responseDTOs;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuardianResponse {

    String name;

    String email;

    String contact;

    String password;

    String username;

    int children;
}
