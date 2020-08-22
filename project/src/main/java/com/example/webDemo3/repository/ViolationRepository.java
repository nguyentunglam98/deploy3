package com.example.webDemo3.repository;

import com.example.webDemo3.entity.Violation;
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
public interface ViolationRepository extends JpaRepository<Violation,Integer> {
    @Query(value = "select v from Violation v where v.typeId = :typeId and (v.status <> 1 or v.status is null)")
    List<Violation> findViolationByTypeId(@Param("typeId") Integer typeId);

    @Query(value = "select v from Violation v where v.violationId = :violationId and (v.status <> 1 or v.status is null)")
    Violation findViolationByViolationId(@Param("violationId") Integer violationId);
}
