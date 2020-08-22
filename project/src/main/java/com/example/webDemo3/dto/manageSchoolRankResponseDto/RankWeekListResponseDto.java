package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;
import java.util.List;

/*
kimpt142 - 21/07
 */
@Data
public class RankWeekListResponseDto {
    private List<RankWeekResponseDto> rankWeekList;
    private Integer checkEdit;
    private MessageDTO message;
}
