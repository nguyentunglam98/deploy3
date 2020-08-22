package com.example.webDemo3.service.manageSchoolYearService;

import com.example.webDemo3.dto.manageSchoolYearResponseDto.DetailSchoolYearResponseDto;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolYearResponseDto.SchoolYearTableResponseDto;
import com.example.webDemo3.dto.request.manageSchoolYearRequestDto.AddSchoolYearRequestDto;
import com.example.webDemo3.dto.request.manageSchoolYearRequestDto.DelSchoolYearRequestDto;
import com.example.webDemo3.dto.request.manageSchoolYearRequestDto.DetailSchoolYearRequestDto;
import com.example.webDemo3.dto.request.manageSchoolYearRequestDto.EditSchoolYearRequestDto;

/*
kimpt142 - 06/07
 */
public interface SchoolYearService {
    SchoolYearTableResponseDto getSchoolYearTable();
    MessageDTO deleteSchoolYearById(DelSchoolYearRequestDto model);
    MessageDTO addchoolYear(AddSchoolYearRequestDto model);
    MessageDTO editSchoolYear(EditSchoolYearRequestDto model);
    DetailSchoolYearResponseDto getDetailSchoolYearById(DetailSchoolYearRequestDto model);
}
