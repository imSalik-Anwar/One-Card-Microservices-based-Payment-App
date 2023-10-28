package com.service.profile.configuration;

import com.service.profile.profiles.Guardian;
import com.service.profile.repository.GuardianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    GuardianRepository guardianRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Guardian guardian = guardianRepository.findByUsername(username);
        if(guardian == null){
            throw new UsernameNotFoundException("Invalid username.");
        }
        return new UserDetailCreator(guardian);
    }
}
