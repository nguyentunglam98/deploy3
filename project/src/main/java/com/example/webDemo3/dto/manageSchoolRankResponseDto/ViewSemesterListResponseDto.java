package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.SchoolSemester;
import lombok.Data;

import java.util.List;

/*
kimpt142 - 24/07
 */
@Data
public class ViewSemesterListResponseDto {
    private List<SchoolSemester> schoolSemesterList;
    private MessageDTO message;
}
