package com.example.webDemo3.controller;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageNewsletterResponseDto.AddNewsletterResponseDto;
import com.example.webDemo3.dto.manageNewsletterResponseDto.NewsletterPageResponseDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.*;
import com.example.webDemo3.service.manageNewsletterService.HandleNewsletterService;
import com.example.webDemo3.dto.manageNewsletterResponseDto.NewsletterListResponseDto;
import com.example.webDemo3.dto.manageNewsletterResponseDto.ViewDetailLetterResponseDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.LoadHomePageRequestDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.SearchLetterRequestDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.ViewDetailLetterRequestDto;
import com.example.webDemo3.service.manageNewsletterService.manageNewsletterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * lamnt98 - 27/07
 */
@RestController
@RequestMapping("/api/newsletter")
public class NewsletterApiController {

    @Autowired
    private HandleNewsletterService handleNewsletterService;

    @Autowired
    private manageNewsletterService manageNewsletterService;

    /**
     * kimpt142
     * 27/07
     * catch request to add newsletter
     * @return messagedto
     */
    @PostMapping("/addnewsletter")
    public AddNewsletterResponseDto addNewsletter(@RequestBody AddNewsletterRequestDto model)
    {
        return handleNewsletterService.addNewsletter(model);
    }

    /**
     * kimpt142
     * 27/07
     * catch request to edit newsletter
     * @return messagedto
     */
    @PostMapping("/editnewsletter")
    public MessageDTO editNewsletter(@RequestBody EditNewsletterRequestDto model)
    {
        return handleNewsletterService.editNewsletter(model);
    }

    /**
     * kimpt142
     * 27/07
     * catch request to confirm request newsletter
     * @return messagedto
     */
    @PostMapping("/confirmnewsletter")
    public MessageDTO confirmRequestNewsletter(@RequestBody ConfirmRequestNewsletterDto model) {
        return handleNewsletterService.confirmRequestNewsletter(model);
    }

    /**
     * kimpt142
     * 27/07
     * catch request to search newsletter by status and create date
     * @return messagedto
     */
    @PostMapping("/searchconfirmnews")
    public NewsletterListResponseDto searchNewsletterByStatusAndDate(@RequestBody SearchNewsByStatusAndDateRequestDto model) {
        return handleNewsletterService.searchNewsletterByStatusAndDate(model);
    }

    /**
     * lamnt98
     * 27/07
     * catch request from client to get all letter
     * @param model
     * @return NewsletterListResponseDto
     */
    @PostMapping("/loadhomepage")
    public NewsletterPageResponseDto loadHomePage(@RequestBody LoadHomePageRequestDto model)
    {
        return  manageNewsletterService.getAllLetter(model);
    }

    /**
     * lamnt98
     * 27/07
     * catch request from client to search letter
     * @param model
     * @return NewsletterListResponseDto
     */
    @PostMapping("/searchletter")
    public NewsletterPageResponseDto searchLetter(@RequestBody SearchLetterRequestDto model)
    {
        return  manageNewsletterService.searchLetter(model);
    }

    /**
     * lamnt98
     * 27/07
     * catch request from client to view detail letter
     * @param model
     * @return ViewDetailLetterResponseDto
     */
    @PostMapping("/viewletter")
    public ViewDetailLetterResponseDto viewLetter(@RequestBody ViewDetailLetterRequestDto model)
    {
        return  manageNewsletterService.viewLetter(model);
    }
}
