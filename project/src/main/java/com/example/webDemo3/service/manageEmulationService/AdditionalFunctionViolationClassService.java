package com.example.webDemo3.service.manageEmulationService;

import com.example.webDemo3.dto.manageEmulationResponseDto.ViolationClassRequestResponseDto;
import com.example.webDemo3.dto.manageEmulationResponseDto.ViolationClassResponseDto;
import com.example.webDemo3.entity.ViolationClass;
import com.example.webDemo3.entity.ViolationClassRequest;

public interface AdditionalFunctionViolationClassService {
    ViolationClassResponseDto convertViolationClassFromEntityToDto(ViolationClass violationClass);
    ViolationClassRequestResponseDto convertViolationClassRequestFromEntityToDto(ViolationClassRequest violationClassRequest);
    String addHistory(String history, String reason, String username, int number);
}
