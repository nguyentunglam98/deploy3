package com.example.webDemo3.dto.manageSchoolYearResponseDto;

import lombok.Data;
import java.sql.Date;

/*
kimpt142 - 06/07
 */
@Data
public class SchoolYearResponseDto {
    private Integer schoolYearId;
    private String YearName;
    private Date fromDate;
    private Date toDate;
}
