package com.example.webDemo3.repository;

import com.example.webDemo3.entity.SchoolRankSemester;
import com.example.webDemo3.entity.SchoolRankYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
kimpt142 - 23/07
 */
public interface SchoolRankYearRepository extends JpaRepository<SchoolRankYear,Integer> {
    @Query(value = "select distinct srw.schoolRankYearId.YEAR_ID from SchoolRankYear srw")
    List<Integer> getAllDistinctYearId();

    @Query(value="select sry from SchoolRankYear sry where sry.schoolRankYearId.YEAR_ID = :yearId "+
            " and (sry.schoolRankYearId.schoolClass.classId = :classId or :classId is NULL) " +
            "order by sry.schoolRankYearId.schoolClass.grade, sry.schoolRankYearId.schoolClass.giftedClass.giftedClassId asc")
    List<SchoolRankYear> findAllByYearId(@Param("yearId") Integer yearId, @Param("classId") Integer classId);
}
