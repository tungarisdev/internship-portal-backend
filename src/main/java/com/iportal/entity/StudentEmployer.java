package com.iportal.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_employer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEmployer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    // Để truy vấn FullName, Major, Email, CV Link
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Column(columnDefinition = "TEXT")
    private String title;
    
    private String cvLink;
    
    private String reply;
    
    // Chỉ lưu id
    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;
    
    private String status;
    
    private LocalDateTime timeUpdate;
}
