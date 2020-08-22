package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import lombok.Data;

/*
kimpt142 - 21/07
 */
@Data
public class RankWeekResponseDto {
    private Integer weekId;
    private Integer classId;
    private String className;
    private Double emulationGrade;
    private Double learningGrade;
    private Double movementGrade;
    private Double laborGrade;
    private Double totalGrade;
    private Integer rank;
}
