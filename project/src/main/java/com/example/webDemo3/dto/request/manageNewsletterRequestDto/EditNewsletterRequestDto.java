package com.example.webDemo3.dto.request.manageNewsletterRequestDto;

import com.example.webDemo3.entity.Newsletter;
import lombok.Data;

/*
kimpt142 - 27/07
 */
@Data
public class EditNewsletterRequestDto {
    private Newsletter newsletter;
    private Integer roleId;
}
