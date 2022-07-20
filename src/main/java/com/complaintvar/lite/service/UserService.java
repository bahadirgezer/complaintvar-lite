package com.complaintvar.lite.service;

import com.complaintvar.lite.dto.UserDTO;
import com.complaintvar.lite.entity.User;
import com.complaintvar.lite.exceptions.DuplicateEntityError;
import com.complaintvar.lite.exceptions.IllegalResourceFormatException;
import com.complaintvar.lite.exceptions.ResourceNotFoundException;
import com.complaintvar.lite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    //TODO: Paginated get all and get some with sorting capabilities

    public UserDTO getUserByID(Long id) {
        log.info("Getting user by ID.");
        User user;

        try {
            user = userRepository.findUserByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }
        log.debug(String.format("Fetched user with ID: %d", id));

        if (user == null) {
            log.debug(String.format("User with ID: %d not found.", id));
            throw new ResourceNotFoundException(String.format("User Not Found. ID: %d", id));
        }

        log.debug(String.format("Returning user DTO with ID: %d", id));
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating user.");
        log.debug("Converting user DTO to user entity.");
        User user = modelMapper.map(userDTO, User.class);

        if (user == null) {
            log.debug("User creation failed. Null entity object.");
            throw new IllegalResourceFormatException("User entity does not exit. Null value.");
        }

        User emailCheck;
        try {
            emailCheck = userRepository.findUserByEmail(user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }
        log.debug("Fetched user");

        if (emailCheck != null) {
            log.debug("Email in use by another user.");
            throw new DuplicateEntityError(String.format("Email \"%s\" already in use. Emails must be unique,", emailCheck.getEmail()));
        }
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        log.debug(String.format("Returning user DTO with ID: %d", savedUser.getId()));
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user");
        log.debug("Converting user DTO to user entity.");
        User newUser = modelMapper.map(userDTO, User.class);
        User user;

        try {
            user = userRepository.findUserByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        if (user == null) {
            log.debug("User update failed. Null entity object in the repository.");
            throw new IllegalResourceFormatException("User entity does not exit. Null value.");
        }

        if (newUser == null) {
            log.debug("User update failed. Null entity object from the parameter.");
            throw new IllegalResourceFormatException("User entity does not exit. Null value.");
        }


        if (newUser.getEmail() != user.getEmail()) { //if the email is updated, check uniqueness.
            log.debug("Checking updated user email.");
            User checkEmail;
            try {
                checkEmail = userRepository.findUserByEmail(newUser.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
                log.debug("Database query exception caught.");
                return null;
            }

            if (checkEmail != null) {
                log.debug("User update failed. Email in use.");
                throw new DuplicateEntityError("Email already in use, cannot update user.");
            }
        }
        user.setEmail(newUser.getEmail());

        if (newUser.getFirstName().isBlank()) { //Whitespaces count as empty.
            log.debug("User name is blank. Illegal format");
            throw new IllegalResourceFormatException("User name cannot be blank.");
        }
        user.setFirstName(newUser.getFirstName());

        if (newUser.getLastName().isBlank()) { //Whitespaces count as empty.
            log.debug("User name is blank. Illegal format");
            throw new IllegalResourceFormatException("User name cannot be blank.");
        }
        user.setLastName(newUser.getLastName());

        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return null;
        }

        log.debug(String.format("Returning user DTO with ID: %d", savedUser.getId()));
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public Boolean deleteUser(Long id) {
        log.info("Deleting user");
        User user;
        log.debug("Fetching user to delete.");
        try {
            user = userRepository.findUserByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return false;
        }

        if (user == null) {
            log.debug("User delete failed. Null entity object.");
            throw new ResourceNotFoundException("User entity does not exit. Null value.");
        }

        log.debug(String.format("Deleting user with ID: %d", user.getId()));
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Database query exception caught.");
            return false;
        }

        return true;
    }
}
