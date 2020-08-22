package com.example.webDemo3.dto.request.manageEmulationRequestDto;

import lombok.Data;

import java.sql.Date;

/**
 * lamnt98
 * 14/07
 * Request of view change request
 */
@Data
public class ViewRequestDto {
    private Integer classId;
    private Integer status;
    private Date createDate;
    private String createBy;
    private Integer pageNumber;
}
