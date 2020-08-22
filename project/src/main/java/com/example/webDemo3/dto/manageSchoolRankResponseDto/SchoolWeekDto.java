package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import lombok.Data;

import java.sql.Date;

/**
 * lamnt98
 * 23/07
 */
@Data
public class SchoolWeekDto {
    private Integer weekId;
    private Integer week;
    private Integer monthId;
    private Integer isCheck;
    private Date rankCreateDate;
}
