package com.example.webDemo3.dto.request.manageClassRequestDto;

import lombok.Data;

/**
 * kimpt142 - 29/6
 */
@Data
public class AddClassRequestDto {
    private String classIdentifier;
    private Integer grade;
    private Integer giftedClassId;
    private Boolean isRedStar;
    private Boolean isMonitor;
}
