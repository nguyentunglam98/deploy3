package com.example.webDemo3.service.impl.manageSchoolRankMonthImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ListWeekSchoolRankResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.SchoolWeekDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewSchoolMonthHistoryResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.*;
import com.example.webDemo3.entity.*;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.exception.MyException;
import com.example.webDemo3.repository.*;
import com.example.webDemo3.service.impl.manageSchoolRankWeekImpl.AdditionFunctionSchoolRankServiceImpl;
import com.example.webDemo3.service.manageSchoolRankMonthService.CreateAndEditSchoolRankMonthService;
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
public class CreateAndEditSchoolRankMonthServiceImpl implements CreateAndEditSchoolRankMonthService {

    @Autowired
    private SchoolRankMonthRepository schoolRankMonthRepository;

    @Autowired
    private SchoolWeekRepository schoolWeekRepository;

    @Autowired
    private SchoolYearRepository schoolYearRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolMonthRepository schoolMonthRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SchoolRankWeekRepository schoolRankWeekRepository;

    @Autowired
    private AdditionFunctionSchoolRankServiceImpl additionFunctionSchoolRankService;

    @Override
    public ListWeekSchoolRankResponseDto loadListWeek(ListWeekSchoolRankRequestDto requestDto) {
        ListWeekSchoolRankResponseDto responseDto = new ListWeekSchoolRankResponseDto();
        List<SchoolWeekDto> weekListDto = new ArrayList<>();
        List<SchoolWeek> weekList = new ArrayList<>();

        Integer currentYearId = requestDto.getCurrentYearId();
        SchoolYear schoolYear = null;
        MessageDTO message = new MessageDTO();

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

            weekList = schoolWeekRepository.findSchoolWeekNotRank(currentYearId);

            if(weekList == null || weekList.size() == 0){
                message = Constant.LIST_WEEK_NULL;
                responseDto.setMessage(message);
                return  responseDto;
            }

            for(SchoolWeek schoolWeek : weekList){
                SchoolWeekDto schoolWeekDto = new SchoolWeekDto();
                schoolWeekDto.setWeekId(schoolWeek.getWeekID());
                schoolWeekDto.setWeek(schoolWeek.getWeek());
                schoolWeekDto.setMonthId(schoolWeek.getMonthID());
                schoolWeekDto.setIsCheck(0);

                weekListDto.add(schoolWeekDto);
            }

            message = Constant.SUCCESS;
            responseDto.setMessage(message);
            responseDto.setWeekList(weekListDto);
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
    public MessageDTO createRankMonth(CreateRankMonthRequestDto requestDto) {
        MessageDTO message = new MessageDTO();
        try {
            message = createRankMonthTransaction(requestDto);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return message;
    }

    @Override
    public ListWeekSchoolRankResponseDto loadEditListWeek(ViewWeekOfEditRankMontRequestDto requestDto) {
        ListWeekSchoolRankResponseDto responseDto = new ListWeekSchoolRankResponseDto();
        List<SchoolWeekDto> weekListDto = new ArrayList<>();
        List<SchoolWeek> weekList = new ArrayList<>();
        List<SchoolWeek> newWeekList = new ArrayList<>();
        MessageDTO message = new MessageDTO();

        Integer monthId = requestDto.getMonthId();
        Integer currentYearId = requestDto.getCurrentYearId();
        SchoolMonth schoolMonth = null;
        SchoolYear schoolYear = null;
        Integer month = null;

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

            weekList = schoolWeekRepository.findSchoolWeekNotRank(currentYearId);

            for(SchoolWeek schoolWeek : weekList){
                SchoolWeekDto schoolWeekDto = new SchoolWeekDto();
                schoolWeekDto.setWeekId(schoolWeek.getWeekID());
                schoolWeekDto.setWeek(schoolWeek.getWeek());
                schoolWeekDto.setMonthId(schoolWeek.getMonthID());
                schoolWeekDto.setRankCreateDate(schoolWeek.getCreateDate());
                schoolWeekDto.setIsCheck(0);

                weekListDto.add(schoolWeekDto);
            }

            //check weekId null or not
            if(monthId == null){
                message = Constant.SCHOOL_MONTH_ID_NULL;
                responseDto.setMessage(message);
            }

            schoolMonth = schoolMonthRepository.findById(monthId).orElse(null);

            //check school month exist or not
            if(schoolMonth == null){
                message = Constant.SCHOOL_MONTH_NOT_EXISTS;
                responseDto.setMessage(message);
                return responseDto;
            }

            newWeekList = schoolWeekRepository.findSchoolWeekByMonthIdAndYearId(monthId,currentYearId);

            for(SchoolWeek schoolWeek : newWeekList){
                SchoolWeekDto schoolWeekDto = new SchoolWeekDto();
                schoolWeekDto.setWeekId(schoolWeek.getWeekID());
                schoolWeekDto.setWeek(schoolWeek.getWeek());
                schoolWeekDto.setMonthId(schoolWeek.getMonthID());
                schoolWeekDto.setRankCreateDate(schoolWeek.getCreateDate());
                schoolWeekDto.setIsCheck(1);

                weekListDto.add(schoolWeekDto);
            }

            //check list week empty or not
            if(weekListDto == null || weekListDto.size() == 0){
                message = Constant.WEEK_LIST_EMPTY;
                responseDto.setMessage(message);
                return responseDto;
            }

            Collections.sort(weekListDto, new Comparator<SchoolWeekDto>() {
                @Override
                public int compare(SchoolWeekDto o1, SchoolWeekDto o2) {
                    return o1.getRankCreateDate().compareTo(o2.getRankCreateDate());
                }
            });

            message = Constant.SUCCESS;
            responseDto.setWeekList(weekListDto);
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
    public MessageDTO editRankMonth(EditRankMonthRequestDto requestDto) {
        MessageDTO message = new MessageDTO();
        try {
            message = editRankMonthTransaction(requestDto);
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
     * get history of school month
     * @param requestDto
     * @return ViewSchoolMonthHistoryResponseDto
     */
    @Override
    public ViewSchoolMonthHistoryResponseDto viewSchoolMonthHistory(ViewSchoolMonthHistoryRequestDto requestDto) {
        ViewSchoolMonthHistoryResponseDto responseDto = new ViewSchoolMonthHistoryResponseDto();

        Integer monthId = requestDto.getMonthId();
        SchoolMonth schoolMonth;
        String history = "";
        MessageDTO message = new MessageDTO();


        try{

            //check weekId null or not
            if(monthId == null){
                message = Constant.MONTHID_EMPTY;
                responseDto.setMessage(message);
                return responseDto;
            }

            schoolMonth = schoolMonthRepository.findById(monthId).orElse(null);
            //check schoolWeek exist or not
            if(schoolMonth == null){
                message = Constant.SCHOOL_MONTH_NOT_EXISTS;
                responseDto.setMessage(message);
                return  responseDto;
            }

            history = schoolMonth.getHistory();

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


    public MessageDTO editRankMonthTransaction(EditRankMonthRequestDto requestDto) throws Exception{

        Integer monthId = requestDto.getMonthId();
        Integer month = requestDto.getMonth();
        String userName = requestDto.getUserName();
        Date createDate = null;

        User user = null;
        List<SchoolWeekDto> weekList = requestDto.getWeekList();
        List<Class> classList = new ArrayList<>();
        List<SchoolRankMonth> schoolRankMonthList = new ArrayList<>();
        String history = "";

        MessageDTO message = new MessageDTO();
        SchoolMonth schoolMonth = null;
        SchoolMonth newSchoolMonth = null;

        //check monthId null or not
        if(monthId == null){
            message = Constant.SCHOOL_MONTH_ID_NULL;
            return message;
        }

        //check month null or not
        if(month == null){
            message = Constant.MONTH_NAME_EMPTY;
            return message;
        }

        //check weekList null or not
        if(weekList == null){
            message = Constant.WEEK_LIST_EMPTY;
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
                message = Constant.NOT_ACCEPT_EDIT_RANK_MONTH;
                return message;
            }

            schoolMonth = schoolMonthRepository.findById(monthId).orElse(null);

            //check schoolMonth exist with monthId
            if(schoolMonth == null){
                message = Constant.SCHOOL_MONTH_NOT_EXISTS;
                return message;
            }else if(schoolMonth.getSemesterId() != 0){
                message = Constant.RANKMONTH_NOT_EDIT;
                return message;
            }

            //check exist schoolMonth with month name
            newSchoolMonth = schoolMonthRepository.findSchoolMonthByMonthAndYearId(month,schoolMonth.getYearId());
            if(newSchoolMonth != null && schoolMonth.getMonthId() != newSchoolMonth.getMonthId()){
                message = Constant.SCHOOL_MONTH_EXISTS;
                return message;
            }

            schoolMonth.setMonth(month);
            history = additionFunctionSchoolRankService.addHistory(schoolMonth.getHistory(),userName,createDate);
            schoolMonth.setHistory(history);
            schoolMonthRepository.save(schoolMonth);
            classList = classRepository.findAll();

            message = createOrEditSchoolRankMonth(classList,weekList,schoolRankMonthList,monthId,message);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            throw new MyException(message.getMessage());
        }
        return message;
    }

    private MessageDTO createRankMonthTransaction(CreateRankMonthRequestDto requestDto) throws Exception{
        MessageDTO message = new MessageDTO();

        String userName = requestDto.getUserName();
        Integer month = requestDto.getMonth();
        Integer currentYearId = requestDto.getCurrentYearId();
        Date createDate = null;

        List<SchoolWeekDto> weekList = requestDto.getWeekList();
        List<SchoolRankMonth> schoolRankMonthList = new ArrayList<>();

        User user;
        SchoolMonth schoolMonth;
        SchoolYear schoolYear;
        Integer monthId;
        List<Class> classList = new ArrayList<>();
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
                message = Constant.NOT_ACCEPT_CREATE_RANK_MONTH;
                return message;
            }

            //check month empty or not
            if(month == null){
                message = Constant.MONTH_NAME_EMPTY;
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
            if(weekList == null){
                message = Constant.WEEK_LIST_EMPTY;
                return message;
            }

            createDate = additionFunctionSchoolRankService.convertDateInComputerToSqlDate();

            schoolMonth = schoolMonthRepository.findSchoolMonthByMonthAndYearId(month,currentYearId);
            //check month exist or not
            if(schoolMonth != null){
                message = Constant.SCHOOL_MONTH_EXISTS;
                return message;
            }

            history = additionFunctionSchoolRankService.addHistory("",userName,createDate);
            schoolMonth = new SchoolMonth();
            schoolMonth.setMonth(month);
            schoolMonth.setSemesterId(0);
            schoolMonth.setYearId(currentYearId);
            schoolMonth.setHistory(history);
            schoolMonth.setCreateDate(createDate);
            schoolMonthRepository.save(schoolMonth);

            monthId = schoolMonthRepository.findSchoolMonthByMonthAndYearId(month,currentYearId).getMonthId();

            classList = classRepository.findAll();

            message = createOrEditSchoolRankMonth(classList,weekList,schoolRankMonthList,monthId,message);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            throw new MyException(message.getMessage());
        }
        return message;
    }

    private MessageDTO createOrEditSchoolRankMonth(List<Class> classList,List<SchoolWeekDto> weekList,List<SchoolRankMonth> schoolRankMonthList, Integer monthId, MessageDTO message) throws Exception{
        boolean check = false;

        for(Class newClass : classList){
            Double totalGrade = 0.0;
            Integer totalRank = 0;

            for(SchoolWeekDto schoolWeekDto : weekList){
                Integer isCheck = schoolWeekDto.getIsCheck();
                //check condition to create and edit rank month or not
                if(isCheck == 1){
                    check = true;
                    SchoolWeek schoolWeek = schoolWeekRepository.findSchoolWeekByWeekID(schoolWeekDto.getWeekId());
                    //check schoolWeek exist or not
                    if(schoolWeek != null){
                        schoolWeek.setMonthID(monthId);
                        schoolWeekRepository.save(schoolWeek);
                        SchoolRankWeek schoolRankWeek = schoolRankWeekRepository.findSchoolRankWeekByWeekIdAndClassId(schoolWeek.getWeekID(),newClass.getClassId());

                        if(schoolRankWeek != null){
                            totalGrade += schoolRankWeek.getTotalGrade();
                            totalRank += schoolRankWeek.getRank();
                        }
                    }
                }
                //check condition to remove week in month
                if(isCheck == 0){
                    SchoolWeek schoolWeek = schoolWeekRepository.findSchoolWeekByWeekID(schoolWeekDto.getWeekId());
                    //check schoolWeek exist or not
                    if(schoolWeek != null){
                        schoolWeek.setMonthID(0);
                        schoolWeekRepository.save(schoolWeek);
                    }
                }
            }
            //check to start create and edit rank
            if(check){
                SchoolRankMonth schoolRankMonth = new SchoolRankMonth();

                SchoolRankMonthId schoolRankMonthId = new SchoolRankMonthId();
                schoolRankMonthId.setMONTH_ID(monthId);
                schoolRankMonthId.setSchoolClass(new Class(newClass.getClassId()));

                schoolRankMonth.setSchoolRankMonthId(schoolRankMonthId);
                schoolRankMonth.setTotalGradeWeek(round(totalGrade));
                schoolRankMonth.setTotalRankWeek(totalRank);

                schoolRankMonthList.add(schoolRankMonth);
            }
        }

        for(SchoolRankMonth schoolRankMonth : schoolRankMonthList){
            Class newClass = classRepository.findById(schoolRankMonth.getSchoolRankMonthId().getSchoolClass().getClassId()).orElse(null);
            if(newClass != null){
                classList.remove(newClass);
            }
        }

        //check schoolRankWeekList null or not
        if((schoolRankMonthList == null || schoolRankMonthList.size() == 0)){
            message = Constant.SCHOOL_RANK_WEEK_NULL;
            throw new MyException(message.getMessage());
        }
        List<SchoolRankMonth> newSchoolRankMonthList1 = arrangeSchoolRankMonth(schoolRankMonthList);

        for(int i = 0; i < newSchoolRankMonthList1.size(); i++){
            SchoolRankMonth schoolRankMonth = newSchoolRankMonthList1.get(i);
            schoolRankMonthRepository.save(schoolRankMonth);
        }

        message = Constant.SUCCESS;
        return message;
    }

    private List<SchoolRankMonth> arrangeSchoolRankMonth(List<SchoolRankMonth> schoolRankMonthList) {
        Collections.sort(schoolRankMonthList, new Comparator<SchoolRankMonth>() {
            @Override
            public int compare(SchoolRankMonth o1, SchoolRankMonth o2) {
                return o1.getTotalRankWeek().compareTo(o2.getTotalRankWeek());
            }
        });
        int rank = 1;
        if(schoolRankMonthList.size() == 1){
            schoolRankMonthList.get(0).setRank(rank);
            return schoolRankMonthList;
        }
        else {
            int count = 0;
            for (int i = 0; i < schoolRankMonthList.size() - 1; i++) {
                SchoolRankMonth schoolRankMonth = schoolRankMonthList.get(i);
                schoolRankMonthList.get(i).setRank(rank);
                SchoolRankMonth schoolRankMonth2 = schoolRankMonthList.get(i + 1);
                if (schoolRankMonth2.getTotalRankWeek().compareTo(schoolRankMonth.getTotalRankWeek()) == 0) {
                    schoolRankMonthList.get(i + 1).setRank(rank);
                    count++;
                } else {
                    rank += count + 1;
                    count = 0;
                    schoolRankMonthList.get(i + 1).setRank(rank);
                }
            }
        }
        return schoolRankMonthList;
    }

    private double round(Double input) {
        return (double) Math.round(input * 100) / 100;
    }
}
