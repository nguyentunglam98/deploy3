package com.example.webDemo3.repository;

import com.example.webDemo3.entity.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<Day,Integer> {
    Day findByDayId(Integer dayId);
}
