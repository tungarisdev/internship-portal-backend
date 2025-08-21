package com.iportal.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recruitments")
public class Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    private Double salaryRange;

    private String address;

    private LocalDate expired;

    private LocalDate postedDate;

    private String status;
    
    private boolean isPosted;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;
    
    private String recruitmentStatus;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User idUserVerify;
    
    @Column(columnDefinition = "TEXT")
    private String draftContentJson;
}
