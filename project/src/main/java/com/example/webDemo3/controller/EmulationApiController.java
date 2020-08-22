package com.example.webDemo3.controller;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageEmulationResponseDto.*;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.*;
import com.example.webDemo3.dto.manageEmulationResponseDto.ViewGradingEmulationResponseDto;
import com.example.webDemo3.dto.manageEmulationResponseDto.ViewViolationClassListResponseDto;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.ChangeRequestDto;
import com.example.webDemo3.dto.request.assignRedStarRequestDto.ViewAssignTaskRequestDto;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.ViewRequestDto;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.ViewViolationOfClassRequestDto;
import com.example.webDemo3.service.assignRedStarService.AssignRedStarService;
import com.example.webDemo3.service.manageEmulationService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
kimpt142 - 14/07
 */
@RestController
@RequestMapping("/api/emulation")
public class EmulationApiController {

    @Autowired
    private GradingEmulationService gradingEmulationService;

    @Autowired
    private ViewRequestService viewRequestService;

    @Autowired
    private ChangeRequestService changeRequestService;

    @Autowired
    private ViolationOfClassService violationOfClassService;

    /**
     * lamnt98
     * 14/07
     * catch request to get list request change
     * @return ViewViolationClassListResponseDto
     */
    @PostMapping("/viewrequest")
    public ViewViolationClassListResponseDto viewRequest(@RequestBody ViewRequestDto model)
    {
        return viewRequestService.viewRequest(model);
    }

    /**
     * lamnt98
     * 16/07
     * catch request to accept request change
     * @return MessageDTO
     */
    @PostMapping("/acceptrequest")
    public MessageDTO acceptRequest(@RequestBody ChangeRequestDto model)
    {
        return changeRequestService.acceptRequest(model);
    }

    /**
     * lamnt98
     * 16/07
     * catch request to reject request change
     * @return MessageDTO
     */
    @PostMapping("/rejectrequest")
    public MessageDTO rejecttRequest(@RequestBody ChangeRequestDto model)
    {
        return changeRequestService.rejectRequest(model);
    }

    /**
     * kimpt142
     * 14/07
     * catch request to get class list and violation list
     * @return reponseDTO with a class list, a violation list and messagedto
     */
    @PostMapping("/viewgradingemulation")
    public ViewGradingEmulationResponseDto viewGradingEmulation()
    {
        ViewGradingEmulationResponseDto responseDto = new ViewGradingEmulationResponseDto();
        responseDto = gradingEmulationService.getClassAndViolationList();
        return responseDto;
    }

    /**
     * kimpt142
     * 14/07
     * redstar add new emulation for a class
     * @param model include vilolation of a class
     * @return message
     */
    @PostMapping("/addgrademulation")
    public MessageDTO addViolationForClass(@RequestBody AddViolationForClassRequestDto model)
    {
        return gradingEmulationService.addViolationForClass(model);
    }

    /**
     * kimpt142
     * 16/07
     * view all vilation of a class in specific day
     * @param model include classid, username, roleid and day
     * @return message
     */
    @PostMapping("/viewviolationofclass")
    public ViewViolationClassListResponseDto getViolationOfClass(@RequestBody ViewViolationOfClassRequestDto model)
    {
        return violationOfClassService.getViolationOfAClass(model);
    }

    /**
     * kimpt142
     * 16/07
     * catch request to edit violation of a class
     * @param model include information of class's violation
     * @return message
     */
    @PostMapping("/requesteditviolation")
    public MessageDTO editViolationOfClass(@RequestBody EditViolationOfClassRequestDto model)
    {
        return violationOfClassService.editViolationOfClass(model);
    }

    /**
     * kimpt142
     * 16/07
     * get classId with username and apply date
     * @param model include username and apply day
     * @return message and classId
     */
    @PostMapping("/getClassIdOfRedStar")
    public ClassOfRedStarResponseDto getClassIdByUserAndDate(@RequestBody ClassOfRedStarRequestDto model)
    {
        return gradingEmulationService.getClassIdByUserAndDate(model);
    }

    /**
     * kimpt142
     * 20/07
     * catch request to delete the request by request id
     * @param model include request id
     * @return message
     */
    @PostMapping("/deleteViolationClassRequest")
    public MessageDTO deleteRequestById(@RequestBody DeleteRequestChangeViolationClassRequestDto model)
    {
        return violationOfClassService.deleteRequestChange(model);
    }
}
