package com.complaintvar.lite.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyDTO {
    Long id;
    String email;
    String name;
    Boolean verified;
}
