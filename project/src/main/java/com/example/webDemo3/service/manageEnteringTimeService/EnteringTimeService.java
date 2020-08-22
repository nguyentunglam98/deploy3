package com.example.webDemo3.service.manageEnteringTimeService;

import com.example.webDemo3.dto.manageEnteringViolationResponseDto.ListDayResponseDto;
import com.example.webDemo3.dto.manageEnteringViolationResponseDto.ListEnteringTimeResponseDto;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.manageEnteringViolationRequestDto.AddVioEnTimeRequestDto;
import com.example.webDemo3.dto.request.manageEnteringViolationRequestDto.DeleteEnteringTimeRequestDto;

public interface EnteringTimeService {
    public ListDayResponseDto getAllDay();
    public MessageDTO deleteEnteringTime(DeleteEnteringTimeRequestDto deleteEnteringTime);
    public ListEnteringTimeResponseDto getListEnteringTime();
    public MessageDTO addEnteringTime(AddVioEnTimeRequestDto addVioEnTimeRequestDto);
}
