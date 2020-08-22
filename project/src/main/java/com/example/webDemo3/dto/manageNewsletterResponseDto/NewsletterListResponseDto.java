package com.example.webDemo3.dto.manageNewsletterResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.Newsletter;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * lamnt98
 * 29/07
 */
@Data
public class NewsletterListResponseDto {
    private Page<Newsletter> listLetter;
    private MessageDTO message;
}
