package com.example.webDemo3.dto.request.manageTimeTableRequestDto;

import lombok.Data;

import java.sql.Date;

@Data
public class ClassTimeTableRequestDto {
    private Date applyDate;
    private Integer classId;
}
