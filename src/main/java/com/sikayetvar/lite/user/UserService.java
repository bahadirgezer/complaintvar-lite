package com.sikayetvar.lite.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByID(Long id) {
        Optional<User> userByID = userRepository.findUserByID(id);

        if (!userByID.isPresent()) {
            throw new IllegalArgumentException();
        }
        return userByID.get();
    }

    public User createUser(User userRequest) {
        //TODO: add exception logic
        return userRepository.save(userRequest);
    }

    public User updateComplaint(Long id, User userRequest) {
        User user = userRepository.findUserByID(id)
                .orElseThrow(() -> new IllegalArgumentException());

        user.setEmail(userRequest.getEmail()); //TODO: add logic
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        //TODO: update password
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findUserByID(id)
                .orElseThrow(() -> new IllegalArgumentException());
        userRepository.delete(user);
    }
}
