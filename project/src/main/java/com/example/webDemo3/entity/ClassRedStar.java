package com.example.webDemo3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

/**
 * lamnt98 - 07/07
 */
@Entity
@Data
@Table(name = "CLASS_RED_STARS")
public class ClassRedStar {

    @ManyToOne
    @JoinColumn(name = "CLASS_ID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Class classSchool;

    @EmbeddedId
    private ClassRedStarId classRedStarId;
}
