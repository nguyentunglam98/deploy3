package com.example.webDemo3.service.assignRedStarService;

import com.example.webDemo3.dto.MessageDTO;

import java.sql.Date;

public interface CreateAssignRedStarService {
    public MessageDTO delete(Date fromDate);
    public MessageDTO checkDate(Date fromDate);
    public MessageDTO create(Date fromDate);
}
