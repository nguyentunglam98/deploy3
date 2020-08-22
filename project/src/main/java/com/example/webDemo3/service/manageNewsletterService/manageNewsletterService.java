package com.example.webDemo3.service.manageNewsletterService;

import com.example.webDemo3.dto.manageNewsletterResponseDto.NewsletterPageResponseDto;
import com.example.webDemo3.dto.manageNewsletterResponseDto.ViewDetailLetterResponseDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.LoadHomePageRequestDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.SearchLetterRequestDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.ViewDetailLetterRequestDto;

/**
 * lamnt98 - 27/07
 */
public interface manageNewsletterService {
     NewsletterPageResponseDto getAllLetter(LoadHomePageRequestDto requestDto);
     NewsletterPageResponseDto searchLetter(SearchLetterRequestDto requestDto);
     ViewDetailLetterResponseDto viewLetter(ViewDetailLetterRequestDto requestDto);
}
