package com.example.webDemo3.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

/**
 * lamnt98
 * 01/07
 */
@Entity
@Table(name = "SCHOOL_YEARS")
@Data
public class SchoolYear {

    @Id
    @Column(name = "YEAR_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer yearID;

    @Column(name = "FROM_DATE")
    private Date fromDate;

    @Column(name = "TO_DATE")
    private Date toDate;

    @Column(name = "FROM_YEAR")
    private Integer fromYear;

    @Column(name = "TO_YEAR")
    private Integer toYear;

    @Column(name = "RANK_HISTORY")
    private String history;

    @Column(name = "RANK_CREATE_DATE")
    private Date createDate;

    public SchoolYear() {
    }

    public SchoolYear(Integer yearID) {
        this.yearID = yearID;
    }
}
