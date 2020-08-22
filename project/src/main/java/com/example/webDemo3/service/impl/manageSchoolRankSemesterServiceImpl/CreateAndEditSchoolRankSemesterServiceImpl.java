package com.example.webDemo3.service.impl.manageSchoolRankSemesterServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.*;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.*;
import com.example.webDemo3.entity.*;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.exception.MyException;
import com.example.webDemo3.repository.*;
import com.example.webDemo3.service.manageSchoolRankWeek.AdditionFunctionSchoolRankService;
import com.example.webDemo3.service.manageSchoolRankSemesterService.CreateAndEditSchoolRankSemester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CreateAndEditSchoolRankSemesterServiceImpl implements CreateAndEditSchoolRankSemester {

    @Autowired
    private SchoolYearRepository schoolYearRepository;

    @Autowired
    private SchoolMonthRepository schoolMonthRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolSemesterRepository schoolSemesterRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SchoolRankMonthRepository schoolRankMonthRepository;

    @Autowired
    private SchoolRankSemesterRepository schoolRankSemesterRepository;

    @Autowired
    private AdditionFunctionSchoolRankService additionFunctionSchoolRankService;

    @Override
    public ListMonthSchoolRankResponseDto loadListMonth(ListMonthSchoolRankRequestDto requestDto) {
        ListMonthSchoolRankResponseDto responseDto = new ListMonthSchoolRankResponseDto();
        List<SchoolMonthDto> monthListDto = new ArrayList<>();
        List<SchoolMonth> monthList =new ArrayList<>();
        MessageDTO message = new MessageDTO();

        Integer currentYearId = requestDto.getCurrentYearId();
        SchoolYear schoolYear = null;

        try{
            if(currentYearId == null){
                message = Constant.YEAR_ID_NULL;
                responseDto.setMessage(message);
                return  responseDto;
            }

            schoolYear = schoolYearRepository.findById(currentYearId).orElse(null);

            //check year exist or not
            if(schoolYear == null){
                message = Constant.YEAR_ID_NULL;
                responseDto.setMessage(message);
                return  responseDto;
            }

            monthList = schoolMonthRepository.findSchoolMonthNotRank(currentYearId);

            if(monthList == null || monthList.size() == 0){
                message = Constant.LIST_MONTH_NULL;
                responseDto.setMessage(message);
                return  responseDto;
            }

            for(SchoolMonth schoolMonth : monthList){
                SchoolMonthDto schoolMonthDto = new SchoolMonthDto();
                schoolMonthDto.setMonthId(schoolMonth.getMonthId());
                schoolMonthDto.setMonth(schoolMonth.getMonth());
                schoolMonthDto.setSemesterId(schoolMonth.getSemesterId());
                schoolMonthDto.setIsCheck(0);

                monthListDto.add(schoolMonthDto);
            }

            message = Constant.SUCCESS;
            responseDto.setMessage(message);
            responseDto.setMonthList(monthListDto);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            responseDto.setMessage(message);
            return responseDto;
        }
        return  responseDto;
    }

    @Override
    @Transactional
    public MessageDTO createRankSemester(CreateRankSemesterRequestDto requestDto) {
        MessageDTO message = new MessageDTO();
        try {
            message = createRankSemesterTransaction(requestDto);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return message;
    }

    @Override
    public ListMonthSchoolRankResponseDto loadEditListMonth(ViewMonthOfEditRankSemesterRequestDto requestDto) {
        ListMonthSchoolRankResponseDto responseDto = new ListMonthSchoolRankResponseDto();
        List<SchoolMonthDto> monthListDto = new ArrayList<>();
        List<SchoolMonth> monthList = new ArrayList<>();
        List<SchoolMonth> newMonthList = new ArrayList<>();
        MessageDTO message = new MessageDTO();

        Integer semesterId = requestDto.getSemesterId();
        Integer currentYearId = requestDto.getCurrentYearId();
        SchoolSemester schoolSemester = null;
        SchoolYear schoolYear = null;
        Integer semester = null;

        try{
            if(currentYearId == null){
                message = Constant.YEAR_ID_NULL;
                responseDto.setMessage(message);
                return  responseDto;
            }

            schoolYear = schoolYearRepository.findById(currentYearId).orElse(null);

            //check year exist or not
            if(schoolYear == null){
                message = Constant.YEAR_ID_NULL;
                responseDto.setMessage(message);
                return  responseDto;
            }

            monthList = schoolMonthRepository.findSchoolMonthNotRank(currentYearId);

            for(SchoolMonth schoolMonth : monthList){
                SchoolMonthDto schoolMonthDto = new SchoolMonthDto();
                schoolMonthDto.setMonthId(schoolMonth.getMonthId());
                schoolMonthDto.setMonth(schoolMonth.getMonth());
                schoolMonthDto.setSemesterId(schoolMonth.getSemesterId());
                schoolMonthDto.setRankCreateDate(schoolMonth.getCreateDate());
                schoolMonthDto.setIsCheck(0);

                monthListDto.add(schoolMonthDto);
            }

            //check weekId null or not
            if(semesterId == null){
                message = Constant.SCHOOL_SEMESTER_ID_NULL;
                responseDto.setMessage(message);
            }

            schoolSemester = schoolSemesterRepository.findById(semesterId).orElse(null);

            //check school month exist or not
            if(schoolSemester == null){
                message = Constant.SCHOOL_SEMESTER_NOT_EXISTS;
                responseDto.setMessage(message);
                return responseDto;
            }

            newMonthList = schoolMonthRepository.findSchoolMonthBySemesterIdAndYearId(semesterId,currentYearId);

            for(SchoolMonth schoolMonth : newMonthList){
                SchoolMonthDto schoolMonthDto = new SchoolMonthDto();
                schoolMonthDto.setMonthId(schoolMonth.getMonthId());
                schoolMonthDto.setMonth(schoolMonth.getMonth());
                schoolMonthDto.setSemesterId(schoolMonth.getSemesterId());
                schoolMonthDto.setRankCreateDate(schoolMonth.getCreateDate());
                schoolMonthDto.setIsCheck(1);

                monthListDto.add(schoolMonthDto);
            }

            //check list week empty or not
            if(monthListDto == null || monthListDto.size() == 0){
                message = Constant.MONTH_LIST_EMPTY;
                responseDto.setMessage(message);
                return responseDto;
            }

            Collections.sort(monthListDto, new Comparator<SchoolMonthDto>() {
                @Override
                public int compare(SchoolMonthDto o1, SchoolMonthDto o2) {
                    return o1.getRankCreateDate().compareTo(o2.getRankCreateDate());
                }
            });

            message = Constant.SUCCESS;
            responseDto.setMonthList(monthListDto);
            responseDto.setMessage(message);

        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            responseDto.setMessage(message);
            return  responseDto;
        }

        return responseDto;
    }

    @Override
    @Transactional
    public MessageDTO editRankSemester(EditRankSemesterRequestDto requestDto) {
        MessageDTO message = new MessageDTO();
        try {
            message = editRankSemesterTransaction(requestDto);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return message;
    }

    /**
     * lamnt98
     * catch request to get hisitory of schoolSemester
     * @param requestDto
     * @return ViewSchoolSemesterHistoryResponseDto
     */
    @Override
    public ViewSchoolSemesterHistoryResponseDto viewSchoolSemesterHistory(ViewSchoolSemesterHistoryRequestDto requestDto) {
        ViewSchoolSemesterHistoryResponseDto responseDto = new ViewSchoolSemesterHistoryResponseDto();

        Integer semesterId = requestDto.getSemesterId();
        SchoolSemester schoolSemester;
        String history = "";
        MessageDTO message = new MessageDTO();


        try{

            //check semesterId null or not
            if(semesterId == null){
                message = Constant.SCHOOL_SEMESTER_ID_NULL;
                responseDto.setMessage(message);
                return responseDto;
            }

            schoolSemester = schoolSemesterRepository.findById(semesterId).orElse(null);
            //check schoolWeek exist or not
            if(schoolSemester == null){
                message = Constant.SCHOOL_SEMESTER_NOT_EXISTS;
                responseDto.setMessage(message);
                return  responseDto;
            }

            history = schoolSemester.getHistory();

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

    private MessageDTO editRankSemesterTransaction(EditRankSemesterRequestDto requestDto)throws Exception{

        Integer semesterId = requestDto.getSemesterId();
        Integer semester = requestDto.getSemester();
        String userName = requestDto.getUserName();
        Date createDate = null;
        User user = null;
        String history = "";

        List<SchoolMonthDto> monthList = requestDto.getMonthList();
        List<Class> classList = new ArrayList<>();
        List<SchoolRankSemester> schoolRankSemesterList = new ArrayList<>();

        MessageDTO message = new MessageDTO();
        SchoolSemester schoolSemester = null;
        SchoolSemester newSchoolSemester = null;

        if(semesterId == null){
            message = Constant.SCHOOL_SEMESTER_ID_NULL;
            return message;
        }

        if(semester == null){
            message = Constant.SEMESTER_NAME_EMPTY;
            return message;
        }

        if(monthList == null){
            message = Constant.MONTH_LIST_EMPTY;
            return message;
        }

        createDate = additionFunctionSchoolRankService.convertDateInComputerToSqlDate();

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
                message = Constant.NOT_ACCEPT_EDIT_RANK_SEMESTER;
                return message;
            }

            schoolSemester = schoolSemesterRepository.findById(semesterId).orElse(null);

            //check schoolMonth exist with monthId
            if(schoolSemester == null){
                message = Constant.SCHOOL_SEMESTER_NOT_EXISTS;
                return message;
            }else if(schoolSemester.getIsRanked() != null && schoolSemester.getIsRanked() != 0){
                message = Constant.RANKSEMESTER_NOT_EDIT;
                return message;
            }

            //check exist schoolMonth with month name
            newSchoolSemester = schoolSemesterRepository.findSchoolSemesterBySemesterAndYearId(semester,schoolSemester.getYearId());
            if(newSchoolSemester != null && schoolSemester.getSemesterId() != newSchoolSemester.getSemesterId()){
                message = Constant.SCHOOL_SEMESTER_EXISTS;
                return message;
            }

            schoolSemester.setSemester(semester);
            history = additionFunctionSchoolRankService.addHistory(schoolSemester.getHistory(),userName,createDate);
            schoolSemester.setHistory(history);
            schoolSemesterRepository.save(schoolSemester);
            classList = classRepository.findAll();

            message = createOrEditSchoolRankSemester(classList,monthList,schoolRankSemesterList,semesterId,message);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            throw new MyException(message.getMessage());
        }
        return message;
    }

    private MessageDTO createRankSemesterTransaction(CreateRankSemesterRequestDto requestDto) throws Exception{

        String userName = requestDto.getUserName();
        Integer semester = requestDto.getSemester();
        Integer currentYearId = requestDto.getCurrentYearId();
        List<SchoolMonthDto> monthList = requestDto.getMonthList();
        List<SchoolRankSemester> schoolRankSemesterList = new ArrayList<>();
        List<Class> classList = new ArrayList<>();
        Date createDate = null;

        MessageDTO message = new MessageDTO();
        User user = null;
        SchoolSemester schoolSemester = null;
        SchoolYear schoolYear;
        Integer semesterId = null;
        String history = "";

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
                message = Constant.NOT_ACCEPT_CREATE_RANK_SEMESTER;
                return message;
            }

            //check month empty or not
            if(semester == null){
                message = Constant.SEMESTER_NAME_EMPTY;
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
            if(monthList == null){
                message = Constant.MONTH_LIST_EMPTY;
                return message;
            }

            createDate = additionFunctionSchoolRankService.convertDateInComputerToSqlDate();

            schoolSemester = schoolSemesterRepository.findSchoolSemesterBySemesterAndYearId(semester,currentYearId);
            //check month exist or not
            if(schoolSemester != null){
                message = Constant.SCHOOL_SEMESTER_EXISTS;
                return message;
            }

            history = additionFunctionSchoolRankService.addHistory("",userName,createDate);
            schoolSemester = new SchoolSemester();
            schoolSemester.setYearId(currentYearId);
            schoolSemester.setSemester(semester);
            schoolSemester.setIsRanked(0);
            schoolSemester.setHistory(history);
            schoolSemester.setCreateDate(createDate);
            schoolSemesterRepository.save(schoolSemester);

            semesterId = schoolSemesterRepository.findSchoolSemesterBySemesterAndYearId(semester,currentYearId).getSemesterId();

            classList = classRepository.findAll();

            message = createOrEditSchoolRankSemester(classList,monthList,schoolRankSemesterList,semesterId,message);

        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            throw new MyException(message.getMessage());
        }
        return message;
    }

    private MessageDTO createOrEditSchoolRankSemester(List<Class> classList, List<SchoolMonthDto> monthList, List<SchoolRankSemester> schoolRankSemesterList, Integer semesterId, MessageDTO message) throws Exception{

        boolean check = false;
        for(Class newClass : classList){
            Double totalGrade = 0.0;
            Integer totalRank = 0;

            for(SchoolMonthDto schoolMonthDto : monthList){
                Integer isCheck = schoolMonthDto.getIsCheck();
                //check condition to create and edit rank month or not
                if(isCheck == 1){
                    check = true;
                    SchoolMonth schoolMonth = schoolMonthRepository.findSchoolMonthByMonthId(schoolMonthDto.getMonthId());
                    //check schoolWeek exist or not
                    if(schoolMonth != null){
                        schoolMonth.setSemesterId(semesterId);
                        schoolMonthRepository.save(schoolMonth);
                        SchoolRankMonth schoolRankMonth = schoolRankMonthRepository.findSchoolRankMonthByMonthIdAndClassId(schoolMonth.getMonthId(),newClass.getClassId());

                        if(schoolRankMonth != null){
                            totalGrade += schoolRankMonth.getTotalGradeWeek();
                            totalRank += schoolRankMonth.getTotalRankWeek();
                        }
                    }
                }
                //check condition to remove week in month
                if(isCheck == 0){
                    SchoolMonth schoolMonth = schoolMonthRepository.findSchoolMonthByMonthId(schoolMonthDto.getMonthId());
                    //check schoolWeek exist or not
                    if(schoolMonth != null){
                        schoolMonth.setSemesterId(0);
                        schoolMonthRepository.save(schoolMonth);
                    }
                }
            }
            //check to start create and edit rank
            if(check){
                SchoolRankSemester schoolRankSemester = new SchoolRankSemester();

                SchoolRankSemesterId schoolRankSemesterId = new SchoolRankSemesterId();
                schoolRankSemesterId.setSEMESTER_ID(semesterId);
                schoolRankSemesterId.setSchoolClass(new Class(newClass.getClassId()));

                schoolRankSemester.setSchoolRankSemesterId(schoolRankSemesterId);
                schoolRankSemester.setTotalGradeMonth(round(totalGrade));
                schoolRankSemester.setTotalRankMonth(totalRank);

                schoolRankSemesterList.add(schoolRankSemester);
            }
        }

        for(SchoolRankSemester schoolRankSemester : schoolRankSemesterList){
            Class newClass = classRepository.findById(schoolRankSemester.getSchoolRankSemesterId().getSchoolClass().getClassId()).orElse(null);
            if(newClass != null){
                classList.remove(newClass);
            }
        }

        //check schoolRankWeekList null or not
        if((schoolRankSemesterList == null || schoolRankSemesterList.size() == 0)){
            message = Constant.SCHOOL_RANK_SEMESTER_NULL;
            throw new MyException(message.getMessage());
        }

        List<SchoolRankSemester> newSchoolRankSemesterList1 = arrangeSchoolRankSemester(schoolRankSemesterList);

        for(int i = 0; i < newSchoolRankSemesterList1.size(); i++){
            SchoolRankSemester schoolRankSemester = newSchoolRankSemesterList1.get(i);
            schoolRankSemesterRepository.save(schoolRankSemester);
        }

        message = Constant.SUCCESS;
        return message;
    }
    private List<SchoolRankSemester> arrangeSchoolRankSemester(List<SchoolRankSemester> schoolRankSemesterList) {
        Collections.sort(schoolRankSemesterList, new Comparator<SchoolRankSemester>() {
            @Override
            public int compare(SchoolRankSemester o1, SchoolRankSemester o2) {
                return o2.getTotalGradeMonth().compareTo(o1.getTotalGradeMonth());
            }
        });
        int rank = 1;
        if(schoolRankSemesterList.size() == 1){
            schoolRankSemesterList.get(0).setRank(rank);
            return schoolRankSemesterList;
        }
        else {
            int count = 0;
            for (int i = 0; i < schoolRankSemesterList.size() - 1; i++) {
                SchoolRankSemester schoolRankSemester = schoolRankSemesterList.get(i);
                schoolRankSemesterList.get(i).setRank(rank);
                SchoolRankSemester schoolRankSemester1 = schoolRankSemesterList.get(i + 1);
                if (schoolRankSemester1.getTotalGradeMonth().compareTo(schoolRankSemester.getTotalGradeMonth()) == 0) {
                    schoolRankSemesterList.get(i + 1).setRank(rank);
                    count++;
                } else {
                    rank += count + 1;
                    count = 0;
                    schoolRankSemesterList.get(i + 1).setRank(rank);
                }
            }
        }
        return schoolRankSemesterList;
    }
    private double round(Double input) {
        return (double) Math.round(input * 100) / 100;
    }
}
