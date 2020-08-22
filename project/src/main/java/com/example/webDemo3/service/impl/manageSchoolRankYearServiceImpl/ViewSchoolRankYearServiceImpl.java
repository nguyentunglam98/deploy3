package com.example.webDemo3.service.impl.manageSchoolRankYearServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageClassResponseDto.ClassListResponseDto;
import com.example.webDemo3.dto.manageClassResponseDto.ClassResponseDto;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.*;
import com.example.webDemo3.dto.manageSchoolYearResponseDto.SchoolYearTableResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.LoadByYearIdRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.SearchRankYearRequestDto;
import com.example.webDemo3.entity.SchoolRankYear;
import com.example.webDemo3.repository.SchoolRankYearRepository;
import com.example.webDemo3.service.manageClassService.ClassService;
import com.example.webDemo3.service.manageSchoolRankYearSerivce.ViewSchoolRankYearService;
import com.example.webDemo3.service.manageSchoolYearService.SchoolYearService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/*
kimpt142 - 24/07
 */
@Service
public class ViewSchoolRankYearServiceImpl implements ViewSchoolRankYearService {

    @Autowired
    private SchoolRankYearRepository schoolRankYearRepository;

    @Autowired
    private ClassService classService;

    @Autowired
    private SchoolYearService schoolYearService;

    /**
     * kimpt142 - 24/07
     * get the rank year list by year id
     * @param model include yearid
     * @return RankYearListResponseDto
     */
    @Override
    public RankYearListResponseDto searchRankYearById(SearchRankYearRequestDto model) {
        RankYearListResponseDto resposeDto = new RankYearListResponseDto();
        List<RankYearResponseDto> rankYearList = new ArrayList<>();
        MessageDTO message;

        Integer yearId = model.getYearId();
        Integer classId = model.getClassId();

        if(yearId == null){
            message = Constant.SCHOOLYEARID_EMPTY;
            resposeDto.setMessage(message);
            return resposeDto;
        }

        List<SchoolRankYear> schoolRankYearList = schoolRankYearRepository.findAllByYearId(yearId, classId);
        if(schoolRankYearList == null || schoolRankYearList.size() == 0)
        {
            message = Constant.RANKYEARLIST_EMPTY;
            resposeDto.setMessage(message);
            return resposeDto;
        }
        else{
            for(SchoolRankYear item : schoolRankYearList) {
                RankYearResponseDto rankYearDto = new RankYearResponseDto();
                rankYearDto.setClassId(item.getSchoolRankYearId().getSchoolClass().getClassId());
                rankYearDto.setClassName(item.getSchoolRankYearId().getSchoolClass().getGrade()+ " "
                        + item.getSchoolRankYearId().getSchoolClass().getGiftedClass().getName());
                rankYearDto.setTotalGradeSemester(round(item.getTotalGradeSemester()));
                rankYearDto.setTotalRankSemester(item.getTotalRankSemester());
                rankYearDto.setRank(item.getRank());
                rankYearList.add(rankYearDto);
            }
        }

        message = Constant.SUCCESS;
        resposeDto.setMessage(message);
        resposeDto.setRankYearList(rankYearList);
        return resposeDto;
    }

    /**
     * kimpt142
     * 24/07
     * download rank year by yearid
     * @param model include yearid
     * @return
     */
    @Override
    public ByteArrayInputStream downloadRankYear(SearchRankYearRequestDto model) {
        //get rank semester list
        List<RankYearResponseDto> rankYearList = searchRankYearById(model).getRankYearList();

        String[] COLUMNs = {"Lớp", "Tổng thứ tự", "Tổng điểm", "Xếp hạng"};

        try {
            Workbook workbook = new HSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Bảng xếp hạng năm học");

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
            for (RankYearResponseDto item : rankYearList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.getClassName());
                row.createCell(1).setCellValue(item.getTotalRankSemester());
                row.createCell(2).setCellValue(round(item.getTotalGradeSemester()));
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
     * 31/07
     * load school year list and class list
     * @return
     */
    @Override
    public LoadRankYearResponseDto loadRankYear() {
        LoadRankYearResponseDto responseDto = new LoadRankYearResponseDto();
        MessageDTO message;
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

        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        return responseDto;
    }

    private double round(Double input) {
        return (double) Math.round(input * 100) / 100;
    }
}
