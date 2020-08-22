package com.example.webDemo3.service.manageEmulationService;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.ChangeRequestDto;

/**
 * lamnt98
 * 16/07
 */
public interface ChangeRequestService {
    public MessageDTO acceptRequest(ChangeRequestDto changeRequestDto);
    public MessageDTO rejectRequest(ChangeRequestDto changeRequestDto);
}
