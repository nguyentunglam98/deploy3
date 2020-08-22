package com.example.webDemo3.repository;

import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.ClassRedStar;
import com.example.webDemo3.entity.ClassRedStarId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Repository
public interface ClassRedStarRepository extends JpaRepository<ClassRedStar, ClassRedStarId> {

    @Query(value = "select c from ClassRedStar c " +
            "where c.classRedStarId.FROM_DATE = :date " +
            "and c.classRedStarId.RED_STAR = :redStar")
    ClassRedStar findByRedStar(@Param("redStar") String redStar,@Param("date") Date date);

    @Query(value = "select c from ClassRedStar c " +
            "where (c.classSchool.classId = :classId or :classId is null) " +
            "and ( c.classRedStarId.RED_STAR like %:redStar%)" +
            "and (c.classRedStarId.FROM_DATE = :fromDate or :fromDate is null)" +
            "order by c.classSchool.grade, c.classSchool.giftedClass.giftedClassId asc " )
    List<ClassRedStar> findAllByCondition(@Param("classId") Integer classId,
                                          @Param("redStar") String redStar,
                                          @Param("fromDate") Date fromDate);

    @Transactional
    @Modifying
    @Query(value = "DELETE from ClassRedStar c where c.classRedStarId.FROM_DATE >= :fromDate")
    void deleteByFromDate(@Param("fromDate") Date fromDate);

    @Query(value = "select distinct c.classRedStarId.FROM_DATE from ClassRedStar c where c.classRedStarId.FROM_DATE >= :fromDate")
    List<Date> findByDate(@Param("fromDate") Date fromDate);

    @Query(value = "select c from ClassRedStar c where c.classRedStarId.FROM_DATE = :fromDate")
    List<ClassRedStar> findAllByDate(@Param("fromDate") Date fromDate);

    @Query(value = "select MAX(c.classRedStarId.FROM_DATE) from ClassRedStar c where c.classRedStarId.FROM_DATE <= :fromDate")
    Date getBiggestClosetDate(@Param("fromDate") Date fromDate);

    @Query(value = "select DISTINCT c.classRedStarId.FROM_DATE from ClassRedStar c order by c.classRedStarId.FROM_DATE desc")
    List<Date> findDistinctByClassRedStarId_FROM_DATE();

    @Query(value = "select c from ClassRedStar c where c.classRedStarId.FROM_DATE = :fromDate")
    Page<ClassRedStar> findByClassRedStarId_FROM_DATE(@Param("fromDate") Date fromDate,Pageable paging);

    @Query(value = "select MAX(c.classRedStarId.FROM_DATE) from ClassRedStar c where c.classRedStarId.FROM_DATE <= :fromDate and c.classRedStarId.RED_STAR like %:RED_STAR%")
    Date getBiggestClosetDateRedStar(@Param("fromDate") Date fromDate, @Param("RED_STAR") String RED_STAR);

    @Query(value = "select c from ClassRedStar c where c.classRedStarId.FROM_DATE = :fromDate and c.classRedStarId.RED_STAR like %:redStar%")
    Page<ClassRedStar> findByClassRedStarId_FROM_DATEAndClassRedStarId_RED_STAR(@Param("fromDate") Date fromDate,@Param("redStar") String redStar, Pageable paging);


    @Query(value = "select MAX(c.classRedStarId.FROM_DATE) from ClassRedStar c where c.classRedStarId.FROM_DATE <= :fromDate and c.classSchool.classId = :classId")
    Date getBiggestClosetDateClassId(@Param("fromDate") Date fromDate, @Param("classId") Integer classId);

    @Query(value = "select c from ClassRedStar c where c.classSchool.classId = :classId and c.classRedStarId.FROM_DATE = :fromDate")
    Page<ClassRedStar> findByClassIdAndClassRedStarId_FROM_DATE(@Param("classId") Integer classId,@Param("fromDate") Date fromDate, Pageable paging);


    @Query(value = "select MAX(c.classRedStarId.FROM_DATE) from ClassRedStar c where c.classRedStarId.FROM_DATE <= :fromDate and c.classSchool.classId = :classId and c.classRedStarId.RED_STAR like %:redStar%")
    Date getBiggestClosetDateClassIdRedStar(@Param("fromDate") Date fromDate, @Param("classId") Integer classId, @Param("redStar") String redStar);

    @Query(value = "select c from ClassRedStar c where c.classSchool.classId = :classId and c.classRedStarId.FROM_DATE = :fromDate and c.classRedStarId.RED_STAR like %:redStar%")
    Page<ClassRedStar> findByClassIdAndClassRedStarId_FROM_DATEAndClassRedStarId_RED_STAR(@Param("classId") Integer classId,@Param("fromDate") Date fromDate, @Param("redStar") String redStar,  Pageable paging);

    @Query(value = "select MAX(c.classRedStarId.FROM_DATE) from ClassRedStar c where c.classRedStarId.FROM_DATE <= :fromDate and c.classSchool.classId = :classId and c.classRedStarId.RED_STAR <> :username")
    Date getBiggestClosetDateClassIdAndDifferRedStar(@Param("fromDate") Date fromDate, @Param("classId") Integer classId, @Param("username") String username);

    @Query(value = "select c from ClassRedStar c where c.classRedStarId.FROM_DATE = :fromDate and c.classRedStarId.RED_STAR = :RED_STAR")
    ClassRedStar findClassRedStarByDateAndRedStar(@Param("fromDate") Date fromDate, @Param("RED_STAR") String RED_STAR);
}
