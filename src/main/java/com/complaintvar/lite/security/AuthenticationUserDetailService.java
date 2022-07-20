package com.complaintvar.lite.security;

import com.complaintvar.lite.entity.User;
import com.complaintvar.lite.exceptions.ResourceNotFoundException;
import com.complaintvar.lite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                //Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                Collections.emptyList());
    }
}