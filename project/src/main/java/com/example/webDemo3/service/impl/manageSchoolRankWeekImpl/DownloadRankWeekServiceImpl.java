package com.example.webDemo3.service.impl.manageSchoolRankWeekImpl;

import com.example.webDemo3.dto.manageSchoolRankResponseDto.RankWeekResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.SearchRankWeekRequestDto;
import com.example.webDemo3.service.manageSchoolRankWeek.DownloadRankWeekService;
import com.example.webDemo3.service.manageSchoolRankWeek.ViewSchoolRankWeekService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/*
kimpt142 - 21/07
 */
@Service
public class DownloadRankWeekServiceImpl implements DownloadRankWeekService {

    @Autowired
    private ViewSchoolRankWeekService viewSchoolRankWeekService;

    /**
     * kimpt142
     * 21/07
     * export excel file with data is rank week
     * @param model
     * @return
     */
    @Override
    public ByteArrayInputStream downloadRankWeek(SearchRankWeekRequestDto model) {
        //get rank week list
        List<RankWeekResponseDto> rankWeekList = viewSchoolRankWeekService.searchRankWeekByWeekAndClass(model).getRankWeekList();

        String[] COLUMNs = {"Lớp", "Điểm nề nếp", "Điểm học tập", "Điểm phong trào", "Điểm lao động", "Tổng điểm", "Xếp hạng"};

        try {
            Workbook workbook = new HSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Bảng xếp hạng tuần");

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
            for (RankWeekResponseDto item : rankWeekList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.getClassName());
                row.createCell(1).setCellValue(round(item.getEmulationGrade()));
                row.createCell(2).setCellValue(round(item.getLearningGrade()));
                row.createCell(3).setCellValue(round(item.getMovementGrade()));
                row.createCell(4).setCellValue(round(item.getLaborGrade()));
                row.createCell(5).setCellValue(round(item.getTotalGrade()));
                row.createCell(6).setCellValue(item.getRank());
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
     * 21/07
     * round number to get 2 number after point
     * @param input
     * @return
     */
    private double round(Double input) {
        return (double) Math.round(input * 100) / 100;
    }
}
