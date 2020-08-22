package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 21/07
 */
@Data
public class ListDateResponseDto {
    private List<DateViolationClassDto> dateList;
    private MessageDTO message;
}
