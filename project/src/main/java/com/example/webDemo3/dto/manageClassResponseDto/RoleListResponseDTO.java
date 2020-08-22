package com.example.webDemo3.dto.manageClassResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.Role;
import lombok.Data;

/**
 * kimpt142 - 25/6
 */
import java.util.List;

@Data
public class RoleListResponseDTO {
    private List<Role> listRole;
    private MessageDTO message;
}
