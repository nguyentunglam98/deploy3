package com.example.webDemo3.dto.manageEnteringViolationResponseDto;

import lombok.Data;

@Data
public class ViolationEnteringTimeResponseDto {
     private Integer violationEnteringTimeId;
     private String roleName;
     private String dayName;
     private String startTime;
     private String endTime;
}
