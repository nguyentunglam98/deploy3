package com.example.webDemo3.service.manageAccountService;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.manageAccountRequestDto.DeleteAccountRequestDto;

/**
 * lamnt98 - 27/06
 */
public interface DeleteAccountService {
    MessageDTO deleteAccount(DeleteAccountRequestDto deleteAccount);
}
