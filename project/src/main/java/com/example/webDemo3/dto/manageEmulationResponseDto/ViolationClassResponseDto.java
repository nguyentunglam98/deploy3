package com.example.webDemo3.dto.manageEmulationResponseDto;

import com.example.webDemo3.entity.ViolationClass;
import com.example.webDemo3.entity.ViolationClassRequest;
import lombok.Data;

import java.sql.Date;

/**
 * lamnt98
 * 14/07
 */
@Data
public class ViolationClassResponseDto {

    private Long violationClassId;
    private Integer classId;
    private String className;
    private String note;
    private Integer quantity;
    private String dayName;
    private String description;
    private Date createDate;
    private String createBy;
    private Integer status;
    private Float substractGrade;
    private String history;

    private ViolationClassRequestResponseDto violationClassRequest;
    private Integer checkEdit;
}
