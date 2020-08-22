package com.example.webDemo3.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "TIMETABLES")
@Data
public class TimeTable implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIMETABLE_ID")
    private Long timetableId;

    @Column(name = "TEACHER_ID")
    private Integer teacherId;

    @Column(name = "CLASS_ID")
    private Integer classId;

    @Column(name = "SLOT")
    private Integer slot;

    @Column(name = "DAY_ID")
    private Integer dayId;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "APPLY_FROM_DATE")
    private Date applyDate;

    @Column(name = "IS_AFTERNOON")
    private Integer isAfternoon;

    @Column(name = "IS_ODD_WEEK")
    private Integer isOddWeek;

    @Column(name = "IS_ADDITIONAL")
    private Integer isAdditional;

}
