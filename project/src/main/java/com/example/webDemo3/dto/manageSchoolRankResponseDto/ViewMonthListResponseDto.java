package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.SchoolMonth;
import lombok.Data;

import java.util.List;

/*
kimpt142
 */
@Data
public class ViewMonthListResponseDto {
    private List<SchoolMonth> schoolMonthList;
    private MessageDTO message;
}
