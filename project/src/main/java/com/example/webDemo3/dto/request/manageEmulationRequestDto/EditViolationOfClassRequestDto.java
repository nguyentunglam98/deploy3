package com.example.webDemo3.dto.request.manageEmulationRequestDto;

import lombok.Data;

import java.sql.Date;

/*
kimpt142 - 16/07
 */
@Data
public class EditViolationOfClassRequestDto {
    private Long violationClassId;
    private Integer classId;
    private Integer roleId;
    private String username;
    private Integer newQuantity;
    private Date editDate;
    private Date createDate;
    private String reason;
    private Integer oldQuantity;
}
