package com.example.webDemo3.service.impl.manageSchoolRankMonthImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageClassResponseDto.ClassListResponseDto;
import com.example.webDemo3.dto.manageClassResponseDto.ClassResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.*;
import com.example.webDemo3.dto.manageSchoolYearResponseDto.SchoolYearTableResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.LoadRankMonthResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.LoadByYearIdRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.SearchRankMonthRequestDto;
import com.example.webDemo3.entity.SchoolMonth;
import com.example.webDemo3.entity.SchoolRankMonth;
import com.example.webDemo3.entity.SchoolYear;
import com.example.webDemo3.repository.SchoolMonthRepository;
import com.example.webDemo3.repository.SchoolRankMonthRepository;
import com.example.webDemo3.repository.SchoolYearRepository;
import com.example.webDemo3.service.manageClassService.ClassService;
import com.example.webDemo3.service.manageSchoolRankMonthService.ViewSchoolRankMonthService;
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
kimp142 - 23/07
 */
@Service
public class ViewSchoolRankMonthServiceImpl implements ViewSchoolRankMonthService {

    @Autowired
    private SchoolMonthRepository schoolMonthRepository;

    @Autowired
    private SchoolRankMonthRepository schoolRankMonthRepository;

    @Autowired
    private SchoolYearService schoolYearService;

    @Autowired
    private SchoolYearRepository schoolYearRepository;

    @Autowired
    private ClassService classService;

    /**
     * kimpt142
     * 23/07
     * get all month in school months table exclude monthid = 0
     * @return
     */
    @Override
    public ViewMonthListResponseDto getMonthListByYearId(LoadByYearIdRequestDto model) {
        ViewMonthListResponseDto responseDto = new ViewMonthListResponseDto();
        MessageDTO message;
        Integer yearId = model.getYearId();

        if(yearId == null){
            message = Constant.SCHOOLYEARID_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }

        List<SchoolMonth> schoolMonthList = schoolMonthRepository.findSchoolMonthByYearIdExcludeZero(yearId);
        if(schoolMonthList != null && schoolMonthList.size() != 0){
            responseDto.setSchoolMonthList(schoolMonthList);
        }
        else{
            message = Constant.MONTHLIST_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }

        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        return responseDto;
    }

    /**
     * kimpt142
     * 23/07
     * get information rank with month id
     * @param model
     * @return
     */
    @Override
    public RankMonthListResponseDto searchRankMonthByMonthId(SearchRankMonthRequestDto model) {
        RankMonthListResponseDto resposeDto = new RankMonthListResponseDto();
        List<RankMonthResponseDto> rankMonthDtoList = new ArrayList<>();
        MessageDTO message;

        Integer monthId = model.getMonthId();
        Integer classId = model.getClassId();

        if(monthId == null){
            message = Constant.MONTHID_EMPTY;
            resposeDto.setMessage(message);
            return resposeDto;
        }

        //check month ranked or not
        SchoolMonth checkSchoolMonth =  schoolMonthRepository.findSchoolMonthByMonthId(monthId);
        if(checkSchoolMonth != null && checkSchoolMonth.getSemesterId() != 0){
            resposeDto.setCheckEdit(1);
        }
        else{
            resposeDto.setCheckEdit(0);
        }

        List<SchoolRankMonth> schoolRankMonthList = schoolRankMonthRepository.findAllBySchoolRankMonthId(monthId, classId);
        if(schoolRankMonthList == null || schoolRankMonthList.size() == 0)
        {
            message = Constant.RANKMONTHLIST_EMPTY;
            resposeDto.setMessage(message);
            return resposeDto;
        }
        else{
            for(SchoolRankMonth item : schoolRankMonthList) {
                RankMonthResponseDto rankMonthDto = new RankMonthResponseDto();
                rankMonthDto.setClassId(item.getSchoolRankMonthId().getSchoolClass().getClassId());
                rankMonthDto.setClassName(item.getSchoolRankMonthId().getSchoolClass().getGrade()+ " "
                        + item.getSchoolRankMonthId().getSchoolClass().getGiftedClass().getName());
                rankMonthDto.setTotalGradeWeek(round(item.getTotalGradeWeek()));
                rankMonthDto.setTotalRankWeek(item.getTotalRankWeek());
                rankMonthDto.setRank(item.getRank());
                rankMonthDtoList.add(rankMonthDto);
            }
        }

        message = Constant.SUCCESS;
        resposeDto.setMessage(message);
        resposeDto.setRankMonthList(rankMonthDtoList);
        return resposeDto;
    }

    /**
     * download the rank month using month id
     * @param model
     * @return
     */
    @Override
    public ByteArrayInputStream downloadRankMonth(SearchRankMonthRequestDto model) {
        //get rank month list
        List<RankMonthResponseDto> rankMonthList = searchRankMonthByMonthId(model).getRankMonthList();

        String[] COLUMNs = {"Lớp", "Tổng thứ tự", "Tổng điểm", "Xếp hạng"};

        try {
            Workbook workbook = new HSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Bảng xếp hạng tháng");

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
            for (RankMonthResponseDto item : rankMonthList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.getClassName());
                row.createCell(1).setCellValue(item.getTotalRankWeek());
                row.createCell(2).setCellValue(round(item.getTotalGradeWeek()));
                row.createCell(3).setCellValue(item.getRank());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        }catch (Exception e){
            System.out.println(e);
        }

        return null;
    }

    /**
     * kimpt142
     * 23/07
     * get school year list and month list by current year id
     */
    @Override
    public LoadRankMonthResponseDto loadRankMonthPage() {
        LoadRankMonthResponseDto responseDto = new LoadRankMonthResponseDto();
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

        //get month list with yearid
        ViewMonthListResponseDto schoolMonthListDto = getMonthListByYearId(yearIdDto);
        responseDto.setSchoolMonthList(schoolMonthListDto.getSchoolMonthList());

        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        return responseDto;
    }

    private double round(Double input) {
        return (double) Math.round(input * 100) / 100;
    }
}
