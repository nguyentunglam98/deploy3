package com.example.webDemo3.service;

import com.example.webDemo3.dto.GenerateNameResponseDto;
import com.example.webDemo3.dto.request.GenerateNameRequestDto;

/*
kimpt142 - 30/6
 */
public interface GenerateAccountService {
    GenerateNameResponseDto generateAccountName(GenerateNameRequestDto model);
}
