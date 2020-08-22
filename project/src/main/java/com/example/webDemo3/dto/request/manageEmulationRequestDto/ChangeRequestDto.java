package com.example.webDemo3.dto.request.manageEmulationRequestDto;

import lombok.Data;

import java.sql.Date;

/**
 * lamnt98
 * 16/07
 */
@Data
public class ChangeRequestDto {
     private Long violationClassId;
     private Integer requestId;
     private String userName;
}
