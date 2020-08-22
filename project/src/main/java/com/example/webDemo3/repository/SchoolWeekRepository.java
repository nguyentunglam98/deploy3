package com.example.webDemo3.repository;

import com.example.webDemo3.entity.SchoolWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SchoolWeekRepository extends JpaRepository<SchoolWeek,Integer> {
    @Query(value = "select s from SchoolWeek s where s.week = :week and s.yearId = :yearId")
    SchoolWeek findSchoolWeeksByWeekAndYearId(@Param("week") Integer week, @Param("yearId") Integer yearId);

    @Query(value="select sw from SchoolWeek sw where sw.weekID <> 0 and sw.yearId = :yearId order by sw.createDate desc ")
    List<SchoolWeek> findSchoolWeekByYearIdExcludeZero(@Param("yearId") Integer yearId);

    SchoolWeek findSchoolWeekByWeekID(Integer weekId);

    @Query(value="select sw from SchoolWeek sw where sw.yearId = :yearId and sw.monthID = 0 and sw.weekID <> 0 order by sw.week asc")
    List<SchoolWeek> findSchoolWeekNotRank(@Param("yearId") Integer yearId);

    @Query(value="select sw from SchoolWeek sw where sw.monthID = :monthId and sw.yearId = :yearId order by sw.week asc")
    List<SchoolWeek> findSchoolWeekByMonthIdAndYearId(@Param("monthId") Integer monthId, @Param("yearId") Integer yearId);
}
