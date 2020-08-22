package com.example.webDemo3.dto.request.manageEmulationRequestDto;

import lombok.Data;

import java.sql.Date;

/**
 * kimpt142 - 27/6
 */
@Data
public class ViolationHistoryResquestDTO {
    private Date fromDate;
    private Date toDate;
    private Integer giftedId;
    private Integer fromYear;
    private Integer toYear;
    private Integer pageNumber;
}
