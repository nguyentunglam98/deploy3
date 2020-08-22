package com.example.webDemo3.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
kimpt142 - 23/07
 */
@Entity
@Table(name = "SCHOOL_RANK_SEMESTERS")
@Data
public class SchoolRankSemester {

    @EmbeddedId
    private SchoolRankSemesterId schoolRankSemesterId;

    @Column(name = "TOTAL_RANK_MONTH")
    private Integer totalRankMonth;

    @Column(name = "TOTAL_GRADE_MONTH")
    private Double totalGradeMonth;

    @Column(name = "RANK")
    private Integer rank;
}
