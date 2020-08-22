package com.example.webDemo3.dto.request.manageEmulationRequestDto;

import lombok.Data;
import java.sql.Date;

/*
kimpt142 - 16/07
 */
@Data
public class ViewViolationOfClassRequestDto {
    private String username;
    private Integer classId;
    private Date date;
    private Integer roleId;
}
