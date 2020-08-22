package com.example.webDemo3.dto.request.assignRedStarRequestDto;

import lombok.Data;

import java.sql.Date;

/**
 * lamnt98
 * 07/07
 */
@Data
public class ViewAssignTaskRequestDto {
    private Date fromDate;
    private Integer orderBy;
    private Integer sortBy;
    private Integer classId;
    private String redStar;
    private Integer pageNumber;
}
