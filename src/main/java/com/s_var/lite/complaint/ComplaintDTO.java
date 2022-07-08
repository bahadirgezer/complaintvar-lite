package com.s_var.lite.complaint;

import com.s_var.lite.user.User;
import com.s_var.lite.company.Company;
import lombok.Data;

@Data
public class ComplaintDTO {
    private Long id;
    private String body;
    private String title;
    private User user;
    private Company company;
}
