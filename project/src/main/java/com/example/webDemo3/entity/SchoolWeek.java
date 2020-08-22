package com.example.webDemo3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

/**
 * lamnt98
 * 01/07
 */
@Entity
@Table(name = "SCHOOL_WEEKS")
@Data
public class SchoolWeek {

    @Id
    @Column(name = "WEEK_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer weekID;

    @Column(name = "Month_ID")
    private Integer monthID;

    @Column(name = "WEEK")
    private Integer week;

    @Column(name = "YEAR_ID")
    private Integer yearId;

    @Column(name = "RANK_HISTORY")
    private String history;

    @Column(name = "RANK_CREATE_DATE")
    private Date createDate;
}
