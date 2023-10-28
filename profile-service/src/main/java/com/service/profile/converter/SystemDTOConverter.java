package com.service.profile.converter;

import com.service.profile.profiles.Card;
import com.service.profile.profiles.Guardian;
import com.service.profile.profiles.User;
import com.service.profile.systemDTOs.CardDataTransfer;
import com.service.profile.systemDTOs.GuardianDataTransfer;
import com.service.profile.systemDTOs.UserDataTransfer;

public class SystemDTOConverter {
    public static GuardianDataTransfer fromGuardianToGuardianResponse(Guardian guardian) {
        return GuardianDataTransfer.builder()
                .id(guardian.getId())
                .email(guardian.getEmail())
                .name(guardian.getName())
                .username(guardian.getUsername())
                .contact(guardian.getContact())
                .password(guardian.getPassword())
                .build();
    }

    public static UserDataTransfer fromUserToUserResponse(User user) {
        return UserDataTransfer.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .contact(user.getContact())
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }

    public static CardDataTransfer fromCardToCardResponse(Card card) {
        return CardDataTransfer.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .cardStatus(card.getCardStatus())
                .balance(card.getBalance())
                .cardLimit(card.getCardLimit())
                .build();
    }
}
