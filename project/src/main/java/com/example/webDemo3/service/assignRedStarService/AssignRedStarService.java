package com.example.webDemo3.service.assignRedStarService;

import com.example.webDemo3.dto.manageAssignRedStarResponseDto.ListClassAndDateResponseDto;
import com.example.webDemo3.dto.manageAssignRedStarResponseDto.ViewAssignTaskResponseDto;
import com.example.webDemo3.dto.request.assignRedStarRequestDto.ViewAssignTaskRequestDto;

public interface AssignRedStarService {
    public ViewAssignTaskResponseDto viewTask(ViewAssignTaskRequestDto assignTaskRequestDto);
    public ListClassAndDateResponseDto listStarClassDate();
}
