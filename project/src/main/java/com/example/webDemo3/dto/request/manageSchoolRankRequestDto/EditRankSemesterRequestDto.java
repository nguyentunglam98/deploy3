package com.example.webDemo3.dto.request.manageSchoolRankRequestDto;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.SchoolMonthDto;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 24/07
 */
@Data
public class EditRankSemesterRequestDto {
    private Integer semesterId;
    private Integer semester;
    private String userName;
    private List<SchoolMonthDto> monthList;
}
