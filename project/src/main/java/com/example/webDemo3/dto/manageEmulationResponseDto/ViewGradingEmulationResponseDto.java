package com.example.webDemo3.dto.manageEmulationResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageClassResponseDto.ClassResponseDto;
import com.example.webDemo3.dto.manageViolationResponseDto.ViolationTypeResponseDto;
import lombok.Data;

import java.util.List;

/*
kimpt142 - 14/07
 */
@Data
public class ViewGradingEmulationResponseDto {
    private List<ClassResponseDto> classList;
    private List<ViolationTypeResponseDto> vioTypeAndVioList;
    private MessageDTO message;
}
