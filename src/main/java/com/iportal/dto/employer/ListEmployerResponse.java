package com.iportal.dto.employer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListEmployerResponse {
	
	private String username;
	
	private String companyName;

    private String address;
    
    private String email;
    
    private String phoneNumber;

    private String website;
    
    private String status;
}
