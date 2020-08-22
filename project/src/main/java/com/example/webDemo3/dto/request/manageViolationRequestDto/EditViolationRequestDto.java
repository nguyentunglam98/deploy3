package com.example.webDemo3.dto.request.manageViolationRequestDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

/**
 * lamnt98
 * 06/07
 */
@Data
public class EditViolationRequestDto {
    private Integer typeId;
    private Integer violationId;
    private String description;
    private Float substractGrade;
    MessageDTO messageDTO;
}
