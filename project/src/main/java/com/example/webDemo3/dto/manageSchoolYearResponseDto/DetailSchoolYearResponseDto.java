package com.example.webDemo3.dto.manageSchoolYearResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

import javax.persistence.Column;
import java.sql.Date;

/*
kimpt142 - 09/07
 */
@Data
public class DetailSchoolYearResponseDto {
    private Date fromDate;
    private Date toDate;
    private Integer fromYear;
    private Integer toYear;
    private MessageDTO message;
}
