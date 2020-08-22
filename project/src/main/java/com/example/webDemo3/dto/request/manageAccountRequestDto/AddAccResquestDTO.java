package com.example.webDemo3.dto.request.manageAccountRequestDto;

import lombok.Data;

/**
 * kimpt142 - 27/6
 */
@Data
public class AddAccResquestDTO {
    private String userName;
    private String passWord;
    private Integer roleId;
    private Integer classId;
    private String fullName;
    private String phone;
    private String email;
}
