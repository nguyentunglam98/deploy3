package com.example.webDemo3.dto.manageNewsletterResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.Newsletter;
import lombok.Data;

/**
 * lamnt98
 * 27/07
 */
@Data
public class ViewDetailLetterResponseDto {
    private Newsletter newsletter;
    private MessageDTO message;
}
