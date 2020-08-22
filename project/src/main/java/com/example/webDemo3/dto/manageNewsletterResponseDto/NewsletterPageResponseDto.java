package com.example.webDemo3.dto.manageNewsletterResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.Newsletter;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 09/08
 */
@Data
public class NewsletterPageResponseDto {
    private List<Newsletter> listLetter;
    private Integer totalPage;
    private MessageDTO message;
}
