package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageClassResponseDto.ClassResponseDto;
import com.example.webDemo3.dto.manageSchoolYearResponseDto.SchoolYearResponseDto;
import com.example.webDemo3.entity.SchoolSemester;
import lombok.Data;

import java.util.List;

/*
kimpt142 - 24/07
 */
@Data
public class LoadRankSemesterResponseDto {
    private Integer currentYearId;
    private List<SchoolYearResponseDto> schoolYearList;
    private List<SchoolSemester> schoolSemesterList;
    private List<ClassResponseDto> classList;
    private MessageDTO message;
}
