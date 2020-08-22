package com.example.webDemo3.service.manageNewsletterService;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageNewsletterResponseDto.AddNewsletterResponseDto;
import com.example.webDemo3.dto.manageNewsletterResponseDto.NewsletterListResponseDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.AddNewsletterRequestDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.ConfirmRequestNewsletterDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.EditNewsletterRequestDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.SearchNewsByStatusAndDateRequestDto;

/*
kimpt142 - 27/07
 */
public interface HandleNewsletterService {
    AddNewsletterResponseDto addNewsletter(AddNewsletterRequestDto model);
    MessageDTO editNewsletter(EditNewsletterRequestDto model);
    MessageDTO confirmRequestNewsletter(ConfirmRequestNewsletterDto model);
    NewsletterListResponseDto searchNewsletterByStatusAndDate(SearchNewsByStatusAndDateRequestDto model);
}
