package com.example.webDemo3.service.manageSchoolRankMonthService;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.RankMonthListResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewMonthListResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.LoadRankMonthResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.LoadByYearIdRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.SearchRankMonthRequestDto;
import java.io.ByteArrayInputStream;

/*
kimpt142 - 23/07
 */
public interface ViewSchoolRankMonthService {
    ViewMonthListResponseDto getMonthListByYearId(LoadByYearIdRequestDto model);
    RankMonthListResponseDto searchRankMonthByMonthId(SearchRankMonthRequestDto model);
    ByteArrayInputStream downloadRankMonth(SearchRankMonthRequestDto model);
    LoadRankMonthResponseDto loadRankMonthPage();
}
