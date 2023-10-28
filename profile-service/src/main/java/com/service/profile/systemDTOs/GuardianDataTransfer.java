package com.service.profile.systemDTOs;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuardianDataTransfer {
    int id;

    String name;

    String email;

    String contact;

    String username;

    String password;
}
