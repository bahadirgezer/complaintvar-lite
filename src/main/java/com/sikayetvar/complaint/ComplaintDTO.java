package com.sikayetvar.complaint;

import com.sikayetvar.company.Company;
import com.sikayetvar.user.User;
import lombok.Data;

@Data
public class ComplaintDTO {
    private Long id;
    private String body;
    private String title;
    private User user;
    private Company company;
}
