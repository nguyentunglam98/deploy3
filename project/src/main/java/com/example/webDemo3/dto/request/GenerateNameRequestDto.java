package com.example.webDemo3.dto.request;

import lombok.Data;

/*
kimpt142 - 30/6
 */
@Data
public class GenerateNameRequestDto {
    private Integer roleId;
    private Integer classId;

    public GenerateNameRequestDto() {
    }

    public GenerateNameRequestDto(Integer roleId, Integer classId) {
        this.roleId = roleId;
        this.classId = classId;
    }
}
