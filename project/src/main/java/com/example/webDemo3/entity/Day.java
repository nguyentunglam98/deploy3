package com.example.webDemo3.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * lamnt98 - 06/07
 */
@Entity
@Data
@Table(name = "DAY")
public class Day {
    @Id
    @Column(name = "DAY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dayId;

    @Column(name = "DAY_NAME")
    private String dayName;

    public Day() {
    }

    public Day(Integer dayId) {
        this.dayId = dayId;
    }
}
