package com.example.webDemo3.service.impl.manageSchoolRankWeekImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.DateViolationClassDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ListDateResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewSchoolWeekHistoryResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.CreateRankWeekRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.EditRankWeekRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.ViewSchoolWeekHistoryRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.ViewWeekAnDateListRequestDto;
import com.example.webDemo3.entity.*;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.exception.MyException;
import com.example.webDemo3.repository.*;
import com.example.webDemo3.service.manageEmulationService.ValidateEmulationService;
import com.example.webDemo3.service.manageSchoolRankWeek.CreateAndEditSchoolRankWeekService;
import com.example.webDemo3.service.manageSchoolRankWeek.SortSchoolRankWeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * lamnt98
 * 21/07
 */

@Service
public class CreateAndEditSchoolRankWeekServiceImpl implements CreateAndEditSchoolRankWeekService {

    @Autowired
    private SchoolRankWeekRepository schoolRankWeekRepository;

    @Autowired
    private ViolationClassRepository violationClassRepository;

    @Autowired
    private ValidateEmulationService validateEmulationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private SchoolWeekRepository schoolWeekRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ViolationTypeRepository violationTypeRepository;

    @Autowired
    private SortSchoolRankWeekService sortSchoolRankWeekService;

    @Autowired
    private ViolationClassRequestRepository violationClassRequestRepository;

    @Autowired
    private SchoolYearRepository schoolYearRepository;

    @Autowired
    private AdditionFunctionSchoolRankServiceImpl additionFunctionSchoolRankService;

    /**
     * lamnt98
     * 22/07
     * Load list date which has not rank in month
     * @return
     */
    @Override
    public ListDateResponseDto loadListDate() {
        ListDateResponseDto responseDto = new ListDateResponseDto();
        List<DateViolationClassDto> dateResponseList = new ArrayList<>();
        List<Date> dateList = new ArrayList<>();
        MessageDTO message = new MessageDTO();

        Date biggestDate = null;

        try{
            biggestDate = violationClassRepository.findBiggestDateRanked();
            dateList = violationClassRepository.findListDateByCondition(biggestDate);

            //check dateList empty or not
            if(dateList.size() == 0){
                message = Constant.DATE_LIST_EMPTY;
                responseDto.setMessage(message);
                return responseDto;
            }

            for(int i = 0; i < dateList.size(); i++){
                Date date = dateList.get(i);
                String dayName = getDayNameByDate(date);

                DateViolationClassDto dateResponseDto = new DateViolationClassDto();

                dateResponseDto.setDate(date);
                dateResponseDto.setDayName(dayName);
                dateResponseDto.setIsCheck(0);

                dateResponseList.add(dateResponseDto);
            }

            message = Constant.SUCCESS;
            responseDto.setDateList(dateResponseList);
            responseDto.setMessage(message);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            responseDto.setMessage(message);
            return responseDto;
        }

        return responseDto;
    }

    /**
     * lamnt98
     * 22/07
     * create rank week for class
     * @return
     */
    @Override
    @Transactional
    public MessageDTO createRankWeek(CreateRankWeekRequestDto requestDto) {
        MessageDTO message = new MessageDTO();
        try {
            message = create(requestDto);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return message;
    }

    private MessageDTO create(CreateRankWeekRequestDto requestDto) throws Exception{
        MessageDTO message = new MessageDTO();

        String userName = requestDto.getUserName();
        Integer week = requestDto.getWeek();
        Integer currentYearId = requestDto.getCurrentYearId();
        Date createDate = null;

        List<DateViolationClassDto> dateList = requestDto.getDateList();
        List<Class> classList = new ArrayList<>();
        SchoolWeek schoolWeek;
        SchoolYear schoolYear;
        List<SchoolRankWeek> schoolRankWeekList = new ArrayList<>();
        Integer weekId;
        Double allTotalGrade;
        Integer monthId = 0;
        boolean edit = false;
        User user;
        String history = "";

        try {
            //check userName empty or not
            if(userName.isEmpty()){
                message = Constant.USERNAME_EMPTY;
                return message;
            }

            user = userRepository.findUserByUsername(userName);
            //check user null or not
            if(user == null){
                message = Constant.USER_NOT_EXIT;
                return message;
            }

            //check user have permisson or not
            if(user.getRole().getRoleId() != Constant.ROLEID_ADMIN){
                message = Constant.NOT_ACCEPT_CREATE_RANK_WEEK;
                return message;
            }

            createDate = additionFunctionSchoolRankService.convertDateInComputerToSqlDate();

            //check userName empty or not
            if(week == null){
                message = Constant.WEEK_NAME_EMPTY;
                return message;
            }

            //check currentYearId null or not
            if(currentYearId == null){
                message = Constant.YEAR_ID_NULL;
                return message;
            }

            schoolYear = schoolYearRepository.findById(currentYearId).orElse(null);

            //check schoolYear exists or not
            if(schoolYear == null){
                message = Constant.SCHOOLYEAR_EMPTY;
                return message;
            }

            //check dateList null or not
            if(dateList == null){
                message = Constant.DATE_LIST_EMPTY;
                return message;
            }

            schoolWeek = schoolWeekRepository.findSchoolWeeksByWeekAndYearId(week,currentYearId);
            //check week exist or not
            if(schoolWeek != null){
                message = Constant.SCHOOL_WEEK_EXISTS;
                return message;
            }

            history = additionFunctionSchoolRankService.addHistory("",userName,createDate);
            schoolWeek = new SchoolWeek();
            schoolWeek.setWeek(week);
            schoolWeek.setMonthID(monthId);
            schoolWeek.setYearId(currentYearId);
            schoolWeek.setHistory(history);
            schoolWeek.setCreateDate(createDate);
            schoolWeekRepository.save(schoolWeek);

            weekId = schoolWeekRepository.findSchoolWeeksByWeekAndYearId(week,currentYearId).getWeekID();

            classList = classRepository.findAll();
            allTotalGrade = violationTypeRepository.sumAllTotalGradeViolationTypeActive();

            message = createOrEditSchoolRankWeek(createDate,userName,classList, dateList,allTotalGrade, schoolRankWeekList,weekId,edit,message);


        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            throw new MyException(message.getMessage());
        }
        return message;
    }

    /**
     * lamnt98
     * 22/07
     * load date which hasn't been ranked and date which is rnked follow week
     * @return
     */
    @Override
    public ListDateResponseDto loadEditListDate(ViewWeekAnDateListRequestDto requestDto) {
        ListDateResponseDto responseDto = new ListDateResponseDto();
        List<DateViolationClassDto> dateResponseList = new ArrayList<>();
        List<Date> dateList = new ArrayList<>();
        List<Date> newDateList = new ArrayList<>();
        Integer weekId = requestDto.getWeekId();
        Integer week = null;
        SchoolWeek schoolWeek = new SchoolWeek();
        MessageDTO message = new MessageDTO();

        Date biggestDate = null;
        Date minDate = null;

        try{

            //check weekId null or not
            if(weekId == null){
                message = Constant.WEEK_ID_NULL;
                responseDto.setMessage(message);
            }

            minDate = violationClassRepository.findMinDateByWeekId(weekId);
            biggestDate = violationClassRepository.findBiggestDateRankedOfEditRank(minDate);
            dateList = violationClassRepository.findListDateByCondition(biggestDate);

            for(int i = 0; i < dateList.size(); i++){
                Date date = dateList.get(i);
                String dayName = getDayNameByDate(date);

                DateViolationClassDto dateResponseDto = new DateViolationClassDto();

                dateResponseDto.setDate(date);
                dateResponseDto.setDayName(dayName);
                dateResponseDto.setIsCheck(0);

                dateResponseList.add(dateResponseDto);
            }

            newDateList = violationClassRepository.findListDateByWeekId(weekId);
            schoolWeek = schoolWeekRepository.findById(weekId).orElse(null);

            //check schoolWeek null or not
            if(schoolWeek != null){
                week = schoolWeek.getWeek();
            }
            for(int i = 0; i < newDateList.size(); i++){
                Date date = newDateList.get(i);
                String dayName = getDayNameByDate(date);

                DateViolationClassDto dateResponseDto = new DateViolationClassDto();

                dateResponseDto.setDate(date);
                dateResponseDto.setDayName(dayName);
                dateResponseDto.setIsCheck(1);
                dateResponseDto.setWeek(week);

                dateResponseList.add(dateResponseDto);
            }

            Collections.sort(dateResponseList, new Comparator<DateViolationClassDto>() {
                @Override
                public int compare(DateViolationClassDto o1, DateViolationClassDto o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });

            //check dateList empty or not
            if(dateResponseList == null || dateResponseList.size() == 0){
                message = Constant.DATE_LIST_EMPTY;
                responseDto.setMessage(message);
                return responseDto;
            }

            message = Constant.SUCCESS;
            responseDto.setDateList(dateResponseList);
            responseDto.setMessage(message);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            responseDto.setMessage(message);
            return responseDto;
        }

        return responseDto;
    }


    /**
     * lamnt98
     * 22/07
     * edit rank week
     * @return
     */
    @Override
    @Transactional
    public MessageDTO editRankWeek(EditRankWeekRequestDto requestDto) {
        MessageDTO message = new MessageDTO();
        try {
            message = edit(requestDto);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return message;
    }

    /**
     * lamnt98
     * 29/07
     * get history of school week
     * @return ViewSchoolWeekHistoryResponseDto
     */
    @Override
    public ViewSchoolWeekHistoryResponseDto viewSchoolWeekHistory(ViewSchoolWeekHistoryRequestDto requestDto) {

        ViewSchoolWeekHistoryResponseDto responseDto = new ViewSchoolWeekHistoryResponseDto();

        Integer weekId = requestDto.getWeekId();
        SchoolWeek schoolWeek;
        String history = "";
        MessageDTO message = new MessageDTO();


        try{

            //check weekId null or not
            if(weekId == null){
                message = Constant.WEEK_ID_NULL;
                responseDto.setMessage(message);
                return responseDto;
            }

            schoolWeek = schoolWeekRepository.findById(weekId).orElse(null);
            //check schoolWeek exist or not
            if(schoolWeek == null){
                message = Constant.SCHOOL_WEEK_NOT_EXIST;
                responseDto.setMessage(message);
                return  responseDto;
            }

            history = schoolWeek.getHistory();

            //check history is empty or not
            if(history == null || history.isEmpty()){
                message = Constant.HISTORY_IS_EMPTY;
                responseDto.setMessage(message);
                return responseDto;
            }

            message = Constant.SUCCESS;
            responseDto.setMessage(message);
            responseDto.setHistory(history);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            responseDto.setMessage(message);
            return responseDto;
        }

        return responseDto;
    }

    private MessageDTO edit(EditRankWeekRequestDto requestDto) throws Exception{

        Integer weekId = requestDto.getWeekId();
        Integer week = requestDto.getWeek();
        String userName = requestDto.getUserName();
        Date createDate = null;
        User user = null;

        List<DateViolationClassDto> dateList = requestDto.getDateList();
        MessageDTO message = new MessageDTO();
        SchoolWeek schoolWeek;
        SchoolWeek newSchoolWeek;
        List<Class> classList = new ArrayList<>();
        Double allTotalGrade;
        boolean edit = true;
        List<SchoolRankWeek> schoolRankWeekList = new ArrayList<>();
        String history = "";


        //check weekId null or not
        if(weekId == null){
            message = Constant.WEEK_ID_NULL;
            return  message;
        }

        //check weekName null or not
        if(week == null){
            message = Constant.WEEK_NAME_EMPTY;
            return message;
        }

        createDate = additionFunctionSchoolRankService.convertDateInComputerToSqlDate();

        //check dateList null or not
        if(dateList == null || dateList.size() == 0){
            message = Constant.DATE_LIST_EMPTY;
            return message;
        }

        try{
            //check userName empty or not
            if(userName.isEmpty()){
                message = Constant.USERNAME_EMPTY;
                return message;
            }

            user = userRepository.findUserByUsername(userName);
            //check user null or not
            if(user == null){
                message = Constant.USER_NOT_EXIT;
                return message;
            }

            //check user have permisson or not
            if(user.getRole().getRoleId() != Constant.ROLEID_ADMIN){
                message = Constant.NOT_ACCEPT_EDIT_RANK_WEEK;
                return message;
            }

            schoolWeek = schoolWeekRepository.findById(weekId).orElse(null);
            //check schoolWeek exists or not
            if(schoolWeek == null){
                message = Constant.SCHOOL_WEEK_NOT_EXIST;
                return message;
            }
            else if(schoolWeek.getMonthID() != 0){
                message = Constant.RANKWEEK_NOT_EDIT;
                return message;
            }

            newSchoolWeek = schoolWeekRepository.findSchoolWeeksByWeekAndYearId(week,schoolWeek.getYearId());

            //check week exist or not
            if(newSchoolWeek != null && schoolWeek.getWeekID() != newSchoolWeek.getWeekID()){
                message = Constant.SCHOOL_WEEK_EXISTS;
                return message;
            }
            schoolWeek.setWeek(week);
            history = additionFunctionSchoolRankService.addHistory(schoolWeek.getHistory(),userName,createDate);
            schoolWeek.setHistory(history);
            schoolWeekRepository.save(schoolWeek);

            classList = classRepository.findAll();
            allTotalGrade = violationTypeRepository.sumAllTotalGradeViolationTypeActive();

            message = createOrEditSchoolRankWeek(createDate,userName,classList, dateList,allTotalGrade, schoolRankWeekList,weekId,edit,message);

        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            throw new MyException(message.getMessage());
        }

        return message;
    }


    public String getDayNameByDate(Date date) {
        LocalDate localDate = date.toLocalDate();
        DayOfWeek day = localDate.getDayOfWeek();
        Integer dayId = day.getValue();
        String dayName = dayRepository.findByDayId(dayId).getDayName();
        return dayName;
    }

    public MessageDTO createOrEditSchoolRankWeek(Date createDate,String userName, List<Class> classList, List<DateViolationClassDto> dateList,Double allTotalGrade, List<SchoolRankWeek> schoolRankWeekList,Integer weekId,boolean edit, MessageDTO message) throws Exception{
        List<ViolationClass> violationClassList = new ArrayList<>();
        List<SchoolRankWeek> deleteList = new ArrayList<>();
        Integer newSize = 0;
        Integer sizeDate = 0;
        for(int i = 0; i < dateList.size(); i++){
            if(dateList.get(i).getIsCheck() == 1){
                sizeDate++;
            }
        }
        for(Class newClass: classList){
            Double allScore = 0.0;
            for(DateViolationClassDto date: dateList){
                Integer isCheck = date.getIsCheck();

                //check isCheck = 1 or not
                if(isCheck == 1){
                    allScore += allTotalGrade;
                    Double totalSubTractGrade = 0.0;
                    violationClassList = violationClassRepository.findByDateClassAndStatus(date.getDate(),newClass.getClassId(),1);

                    for(ViolationClass violationClass: violationClassList){
                        ViolationClassRequest classRequest = violationClassRequestRepository.findClassRequestByIdAndStatus(Long.valueOf(violationClass.getId()),0);
                        //check violation class request exist or not
                        if(classRequest != null){
                            String newMessage = Constant.RANK_HAS_VIOLATION_CLASS_REQUEST_NOT_EXCEPT_EXIST.getMessage();
                            message.setMessageCode(Constant.RANK_HAS_VIOLATION_CLASS_REQUEST_NOT_EXCEPT_EXIST.getMessageCode());
                            message.setMessage( "Ngày " + date.getDate() + " " + newMessage);
                            throw new MyException(message.getMessage());
                        }
                        violationClass.setWeekId(weekId);
                        totalSubTractGrade = (double)violationClass.getQuantity() * violationClass.getViolation().getSubstractGrade();
                        allScore = allScore - totalSubTractGrade;
                        violationClassRepository.save(violationClass);
                    }
                }
                //check isCheck = 0 or not
                if(isCheck == 0){
                    violationClassList = violationClassRepository.findByDateClassAndStatus(date.getDate(),newClass.getClassId(),1);

                    for(ViolationClass violationClass: violationClassList){
                        SchoolRankWeek schoolRankWeek = schoolRankWeekRepository.findSchoolRankWeekByWeekIdAndClassId(violationClass.getWeekId(), violationClass.getClassId());
                        //check schoolRankWeek is not null to delete
                        if(schoolRankWeek != null){
                            deleteList.add(schoolRankWeek);
                            schoolRankWeekRepository.delete(schoolRankWeek);
                        }
                        violationClass.setWeekId(0);
                        violationClassRepository.save(violationClass);
                    }
                }
            }
            //check size = 0 or not
            if(sizeDate != 0){
                Double learningGrade = Constant.LEARNING_GRADE;
                Double movementGrade = Constant.MOVEMENT_GRADE;
                Double laborGrade = Constant.LABOR_GRADE;
                Double EMULATION_GRADE = round(allScore / sizeDate);
                SchoolRankWeek newSchoolRankWeek = schoolRankWeekRepository.findSchoolRankWeekByWeekIdAndClassId(weekId,newClass.getClassId());

                //check newSchoolRankWeek null or not to save leanringGrade, movementGrade, laborGrade change
                if(newSchoolRankWeek != null){
                    learningGrade = newSchoolRankWeek.getLearningGrade();
                    movementGrade = newSchoolRankWeek.getMovementGrade();
                    laborGrade = newSchoolRankWeek.getLaborGrade();
                }

                //save leanringGrade, movementGrade, laborGrade of which rank class in date change was delete
                for(SchoolRankWeek schoolRankWeek : deleteList){
                    if(schoolRankWeek.getSchoolRankWeekId().getWEEK_ID() == weekId && schoolRankWeek.getSchoolRankWeekId().getSchoolClass().getClassId() == newClass.getClassId()){
                        learningGrade = schoolRankWeek.getLearningGrade();
                        movementGrade = schoolRankWeek.getMovementGrade();
                        laborGrade = schoolRankWeek.getLaborGrade();
                    }
                }
                Double TOTAL_GRADE = round(EMULATION_GRADE + learningGrade + movementGrade + laborGrade);

                SchoolRankWeekId schoolRankWeekId = new SchoolRankWeekId();
                schoolRankWeekId.setSchoolClass(new Class(newClass.getClassId()));
                schoolRankWeekId.setWEEK_ID(weekId);

                SchoolRankWeek schoolRankWeek = new SchoolRankWeek();
                schoolRankWeek.setSchoolRankWeekId(schoolRankWeekId);
                schoolRankWeek.setEmulationGrade(EMULATION_GRADE);
                schoolRankWeek.setLaborGrade(laborGrade);
                schoolRankWeek.setLearningGrade(learningGrade);
                schoolRankWeek.setMovementGrade(movementGrade);
                schoolRankWeek.setTotalGrade(TOTAL_GRADE);

                schoolRankWeekList.add(schoolRankWeek);
            }
        }

        //remove class have violation
        for(SchoolRankWeek schoolRankWeek : schoolRankWeekList){
            Class newClass = classRepository.findById(schoolRankWeek.getSchoolRankWeekId().getSchoolClass().getClassId()).orElse(null);
            if(newClass != null){
                classList.remove(newClass);
            }
        }

        //check size o 0 or not
        if(newSize != 0){
            for(Class newClass: classList){
                Double learningGrade = Constant.LEARNING_GRADE;
                Double movementGrade = Constant.MOVEMENT_GRADE;
                Double laborGrade = Constant.LABOR_GRADE;

                SchoolRankWeek newSchoolRankWeek = schoolRankWeekRepository.findSchoolRankWeekByWeekIdAndClassId(weekId,newClass.getClassId());
                //check newSchoolRankWeek null or not to save leanringGrade, movementGrade, laborGrade change
                if(newSchoolRankWeek != null){
                    learningGrade = newSchoolRankWeek.getLearningGrade();
                    movementGrade = newSchoolRankWeek.getMovementGrade();
                    laborGrade = newSchoolRankWeek.getLaborGrade();
                }

                //save leanringGrade, movementGrade, laborGrade of which rank class in date change was delete
                for(SchoolRankWeek schoolRankWeek : deleteList){
                    if(schoolRankWeek.getSchoolRankWeekId().getWEEK_ID() == weekId && schoolRankWeek.getSchoolRankWeekId().getSchoolClass().getClassId() == newClass.getClassId()){
                        learningGrade = schoolRankWeek.getLearningGrade();
                        movementGrade = schoolRankWeek.getMovementGrade();
                        laborGrade = schoolRankWeek.getLaborGrade();
                    }
                }

                Double EMULATION_GRADE = allTotalGrade;
                Double TOTAL_GRADE = round(EMULATION_GRADE + learningGrade + movementGrade + laborGrade);

                SchoolRankWeekId schoolRankWeekId = new SchoolRankWeekId();
                schoolRankWeekId.setSchoolClass(new Class(newClass.getClassId()));
                schoolRankWeekId.setWEEK_ID(weekId);

                SchoolRankWeek schoolRankWeek = new SchoolRankWeek();
                schoolRankWeek.setSchoolRankWeekId(schoolRankWeekId);
                schoolRankWeek.setEmulationGrade(EMULATION_GRADE);
                schoolRankWeek.setLaborGrade(laborGrade);
                schoolRankWeek.setLearningGrade(learningGrade);
                schoolRankWeek.setMovementGrade(movementGrade);
                schoolRankWeek.setTotalGrade(TOTAL_GRADE);

                schoolRankWeekList.add(schoolRankWeek);
            }
        }

        //check schoolRankWeekList null or not
        if((schoolRankWeekList == null || schoolRankWeekList.size() == 0) && !edit){
            message = Constant.SCHOOL_RANK_WEEK_NULL;
            throw new MyException(message.getMessage());
        }
        List<SchoolRankWeek> newSchoolRankWeekList = sortSchoolRankWeekService.arrangeSchoolRankWeek(schoolRankWeekList);

        for(int i = 0; i < newSchoolRankWeekList.size(); i++){
            SchoolRankWeek schoolRankWeek = newSchoolRankWeekList.get(i);
            schoolRankWeekRepository.save(schoolRankWeek);
        }

        message = Constant.SUCCESS;
        return message;
    }

    private double round(Double input) {
        return (double) Math.round(input * 100) / 100;
    }
}
