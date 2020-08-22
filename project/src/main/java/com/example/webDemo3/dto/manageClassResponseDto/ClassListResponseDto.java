package com.example.webDemo3.dto.manageClassResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

import java.util.List;

/**
 * kimpt142 - 28/6
 */
@Data
public class ClassListResponseDto {
    private List<ClassResponseDto> classList;
    private MessageDTO message;
}
