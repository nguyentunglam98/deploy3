package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolYearResponseDto.SchoolYearResponseDto;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 23/07
 */
@Data
public class ListSemesterSchoolRankResponseDto {
    private List<SchoolSemesterDto> semesterList;
    private List<SchoolYearResponseDto> schoolYearList;
    private MessageDTO message;
}
