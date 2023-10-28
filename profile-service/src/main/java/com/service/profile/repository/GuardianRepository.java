package com.service.profile.repository;

import com.service.profile.profiles.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Integer> {
    Optional<Guardian> findByEmailOrContact(String email, String contact);

    Guardian findByEmail(String email);

    Guardian findByUsername(String username);
}
