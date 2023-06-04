package com.soeguet.tracker.service;

import com.soeguet.tracker.repository.CustomUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final CustomUserRepository customUserRepository;

    public CustomUserDetailsService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return customUserRepository.findByUsername(username).orElseThrow();
    }
}
