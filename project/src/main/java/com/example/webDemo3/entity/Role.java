package com.example.webDemo3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * kimpt142 - 25/6
 */
@Entity
@Table(name = "ROLES")
@Data
public class Role implements Serializable {

    @Id
    @Column(name = "ROLE_ID")
    private Integer roleId;

    @Column(name = "ROLE_NAME")
    private String roleName;

    public Role() {
    }

    public Role(Integer roleId) {
        this.roleId = roleId;
    }
}
