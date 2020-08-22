package com.example.webDemo3.service.impl.manageSchoolRankYearServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ListSemesterSchoolRankResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.SchoolSemesterDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewSchoolYearHistoryResponseDto;
import com.example.webDemo3.dto.manageSchoolYearResponseDto.SchoolYearResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.CreateRankYearRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.EditRankYearRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.ViewSchoolYearHistoryRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.ViewSemesterOfEditRankYearRequestDto;
import com.example.webDemo3.entity.*;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.exception.MyException;
import com.example.webDemo3.repository.*;
import com.example.webDemo3.service.manageSchoolRankWeek.AdditionFunctionSchoolRankService;
import com.example.webDemo3.service.manageSchoolRankYearSerivce.CreateAndEditSchoolRankYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Date;
import java.util.*;

@Service
public class CreateAndEditSchoolRankYearServiceImpl implements CreateAndEditSchoolRankYearService {

    @Autowired
    private SchoolSemesterRepository schoolSemesterRepository;

    @Autowired
    private SchoolYearRepository schoolYearRepository;

    @Autowired
    private SchoolRankYearRepository schoolRankYearRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SchoolRankSemesterRepository schoolRankSemesterRepository;

    @Autowired
    private AdditionFunctionSchoolRankService additionFunctionSchoolRankService;

    @Override
    public ListSemesterSchoolRankResponseDto loadListSemester() {
        ListSemesterSchoolRankResponseDto responseDto = new ListSemesterSchoolRankResponseDto();
        List<SchoolSemesterDto> semesterListDto = new ArrayList<>();
        List<SchoolSemester> semesterList =new ArrayList<>();
        List<SchoolYearResponseDto> schoolYearListDto = new ArrayList<>();
        List<SchoolYear> schoolYearList = new ArrayList<>();
        List<Integer> yearIdList = new ArrayList<>();
        MessageDTO message = new MessageDTO();


        try{
            schoolYearList = schoolYearRepository.findAll();
            yearIdList = schoolRankYearRepository.getAllDistinctYearId();

            Iterator<SchoolYear> iterator = schoolYearList.iterator();
            while (iterator.hasNext()) {
                SchoolYear schoolYear = iterator.next();
                //check to remove schoolyear has yearId = 0
                if(schoolYear.getYearID() == 0){
                    iterator.remove();
                }

                //check list yearId empty or not
                if(yearIdList.size() != 0){
                    //check year has ranked or not
                    if(yearIdList.contains(schoolYear.getYearID())){
                        iterator.remove();
                    }
                }
            }

            for(SchoolYear schoolYear : schoolYearList){
                SchoolYearResponseDto schoolYearResponseDto = new SchoolYearResponseDto();
                schoolYearResponseDto.setFromDate(schoolYear.getFromDate());
                schoolYearResponseDto.setToDate(schoolYear.getToDate());
                schoolYearResponseDto.setSchoolYearId(schoolYear.getYearID());
                schoolYearResponseDto.setYearName(schoolYear.getFromYear() + " - " + schoolYear.getToYear());

                schoolYearListDto.add(schoolYearResponseDto);
            }

            responseDto.setSchoolYearList(schoolYearListDto);

            semesterList = schoolSemesterRepository.findSchoolSemesterNotRank();

            for(SchoolSemester schoolSemester : semesterList){
                SchoolSemesterDto schoolSemesterDto = new SchoolSemesterDto();
                schoolSemesterDto.setSemesterId(schoolSemester.getSemesterId());
                schoolSemesterDto.setSemester(schoolSemester.getSemester());
                schoolSemesterDto.setYearId(schoolSemester.getYearId());
                schoolSemesterDto.setIsCheck(0);

                semesterListDto.add(schoolSemesterDto);
            }

            responseDto.setSemesterList(semesterListDto);

            //check list school year which is not ranked empty or not
            if(schoolYearList == null || schoolYearList.size() == 0){
                message = Constant.SCHOOL_RANK_YEAR_EMPTY;
                responseDto.setMessage(message);
                return  responseDto;
            }
            //check list semester which is not ranked empty or not
            if(semesterList == null || semesterList.size() == 0){
                message = Constant.SEMESTER_LIST_EMPTY;
                responseDto.setMessage(message);
                return  responseDto;
            }

            message = Constant.SUCCESS;
            responseDto.setMessage(message);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            responseDto.setMessage(message);
            return responseDto;
        }
        return  responseDto;
    }

    @Override
    public ListSemesterSchoolRankResponseDto loadEditListSemester(ViewSemesterOfEditRankYearRequestDto requestDto) {
        ListSemesterSchoolRankResponseDto responseDto = new ListSemesterSchoolRankResponseDto();
        List<SchoolSemesterDto> semesterListDto = new ArrayList<>();
        List<SchoolSemester> semesterList = new ArrayList<>();
        List<SchoolSemester> newSemesterList = new ArrayList<>();
        MessageDTO message = new MessageDTO();

        Integer yearId = requestDto.getYearId();
        SchoolYear schoolYear;

        try {
            if (yearId == null) {
                message = Constant.YEAR_ID_NULL;
                responseDto.setMessage(message);
                return responseDto;
            }

            schoolYear = schoolYearRepository.findById(yearId).orElse(null);

            //check year exist or not
            if (schoolYear == null) {
                message = Constant.YEAR_ID_NULL;
                responseDto.setMessage(message);
                return responseDto;
            }

            semesterList = schoolSemesterRepository.findSchoolSemesterNotRank();

            for (SchoolSemester schoolSemester : semesterList) {
                SchoolSemesterDto schoolSemesterDto = new SchoolSemesterDto();
                schoolSemesterDto.setSemesterId(schoolSemester.getSemesterId());
                schoolSemesterDto.setSemester(schoolSemester.getSemester());
                schoolSemesterDto.setYearId(schoolSemester.getYearId());
                schoolSemesterDto.setRankCreateDate(schoolSemester.getCreateDate());
                schoolSemesterDto.setIsCheck(0);

                semesterListDto.add(schoolSemesterDto);
            }

            newSemesterList = schoolSemesterRepository.findSchoolSemesterRank(yearId);

            for (SchoolSemester schoolSemester : newSemesterList) {
                SchoolSemesterDto schoolSemesterDto = new SchoolSemesterDto();
                schoolSemesterDto.setSemesterId(schoolSemester.getSemesterId());
                schoolSemesterDto.setSemester(schoolSemester.getSemester());
                schoolSemesterDto.setYearId(schoolSemester.getYearId());
                schoolSemesterDto.setRankCreateDate(schoolSemester.getCreateDate());
                schoolSemesterDto.setIsCheck(1);

                semesterListDto.add(schoolSemesterDto);
            }

            if (semesterListDto == null || semesterListDto.size() == 0) {
                message = Constant.SEMESTER_LIST_EMPTY;
                responseDto.setMessage(message);
                return responseDto;
            }

            Collections.sort(semesterListDto, new Comparator<SchoolSemesterDto>() {
                @Override
                public int compare(SchoolSemesterDto o1, SchoolSemesterDto o2) {
                    return o1.getRankCreateDate().compareTo(o2.getRankCreateDate());
                }
            });

            message = Constant.SUCCESS;
            responseDto.setSemesterList(semesterListDto);
            responseDto.setMessage(message);

        } catch (Exception e) {
            message.setMessageCode(1);
            message.setMessage(e.toString());
            responseDto.setMessage(message);
            return responseDto;
        }
        return responseDto;
    }

    @Override
    @Transactional
    public MessageDTO createRankYear(CreateRankYearRequestDto requestDto) {
        MessageDTO message = new MessageDTO();
        try {
            message = createRankYearTransaction(requestDto);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return message;
    }

    @Override
    @Transactional
    public MessageDTO editRankYear(EditRankYearRequestDto requestDto) {
        MessageDTO message = new MessageDTO();
        try {
            message = editRankYearTransaction(requestDto);
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
     * view history of school year
     * @param requestDto
     * @return ViewSchoolYearHistoryResponseDto
     */
    @Override
    public ViewSchoolYearHistoryResponseDto viewSchoolYearHistory(ViewSchoolYearHistoryRequestDto requestDto) {
        ViewSchoolYearHistoryResponseDto responseDto = new ViewSchoolYearHistoryResponseDto();

        Integer yearId = requestDto.getYearId();
        SchoolYear schoolYear;
        String history = "";
        MessageDTO message = new MessageDTO();


        try{

            //check yearId null or not
            if(yearId == null){
                message = Constant.YEAR_ID_NULL;
                responseDto.setMessage(message);
                return responseDto;
            }

            schoolYear = schoolYearRepository.findById(yearId).orElse(null);
            //check schoolYear exist or not
            if(schoolYear == null){
                message = Constant.SCHOOL_YEAR_NOT_EXISTS;
                responseDto.setMessage(message);
                return  responseDto;
            }

            history = schoolYear.getHistory();

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

    private MessageDTO editRankYearTransaction(EditRankYearRequestDto requestDto) throws Exception{

        Integer yearId = requestDto.getYearId();
        String userName = requestDto.getUserName();
        Date createDate = null;
        User user = null;
        List<SchoolSemesterDto> semesterList = requestDto.getSemesterList();

        List<Class> classList = new ArrayList<>();
        List<SchoolRankYear> schoolRankYearList = new ArrayList<>();

        MessageDTO message = new MessageDTO();
        SchoolYear schoolYear = null;
        String history = "";

        //check yearId null or not
        if(yearId == null){
            message = Constant.YEAR_ID_NULL;
            return message;
        }

        //check semesterList null or not
        if(semesterList == null){
            message = Constant.SEMESTER_LIST_EMPTY;
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
                message = Constant.NOT_ACCEPT_EDIT_RANK_YEAR;
                return message;
            }

            schoolYear = schoolYearRepository.findById(yearId).orElse(null);

            //check schoolYear exist with yearId
            if(schoolYear == null){
                message = Constant.SCHOOLYEAR_EMPTY;
                return message;
            }

            history = additionFunctionSchoolRankService.addHistory(schoolYear.getHistory(),userName,createDate);
            schoolYear.setHistory(history);
            schoolYearRepository.save(schoolYear);

            classList = classRepository.findAll();

            message = createOrEditSchoolRankYear(classList,semesterList,schoolRankYearList,yearId,message);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            throw new MyException(message.getMessage());
        }
        return message;

    }

    private MessageDTO createRankYearTransaction(CreateRankYearRequestDto requestDto) throws Exception{

        String userName = requestDto.getUserName();
        Integer yearId = requestDto.getYearId();
        Date createDate = null;
        List<SchoolSemesterDto> semesterList = requestDto.getSemesterList();

        List<SchoolRankYear> schoolRankYearList = new ArrayList<>();
        List<Class> classList = new ArrayList<>();

        MessageDTO message = new MessageDTO();
        User user = null;
        SchoolYear schoolYear = null;
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
                message = Constant.NOT_ACCEPT_CREATE_RANK_SEMESTER;
                return message;
            }

            //check currentYearId null or not
            if(yearId == null){
                message = Constant.YEAR_ID_NULL;
                return message;
            }

            schoolYear = schoolYearRepository.findById(yearId).orElse(null);

            //check schoolYear exists or not
            if(schoolYear == null){
                message = Constant.SCHOOLYEAR_EMPTY;
                return message;
            }

            //check dateList null or not
            if(semesterList == null){
                message = Constant.SEMESTER_LIST_EMPTY;
                return message;
            }

            createDate = additionFunctionSchoolRankService.convertDateInComputerToSqlDate();

            history = additionFunctionSchoolRankService.addHistory("",userName,createDate);
            schoolYear.setHistory(history);
            schoolYear.setCreateDate(createDate);
            schoolYearRepository.save(schoolYear);

            classList = classRepository.findAll();

            message = createOrEditSchoolRankYear(classList,semesterList,schoolRankYearList,yearId,message);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            throw new MyException(message.getMessage());
        }
        return  message;
    }

    private MessageDTO createOrEditSchoolRankYear(List<Class> classList, List<SchoolSemesterDto> semesterList, List<SchoolRankYear> schoolRankYearList, Integer yearId, MessageDTO message) throws Exception{

        boolean check = false;
        for(Class newClass : classList){
            Double totalGrade = 0.0;
            Integer totalRank = 0;

            for(SchoolSemesterDto schoolSemesterDto : semesterList){
                Integer isCheck = schoolSemesterDto.getIsCheck();
                //check condition to create and edit rank month or not
                if(isCheck == 1){
                    check = true;
                    SchoolSemester schoolSemester = schoolSemesterRepository.findSchoolSemesterBySemesterId(schoolSemesterDto.getSemesterId());
                    //check schoolWeek exist or not
                    if(schoolSemester != null){
                        schoolSemester.setYearId(yearId);
                        schoolSemester.setIsRanked(1);
                        schoolSemesterRepository.save(schoolSemester);
                        SchoolRankSemester schoolRankSemester = schoolRankSemesterRepository.findSchoolRankSemesterBySemesterIdAndClassId(schoolSemester.getSemesterId(),newClass.getClassId());

                        if(schoolRankSemester != null){
                            totalGrade += schoolRankSemester.getTotalGradeMonth();
                            totalRank += schoolRankSemester.getTotalRankMonth();
                        }
                    }
                }
                //check condition to remove week in month
                if(isCheck == 0){
                    SchoolSemester schoolSemester = schoolSemesterRepository.findSchoolSemesterBySemesterId(schoolSemesterDto.getSemesterId());
                    //check schoolWeek exist or not
                    if(schoolSemester != null){
                        schoolSemester.setIsRanked(0);
                        schoolSemesterRepository.save(schoolSemester);
                    }
                }
            }
            //check to start create and edit rank
            if(check){
                SchoolRankYear schoolRankYear = new SchoolRankYear();

                SchoolRankYearId schoolRankYearId = new SchoolRankYearId();
                schoolRankYearId.setYEAR_ID(yearId);
                schoolRankYearId.setSchoolClass(new Class(newClass.getClassId()));

                schoolRankYear.setSchoolRankYearId(schoolRankYearId);
                schoolRankYear.setTotalGradeSemester(round(totalGrade));
                schoolRankYear.setTotalRankSemester(totalRank);

                schoolRankYearList.add(schoolRankYear);
            }
        }

        for(SchoolRankYear schoolRankYear : schoolRankYearList){
            Class newClass = classRepository.findById(schoolRankYear.getSchoolRankYearId().getSchoolClass().getClassId()).orElse(null);
            if(newClass != null){
                classList.remove(newClass);
            }
        }

        //check schoolRankWeekList null or not
        if((schoolRankYearList == null || schoolRankYearList.size() == 0)){
            message = Constant.SCHOOL_RANK_YEAR_NULL;
            throw new MyException(message.getMessage());
        }

        List<SchoolRankYear> newSchoolRankYearList1 = arrangeSchoolRankYear(schoolRankYearList);

        for(int i = 0; i < newSchoolRankYearList1.size(); i++){
            SchoolRankYear schoolRankYear = newSchoolRankYearList1.get(i);
            schoolRankYearRepository.save(schoolRankYear);
        }

        message = Constant.SUCCESS;
        return message;
    }

    private List<SchoolRankYear> arrangeSchoolRankYear(List<SchoolRankYear> schoolRankYearList) {
        Collections.sort(schoolRankYearList, new Comparator<SchoolRankYear>() {
            @Override
            public int compare(SchoolRankYear o1, SchoolRankYear o2) {
                return o2.getTotalGradeSemester().compareTo(o1.getTotalGradeSemester());
            }
        });
        int rank = 1;
        if(schoolRankYearList.size() == 1){
            schoolRankYearList.get(0).setRank(rank);
            return schoolRankYearList;
        }
        else {
            int count = 0;
            for (int i = 0; i < schoolRankYearList.size() - 1; i++) {
                SchoolRankYear schoolRankYear = schoolRankYearList.get(i);
                schoolRankYearList.get(i).setRank(rank);
                SchoolRankYear schoolRankYear1 = schoolRankYearList.get(i + 1);
                if (schoolRankYear1.getTotalGradeSemester().compareTo(schoolRankYear.getTotalGradeSemester()) == 0) {
                    schoolRankYearList.get(i + 1).setRank(rank);
                    count++;
                } else {
                    rank += count + 1;
                    count = 0;
                    schoolRankYearList.get(i + 1).setRank(rank);
                }
            }
        }
        return schoolRankYearList;
    }
    private double round(Double input) {
        return (double) Math.round(input * 100) / 100;
    }

}
