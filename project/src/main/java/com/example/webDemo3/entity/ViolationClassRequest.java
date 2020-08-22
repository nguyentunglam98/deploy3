package com.example.webDemo3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

/**
 * lamnt98
 * 14/07
 */
@Entity
@Data
@Table(name = "VIOLATION_CLASS_REQUESTS")
public class ViolationClassRequest {
    @Id
    @Column(name = "REQUEST_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    @ManyToOne
    @JoinColumn(name = "VIOLATION_CLASS_ID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ViolationClass violationClass;

    @Column(name = "CREATE_BY")
    private String creatBy;

    @Column(name = "QUANTITY_NEW")
    private Integer quantityNew;

    @Column(name = "DATE_CHANGE")
    private Date dateChange;

    @Column(name = "REASON")
    private String reason;

    @Column(name = "STATUS_CHANGE")
    private Integer statusChange;

    @Column(name = "QUANTITY_OLD")
    private Integer quantityOld;

}
