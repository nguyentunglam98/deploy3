package com.example.webDemo3.dto.request.manageTeacherRequestDto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteTeacherRequestDto {
    private List<Integer> listTeacher;
}
