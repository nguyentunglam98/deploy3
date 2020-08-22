package com.example.webDemo3.controller;

import com.example.webDemo3.dto.*;
import com.example.webDemo3.dto.manageViolationResponseDto.ListVioAndTypeResponseDto;
import com.example.webDemo3.dto.manageViolationResponseDto.ListViolationTypeResponseDto;
import com.example.webDemo3.dto.manageViolationResponseDto.ViewViolationResponseDto;
import com.example.webDemo3.dto.manageViolationResponseDto.ViewViolationTypeResponseDto;
import com.example.webDemo3.dto.request.manageViolationRequestDto.*;
import com.example.webDemo3.service.manageViolationService.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * lamnt98
 * 06/07
 */
@RestController
@RequestMapping("/api/admin")
public class ViolationApiController {
    @Autowired
    private ViolationService violationService;

    /**
     * lamnt98
     * 06/07
     * catch request from client to get all list violation and type
     * @param
     * @return ListVioAndTypeResponseDto
     */
    @PostMapping("/violationandviolationtype")
    public ListVioAndTypeResponseDto listViolationAndType()
    {
        return violationService.getListViolationAndType();
    }

    /**
     * lamnt98
     * 06/07
     * catch request from client to get violation by violationId
     * @param model
     * @return ViewViolationResponseDto
     */
    @PostMapping("/getviolation")
    public ViewViolationResponseDto getViolation(@RequestBody ViewViolationRequestDto model)
    {
        return violationService.getViolationById(model);
    }

    /**
     * lamnt98
     * 06/07
     * catch request from client to edit violation
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/editviolation")
    public MessageDTO getViolation(@RequestBody EditViolationRequestDto model)
    {
        return violationService.editViolation(model);
    }

    /**
     * lamnt98
     * 06/07
     * catch request from client to add violation
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/addviolation")
    public MessageDTO addViolation(@RequestBody AddViolationRequestDto model)
    {
        return violationService.addViolation(model);
    }

    /**
     * lamnt98
     * 06/07
     * catch request from client to delete violation
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/deleteviolation")
    public MessageDTO deleteViolation(@RequestBody DeleteViolationRequestDio model)
    {
        return violationService.deleteViolation(model);
    }

    /**
     * lamnt98
     * 06/07
     * catch request from client to get violation type by violationId
     * @param model
     * @return ViewViolationTypeResponseDto
     */
    @PostMapping("/getviolationtype")
    public ViewViolationTypeResponseDto getViolation(@RequestBody ViewViolatoinTypeRequestDto model)
    {
        return violationService.getViolationTypeById(model);
    }

    /**
     * lamnt98
     * 06/07
     * catch request from client to edit violation type
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/editviolationtype")
    public MessageDTO editViolationType(@RequestBody EditViolationTypeRequestDto model)
    {
        return violationService.editViolationType(model);
    }

    /**
     * lamnt98
     * 06/07
     * catch request from client to add violation type
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/addviolationtype")
    public MessageDTO addViolationType(@RequestBody AddViolationTypeRequestDto model)
    {
        return violationService.addViolationType(model);
    }

    /**
     * lamnt98
     * 06/07
     * catch request from client to delete violation type
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/deleteviolationtype")
    public MessageDTO deleteViolationType(@RequestBody DeleteViolationTypeRequestDto model)
    {
        return violationService.deleteViolationType(model);
    }

    /**
     * lamnt98
     * 06/07
     * catch request from client to get all violation type
     * @param
     * @return ListViolationTypeResponseDto
     */
    @PostMapping("/getlistviolationtype")
    public ListViolationTypeResponseDto getListViolationType()
    {
        return violationService.getAllViolationType();
    }

}
