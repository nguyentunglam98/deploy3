package com.example.webDemo3.service.impl.assignRedStarServiceImpl;

import com.example.webDemo3.dto.manageAssignRedStarResponseDto.ViewAssignTaskResponseDto;
import com.example.webDemo3.dto.manageEmulationResponseDto.ClassRedStarResponseDto;
import com.example.webDemo3.dto.request.assignRedStarRequestDto.DownloadAssignRedStarRequestDto;
import com.example.webDemo3.entity.ClassRedStar;
import com.example.webDemo3.repository.ClassRedStarRepository;
import com.example.webDemo3.service.assignRedStarService.DownloadAssignRedStarService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DownloadAssignRedStarServiceImpl implements DownloadAssignRedStarService {

    @Autowired
    private ClassRedStarRepository classRedStarRepository;

    @Override
    public ByteArrayInputStream download(DownloadAssignRedStarRequestDto data){
        //if(data.getClassId())

        String[] COLUMNs = {"Lớp", "Sao đỏ 1", "Sao đỏ 2"};
        try {
            List<ClassRedStar> assignList = classRedStarRepository.findAllByCondition(data.getClassId()
                    ,"",data.getFromDate());
            List<ClassRedStarResponseDto> listAssignTask = new ArrayList<>();
            String redstar1 = "";
            for (int i = 0; i < assignList.size(); i++) {
                ClassRedStarResponseDto itemResponse = new ClassRedStarResponseDto();
                ClassRedStar item = assignList.get(i);
                if(i % 2 == 0){
                    redstar1 = item.getClassRedStarId().getRED_STAR();
                }
                if (i % 2 == 0 && item.getClassRedStarId().getRED_STAR().contains(data.getRedStar())) {
                    itemResponse.setClassId(item.getClassSchool().getClassId());
                    itemResponse.setClassName(item.getClassSchool().getGrade() + " "
                            + item.getClassSchool().getGiftedClass().getName());
                    itemResponse.setRedStar1(redstar1);
                    listAssignTask.add(itemResponse);
                } else if (i % 2 == 1) {
                    if (listAssignTask.size() > 0 && item.getClassSchool().getClassId() == listAssignTask.get(listAssignTask.size() - 1).getClassId()) {
                        listAssignTask.get(listAssignTask.size() - 1).setRedStar2(item.getClassRedStarId().getRED_STAR());
                    }
                    else if(item.getClassRedStarId().getRED_STAR().contains(data.getRedStar())){
                        itemResponse.setClassId(item.getClassSchool().getClassId());
                        itemResponse.setClassName(item.getClassSchool().getGrade() + " "
                                + item.getClassSchool().getGiftedClass().getName());
                        itemResponse.setRedStar1(redstar1);
                        itemResponse.setRedStar2(item.getClassRedStarId().getRED_STAR());
                        listAssignTask.add(itemResponse);
                    }
                }
            }

            Workbook workbook = new HSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("phân công");

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
            for (ClassRedStarResponseDto item : listAssignTask) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.getClassName());
                row.createCell(1).setCellValue(item.getRedStar1());
                row.createCell(2).setCellValue(item.getRedStar2());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        }catch (Exception e){
            System.out.println(e);
        }

        return null;
    }

}
