package com.example.webDemo3.dto.request.manageSchoolRankRequestDto;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.SchoolSemesterDto;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 24/07
 */
@Data
public class CreateRankYearRequestDto {
    private String userName;
    private Integer yearId;
    private List<SchoolSemesterDto> semesterList;
}
