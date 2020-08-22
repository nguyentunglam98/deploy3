package com.example.webDemo3.dto.manageEmulationResponseDto;

import lombok.Data;

import java.sql.Date;

/**
 * lamnt98
 * 14/07
 */
@Data
public class ViolationClassRequestResponseDto {
    private Integer requestId;
    private Long violationClassId;
    private Date changeDate;
    private String createBy;
    private Integer status;
    private String reason;
    private Integer quantityNew;
    private Float substractGrade;
    private Integer quantityOld;
}
