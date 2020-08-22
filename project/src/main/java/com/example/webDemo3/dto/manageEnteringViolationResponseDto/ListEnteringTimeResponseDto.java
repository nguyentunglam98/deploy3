package com.example.webDemo3.dto.manageEnteringViolationResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 07/07
 */
@Data
public class ListEnteringTimeResponseDto {
    private List<ViolationEnteringTimeResponseDto> listEmteringTime;
    private MessageDTO message;
}
