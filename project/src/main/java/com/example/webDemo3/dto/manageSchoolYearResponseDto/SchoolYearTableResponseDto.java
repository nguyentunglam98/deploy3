package com.example.webDemo3.dto.manageSchoolYearResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;
import java.util.List;

/*
kimpt142 - 06/07
 */
@Data
public class SchoolYearTableResponseDto {
    private List<SchoolYearResponseDto> schoolYearList;
    private MessageDTO message;
}
