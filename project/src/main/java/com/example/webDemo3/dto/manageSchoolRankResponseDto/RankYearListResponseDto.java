package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;
import java.util.List;

/*
kimpt142 - 24/07
 */
@Data
public class RankYearListResponseDto {
    private List<RankYearResponseDto> rankYearList;
    private MessageDTO message;
}
