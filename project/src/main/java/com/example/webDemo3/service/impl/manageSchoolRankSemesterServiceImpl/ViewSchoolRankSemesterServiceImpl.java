package com.example.webDemo3.service.impl.manageSchoolRankSemesterServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageClassResponseDto.ClassListResponseDto;
import com.example.webDemo3.dto.manageClassResponseDto.ClassResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.*;
import com.example.webDemo3.dto.manageSchoolYearResponseDto.SchoolYearTableResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.LoadByYearIdRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.SearchRankSemesterRequestDto;
import com.example.webDemo3.entity.SchoolMonth;
import com.example.webDemo3.entity.SchoolRankSemester;
import com.example.webDemo3.entity.SchoolSemester;
import com.example.webDemo3.entity.SchoolYear;
import com.example.webDemo3.repository.SchoolRankSemesterRepository;
import com.example.webDemo3.repository.SchoolSemesterRepository;
import com.example.webDemo3.repository.SchoolYearRepository;
import com.example.webDemo3.service.manageClassService.ClassService;
import com.example.webDemo3.service.manageSchoolRankSemesterService.ViewSchoolRankSemesterService;
import com.example.webDemo3.service.manageSchoolYearService.SchoolYearService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/*
kimpt142 - 24/07
 */
@Service
public class ViewSchoolRankSemesterServiceImpl implements ViewSchoolRankSemesterService {

    @Autowired
    private SchoolSemesterRepository schoolSemesterRepository;

    @Autowired
    private SchoolRankSemesterRepository schoolRankSemesterRepository;

    @Autowired
    private SchoolYearService schoolYearService;

    @Autowired
    private SchoolYearRepository schoolYearRepository;

    @Autowired
    private ClassService classService;

    /**
     * kimpt142
     * 24/07
     * get year list and semester list by yearid
     * @return schoolyearlist, semesterlist and message
     */
    @Override
    public LoadRankSemesterResponseDto loadRankSemesterPage() {
        LoadRankSemesterResponseDto responseDto = new LoadRankSemesterResponseDto();
        MessageDTO message;
        Integer currentYearId = null;
        List<ClassResponseDto> classList;

        ClassListResponseDto classListDto = classService.getClassList();
        //get year list
        SchoolYearTableResponseDto schoolYearListDto = schoolYearService.getSchoolYearTable();
        if(schoolYearListDto.getMessage().getMessageCode() == 1){
            message = schoolYearListDto.getMessage();
            responseDto.setMessage(message);
            return responseDto;
        }
        else{
            responseDto.setSchoolYearList(schoolYearListDto.getSchoolYearList());
        }

        //get class list
        if (classListDto.getMessage().getMessageCode() == 0) {
            classList = classListDto.getClassList();
            responseDto.setClassList(classList);
        } else {
            responseDto.setMessage(classListDto.getMessage());
            return responseDto;
        }

        Date dateCurrent = new Date(System.currentTimeMillis());
        SchoolYear schoolCurrent = schoolYearRepository.findSchoolYearsByDate(dateCurrent);
        if(schoolCurrent != null) {
            currentYearId = schoolCurrent.getYearID();
            responseDto.setCurrentYearId(currentYearId);
        }

        LoadByYearIdRequestDto yearIdDto = new LoadByYearIdRequestDto();
        yearIdDto.setYearId(currentYearId);

        ViewSemesterListResponseDto schoolSemesterListDto = getSemesterListByYearId(yearIdDto);
        responseDto.setSchoolSemesterList(schoolSemesterListDto.getSchoolSemesterList());


        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        return responseDto;
    }

    /**
     * kimpt142
     * 24/07
     * get semster list by year id and exclude item with semester id = 0
     * @param model include yearid
     * @return a semester list and message
     */
    @Override
    public ViewSemesterListResponseDto getSemesterListByYearId(LoadByYearIdRequestDto model) {
        ViewSemesterListResponseDto responseDto = new ViewSemesterListResponseDto();
        MessageDTO message;

        Integer yearId = model.getYearId();
        if(yearId == null){
            message = Constant.SCHOOLYEARID_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }

        List<SchoolSemester> schoolSemesterList = schoolSemesterRepository.findSchoolSemesterByYearIdExcludeZero(yearId);
        if(schoolSemesterList == null || schoolSemesterList.size() == 0){
            message = Constant.SEMESTERLIST_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }else{
            responseDto.setSchoolSemesterList(schoolSemesterList);
        }

        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        return responseDto;
    }

    /**
     * kimpt142
     * 24/07
     * get rank semester list by month id
     * @param model
     * @return
     */
    @Override
    public RankSemesterListResponseDto searchRankSemesterById(SearchRankSemesterRequestDto model) {
        RankSemesterListResponseDto resposeDto = new RankSemesterListResponseDto();
        List<RankSemesterResponseDto> rankSemesterList = new ArrayList<>();
        MessageDTO message;

        Integer semesterId = model.getSemesterId();
        Integer classId = model.getClassId();

        if(semesterId == null){
            message = Constant.SEMESTERID_EMPTY;
            resposeDto.setMessage(message);
            return resposeDto;
        }

        //check semester ranked or not
        SchoolSemester checkSchoolSemester =  schoolSemesterRepository.findSchoolSemesterBySemesterId(semesterId);
        if(checkSchoolSemester != null && checkSchoolSemester.getIsRanked() != null
                && checkSchoolSemester.getIsRanked() != 0){
            resposeDto.setCheckEdit(1);
        }
        else{
            resposeDto.setCheckEdit(0);
        }

        List<SchoolRankSemester> schoolRankSemesterList = schoolRankSemesterRepository.findAllBySchoolRankSemesterId(semesterId, classId);
        if(schoolRankSemesterList == null || schoolRankSemesterList.size() == 0)
        {
            message = Constant.RANKSEMESTERLIST_EMPTY;
            resposeDto.setMessage(message);
            return resposeDto;
        }
        else{
            for(SchoolRankSemester item : schoolRankSemesterList) {
                RankSemesterResponseDto rankSemesterDto = new RankSemesterResponseDto();
                rankSemesterDto.setClassId(item.getSchoolRankSemesterId().getSchoolClass().getClassId());
                rankSemesterDto.setClassName(item.getSchoolRankSemesterId().getSchoolClass().getGrade()+ " "
                        + item.getSchoolRankSemesterId().getSchoolClass().getGiftedClass().getName());
                rankSemesterDto.setTotalGradeMonth(round(item.getTotalGradeMonth()));
                rankSemesterDto.setTotalRankMonth(item.getTotalRankMonth());
                rankSemesterDto.setRank(item.getRank());
                rankSemesterList.add(rankSemesterDto);
            }
        }

        message = Constant.SUCCESS;
        resposeDto.setMessage(message);
        resposeDto.setRankSemesterList(rankSemesterList);
        return resposeDto;
    }

    /**
     * kimpt142
     * 24/07
     * download file excel with the rank semester using semester id
     * @param model
     * @return
     */
    @Override
    public ByteArrayInputStream downloadRankSemester(SearchRankSemesterRequestDto model) {
        //get rank semester list
        List<RankSemesterResponseDto> rankSemesterList = searchRankSemesterById(model).getRankSemesterList();

        String[] COLUMNs = {"Lớp", "Tổng thứ tự", "Tổng điểm", "Xếp hạng"};

        try {
            Workbook workbook = new HSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Bảng xếp hạng học kỳ");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (RankSemesterResponseDto item : rankSemesterList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.getClassName());
                row.createCell(1).setCellValue(item.getTotalRankMonth());
                row.createCell(2).setCellValue(round(item.getTotalGradeMonth()));
                row.createCell(3).setCellValue(item.getRank());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        }catch (Exception e){
            System.out.println(e);
        }

        return null;
    }

    private double round(Double input) {
        return (double) Math.round(input * 100) / 100;
    }
}
