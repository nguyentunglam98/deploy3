package com.example.webDemo3.dto.request.manageEmulationRequestDto;

import lombok.Data;
import java.sql.Date;
import java.util.List;

/*
kimpt142 - 14/07
 */
@Data
public class AddViolationForClassRequestDto {
    private String username;
    private Integer classId;
    private Date date;
    private Integer yearId;
    private Integer roleId;
    private List<SubViolationForClassRequestDto> violationList;
}
