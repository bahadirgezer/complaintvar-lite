package com.sikayetvar.user;

import com.sikayetvar.complaint.Complaint;
import com.sikayetvar.complaint.ComplaintDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
public class UserController {
    //TODO: add methods for email
    @Autowired
    private ModelMapper modelMapper;
    private UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream().map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/id")
    public ResponseEntity<UserDTO> getUserByPath(@PathVariable(name = "id") Long id) {
        User user = userService.getUserByID(id);

        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping(params = "id")
    public ResponseEntity<UserDTO> getUserByParam(@RequestParam Long id) {
        User user = userService.getUserByID(id);

        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        User userRequest = modelMapper.map(userDTO, User.class);
        User user = userService.createUser(userRequest);

        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return new ResponseEntity<UserDTO>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserByPath(@PathVariable(name = "id") Long id, @RequestBody UserDTO userDTO) {
        User userRequest = modelMapper.map(userDTO, User.class);
        User user = userService.updateComplaint(id, userRequest);

        //covert updated entity back to DTO for posting
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok().body(userResponse);
    }

    @PutMapping(params = "id")
    public ResponseEntity<UserDTO> updateUserByParam(@RequestParam Long id, @RequestBody UserDTO userDTO) {
        User userRequest = modelMapper.map(userDTO, User.class);
        User user = userService.updateComplaint(id, userRequest);

        //covert updated entity back to DTO for posting
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok().body(userResponse);
    }

    //TODO: adding response to delete might be better
    @DeleteMapping("/{id}")
    public void deleteUserByPath(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
    }

    @DeleteMapping(params = "id")
    public void deleteUserByParam(@RequestParam Long id) {
        userService.deleteUser(id);
    }
}
