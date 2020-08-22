package com.example.webDemo3.dto.request.manageViolationRequestDto;

import lombok.Data;

/**
 * lamnt98
 * 06/07
 */
@Data
public class EditViolationTypeRequestDto {
    private Integer typeId;
    private  String name;
    private Float totlaGrade;
}
