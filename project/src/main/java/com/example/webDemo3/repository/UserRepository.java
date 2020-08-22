package com.example.webDemo3.repository;

import com.example.webDemo3.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String>, PagingAndSortingRepository<User, String> {

    @Query("select u from User u where u.role.roleId = 3 and (u.status <> 1 or u.status is null) order by u.classSchool.grade")
    List<User> findRedStar();

    User findUserByUsername(String username);

    @Query("select u.username from User u")
    List<String> findAllUsername();

    @Query(value = "select u from User u where (u.username like %:username% and u.role.roleId = :roleId) and (u.status <> 1 or u.status is null)")
    Page<User> searchUserByCondition(@Param("username") String username, @Param("roleId") Integer roleId, Pageable paging);

    @Query(value = "select u from User u where (u.username like %:username%) and (u.status <> 1 or u.status is null)")
    Page<User> searchUserByUsername(@Param("username") String username, Pageable paging);

    List<User> findAllByClassSchoolClassId(Integer classId);

    List<User> findAllByRoleRoleId(Integer roleId);

    @Query("select u.username from User u where (u.status <> 1 or u.status is null) ")
    List<String> findAllUsernameActive();
}
