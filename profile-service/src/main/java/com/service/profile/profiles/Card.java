package com.service.profile.profiles;

import com.service.profile.enums.CardStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "card-ps")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String cardNumber; // 10 digit card number unique to every card

    float balance;

    float cardLimit;

    @Enumerated(EnumType.STRING)
    CardStatus cardStatus;

    // Relations ==================================================
    @OneToOne
    @JoinColumn
    User user;
}
