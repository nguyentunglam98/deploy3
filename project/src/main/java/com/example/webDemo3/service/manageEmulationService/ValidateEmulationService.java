package com.example.webDemo3.service.manageEmulationService;

import com.example.webDemo3.dto.MessageDTO;

import java.sql.Date;
import java.sql.Time;

/*
kimpt142 - 14/07
 */
public interface ValidateEmulationService {
    Boolean checkRoleForEmulate(Integer classID, String username, Date dayEmulate);
    Boolean checkRankedDateByViolationId(Long violationClassId);
    Integer getDayIdByDate(Date date);
    Boolean checkMonitorOfClass(Integer classId, String username);
    MessageDTO checkRoleForAddViolationClass(String username, Integer roleId, Integer classId, Date addDate);
    MessageDTO checkRoleForEditViolationClass(String username, Integer roleId,Integer classId, Date date);
    Boolean checkRankedDate(Integer classId,Date date);
}
