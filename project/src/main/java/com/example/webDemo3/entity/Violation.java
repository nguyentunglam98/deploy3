package com.example.webDemo3.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * lamnt98
 * 06/07
 */
@Entity
@Table(name = "VIOLATIONS")
@Data
public class Violation {
    @Id
    @Column(name = "VIOLATION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer violationId;

    @Column(name = "TYPE_ID")
    private Integer typeId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SUBSTRACT_GRADE")
    private Float substractGrade;

    @Column(name = "STATUS")
    private Integer status;

    public Violation() {
    }

    public Violation(Integer violationId){
        this.violationId = violationId;
    }

}
