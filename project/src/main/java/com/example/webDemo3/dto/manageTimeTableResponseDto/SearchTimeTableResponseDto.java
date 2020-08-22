package com.example.webDemo3.dto.manageTimeTableResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchTimeTableResponseDto {
    private List<List<MorInforTimeTableDto>> morningTimeTableList;
    private List<List<AfterInforTimeTableDto>> afternoonTimeTableTableList;
    private MessageDTO message;
}
