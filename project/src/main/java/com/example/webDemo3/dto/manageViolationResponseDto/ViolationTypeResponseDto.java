package com.example.webDemo3.dto.manageViolationResponseDto;

import com.example.webDemo3.entity.Violation;
import lombok.Data;

import java.util.List;

/**
 * lamnt98
 * 06/07
 */
@Data
public class ViolationTypeResponseDto {

    private Integer typeId;

    private String name;

    private Float totalGrade;

    private Integer status;

    private List<Violation> violation;
}
