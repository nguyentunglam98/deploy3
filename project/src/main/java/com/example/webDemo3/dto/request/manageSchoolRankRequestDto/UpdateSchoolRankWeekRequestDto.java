package com.example.webDemo3.dto.request.manageSchoolRankRequestDto;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.RankWeekResponseDto;
import lombok.Data;

import java.util.List;

/*
kimpt142 - 21/07
 */
@Data
public class UpdateSchoolRankWeekRequestDto {
    private List<RankWeekResponseDto> rankWeekList;
    private String userName;
}
