package com.example.webDemo3.controller;

import com.example.webDemo3.dto.*;
import com.example.webDemo3.dto.manageClassResponseDto.AddClassResponseDto;
import com.example.webDemo3.dto.manageClassResponseDto.ClassInforResponseDto;
import com.example.webDemo3.dto.manageClassResponseDto.ClassTableResponseDto;
import com.example.webDemo3.dto.manageClassResponseDto.GiftedClassResponseDto;
import com.example.webDemo3.dto.request.manageClassRequestDto.*;
import com.example.webDemo3.service.manageClassService.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class ClassApiController {

    @Autowired
    private ClassService classService;

    /**
     * kimpt142
     * 29/6
     * catch request to get class list with table
     * @return reponseDTO with a class list and messagedto
     */
    @PostMapping("/classtable")
    public ClassTableResponseDto getClassTale(@RequestBody ClassTableRequestDto requestModel)
    {
        ClassTableResponseDto responseDto = new ClassTableResponseDto();
        responseDto = classService.getClassTable(requestModel);
        return  responseDto;
    }

    /**
     * kimpt142
     * 29/6
     * catch request to get gifted class list
     * @return reponseDTO with a gifted class list and messagedto
     */
    @PostMapping("/giftedclasslist")
    public GiftedClassResponseDto getGiftedClassList()
    {
        GiftedClassResponseDto responseDto = new GiftedClassResponseDto();
        responseDto = classService.getGiftedClassList();
        return  responseDto;
    }

    /**
     * kimpt142
     * 29/6
     * catch request to add new class
     * @return MessageDTO
     */
    @PostMapping("/addclass")
    public AddClassResponseDto addNewClass(@RequestBody AddClassRequestDto model)
    {
        return classService.addNewClass(model);
    }

    /**
     * kimpt142
     * 30/6
     * catch request to add new gifted class
     * @return MessageDTO
     */
    @PostMapping("/addgifftedclass")
    public MessageDTO addNewGiftedClass(@RequestBody AddGiftedClassRequestDto model)
    {
        return classService.addGiftedClass(model);
    }

    /**
     * kimpt142
     * 30/6
     * catch request to edit class
     * @return MessageDTO
     */
    @PostMapping("/editclass")
    public MessageDTO editClass(@RequestBody EditClassRequestDto model)
    {
        return classService.editClass(model);
    }

    /**
     * kimpt142
     * 1/7
     * catch request to get class infor
     * @return ClassInforResponseDto include identifier, status and message
     */
    @PostMapping("/viewclassinfor")
    public ClassInforResponseDto viewClassInfor(@RequestBody ClassInforRequestDto model)
    {
        return classService.getClassInfor(model);
    }

    /**
     * kimpt142
     * 3/7
     * catch request to delete gifted class
     * @return message
     */
    @PostMapping("/deletegiftedclass")
    public MessageDTO deleteGiftedClass(@RequestBody DelGifedClassRequestDto model)
    {
        return classService.deleteGiftedClassById(model);
    }
}
