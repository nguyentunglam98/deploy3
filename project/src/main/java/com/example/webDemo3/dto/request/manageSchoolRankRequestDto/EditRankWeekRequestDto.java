package com.example.webDemo3.dto.request.manageSchoolRankRequestDto;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.DateViolationClassDto;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 21/07
 */
@Data
public class EditRankWeekRequestDto {
    private Integer weekId;
    private Integer week;
    private String userName;
    private List<DateViolationClassDto> dateList;
}
