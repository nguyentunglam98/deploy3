package com.example.webDemo3.dto.manageViolationResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 06/07
 */
@Data
public class ListVioAndTypeResponseDto {
    private List<ViolationTypeResponseDto> listViolationType;
    private MessageDTO messageDTO;
}
