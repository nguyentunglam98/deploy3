package com.example.webDemo3.service.manageSchoolRankYearSerivce;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.LoadRankYearResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.RankYearListResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.LoadByYearIdRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.SearchRankYearRequestDto;

import java.io.ByteArrayInputStream;

/*
kimpt142 - 24/07
 */
public interface ViewSchoolRankYearService {
    RankYearListResponseDto searchRankYearById(SearchRankYearRequestDto model);
    ByteArrayInputStream downloadRankYear(SearchRankYearRequestDto model);
    LoadRankYearResponseDto loadRankYear();
}
