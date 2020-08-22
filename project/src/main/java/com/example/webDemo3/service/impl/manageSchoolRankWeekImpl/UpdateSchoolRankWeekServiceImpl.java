package com.example.webDemo3.service.impl.manageSchoolRankWeekImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.RankWeekResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.UpdateSchoolRankWeekRequestDto;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.SchoolRankWeek;
import com.example.webDemo3.entity.SchoolRankWeekId;
import com.example.webDemo3.entity.SchoolWeek;
import com.example.webDemo3.exception.MyException;
import com.example.webDemo3.repository.ClassRepository;
import com.example.webDemo3.repository.SchoolRankWeekRepository;
import com.example.webDemo3.repository.SchoolWeekRepository;
import com.example.webDemo3.service.manageSchoolRankWeek.AdditionFunctionSchoolRankService;
import com.example.webDemo3.service.manageSchoolRankWeek.SortSchoolRankWeekService;
import com.example.webDemo3.service.manageSchoolRankWeek.UpdateSchoolRankWeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/*
kimpt142 - 21/07
 */
@Service
public class UpdateSchoolRankWeekServiceImpl implements UpdateSchoolRankWeekService {

    @Autowired
    private SchoolRankWeekRepository schoolRankWeekRepository;

    @Autowired
    private SchoolWeekRepository schoolWeekRepository;

    @Autowired
    private SortSchoolRankWeekService sortSchoolRankWeekService;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private AdditionFunctionSchoolRankService additionFunctionService;

    /**
     * kimpt142
     * 21/07
     * update the rank week list after user edit
     * @param model
     * @return
     */
    @Override
    @Transactional
    public MessageDTO updateSchoolRankWeek(UpdateSchoolRankWeekRequestDto model) {
        List<RankWeekResponseDto> rankWeekList = model.getRankWeekList();
        String userName = model.getUserName();
        MessageDTO message = new MessageDTO();
        List<SchoolRankWeek> schoolRankWeekList = new ArrayList<>();
        if(rankWeekList != null && rankWeekList.size() != 0) {
            Integer weekId = rankWeekList.get(0).getWeekId();
            SchoolWeek schoolWeek = schoolWeekRepository.findSchoolWeekByWeekID(weekId);
            if(schoolWeek == null)
            {
                message = Constant.SCHOOL_WEEK_ID_NULL;
                return message;
            }
            else if(schoolWeek.getMonthID() != 0){
                message = Constant.RANKWEEK_NOT_EDIT;
                return message;
            }

            for (RankWeekResponseDto item : rankWeekList) {
                Class checkClass = classRepository.findByClassId(item.getClassId());
                if(checkClass == null){
                    message = Constant.CLASS_NOT_EXIST;
                    return message;
                }
                else if(item.getLearningGrade() > Constant.LEARNING_GRADE){
                    message = Constant.LEARNINGGRADE_GREATER;
                    return message;
                }
                else if(item.getMovementGrade() > Constant.MOVEMENT_GRADE){
                    message = Constant.MOVEMENTGRADE_GREATER;
                    return message;
                }
                else if(item.getLaborGrade() > Constant.LABOR_GRADE){
                    message = Constant.LABORGRADE_GREATER;
                    return message;
                }
                else {
                    SchoolRankWeek schoolRankWeek = updateSchoolRankWeek(item);
                    schoolRankWeekList.add(schoolRankWeek);
                }
            }

            schoolRankWeekList = sortSchoolRankWeekService.arrangeSchoolRankWeek(schoolRankWeekList);

            try {
                message = updateSchoolRankWeek(schoolRankWeekList);
                message = addHistoryUpdate(schoolWeek, userName);
            } catch (Exception e) {
                message.setMessageCode(1);
                message.setMessage(e.toString());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return message;
            }
        }
        else{
            message = Constant.RANKLIST_EMPTY;
            return message;
        }
        message = Constant.SUCCESS;
        return message;
    }

    /**
     * kimpt142
     * 21/07
     * update a rank week response dto into school rank week table
     * @param responseDto
     */
    private SchoolRankWeek updateSchoolRankWeek(RankWeekResponseDto responseDto){
        SchoolRankWeek schoolRankWeek = new SchoolRankWeek();
        SchoolRankWeekId schoolRankWeekId = new SchoolRankWeekId();

        schoolRankWeekId.setWEEK_ID(responseDto.getWeekId());
        schoolRankWeekId.setSchoolClass(new Class(responseDto.getClassId()));

        Double learningGrade = responseDto.getLearningGrade();
        Double movementGrade = responseDto.getMovementGrade();
        Double laborGrade = responseDto.getLaborGrade();
        Double newTotalGrade = learningGrade + movementGrade + laborGrade + responseDto.getEmulationGrade();
        schoolRankWeek.setSchoolRankWeekId(schoolRankWeekId);
        schoolRankWeek.setLearningGrade(round(learningGrade));
        schoolRankWeek.setMovementGrade(round(movementGrade));
        schoolRankWeek.setLaborGrade(round(laborGrade));
        schoolRankWeek.setTotalGrade(round(newTotalGrade));
        schoolRankWeek.setEmulationGrade(round(responseDto.getEmulationGrade()));

        return schoolRankWeek;
    }

    /**
     * kimpt142
     * update school rank week with transaction
     * @param schoolRankWeekList
     * @return
     * @throws Exception
     */
    private MessageDTO updateSchoolRankWeek(List<SchoolRankWeek> schoolRankWeekList) throws Exception{
        MessageDTO message = new MessageDTO();
        try {
            for (SchoolRankWeek item : schoolRankWeekList) {
                schoolRankWeekRepository.save(item);
            }
        }
        catch (Exception e){
            message = Constant.UPDATE_SCHOOL_RANK_FAIL;
            throw new MyException(message.getMessage());
        }
        return message;
    }

    /**
     * kimpt142
     * 30/07
     * add history into schoolweek table
     * @param schoolWeek
     * @param userName
     * @return
     * @throws Exception
     */
    private MessageDTO addHistoryUpdate(SchoolWeek schoolWeek, String userName) throws Exception
    {
        MessageDTO message;
        Date currentDate = new Date(System.currentTimeMillis());
        String oldHistory = schoolWeek.getHistory();
        String newHistory = additionFunctionService.addHistory(oldHistory, userName, currentDate);
        schoolWeek.setHistory(newHistory);
        try {
            schoolWeekRepository.save(schoolWeek);
        }
        catch (Exception e){
            message = Constant.ADD_HISTORY_WEEK_FAIL;
            throw new MyException(message.getMessage());
        }
        message = Constant.SUCCESS;
        return message;
    }

    private double round(Double input) {
        return (double) Math.round(input * 100) / 100;
    }
}