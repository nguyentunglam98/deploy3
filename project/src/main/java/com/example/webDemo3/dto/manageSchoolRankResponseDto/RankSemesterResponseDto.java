package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import lombok.Data;

/*
kimpt142 - 24/07
 */
@Data
public class RankSemesterResponseDto {
    private Integer classId;
    private String className;
    private Integer totalRankMonth;
    private Double totalGradeMonth;
    private Integer rank;
}
