package com.example.webDemo3.service.impl.manageEmulationServiceImpl;

import com.example.webDemo3.dto.manageEmulationResponseDto.ViolationClassRequestResponseDto;
import com.example.webDemo3.dto.manageEmulationResponseDto.ViolationClassResponseDto;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.Violation;
import com.example.webDemo3.entity.ViolationClass;
import com.example.webDemo3.entity.ViolationClassRequest;
import com.example.webDemo3.repository.ClassRepository;
import com.example.webDemo3.repository.DayRepository;
import com.example.webDemo3.repository.ViolationRepository;
import com.example.webDemo3.service.manageEmulationService.AdditionalFunctionViolationClassService;
import com.example.webDemo3.service.manageEmulationService.ValidateEmulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;

/*
kimpt142 - 20/07
 */
@Service
public class AdditionalFunctionViolationClassServiceImpl implements AdditionalFunctionViolationClassService {
    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ViolationRepository violationRepository;

    @Autowired
    private ValidateEmulationService validateEmulationService;

    @Autowired
    private DayRepository dayRepository;

    /**
     * lamnt98
     * 16/07
     * convert violation class entity to response dto
     * @param violationClass
     * @return
     */
    @Override
    public ViolationClassResponseDto convertViolationClassFromEntityToDto(ViolationClass violationClass) {
        ViolationClassResponseDto responseDto = new ViolationClassResponseDto();
        Class newClass = classRepository.findById(violationClass.getClassId()).orElse(null);
        Violation violation = violationRepository.findById(violationClass.getClassId()).orElse(null);

        responseDto.setViolationClassId(violationClass.getId());
        responseDto.setNote(violationClass.getNote());
        responseDto.setQuantity(violationClass.getQuantity());
        responseDto.setDescription(violationClass.getViolation().getDescription());
        responseDto.setCreateDate(violationClass.getDate());
        responseDto.setCreateBy(violationClass.getCreateBy());
        responseDto.setStatus(violationClass.getStatus());
        responseDto.setHistory(violationClass.getHistory());

        //check violation null or not
        if(violation != null){
            responseDto.setSubstractGrade(violation.getSubstractGrade());
        }


        //check newClass exists or not
        if(newClass != null){
            responseDto.setClassId(newClass.getClassId());
            Integer grade = newClass.getGrade();
            String name = newClass.getGiftedClass().getName();
            responseDto.setClassName(String.valueOf(grade) + " " + name);
        }

        Integer dayId = validateEmulationService.getDayIdByDate(violationClass.getDate());
        String dayName = dayRepository.findByDayId(dayId).getDayName();
        responseDto.setDayName(dayName);

        return responseDto;
    }

    /**
     * lamnt98
     * 16/07
     * convert violation class request entity to response dto
     * @param violationClassRequest
     * @return
     */
    @Override
    public ViolationClassRequestResponseDto convertViolationClassRequestFromEntityToDto(ViolationClassRequest violationClassRequest) {
        ViolationClassRequestResponseDto responseDto = new ViolationClassRequestResponseDto();
        Violation violation = violationRepository.findById(violationClassRequest.getViolationClass().getClassId()).orElse(null);

        responseDto.setRequestId(violationClassRequest.getRequestId());
        responseDto.setViolationClassId(violationClassRequest.getViolationClass().getId());
        responseDto.setChangeDate(violationClassRequest.getDateChange());
        responseDto.setCreateBy(violationClassRequest.getCreatBy());
        responseDto.setStatus(violationClassRequest.getStatusChange());
        responseDto.setReason(violationClassRequest.getReason());
        responseDto.setQuantityNew(violationClassRequest.getQuantityNew());
        responseDto.setQuantityOld(violationClassRequest.getQuantityOld());

        //check violation null or not
        if(violation != null){
            responseDto.setSubstractGrade(violation.getSubstractGrade());
        }

        return responseDto;
    }

    /**
     * lamnt98
     * 19/07
     * add history infor when change violation of class
     * @param history
     * @param reason
     * @param username
     * @param number
     * @return
     */
    @Override
    public String addHistory(String history, String reason, String username, int number) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        //check history null or not
        if(history == null){
            history =  "<ul> <li><span class=\"font-500\">" +strDate + " - " + username + ".</span>";
        }else{
            history += "<ul> <li><span class=\"font-500\">" + strDate + " - " + username + ".</span>";
        }
        history += "<ol>Lý do: " + reason + ".</ol>";
        history += "<ol>Số lần vi phạm trước thay đổi: " + number + ".</ol> </li> </ul>";
        return  history;
    }
}
