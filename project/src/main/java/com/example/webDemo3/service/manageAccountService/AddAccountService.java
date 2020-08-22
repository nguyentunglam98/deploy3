package com.example.webDemo3.service.manageAccountService;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.manageAccountRequestDto.AddAccResquestDTO;

/**
 * kimtp142 - 27/6
 */
public interface AddAccountService {
    MessageDTO addAccount(AddAccResquestDTO resquestDTO);
}
