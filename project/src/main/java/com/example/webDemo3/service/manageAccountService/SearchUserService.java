package com.example.webDemo3.service.manageAccountService;

import com.example.webDemo3.dto.manageAccountResponseDto.SearchUserResponseDto;
import com.example.webDemo3.dto.request.manageAccountRequestDto.SearchUserRequestDto;

public interface SearchUserService {
    SearchUserResponseDto searchUser(SearchUserRequestDto requestModel);
}
