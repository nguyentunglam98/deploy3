package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 23/07
 */
@Data
public class ListMonthSchoolRankResponseDto {
    private List<SchoolMonthDto> monthList;
    MessageDTO message;
}
