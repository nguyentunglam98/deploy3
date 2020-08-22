package com.example.webDemo3.dto.manageViolationResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.Violation;
import com.example.webDemo3.entity.ViolationType;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 06/07
 */
@Data
public class ViewViolationResponseDto {
    private List<ViolationType> violationTypeList;
    private Violation currentViolation;
    private MessageDTO messageDTO;
}
