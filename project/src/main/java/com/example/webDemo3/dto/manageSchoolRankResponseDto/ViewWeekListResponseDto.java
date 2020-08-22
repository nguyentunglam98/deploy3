package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.SchoolWeek;
import lombok.Data;

import java.util.List;

/*
kimpt142 - 23/07
 */
@Data
public class ViewWeekListResponseDto {
    private List<SchoolWeek> schoolWeekList;
    private MessageDTO message;
}
