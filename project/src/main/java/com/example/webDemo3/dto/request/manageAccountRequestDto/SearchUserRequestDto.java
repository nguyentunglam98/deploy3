package com.example.webDemo3.dto.request.manageAccountRequestDto;

import lombok.Data;

/**
 * kimtp142 - 28/6
 */
@Data
public class SearchUserRequestDto {
    private String userName;
    private Integer roleId;
    private Integer sortBy;
    private Integer orderBy;
    private Integer pageNumber;
}
