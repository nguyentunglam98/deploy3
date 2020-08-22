package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageClassResponseDto.ClassResponseDto;
import com.example.webDemo3.dto.manageSchoolYearResponseDto.SchoolYearResponseDto;
import lombok.Data;
import java.util.List;

/*
kimpt142 - 31/07
 */
@Data
public class LoadRankYearResponseDto {
    private List<SchoolYearResponseDto> schoolYearList;
    private List<ClassResponseDto> classList;
    private MessageDTO message;
}
