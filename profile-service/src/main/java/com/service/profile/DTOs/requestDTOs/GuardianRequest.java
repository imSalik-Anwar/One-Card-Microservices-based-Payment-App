package com.service.profile.DTOs.requestDTOs;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuardianRequest {

    String name;

    String email;

    String contact;

    String username;

    String password;
}
