package com.example.webDemo3.dto.manageViolationResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

/**
 * lamnt98
 * 06/07
 */
@Data
public class ViewViolationTypeResponseDto {
    private Integer typeId;
    private  String name;
    private Float totlaGrade;
    MessageDTO messageDTO;
}
