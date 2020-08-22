package com.example.webDemo3.service.manageTimeTableService;

import com.example.webDemo3.dto.MessageDTO;
import org.apache.poi.ss.usermodel.Workbook;

import java.sql.Date;

public interface AddTimeTableService {

    public MessageDTO addTimetable(Workbook workbook, Date applyDate);

    public Boolean checkDateDuplicate(Date applyDate);
}
