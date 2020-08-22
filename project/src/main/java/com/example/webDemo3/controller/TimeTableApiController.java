package com.example.webDemo3.controller;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.*;
import com.example.webDemo3.dto.manageTimeTableResponseDto.ListApplyDateAndClassResponseDto;
import com.example.webDemo3.dto.manageTimeTableResponseDto.ListApplyDateandTeacherResponseDto;
import com.example.webDemo3.dto.manageTimeTableResponseDto.SearchTimeTableResponseDto;
import com.example.webDemo3.dto.request.manageTimeTableRequestDto.CheckDateRequestDto;
import com.example.webDemo3.dto.request.manageTimeTableRequestDto.ClassTimeTableRequestDto;
import com.example.webDemo3.dto.request.manageTimeTableRequestDto.TeacherTimeTableRequestDto;
import com.example.webDemo3.service.manageTimeTableService.AddTimeTableService;
import com.example.webDemo3.service.manageTimeTableService.TimeTableService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@RestController
@RequestMapping("/api/timetable")
public class TimeTableApiController {

    @Autowired
    private TimeTableService viewTimTaClassService;

    @Autowired
    private AddTimeTableService addTimeTableService;

    @PostMapping("/update")
    public MessageDTO mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile,
                                     @RequestParam("date") Date date, Model model)
    {
        MessageDTO message = new MessageDTO();
        Workbook workbook = null;
        try {
            workbook = new HSSFWorkbook(reapExcelDataFile.getInputStream());
        }catch (Exception eo){
            try {
                workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
            }catch (Exception e) {
                message = Constant.INCORRECT_FILE_FORMAT;
                System.out.println(e);
            }
        }
        if(workbook != null){
            //Date date = Date.valueOf("2020-01-01");
            message = addTimeTableService.addTimetable(workbook,date);
        }
        return message;
    }

    @PostMapping("/checkDate")
    public MessageDTO checkDulicate(@RequestBody CheckDateRequestDto data)
    {
        MessageDTO message = new MessageDTO();
        if (addTimeTableService.checkDateDuplicate(data.getDate())){
            message = Constant.CONFIRM_UPDATE_TIMTABLE;
        }
        else {
            message.setMessageCode(0);
        }
        return message;
    }

    /**
     * lamnt98
     * 09/07
     * catch request from client to get list class and applyDate, currentId, classId
     * @param
     * @return ListApplyDateAndClassResponseDto
     */
    @PostMapping("/getapplydateandclass")
    public ListApplyDateAndClassResponseDto getListClassAndApplyDate()
    {
        return viewTimTaClassService.getApplyDateAndClassList();
    }

    /**
     * lamnt98
     * 09/07
     * catch request from client to get list teacher and applyDate, currentId, teacherId
     * @param
     * @return ListApplyDateandTeacherResponseDto
     */
    @PostMapping("/getapplydateandteacher")
    public ListApplyDateandTeacherResponseDto getListTeacherAndApplyDate()
    {
        return viewTimTaClassService.getApplyDateAndTeacherList();
    }

    /**
     * lamnt98
     * 09/07
     * catch request from client to search class timetable by currentDate and classId
     * @param module
     * @return SearchTimeTableResponseDto
     */
    @PostMapping("/searchclasstimetable")
    public SearchTimeTableResponseDto searchClassTimeTable(@RequestBody ClassTimeTableRequestDto module)
    {
        return viewTimTaClassService.searchClassTimeTable(module);
    }

    /**
     * lamnt98
     * 09/07
     * catch request from client to search teacher timetable by currentDate and teacherId
     * @param module
     * @return SearchTimeTableResponseDto
     */
    @PostMapping("/searchteachertimetable")
    public SearchTimeTableResponseDto searchTeacherTimeTable(@RequestBody TeacherTimeTableRequestDto module)
    {
        return viewTimTaClassService.searchTeacherTimeTable(module);
    }
}
