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
@Table(name = "SCHOOL_RANK_MONTHS")
@Data
public class SchoolRankMonth {

    @EmbeddedId
    private SchoolRankMonthId schoolRankMonthId;

    @Column(name = "TOTAL_RANK_WEEK")
    private Integer totalRankWeek;

    @Column(name = "TOTAL_GRADE_WEEK")
    private Double totalGradeWeek;

    @Column(name = "RANK")
    private Integer rank;
}
