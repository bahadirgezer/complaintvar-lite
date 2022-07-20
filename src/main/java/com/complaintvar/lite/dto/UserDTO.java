package com.complaintvar.lite.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    Long id;
    String email;
    String firstName;
    String lastName;
    String password;
}

