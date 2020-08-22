package com.example.webDemo3.repository;

import com.example.webDemo3.entity.SchoolRankMonth;
import com.example.webDemo3.entity.SchoolRankWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
kimpt142 - 23/07
 */
public interface SchoolRankMonthRepository extends JpaRepository<SchoolRankMonth,Integer> {
    @Query(value="select srm from SchoolRankMonth srm where srm.schoolRankMonthId.MONTH_ID = :monthId "+
            " and (srm.schoolRankMonthId.schoolClass.classId = :classId or :classId is NULL) " +
            "order by srm.schoolRankMonthId.schoolClass.grade, srm.schoolRankMonthId.schoolClass.giftedClass.giftedClassId asc")
    List<SchoolRankMonth> findAllBySchoolRankMonthId(@Param("monthId") Integer monthId, @Param("classId") Integer classId);


    @Query(value = "select srw from SchoolRankMonth srw where srw.schoolRankMonthId.MONTH_ID = :monthId and srw.schoolRankMonthId.schoolClass.classId = :classId")
    SchoolRankMonth findSchoolRankMonthByMonthIdAndClassId(@Param("monthId")Integer monthId, @Param("classId") Integer classId);
}
