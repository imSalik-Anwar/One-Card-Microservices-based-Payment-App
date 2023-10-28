package com.service.profile.systemDTOs;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bundle {

    CardDataTransfer cardDataTransfer;

    GuardianDataTransfer guardianDataTransfer;

    UserDataTransfer userDataTransfer;
}
