package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import lombok.Data;

import java.sql.Date;

/**
 * lamnt98
 * 23/07
 */
@Data
public class SchoolMonthDto {
    private Integer monthId;
    private Integer month;
    private Integer semesterId;
    private Integer isCheck;
    private Date rankCreateDate;
}
