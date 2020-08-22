package com.example.webDemo3.repository;

import com.example.webDemo3.entity.Teacher;
import com.example.webDemo3.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Integer>{
    @Query(value = "select t from Teacher t where t.teacherIdentifier = :teacherIdentifier and (t.status <> 1 or t.status is null)")
    Teacher findTeacherTeacherIdentifier(@Param("teacherIdentifier") String teacherIdentifier);

    @Query(value = "select t from Teacher t where (t.fullName like %:fullName%) and (t.status <> 1 or t.status is null)")
    Page<Teacher> searchTeacherBy(@Param("fullName") String fullName, Pageable paging);

    @Query(value = "select t from Teacher t where (t.status <> 1 or t.status is null)")
    Page<Teacher> selectAll(Pageable paging);
}
