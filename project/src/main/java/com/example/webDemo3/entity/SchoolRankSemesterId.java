package com.example.webDemo3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/*
kimpt142 - 23/07
 */
@Data
@Embeddable
public class SchoolRankSemesterId implements Serializable {

    @Column
    private Integer SEMESTER_ID;

    @ManyToOne
    @JoinColumn(name = "CLASS_ID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Class schoolClass;
}
