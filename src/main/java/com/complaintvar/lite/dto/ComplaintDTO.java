package com.complaintvar.lite.dto;

import com.complaintvar.lite.entity.User;
import com.complaintvar.lite.entity.Company;
import lombok.Data;

@Data
public class ComplaintDTO {
    private Long id;
    private String body;
    private String title;
    private User user;
    private Company company;
}
