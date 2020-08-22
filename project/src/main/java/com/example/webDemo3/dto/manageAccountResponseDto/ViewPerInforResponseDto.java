package com.example.webDemo3.dto.manageAccountResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

@Data
public class ViewPerInforResponseDto {
    private String fullName;
    private String userName;
    private String roleName;
    private String phone;
    private String email;
    private String className;
    private MessageDTO message;
}
