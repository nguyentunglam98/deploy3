package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;
import java.util.List;

/*
kimpt142 - 23/07
 */
@Data
public class RankMonthListResponseDto {
    private List<RankMonthResponseDto> rankMonthList;
    private Integer checkEdit;
    private MessageDTO message;
}
