package com.example.webDemo3.repository;

import com.example.webDemo3.entity.SchoolRankMonth;
import com.example.webDemo3.entity.SchoolRankSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
kimpt142 - 23/07
 */
public interface SchoolRankSemesterRepository extends JpaRepository<SchoolRankSemester,Integer> {

    @Query(value="select srs from SchoolRankSemester srs where srs.schoolRankSemesterId.SEMESTER_ID = :semesterId "+
            " and (srs.schoolRankSemesterId.schoolClass.classId = :classId or :classId is NULL) " +
            "order by srs.schoolRankSemesterId.schoolClass.grade, srs.schoolRankSemesterId.schoolClass.giftedClass.giftedClassId asc")
    List<SchoolRankSemester> findAllBySchoolRankSemesterId(@Param("semesterId") Integer semesterId, @Param("classId") Integer classId);

    @Query(value = "select srw from SchoolRankSemester srw where srw.schoolRankSemesterId.SEMESTER_ID = :semesterId and srw.schoolRankSemesterId.schoolClass.classId = :classId")
    SchoolRankSemester findSchoolRankSemesterBySemesterIdAndClassId(@Param("semesterId")Integer semesterId, @Param("classId") Integer classId);
}
