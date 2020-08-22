package com.example.webDemo3.service.manageEmulationService;

import com.example.webDemo3.dto.manageEmulationResponseDto.ViewViolationClassListResponseDto;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.ViewRequestDto;
import com.example.webDemo3.entity.ViolationClassRequest;

import java.util.List;

/**
 * lamnt98
 * 14/07
 */
public interface ViewRequestService {
    public ViewViolationClassListResponseDto viewRequest(ViewRequestDto viewRequest);
}
