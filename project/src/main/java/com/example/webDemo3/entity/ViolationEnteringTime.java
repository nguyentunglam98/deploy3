package com.example.webDemo3.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Time;

/**
 * lamnt98
 * 06/07
 */
@Entity
@Table(name = "VIOLATION_ENTERING_TIME")
@Data
public class ViolationEnteringTime {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer violationEnteringTimeId;

    @Column(name = "ROLE_ID")
    private Integer roleId;

    @Column(name = "DAY_ID")
    private Integer dayId;

    @Column(name = "START_TIME")
    private Time startTime;

    @Column(name = "END_TIME")
    private Time endTime;
}
