package com.example.webDemo3.service.manageTimeTableService;

import com.example.webDemo3.dto.manageTimeTableResponseDto.ListApplyDateAndClassResponseDto;
import com.example.webDemo3.dto.manageTimeTableResponseDto.ListApplyDateandTeacherResponseDto;
import com.example.webDemo3.dto.manageTimeTableResponseDto.SearchTimeTableResponseDto;
import com.example.webDemo3.dto.request.manageTimeTableRequestDto.ClassTimeTableRequestDto;
import com.example.webDemo3.dto.request.manageTimeTableRequestDto.TeacherTimeTableRequestDto;

public interface TimeTableService {


    public ListApplyDateAndClassResponseDto getApplyDateAndClassList();

    public ListApplyDateandTeacherResponseDto getApplyDateAndTeacherList();

    public SearchTimeTableResponseDto searchClassTimeTable(ClassTimeTableRequestDto classTimeTable);

    public SearchTimeTableResponseDto searchTeacherTimeTable(TeacherTimeTableRequestDto teacherTimeTable);

}
