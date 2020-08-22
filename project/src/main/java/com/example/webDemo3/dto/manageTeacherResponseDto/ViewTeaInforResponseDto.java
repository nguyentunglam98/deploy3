package com.example.webDemo3.dto.manageTeacherResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

@Data
public class ViewTeaInforResponseDto {
    private Integer teacherId;
    private String fullName;
    private String phone;
    private String email;
    private String teacherIdentifier;
    private MessageDTO messageDTO;
}
