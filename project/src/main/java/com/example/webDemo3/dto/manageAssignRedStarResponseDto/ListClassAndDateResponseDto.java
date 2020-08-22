package com.example.webDemo3.dto.manageAssignRedStarResponseDto;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.User;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class ListClassAndDateResponseDto {
    List<Class> listClass;
    List<Date> listDate;
    private MessageDTO message;
}
