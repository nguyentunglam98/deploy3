package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import lombok.Data;

import java.sql.Date;

/**
 * lamnt98
 * 21/07
 */
@Data
public class DateViolationClassDto {
    private Date date;
    private String dayName;
    private Integer isCheck;
    private Integer week;
}
