package com.example.webDemo3.service.manageAccountService;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.manageAccountRequestDto.ChangePasswordRequestDto;

public interface ChangePasswordService {
    MessageDTO checkChangePasswordUser(ChangePasswordRequestDto user);
}
