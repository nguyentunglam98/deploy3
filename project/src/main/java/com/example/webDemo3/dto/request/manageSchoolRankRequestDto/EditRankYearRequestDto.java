package com.example.webDemo3.dto.request.manageSchoolRankRequestDto;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.SchoolSemesterDto;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 24/07
 */
@Data
public class EditRankYearRequestDto {
    private Integer yearId;
    private String userName;
    List<SchoolSemesterDto> semesterList;
}
