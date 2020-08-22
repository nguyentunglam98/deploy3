package com.example.webDemo3.dto.manageTimeTableResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.Teacher;
import lombok.Data;

import java.sql.Date;
import java.util.List;

/**
 * lamnt98
 * 03/07
 */
@Data
public class ListApplyDateAndClassResponseDto {
    private Date currentDate;
    private Integer classId;
    private List<Date> appyDateList;
    private List<Class> classList;
    private MessageDTO message;
}
