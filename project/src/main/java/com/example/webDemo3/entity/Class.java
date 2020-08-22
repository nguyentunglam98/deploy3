package com.example.webDemo3.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * lamnt98 - 27/06
 */
@Entity
@Data
@Table(name = "CLASSES")
public class Class {

    @Id
    @Column(name = "CLASS_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer classId;

    @Column(name = "GRADE")
    private Integer grade;

    @Column(name = "CLASS_IDENTIFIER")
    private String classIdentifier;

    @Column(name = "STATUS")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "GIFTED_CLASS_ID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private GiftedClass giftedClass;

    public Class() {
    }

    public Class(Integer classId) {
        this.classId = classId;
    }
}
