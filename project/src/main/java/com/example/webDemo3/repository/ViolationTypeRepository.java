package com.example.webDemo3.repository;

import com.example.webDemo3.entity.ViolationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * lamnt98
 * 06/07
 */
@Repository
public interface ViolationTypeRepository extends JpaRepository<ViolationType,Integer> {
    @Query(value = "select v from ViolationType v where (v.status <> 1 or v.status is null)")
    List<ViolationType> selectAllViolationTypeActive();

    @Query(value = "select v from ViolationType v where v.typeId = :typeId and (v.status <> 1 or v.status is null)")
    ViolationType searchViolationTypeByTypeId(@Param("typeId") Integer typeId);

    @Query(value = "select SUM(v.totalGrade) from ViolationType v where v.status <> 1 or v.status is null")
    Double sumAllTotalGradeViolationTypeActive();
}
