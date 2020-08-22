package com.example.webDemo3.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "SCHOOL_RANK_WEEKS")
@Data
public class SchoolRankWeek {

    @EmbeddedId
    private SchoolRankWeekId schoolRankWeekId;

    @Column(name = "EMULATION_GRADE")
    private Double emulationGrade;

    @Column(name = "LEARNING_GRADE")
    private Double learningGrade;

    @Column(name = "MOVEMENT_GRADE")
    private Double movementGrade;

    @Column(name = "LABOR_GRADE")
    private Double laborGrade;

    @Column(name = "TOTAL_GRADE")
    private Double totalGrade;

    @Column(name = "RANK")
    private Integer rank;
}
