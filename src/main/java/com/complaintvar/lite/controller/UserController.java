package com.complaintvar.lite.controller;

import com.complaintvar.lite.dto.UserDTO;
import com.complaintvar.lite.exceptions.ResourceNotFoundException;
import com.complaintvar.lite.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    //TODO: add methods for email
    private final UserService userService;
    
    //TODO: Pagination
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserByPath(@PathVariable Long id) {
        return getUser(id);
    }

    @GetMapping(params = "id")
    public ResponseEntity<UserDTO> getUserByParam(@RequestParam Long id) {
        return getUser(id);
    }

    private ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info(String.format("Getting user with id: %d", id));
        UserDTO userDTO = userService.getUserByID(id);
        if (userDTO == null) {
            log.debug("UserDTO object is null.");
            throw new ResourceNotFoundException("Cannot get user. Null return.");
        }
        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO newUserDTO) {
        log.info("Creating new user");
        UserDTO userDTO = userService.createUser(newUserDTO);
        if (userDTO == null) {
            log.debug("UserDTO object is null.");
            throw new ResourceNotFoundException("Cannot get user. Null return.");
        }
        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserByPath(@PathVariable(name = "id") Long id, @RequestBody UserDTO newUserDTO) {
        return updateUser(id, newUserDTO);
    }


    @PutMapping(params = "id")
    public ResponseEntity<UserDTO> updateUserByParam(@RequestParam Long id, @RequestBody UserDTO newUserDTO) {
        return updateUser(id, newUserDTO);
    }

    private ResponseEntity<UserDTO> updateUser(@PathVariable(name = "id") Long id, @RequestBody UserDTO newUserDTO) {
        log.info(String.format("Updating user with id: %d"), id);
        UserDTO userDTO = userService.updateUser(id, newUserDTO);
        if (userDTO == null) {
            log.debug("UserDTO object is null.");
            throw new ResourceNotFoundException("Cannot get user. Null return.");
        }
        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteUserByPath(@PathVariable(name = "id") Long id) {
        return userService.deleteUser(id) ? HttpStatus.ACCEPTED : HttpStatus.NOT_ACCEPTABLE;
    }

    @DeleteMapping(params = "id")
    public HttpStatus deleteUserByParam(@RequestParam Long id) {
        return userService.deleteUser(id) ? HttpStatus.ACCEPTED : HttpStatus.NOT_ACCEPTABLE;
    }
}