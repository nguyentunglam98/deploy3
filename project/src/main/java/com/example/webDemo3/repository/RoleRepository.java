package com.example.webDemo3.repository;

import com.example.webDemo3.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * kimpt142 - 25/6
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleId(Integer roleId);
}
