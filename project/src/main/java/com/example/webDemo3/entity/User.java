package com.example.webDemo3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "USERS")
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;

    @Id
    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "PASS_WORD")
    private String password;

    @Column(name = "FULL_NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "STATUS")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Role role;

    @ManyToOne
    @JoinColumn(name = "CLASS_ID_OFUSER")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Class classSchool;

}