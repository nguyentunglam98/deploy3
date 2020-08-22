package com.example.webDemo3.dto.manageNewsletterResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

/**
 * kimpt142 - 28/07
 */
@Data
public class AddNewsletterResponseDto {
    private Integer newsletterId;
    private MessageDTO message;
}
