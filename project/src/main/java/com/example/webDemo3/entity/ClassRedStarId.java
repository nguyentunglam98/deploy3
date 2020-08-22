package com.example.webDemo3.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Date;

@Data
@Embeddable
public class ClassRedStarId implements Serializable {

    @Column
    private String RED_STAR;

    @Column
    private Date FROM_DATE;
}
