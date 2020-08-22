package com.example.webDemo3.dto.request.assignRedStarRequestDto;

import lombok.Data;

import java.sql.Date;

@Data
public class DownloadAssignRedStarRequestDto {
    private Date fromDate;
    private Integer classId;
    private String redStar;
}
