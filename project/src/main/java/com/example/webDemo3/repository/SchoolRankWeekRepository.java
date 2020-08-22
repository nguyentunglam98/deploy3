package com.example.webDemo3.repository;

import com.example.webDemo3.entity.SchoolRankWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;

/*
kimpt142
 */
@Repository
public interface SchoolRankWeekRepository extends JpaRepository<SchoolRankWeek,Integer> {

    @Query(value = "select srw from SchoolRankWeek srw " +
            "where (srw.schoolRankWeekId.WEEK_ID = :weekId or :weekId is NULL ) " +
            "and (srw.schoolRankWeekId.schoolClass.classId = :classId or :classId is NULL) "+
            "order by srw.schoolRankWeekId.schoolClass.grade, srw.schoolRankWeekId.schoolClass.giftedClass.giftedClassId asc")
    List<SchoolRankWeek> findByWeekIđAndClassId(@Param("weekId")Integer weekId, @Param("classId") Integer classId);

    @Query(value = "select srw from SchoolRankWeek srw where srw.schoolRankWeekId.WEEK_ID = :weekId and srw.schoolRankWeekId.schoolClass.classId = :classId")
    SchoolRankWeek findSchoolRankWeekByWeekIdAndClassId(@Param("weekId")Integer weekId, @Param("classId") Integer classId);
}
