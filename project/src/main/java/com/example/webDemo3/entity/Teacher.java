package com.example.webDemo3.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * lamnt98 - 28/06
 */
@Entity
@Data
@Table(name = "TEACHERS")
public class Teacher {
    @Id
    @Column(name = "TEACHER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teacherId;

    @Column(name = "TEACHER_IDENTIFIER")
    private String teacherIdentifier;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "STATUS")
    private Integer status;

}
