package com.example.webDemo3.service.manageSchoolRankSemesterService;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.LoadRankSemesterResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.RankSemesterListResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewSemesterListResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.LoadByYearIdRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.SearchRankSemesterRequestDto;

import java.io.ByteArrayInputStream;

/*
kimpt142 - 24/07
 */
public interface ViewSchoolRankSemesterService {
    LoadRankSemesterResponseDto loadRankSemesterPage();
    ViewSemesterListResponseDto getSemesterListByYearId(LoadByYearIdRequestDto model);
    RankSemesterListResponseDto searchRankSemesterById(SearchRankSemesterRequestDto model);
    ByteArrayInputStream downloadRankSemester(SearchRankSemesterRequestDto model);
}
