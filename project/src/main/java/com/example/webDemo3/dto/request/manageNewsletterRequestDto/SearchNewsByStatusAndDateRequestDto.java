package com.example.webDemo3.dto.request.manageNewsletterRequestDto;

import lombok.Data;

import java.sql.Date;

/*
kimpt142 - 27/07
 */
@Data
public class SearchNewsByStatusAndDateRequestDto {
    private Integer status;
    private Date createDate;
    private String userName;
    private Integer pageNumber;
}
