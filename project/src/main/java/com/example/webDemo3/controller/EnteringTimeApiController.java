package com.example.webDemo3.controller;

import com.example.webDemo3.dto.manageEnteringViolationResponseDto.ListDayResponseDto;
import com.example.webDemo3.dto.manageEnteringViolationResponseDto.ListEnteringTimeResponseDto;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.manageEnteringViolationRequestDto.AddVioEnTimeRequestDto;
import com.example.webDemo3.dto.request.manageEnteringViolationRequestDto.DeleteEnteringTimeRequestDto;
import com.example.webDemo3.service.manageEnteringTimeService.EnteringTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class EnteringTimeApiController {

    @Autowired
    private EnteringTimeService enteringTimeService;

    /**
     * lamnt98
     * 07/07
     * catch request from client to get all day
     * @param
     * @return ListDayResponseDto
     */
    @PostMapping("/getallday")
    public ListDayResponseDto getAllDay()
    {
        return  enteringTimeService.getAllDay();
    }

    /**
     * lamnt98
     * 07/07
     * catch request from client to view all violation entering time
     * @param
     * @return ListEnteringTimeResponseDto
     */
    @PostMapping("/viewenteringtime")
    public ListEnteringTimeResponseDto viewEnteringTime()
    {
        return  enteringTimeService.getListEnteringTime();
    }

    /**
     * lamnt98
     * 07/07
     * catch request from client to delete violation entering time
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/deleteenteringtime")
    public MessageDTO deleteEnteringTime(@RequestBody DeleteEnteringTimeRequestDto model)
    {
        return  enteringTimeService.deleteEnteringTime(model);
    }

    /**
     * lamnt98
     * 07/07
     * catch request from client to add violation entering time
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/addenteringtime")
    public MessageDTO addEnteringTime(@RequestBody AddVioEnTimeRequestDto model)
    {
        return  enteringTimeService.addEnteringTime(model);
    }
}
