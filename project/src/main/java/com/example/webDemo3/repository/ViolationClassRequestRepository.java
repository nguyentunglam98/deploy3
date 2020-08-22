package com.example.webDemo3.repository;

import com.example.webDemo3.entity.ViolationClass;
import com.example.webDemo3.entity.ViolationClassRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

/**
 * lamnt98
 * 14/07
 */
@Repository
public interface ViolationClassRequestRepository extends JpaRepository<ViolationClassRequest, Integer> {
    @Query(value = "select v from ViolationClassRequest v " +
            "where (v.violationClass.classId = :classId or :classId is NULL ) " +
            "and (v.dateChange = :creatDate or :creatDate is NULL) " +
            "and v.creatBy like %:createBy% " +
            "and v.statusChange = :status ")
    Page<ViolationClassRequest> findViolationClassRequestByConditionAndStatus(@Param("classId")Integer classId,
                                                                @Param("creatDate") Date creatDate,
                                                                @Param("createBy") String createBy,
                                                                @Param("status") Integer status,
                                                                Pageable paging);


    @Query(value = "select v from ViolationClassRequest v " +
            "where (v.violationClass.classId = :classId or :classId is NULL )" +
            "and (v.dateChange = :createDate or :createDate is NULL )" +
            "and v.creatBy like %:createBy%")
    Page<ViolationClassRequest> findViolationClassRequestByCondition(@Param("classId") Integer classId,
                                                       @Param("createDate") Date createDate,
                                                       @Param("createBy") String createBy,
                                                       Pageable paging);


    @Query(value="select vc from ViolationClassRequest vc where vc.creatBy = :creatBy and vc.dateChange = :dateChange and vc.statusChange = :statusChange")
    ViolationClassRequest findVioClassRequestByCreaByDateAndStatus(@Param("creatBy")String creatBy, @Param("dateChange")Date dateChange, @Param("statusChange")Integer statusChange);

    @Query(value = "select vr from ViolationClassRequest vr where vr.violationClass.id = :Id and vr.creatBy = :username and vr.statusChange = 0")
    ViolationClassRequest findNewEditRequest(@Param("Id") Long Id, @Param("username") String username);

    @Query(value = "select vr from ViolationClassRequest vr where vr.violationClass.id = :Id and vr.statusChange = :status")
    ViolationClassRequest findClassRequestByIdAndStatus(@Param("Id") Long Id, @Param("status") Integer status);

}
