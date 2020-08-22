package com.example.webDemo3.repository;

import com.example.webDemo3.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;


@Repository
public interface TimetableRepository extends JpaRepository<TimeTable,Long> {
    @Transactional
    @Modifying
    @Query(value = "DELETE from TimeTable t where t.applyDate = :date")
    void deleteByApplyDate(@Param("date") Date date);

    @Query(value = "select distinct t.applyDate from TimeTable t where t.applyDate = :date ")
    Date getAllByApplyDate(@Param("date") Date date);

    @Query(value = "select distinct t.applyDate from TimeTable t order by t.applyDate desc")
    List<Date> getAllDate();

     @Query(value = "select t from TimeTable t where t.applyDate = :applyDate and t.classId = :classId and (t.isAfternoon = 0 or t.isAfternoon is null) and t.isAdditional = :isAdditional")
     List<TimeTable> getMorningClassTimeTable(@Param("applyDate") Date applyDate, @Param("classId") Integer classId, @Param("isAdditional") Integer isAdditional);

    @Query(value = "select t from TimeTable t where t.applyDate = :applyDate and t.classId = :classId and t.isAfternoon = 1 and t.isAdditional = :isAdditional")
    List<TimeTable> getAfternoonClassTimeTable(@Param("applyDate") Date applyDate, @Param("classId") Integer classId, @Param("isAdditional") Integer isAdditional);

    @Query(value = "select t from TimeTable t where t.applyDate = :applyDate and t.teacherId = :teacherId and (t.isAfternoon = 0 or t.isAfternoon is null)")
    List<TimeTable> getMorningTeacherTimeTable(@Param("applyDate") Date applyDate, @Param("teacherId") Integer teacherId);

    @Query(value = "select t from TimeTable t where t.applyDate = :applyDate and t.teacherId = :teacherId and t.isAfternoon = 1")
    List<TimeTable> getAfternoonTeacherTimeTable(@Param("applyDate") Date applyDate, @Param("teacherId") Integer teacherId);
}
