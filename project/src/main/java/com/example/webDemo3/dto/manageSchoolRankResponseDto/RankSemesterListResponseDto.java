package com.example.webDemo3.dto.manageSchoolRankResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;
import java.util.List;

/*
kimpt142 - 24/0
 */
@Data
public class RankSemesterListResponseDto {
    private List<RankSemesterResponseDto> rankSemesterList;
    private Integer checkEdit;
    private MessageDTO message;
}
