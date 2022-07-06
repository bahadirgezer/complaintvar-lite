package com.sikayetvar.lite.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
