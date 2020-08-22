package com.example.webDemo3.dto.manageAssignRedStarResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageEmulationResponseDto.ClassRedStarResponseDto;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 07/07
 */
@Data
public class ViewAssignTaskResponseDto {
    List<ClassRedStarResponseDto> listAssignTask;
    private MessageDTO message;
}
