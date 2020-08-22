package com.example.webDemo3.dto.request.manageTeacherRequestDto;

import lombok.Data;

@Data
public class AddTeacherRequestDto {
    private String fullName;
    private String phone;
    private String email;
    private String teacherIdentifier;
}
