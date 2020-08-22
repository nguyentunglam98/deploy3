package com.example.webDemo3.dto.request.manageSchoolRankRequestDto;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.SchoolWeekDto;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 23/07
 */
@Data
public class CreateRankMonthRequestDto {
    private String userName;
    private Integer month;
    private Integer currentYearId;
    private List<SchoolWeekDto> weekList;
}
