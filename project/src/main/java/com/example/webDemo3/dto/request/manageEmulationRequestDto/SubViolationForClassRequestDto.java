package com.example.webDemo3.dto.request.manageEmulationRequestDto;

import lombok.Data;

/*
kimpt142 - 14/07
 */
@Data
public class SubViolationForClassRequestDto {
    private Integer violationId;
    private Integer quantity;
    private Float substractGrade;
    private String note;
}
