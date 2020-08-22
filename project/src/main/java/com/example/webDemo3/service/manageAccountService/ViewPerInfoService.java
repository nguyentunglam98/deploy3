package com.example.webDemo3.service.manageAccountService;

import com.example.webDemo3.dto.manageAccountResponseDto.ViewPerInforResponseDto;
import com.example.webDemo3.dto.request.manageAccountRequestDto.ViewPerInforRequestDto;

public interface ViewPerInfoService {
    ViewPerInforResponseDto getUserInformation(ViewPerInforRequestDto userName);
}
