package com.service.profile.profiles;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user-ps")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    @Size(min = 2)
    String name;

    @Column(nullable = false, unique = true)
    @Email
    String email;

    @Size(min = 10, max = 10)
    @Column(nullable = false, unique = true)
    String contact;

    @Size(min = 6)
    @Column(nullable = false)
    String password;

    @Column(nullable = false, unique = true)
    String username;

    final String role = "ROLE_USER";

    String passwordResetReqId;

    // Relations ===========================================
    @ManyToOne
    @JoinColumn(name = "guardian_id")
    Guardian guardian;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Card card;
}
