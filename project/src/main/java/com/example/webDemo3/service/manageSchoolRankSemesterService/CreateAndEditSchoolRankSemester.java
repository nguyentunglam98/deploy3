package com.example.webDemo3.service.manageSchoolRankSemesterService;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ListMonthSchoolRankResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewSchoolSemesterHistoryResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.*;

public interface CreateAndEditSchoolRankSemester {
    public ListMonthSchoolRankResponseDto loadListMonth(ListMonthSchoolRankRequestDto requestDto);
    public MessageDTO createRankSemester(CreateRankSemesterRequestDto requestDto);
    public ListMonthSchoolRankResponseDto loadEditListMonth(ViewMonthOfEditRankSemesterRequestDto requestDto);
    public MessageDTO editRankSemester(EditRankSemesterRequestDto requestDto);
    public ViewSchoolSemesterHistoryResponseDto viewSchoolSemesterHistory(ViewSchoolSemesterHistoryRequestDto requestDto);

}
