package com.service.profile.DTOs.requestDTOs;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddUserRequest {

    String childName;

    String childEmail;

    String childContact;

    String childUsername;

    String password;
}
