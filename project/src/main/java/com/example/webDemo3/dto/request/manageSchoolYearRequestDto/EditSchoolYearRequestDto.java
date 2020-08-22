package com.example.webDemo3.dto.request.manageSchoolYearRequestDto;

import lombok.Data;

import java.sql.Date;

/*
kimpt142 - 06/07
 */
@Data
public class EditSchoolYearRequestDto {
    private Integer schoolYearId;
    private Integer fromYear;
    private Integer toYear;
    private Date fromDate;
    private Date toDate;
}
