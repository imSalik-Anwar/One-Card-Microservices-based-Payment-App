package com.service.profile.profiles;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
@Table(name = "guardian-ps")
public class Guardian {

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
    @Column(unique = true, nullable = false)
    String contact;

    @Size(min = 3, max = 8)
    @Column(nullable = false, unique = true)
    String username;

    @Size(min = 6)
    @Column(nullable = false)
    String password;

    final String role = "ROLE_GUARDIAN";

    String passwordResetReqId;

    // Relations ============================================
    @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL)
    List<User> users = new ArrayList<>();
}
