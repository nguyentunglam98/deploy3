package com.example.webDemo3.dto.request.manageSchoolRankRequestDto;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.DateViolationClassDto;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 21/07
 */
@Data
public class CreateRankWeekRequestDto {
    private String userName;
    private Integer week;
    private Integer currentYearId;
    private List<DateViolationClassDto> dateList;
}
