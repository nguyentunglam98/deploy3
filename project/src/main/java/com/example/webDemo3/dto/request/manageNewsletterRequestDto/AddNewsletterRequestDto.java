package com.example.webDemo3.dto.request.manageNewsletterRequestDto;

import com.example.webDemo3.entity.Newsletter;
import lombok.Data;

/**
 * kimpt142 - 27/07
 */
@Data
public class AddNewsletterRequestDto {
    private String username;
    private String header;
    private String headerImage;
    private String content;
    private Integer gim;
    private Integer roleId;
}
