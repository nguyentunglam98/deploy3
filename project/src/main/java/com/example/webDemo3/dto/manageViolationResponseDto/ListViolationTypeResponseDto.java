package com.example.webDemo3.dto.manageViolationResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.ViolationType;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 07/07
 */
@Data
public class ListViolationTypeResponseDto {
    private List<ViolationType> violationTypeList;
    private MessageDTO message;
}
