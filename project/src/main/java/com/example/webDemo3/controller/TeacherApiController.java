package com.example.webDemo3.controller;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageTeacherResponseDto.ViewTeaInforResponseDto;
import com.example.webDemo3.dto.manageTeacherResponseDto.ViewTeaListResponseDto;
import com.example.webDemo3.dto.request.manageTeacherRequestDto.*;
import com.example.webDemo3.service.manageTeacherService.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class TeacherApiController {

    @Autowired
    private TeacherService teacherService;

    /**
     * lamnt98
     * 30/06
     * catch request from client to update teacher information
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/editteacherinformation")
    public MessageDTO editTeacherInfor(@RequestBody EditTeaInforRequestDto model)
    {
        return teacherService.editTeacherInformation(model);
    }

    /**
     * lamnt98
     * 30/06
     * catch request from client to delete teacher
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/deleteteacher")
    public MessageDTO deleteTeacher(@RequestBody DeleteTeacherRequestDto model)
    {
        return teacherService.deleteTeacher(model);
    }

    /**
     * lamnt98
     * 30/06
     * catch request from client to add teacher
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/addteacher")
    public MessageDTO addTeacher(@RequestBody AddTeacherRequestDto model)
    {
        return teacherService.addTeacher(model);
    }

    /**
     * lamnt98
     * 30/06
     * catch request from client to search list teacher
     * @param model
     * @return ViewTeaListResponseDto
     */
    @PostMapping("/teacherlist")
    public ViewTeaListResponseDto searchTeacher(@RequestBody ViewTeaListRequestDto model)
    {
        return teacherService.searchTeacher(model);
    }

    /**
     * lamnt98
     * 01/07
     * catch request from client to view teacher information
     * @param model
     * @return ViewTeaListResponseDto
     */
    @PostMapping("/viewteacherinformation")
    public ViewTeaInforResponseDto viewTeacherInformation(@RequestBody ViewTeaInforRequestDto model)
    {
        return teacherService.viewTeacherInfor(model);
    }
}
