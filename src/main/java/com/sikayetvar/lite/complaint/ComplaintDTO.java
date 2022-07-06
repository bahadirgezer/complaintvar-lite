package com.sikayetvar.lite.complaint;

import com.sikayetvar.lite.company.Company;
import com.sikayetvar.lite.user.User;
import lombok.Data;

@Data
public class ComplaintDTO {
    private Long id;
    private String body;
    private String title;
    private User user;
    private Company company;
}
