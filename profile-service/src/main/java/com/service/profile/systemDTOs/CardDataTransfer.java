package com.service.profile.systemDTOs;

import com.service.profile.enums.CardStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDataTransfer {
    int id;

    String cardNumber; // 10 digit card number unique to every card

    float balance;

    float cardLimit;

    CardStatus cardStatus;
}
