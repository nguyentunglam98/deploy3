package com.example.webDemo3.dto.manageEmulationResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 14/07
 * Response list for view change request
 */
@Data
public class ViewViolationClassListResponseDto {

    List<ViolationClassResponseDto> viewViolationClassList;
    private Integer totalPage;
    private MessageDTO message;
}
