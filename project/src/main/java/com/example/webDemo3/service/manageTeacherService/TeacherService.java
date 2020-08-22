package com.example.webDemo3.service.manageTeacherService;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageTeacherResponseDto.ViewTeaInforResponseDto;
import com.example.webDemo3.dto.manageTeacherResponseDto.ViewTeaListResponseDto;
import com.example.webDemo3.dto.request.manageTeacherRequestDto.*;

public interface TeacherService {
    MessageDTO addTeacher(AddTeacherRequestDto addTeacher);
    MessageDTO deleteTeacher(DeleteTeacherRequestDto deleteTeacher);
    MessageDTO editTeacherInformation(EditTeaInforRequestDto editTeaInforRequestDto);
    ViewTeaListResponseDto searchTeacher(ViewTeaListRequestDto viewTeacherList);
    ViewTeaInforResponseDto viewTeacherInfor(ViewTeaInforRequestDto viewTeaInforRequestDto);
}
