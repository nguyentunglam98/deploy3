package com.example.webDemo3.controller;

import com.example.webDemo3.dto.manageSchoolYearResponseDto.DetailSchoolYearResponseDto;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolYearResponseDto.SchoolYearTableResponseDto;
import com.example.webDemo3.dto.request.manageSchoolYearRequestDto.AddSchoolYearRequestDto;
import com.example.webDemo3.dto.request.manageSchoolYearRequestDto.DelSchoolYearRequestDto;
import com.example.webDemo3.dto.request.manageSchoolYearRequestDto.DetailSchoolYearRequestDto;
import com.example.webDemo3.dto.request.manageSchoolYearRequestDto.EditSchoolYearRequestDto;
import com.example.webDemo3.service.manageSchoolYearService.SchoolYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
kimpt142 - 06/07
 */
@RestController
@RequestMapping("/api/admin")
public class SchoolYearApiController {
    @Autowired
    private SchoolYearService schoolYearService;

    /**
     * kimpt142
     * 0/07
     * catch request to get the school year list
     * @param
     * @return SchoolYearTableResponseDto
     */
    @PostMapping("/schoolyearlist")
    public SchoolYearTableResponseDto getSchoolYearList()
    {
        return schoolYearService.getSchoolYearTable();
    }

    /**
     * kimpt142
     * 0/07
     * catch request to delete school year
     * @param
     * @return MessageDTO
     */
    @PostMapping("/delschoolyear")
    public MessageDTO deleteSchoolYearById(@RequestBody DelSchoolYearRequestDto model)
    {
        return schoolYearService.deleteSchoolYearById(model);
    }

    /**
     * kimpt142
     * 06/07
     * catch request to add school year
     * @param
     * @return MessageDTO
     */
    @PostMapping("/addschoolyear")
    public MessageDTO addNewSchoolYear(@RequestBody AddSchoolYearRequestDto model)
    {
        return schoolYearService.addchoolYear(model);
    }

    /**
     * kimpt142
     * 06/07
     * catch request to edit school year
     * @param
     * @return MessageDTO
     */
    @PostMapping("/editschoolyear")
    public MessageDTO editSchoolYear(@RequestBody EditSchoolYearRequestDto model)
    {
        return schoolYearService.editSchoolYear(model);
    }

    /**
     * kimpt142
     * 09/07
     * catch request to get information of school year
     * @param model include schoolYearId
     * @return MessageDTO and infor
     */
    @PostMapping("/getschoolyear")
    public DetailSchoolYearResponseDto getDetailSchoolYearById(@RequestBody DetailSchoolYearRequestDto model)
    {
        return schoolYearService.getDetailSchoolYearById(model);
    }
}
