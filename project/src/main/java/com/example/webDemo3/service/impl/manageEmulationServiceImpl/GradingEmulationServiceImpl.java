package com.example.webDemo3.service.impl.manageEmulationServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageClassResponseDto.ClassResponseDto;
import com.example.webDemo3.dto.manageEmulationResponseDto.ClassOfRedStarResponseDto;
import com.example.webDemo3.dto.manageEmulationResponseDto.ViewGradingEmulationResponseDto;
import com.example.webDemo3.dto.manageViolationResponseDto.ViolationTypeResponseDto;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.AddViolationForClassRequestDto;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.ClassOfRedStarRequestDto;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.SubViolationForClassRequestDto;
import com.example.webDemo3.entity.*;
import com.example.webDemo3.repository.ClassRedStarRepository;
import com.example.webDemo3.repository.SchoolYearRepository;
import com.example.webDemo3.repository.ViolationClassRepository;
import com.example.webDemo3.service.manageClassService.ClassService;
import com.example.webDemo3.service.manageEmulationService.GradingEmulationService;
import com.example.webDemo3.service.manageEmulationService.ValidateEmulationService;
import com.example.webDemo3.service.manageViolationService.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/*
kimpt142 - 14/07
 */
@Service
public class GradingEmulationServiceImpl implements GradingEmulationService {

    @Autowired
    private ClassService classService;

    @Autowired
    private ViolationService violationService;

    @Autowired
    SchoolYearRepository schoolYearRepository;

    @Autowired
    private ValidateEmulationService validateEmulationService;

    @Autowired
    private ViolationClassRepository violationClassRepository;

    @Autowired
    private ClassRedStarRepository classRedStarRepository;

    /**
     * kimpt142
     * 14/07
     * get class list and violation list to show in grading emulation
     * @return a class list, a list include violation type and violation and message
     */
    @Override
    public ViewGradingEmulationResponseDto getClassAndViolationList() {
        ViewGradingEmulationResponseDto responseDto = new ViewGradingEmulationResponseDto();
        MessageDTO message = new MessageDTO();

        List<ClassResponseDto> classList = classService.getClassList().getClassList();
        List<ViolationTypeResponseDto> vioTypeAndVioList = violationService.getListViolationAndType().getListViolationType();

        if(classList == null){
            message = Constant.CLASSLIST_NOT_EXIT;
            responseDto.setMessage(message);
            return responseDto;
        }

        if(vioTypeAndVioList == null){
            message = Constant.VIOLATION_TYPE_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }

        responseDto.setClassList(classList);
        responseDto.setVioTypeAndVioList(vioTypeAndVioList);
        responseDto.setMessage(Constant.SUCCESS);
        return responseDto;
    }

    /**
     * kimpt142
     * 14/07
     * add new grade emulation for class (insert into violation class)
     * @param model include information class, violation and date
     * @return message
     */
    @Override
    public MessageDTO addViolationForClass(AddViolationForClassRequestDto model) {

        MessageDTO message = new MessageDTO();
        String username = model.getUsername();
        Integer classId = model.getClassId();
        Date date = model.getDate();
        Integer currentYearId = model.getYearId();
        Integer roleId = model.getRoleId();
        Integer weekId = 0;
        Integer status;
        List<SubViolationForClassRequestDto> violationList = model.getViolationList();

        if(username == null || username.trim().isEmpty()){
            message = Constant.USER_NOT_EXIT;
            return message;
        }

        if(classId == null){
            message = Constant.CLASS_EMPTY;
            return message;
        }

        if(date == null){
            message = Constant.DATE_EMPTY;
            return message;
        }

        if(roleId == null){
            message = Constant.ROLE_NOT_EXIST;
            return message;
        }

        if(currentYearId == null){
            Date dateCurrent = new Date(System.currentTimeMillis());
            SchoolYear schoolCurrent = schoolYearRepository.findSchoolYearsByDate(dateCurrent);
            if(schoolCurrent != null) {
                currentYearId = schoolCurrent.getYearID();
            }
        }

        if(validateEmulationService.checkRankedDate(classId, date)){
            message = Constant.DATE_RANKED;
            return message;
        }

        SchoolYear schoolYearOfDate = schoolYearRepository.findSchoolYearsByDate(date);
        if(schoolYearOfDate == null || schoolYearOfDate.getYearID() != currentYearId){
            message = Constant.ADD_VIOLATION_NOT_CURRENTYEAR;
            return message;
        }

        if(violationList == null || violationList.size() == 0){
            message = Constant.VIOLATION_ID_NULL;
            return message;
        }
        status = 1;

        message = validateEmulationService.checkRoleForAddViolationClass(username,roleId,classId, date);
        if(message.getMessageCode() == 0){
            try {
                addViolationClassToDB(violationList,classId, date, weekId, currentYearId, status, username);
            }catch (Exception e){
                message.setMessageCode(1);
                message.setMessage(e.toString());
                return message;
            }
        }
        else {
            return message;
        }

        message = Constant.SUCCESS;
        return message;
    }

    /**
     * kimpt142
     * 16/07
     * get class is emulated by username and specific date
     * @param model include username and date
     * @return classId
     */
    @Override
    public ClassOfRedStarResponseDto getClassIdByUserAndDate(ClassOfRedStarRequestDto model) {
        ClassOfRedStarResponseDto responseDto = new ClassOfRedStarResponseDto();
        MessageDTO message = new MessageDTO();

        String username = model.getUsername();
        Date applyDate = model.getApplyDate();
        Date fromDateApply = classRedStarRepository.getBiggestClosetDateRedStar(applyDate, username);
        ClassRedStar classRedStar = classRedStarRepository.findClassRedStarByDateAndRedStar(fromDateApply, username);

        if(classRedStar == null){
            message = Constant.CLASS_RED_STAR_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }
        else{
            responseDto.setCurrentClassId(classRedStar.getClassSchool().getClassId());
            responseDto.setMessage(Constant.SUCCESS);
            return responseDto;
        }
    }

    /**
     * kimpt142 - 19/07
     * add violation list into database
     * @param violationClassList
     * @param classId
     * @param date
     * @param weekId
     * @param yearId
     * @param status
     * @param username
     */
    private void addViolationClassToDB(List<SubViolationForClassRequestDto> violationClassList,
                                       Integer classId,
                                       Date date,
                                       Integer weekId,
                                       Integer yearId,
                                       Integer status,
                                       String username){
        Integer dayId = validateEmulationService.getDayIdByDate(date);
        for(SubViolationForClassRequestDto item : violationClassList) {
            ViolationClass violationClass = new ViolationClass();
            violationClass.setClassId(classId);
            violationClass.setDate(date);
            violationClass.setViolation(new Violation(item.getViolationId()));
            violationClass.setQuantity(item.getQuantity());
            violationClass.setNote(item.getNote());
            violationClass.setWeekId(weekId);
            violationClass.setYear(new SchoolYear(yearId));
            violationClass.setDay(new Day(dayId));
            violationClass.setStatus(status);
            violationClass.setCreateBy(username);
            violationClassRepository.save(violationClass);
        }
    }
}
