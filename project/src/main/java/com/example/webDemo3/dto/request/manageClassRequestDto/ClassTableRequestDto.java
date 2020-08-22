package com.example.webDemo3.dto.request.manageClassRequestDto;

import lombok.Data;

/*
kimpt142 - 1/7
 */
@Data
public class ClassTableRequestDto {
    private String classIdentifier;
    private Integer grade;
    private Integer sortBy;
    private Integer orderBy;
    private Integer status;
    private Integer pageNumber;
}
