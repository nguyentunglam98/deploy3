package com.example.webDemo3.service.manageSchoolRankYearSerivce;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ListSemesterSchoolRankResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewSchoolYearHistoryResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.*;

public interface CreateAndEditSchoolRankYearService {
    public ListSemesterSchoolRankResponseDto loadListSemester();
    public ListSemesterSchoolRankResponseDto loadEditListSemester(ViewSemesterOfEditRankYearRequestDto requestDto);
    public MessageDTO createRankYear(CreateRankYearRequestDto requestDto);
    public MessageDTO editRankYear(EditRankYearRequestDto requestDto);
    public ViewSchoolYearHistoryResponseDto viewSchoolYearHistory(ViewSchoolYearHistoryRequestDto requestDto);
}
