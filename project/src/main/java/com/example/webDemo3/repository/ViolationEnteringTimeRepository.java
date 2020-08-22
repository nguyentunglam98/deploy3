package com.example.webDemo3.repository;

import com.example.webDemo3.entity.ViolationEnteringTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.sql.Time;
import java.util.List;

public interface ViolationEnteringTimeRepository extends JpaRepository<ViolationEnteringTime,Integer> {
    @Query(value="select vet from ViolationEnteringTime vet where vet.roleId = :roleId  and vet.dayId = :dayId and vet.startTime <= :time and vet.endTime >= :time")
    List<ViolationEnteringTime> findEnteringTimeRoleIdAndDayId(@Param("roleId")Integer roleId, @Param("dayId")Integer dayId, @Param("time") Time time);

    @Query(value="select vet from ViolationEnteringTime vet where vet.roleId = :roleId and vet.dayId = :dayId")
    List<ViolationEnteringTime> findEnteringTimeByRoleIdAndDayId(@Param("roleId")Integer roleId, @Param("dayId")Integer dayId);
}
