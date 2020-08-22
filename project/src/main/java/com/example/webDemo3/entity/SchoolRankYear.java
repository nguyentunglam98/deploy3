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
@Table(name = "SCHOOL_RANK_YEARS")
@Data
public class SchoolRankYear {

    @EmbeddedId
    private SchoolRankYearId schoolRankYearId;

    @Column(name = "TOTAL_RANK_SEMESTER")
    private Integer totalRankSemester;

    @Column(name = "TOTAL_GRADE_SEMESTER")
    private Double totalGradeSemester;

    @Column(name = "RANK")
    private Integer rank;
}
