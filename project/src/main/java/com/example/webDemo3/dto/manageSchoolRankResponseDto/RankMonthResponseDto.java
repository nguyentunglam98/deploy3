package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import lombok.Data;

/*
kimpt142 - 23/07
 */
@Data
public class RankMonthResponseDto {
    private Integer classId;
    private String className;
    private Integer totalRankWeek;
    private Double totalGradeWeek;
    private Integer rank;
}