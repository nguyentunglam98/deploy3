package com.example.webDemo3.service.assignRedStarService;

import com.example.webDemo3.dto.request.assignRedStarRequestDto.DownloadAssignRedStarRequestDto;

import java.io.ByteArrayInputStream;

public interface DownloadAssignRedStarService {
    public ByteArrayInputStream download(DownloadAssignRedStarRequestDto data);
}
