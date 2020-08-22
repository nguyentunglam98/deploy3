package com.example.webDemo3.dto.request.manageTeacherRequestDto;

import lombok.Data;

/**
 * lamnt98
 * 29-06
 */
@Data
public class EditTeaInforRequestDto {
    private Integer teacherId;
    private String fullName;
    private String phone;
    private String email;
    private String teacherIdentifier;
}
