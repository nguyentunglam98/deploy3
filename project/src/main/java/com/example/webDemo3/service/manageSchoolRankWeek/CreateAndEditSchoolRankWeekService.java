package com.example.webDemo3.service.manageSchoolRankWeek;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ListDateResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewSchoolWeekHistoryResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.CreateRankWeekRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.EditRankWeekRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.ViewSchoolWeekHistoryRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.ViewWeekAnDateListRequestDto;

public interface CreateAndEditSchoolRankWeekService {
     public ListDateResponseDto loadListDate();
     public MessageDTO createRankWeek(CreateRankWeekRequestDto requestDto);
     public ListDateResponseDto loadEditListDate(ViewWeekAnDateListRequestDto requestDto);
     public MessageDTO editRankWeek(EditRankWeekRequestDto requestDto);
     public ViewSchoolWeekHistoryResponseDto viewSchoolWeekHistory(ViewSchoolWeekHistoryRequestDto requestDto);

}
