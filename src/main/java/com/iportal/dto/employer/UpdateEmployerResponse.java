package com.iportal.dto.employer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployerResponse {
    
    private String companyName;

    private String address;

    private String email;
    
    private String phoneNumber;

    private String website;
}
