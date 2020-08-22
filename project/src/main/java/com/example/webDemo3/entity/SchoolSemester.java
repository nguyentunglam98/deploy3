package com.example.webDemo3.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

/*
kimpt142 - 23/07
 */
@Entity
@Table(name = "SCHOOL_SEMESTERS")
@Data
public class SchoolSemester {

    @Id
    @Column(name = "SEMESTER_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer semesterId;

    @Column(name = "YEAR_ID")
    private Integer yearId;

    @Column(name = "SEMESTER")
    private Integer semester;

    @Column(name = "RANK_HISTORY")
    private String history;

    @Column(name = "IS_RANKED")
    private Integer isRanked;

    @Column(name = "RANK_CREATE_DATE")
    private Date createDate;
}
