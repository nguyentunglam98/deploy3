package com.example.webDemo3.dto.manageClassResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;
import com.example.webDemo3.entity.Class;
import org.springframework.data.domain.Page;

/**
 * kimpt142 - 29/6
 */
@Data
public class ClassTableResponseDto {
    private Page<Class> classList;
    private MessageDTO message;
}
