package com.example.webDemo3.service.manageEmulationService;

import com.example.webDemo3.dto.manageEmulationResponseDto.ViewViolationClassListResponseDto;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.ViolationHistoryResquestDTO;

public interface ViolationHistoryService {
    public ViewViolationClassListResponseDto getHistoryViolationOfClas(ViolationHistoryResquestDTO model);
}
