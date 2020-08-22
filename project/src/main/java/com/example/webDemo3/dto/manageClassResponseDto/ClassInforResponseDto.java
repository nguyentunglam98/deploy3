package com.example.webDemo3.dto.manageClassResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

@Data
public class ClassInforResponseDto {
    private String classIdentifier;
    private Integer grade;
    private String giftedClassName;
    private Integer status;
    private MessageDTO message;
}
