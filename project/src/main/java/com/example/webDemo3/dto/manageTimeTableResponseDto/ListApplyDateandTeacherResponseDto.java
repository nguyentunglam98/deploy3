package com.example.webDemo3.dto.manageTimeTableResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.Teacher;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class ListApplyDateandTeacherResponseDto {
    private Date currentDate;
    private Integer teacherId;
    private List<Date> appyDateList;
    private List<Teacher> teacherList;
    private MessageDTO message;
}
