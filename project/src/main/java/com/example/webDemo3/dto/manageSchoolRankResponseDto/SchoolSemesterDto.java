package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import lombok.Data;

import java.sql.Date;

/**
 * lamnt98
 * 24/07
 */
@Data
public class SchoolSemesterDto {
    private Integer semesterId;
    private Integer semester;
    private Integer yearId;
    private Integer isCheck;
    private Date rankCreateDate;
}
