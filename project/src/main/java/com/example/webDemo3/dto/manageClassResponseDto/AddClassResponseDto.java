package com.example.webDemo3.dto.manageClassResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.User;
import lombok.Data;
import java.util.List;

/*
kimpt142 - 30/6
 */
@Data
public class AddClassResponseDto {
    private List<User> userList;
    private Integer classId;
    private MessageDTO message;
}
