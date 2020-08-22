package com.example.webDemo3.dto.request.manageSchoolRankRequestDto;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.SchoolMonthDto;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 24/07
 */
@Data
public class CreateRankSemesterRequestDto {
    private String userName;
    private Integer semester;
    private Integer currentYearId;
    private List<SchoolMonthDto> monthList;
}
