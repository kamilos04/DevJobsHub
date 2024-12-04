package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String stringId) throws UsernameNotFoundException{
        Long id = Long.parseLong(stringId);
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){throw new UsernameNotFoundException("User not found");}
        User user = userOptional.get();
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(Long.toString(user.getId()))
                .password(user.getPassword())
                .build();
        return userDetails;
    }
}
