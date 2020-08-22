package com.example.webDemo3.service.manageSchoolRankWeek;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.RankWeekListResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewWeekAndClassListResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewWeekListResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.SearchRankWeekRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.LoadByYearIdRequestDto;

/*
kimpt142 - 21/07
 */
public interface ViewSchoolRankWeekService {
    ViewWeekAndClassListResponseDto loadRankWeekPage();
    RankWeekListResponseDto searchRankWeekByWeekAndClass(SearchRankWeekRequestDto model);
    ViewWeekListResponseDto getWeekListByYearId(LoadByYearIdRequestDto model);
}
