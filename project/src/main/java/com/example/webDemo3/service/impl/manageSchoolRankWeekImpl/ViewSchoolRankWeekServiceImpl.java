package com.example.webDemo3.service.impl.manageSchoolRankWeekImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageClassResponseDto.ClassListResponseDto;
import com.example.webDemo3.dto.manageClassResponseDto.ClassResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.RankWeekListResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.RankWeekResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewWeekAndClassListResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ViewWeekListResponseDto;
import com.example.webDemo3.dto.manageSchoolYearResponseDto.SchoolYearTableResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.SearchRankWeekRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.LoadByYearIdRequestDto;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.SchoolRankWeek;
import com.example.webDemo3.entity.SchoolWeek;
import com.example.webDemo3.entity.SchoolYear;
import com.example.webDemo3.repository.SchoolRankWeekRepository;
import com.example.webDemo3.repository.SchoolWeekRepository;
import com.example.webDemo3.repository.SchoolYearRepository;
import com.example.webDemo3.service.manageClassService.ClassService;
import com.example.webDemo3.service.manageSchoolRankWeek.ViewSchoolRankWeekService;
import com.example.webDemo3.service.manageSchoolYearService.SchoolYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/*
kimpt142 - 21/07
 */
@Service
public class ViewSchoolRankWeekServiceImpl implements ViewSchoolRankWeekService {

    @Autowired
    private SchoolWeekRepository schoolWeekRepository;

    @Autowired
    private ClassService classService;

    @Autowired
    private SchoolRankWeekRepository schoolRankWeekRepository;

    @Autowired
    private SchoolYearService schoolYearService;

    @Autowired
    private SchoolYearRepository schoolYearRepository;

    /**
     * kimpt142
     * 21/07
     * get week list and class list
     * @return ViewWeekAndClassListResponseDto
     */
    @Override
    public ViewWeekAndClassListResponseDto loadRankWeekPage() {
        ViewWeekAndClassListResponseDto responseDto = new ViewWeekAndClassListResponseDto();
        List<ClassResponseDto> classList = new ArrayList<>();
        ClassListResponseDto classListDto = classService.getClassList();
        MessageDTO message;
        Integer currentYearId = null;

        //get class list
        if (classListDto.getMessage().getMessageCode() == 0) {
            classList = classListDto.getClassList();
            responseDto.setClassList(classList);
        } else {
            responseDto.setMessage(classListDto.getMessage());
            return responseDto;
        }

        //get year list
        SchoolYearTableResponseDto schoolYearListDto = schoolYearService.getSchoolYearTable();
        if(schoolYearListDto.getMessage().getMessageCode() == 1){
            message = schoolYearListDto.getMessage();
            responseDto.setMessage(message);
            return responseDto;
        }
        else {
            responseDto.setSchoolYearList(schoolYearListDto.getSchoolYearList());
        }

        Date dateCurrent = new Date(System.currentTimeMillis());
        SchoolYear schoolCurrent = schoolYearRepository.findSchoolYearsByDate(dateCurrent);
        if(schoolCurrent != null) {
            currentYearId = schoolCurrent.getYearID();
            responseDto.setCurrentYearId(currentYearId);
        }

        LoadByYearIdRequestDto yearIdDto = new LoadByYearIdRequestDto();
        yearIdDto.setYearId(currentYearId);

        //get week list by yearid
        ViewWeekListResponseDto schoolWeekListDto = getWeekListByYearId(yearIdDto);
        responseDto.setSchoolWeekList(schoolWeekListDto.getSchoolWeekList());

        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        return responseDto;
    }

    /**
     * kimpt142
     * 21/07
     * search school rank week using week id and class id
     * @param model include weekId and classId
     * @return RankWeekListResponseDto
     */
    @Override
    public RankWeekListResponseDto searchRankWeekByWeekAndClass(SearchRankWeekRequestDto model) {
        RankWeekListResponseDto responseDto = new RankWeekListResponseDto();
        MessageDTO message;
        List<RankWeekResponseDto> rankWeekListDto;
        List<SchoolRankWeek> rankWeekList;

        Integer weekId = model.getWeekId();
        Integer classId = model.getClassId();

        if (weekId == null) {
            message = Constant.WEEK_ID_NULL;
            responseDto.setMessage(message);
            return responseDto;
        }

        //check week is ranked or not
        SchoolWeek checkSchoolWeek = schoolWeekRepository.findSchoolWeekByWeekID(weekId);
        if(checkSchoolWeek != null && checkSchoolWeek.getMonthID() != 0){
            responseDto.setCheckEdit(1);
        }
        else{
            responseDto.setCheckEdit(0);
        }

        rankWeekList = schoolRankWeekRepository.findByWeekIđAndClassId(weekId, classId);
        if(rankWeekList == null || rankWeekList.size() == 0){
            message = Constant.RANKLIST_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }

        rankWeekListDto = convertSchoolRankWeekToDto(rankWeekList);
        responseDto.setRankWeekList(rankWeekListDto);
        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        return responseDto;
    }

    /**
     * kimpt142
     * 23/07
     * get week list by year id
     * @param model
     * @return week list
     */
    @Override
    public ViewWeekListResponseDto getWeekListByYearId(LoadByYearIdRequestDto model) {
        ViewWeekListResponseDto responseDto = new ViewWeekListResponseDto();
        MessageDTO message;
        Integer yearId = model.getYearId();

        if(yearId == null){
            message = Constant.SCHOOLYEARID_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }

        List<SchoolWeek> schoolWeekList = schoolWeekRepository.findSchoolWeekByYearIdExcludeZero(yearId);

        if(schoolWeekList == null || schoolWeekList.size() == 0){
            message = Constant.LIST_WEEK_NULL;
            responseDto.setMessage(message);
            return responseDto;
        }
        responseDto.setSchoolWeekList(schoolWeekList);
        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        return responseDto;
    }

    /**
     * kimpt142
     * 21/07
     * convert school rank week entity list to response dto list
     * @param rankWeekList is entity list
     * @return response list
     */
    private List<RankWeekResponseDto> convertSchoolRankWeekToDto(List<SchoolRankWeek> rankWeekList){
        List<RankWeekResponseDto> responseList = new ArrayList<>();
        for(SchoolRankWeek item : rankWeekList){
            Class schoolClass = item.getSchoolRankWeekId().getSchoolClass();
            RankWeekResponseDto response = new RankWeekResponseDto();
            response.setWeekId(item.getSchoolRankWeekId().getWEEK_ID());
            response.setClassId(schoolClass.getClassId());

            Integer grade = schoolClass.getGrade();
            String giftedName = schoolClass.getGiftedClass().getName();
            if(grade != null && !giftedName.trim().equals("")){
                response.setClassName(grade.toString() + " " + giftedName);
            }

            response.setEmulationGrade(item.getEmulationGrade());
            response.setLearningGrade(item.getLearningGrade());
            response.setMovementGrade(item.getMovementGrade());
            response.setLaborGrade(item.getLaborGrade());
            response.setTotalGrade(item.getTotalGrade());
            response.setRank(item.getRank());
            responseList.add(response);
        }
        return responseList;
    }
}
