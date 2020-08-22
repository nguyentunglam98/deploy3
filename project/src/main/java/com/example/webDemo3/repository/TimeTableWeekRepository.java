package com.example.webDemo3.repository;

import com.example.webDemo3.entity.SchoolWeek;
import com.example.webDemo3.entity.TimeTableWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeTableWeekRepository extends JpaRepository<TimeTableWeek,Integer> {
    @Query(value = "select t from TimeTableWeek t where t.yearID = :yearId order by t.fromDate desc")
    List<TimeTableWeek> findByYearIdANdSortByFromDate(@Param("yearId") Integer yearId);
}
