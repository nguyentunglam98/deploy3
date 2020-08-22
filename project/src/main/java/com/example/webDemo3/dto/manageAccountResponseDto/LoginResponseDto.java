package com.example.webDemo3.dto.manageAccountResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

@Data
public class LoginResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
//    private LoginResponseDto loginResponseDto;
    private MessageDTO message;
    private Integer roleid;
    private Integer currentYearId;
    private String AsignedClass;

    public LoginResponseDto(){}

    public LoginResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginResponseDto(MessageDTO message, Integer roleid, Integer currentYearId) {
        this.message = message;
        this.roleid = roleid;
        this.currentYearId = currentYearId;
    }
}
