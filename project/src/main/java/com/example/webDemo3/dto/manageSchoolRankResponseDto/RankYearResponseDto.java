package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import lombok.Data;

/*
kimpt142 - 24/07
 */
@Data
public class RankYearResponseDto {
    private Integer classId;
    private String className;
    private Integer totalRankSemester;
    private Double totalGradeSemester;
    private Integer rank;
}
