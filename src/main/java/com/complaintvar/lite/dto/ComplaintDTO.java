package com.complaintvar.lite.dto;

import com.complaintvar.lite.entity.User;
import com.complaintvar.lite.entity.Company;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintDTO {
    Long id;
    String body;
    String title;
    User user;
    Company company;
}
