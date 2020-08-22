package com.example.webDemo3.repository;

import com.example.webDemo3.entity.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface SchoolYearRepository extends JpaRepository<SchoolYear,Integer>{
    @Query(value = "select s from SchoolYear s where s.yearID <> 0 order by s.fromDate desc")
    List<SchoolYear> findAllSortByFromDate();

    @Query(value = "select s from SchoolYear s where s.fromDate <= :date and s.toDate >= :date and s.yearID <> 0")
    SchoolYear findSchoolYearsByDate(@Param("date") Date date);
}
