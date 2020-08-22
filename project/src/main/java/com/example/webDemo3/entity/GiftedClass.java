package com.example.webDemo3.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * lamnt98 - 27/06
 */
@Entity
@Data
@Table(name = "GIFTED_CLASS")
public class GiftedClass {
    @Id
    @Column(name = "GIFTED_CLASS_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer giftedClassId;

    @Column(name = "NAME")
    private String name;

    public GiftedClass() {
    }

    public GiftedClass(Integer giftedClassId) {
        this.giftedClassId = giftedClassId;
    }
}
