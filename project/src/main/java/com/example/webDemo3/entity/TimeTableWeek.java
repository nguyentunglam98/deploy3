package com.example.webDemo3.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

/**
 * lamnt98
 * 04/07
 */
@Entity
@Table(name = "TIMETABLE_WEEKS")
@Data
public class TimeTableWeek {
    @Id
    @Column(name = "TIMETABLE_WEEK_ID")
    private Integer timeTableWeekId;

    @Column(name = "FROM_DATE")
    private Date fromDate;

    @Column(name = "TO_DATE")
    private Date toDate;

    @Column(name = "YEAR_ID")
    private Integer yearID;
}
