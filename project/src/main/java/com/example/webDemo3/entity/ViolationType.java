package com.example.webDemo3.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * lamnt98
 * 06/07
 */
@Entity
@Table(name = "VIOLATION_TYPE")
@Data
public class ViolationType {
    @Id
    @Column(name = "TYPE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TOTAL_GRADE")
    private Float totalGrade;

    @Column(name = "STATUS")
    private Integer status;

}
