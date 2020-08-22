package com.example.webDemo3.service.impl.manageTimeTableServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.Teacher;
import com.example.webDemo3.entity.TimeTable;
import com.example.webDemo3.exception.MyException;
import com.example.webDemo3.repository.ClassRepository;
import com.example.webDemo3.repository.TeacherRepository;
import com.example.webDemo3.repository.TimetableRepository;
import com.example.webDemo3.service.manageTimeTableService.AddTimeTableService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddTimeTableServiceImpl implements AddTimeTableService {
    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ClassRepository classRepository;

    private static int rowMax = 35;
    private static int startCol = 3;
    private static int classRow = 4;
    private static int rowMaxAfternoon = 19;
    private static int messageCodeFail = 1;

    private static String sheetMoring = "TKB Sang";
    private static String sheetAfternoon = "TKB Chiều";
    private static String notHaveSheet = "không có sheet ";
    private static String TimeTableBlank = "thời khóa biểu trống ";
    private static String notFindClass = "không tìm thấy lớp " ;
    private static String notFindTearcher = "không tìm thấy giáo viên ";

    @Override
    public Boolean checkDateDuplicate(Date applyDate){
        Date date = timetableRepository.getAllByApplyDate(applyDate);
        if(date == null){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    @Transactional
    public MessageDTO addTimetable(Workbook workbook, Date applyDate) {
        MessageDTO message = new MessageDTO();

        Sheet worksheetMorning = workbook.getSheet(sheetMoring) ;//.getSheetAt(0);
        Sheet worksheetAfternoon = workbook.getSheet(sheetAfternoon);
        if(worksheetMorning == null){
            message.setMessageCode(messageCodeFail);
            message.setMessage(notHaveSheet + sheetMoring);
            return message;
        }
        if(worksheetAfternoon == null){
            message.setMessageCode(messageCodeFail);
            message.setMessage(notHaveSheet + sheetMoring);
            return message;
        }
        try {
            if(checkDateDuplicate(applyDate)){
                timetableRepository.deleteByApplyDate(applyDate);
            }
            message = addTimetableMorning(worksheetMorning, applyDate);
            message = addTimetableAfternoon(worksheetAfternoon, applyDate);
        } catch (Exception e) {
            message.setMessageCode(messageCodeFail);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return message;
    }

    public MessageDTO addTimetableMorning(Sheet worksheet,Date applyDate) throws MyException
    {
        MessageDTO message = new MessageDTO();
        List<String> classList = new ArrayList<>();
        List<List<String>> dataList = new ArrayList<List<String>>();
        int row = rowMax;
        int j = startCol;
        while (true) {
            String classItem = getData(worksheet, classRow, j);
            if (classItem == null || classItem.trim().isEmpty()) {
                break;
            }
            classList.add(classItem);
            List<String> subDatalist = new ArrayList<>();
            for (int i = classRow + 1; i < row; i++) {
                String s = getData(worksheet, i, j);
                subDatalist.add(s);
            }
            dataList.add(subDatalist);
            j++;
        }
        if(dataList.size() == 0){
            throw new MyException(TimeTableBlank);
        }

        String preClass = "";
        int isAdd = 0;
        String lop;
        int day, slot;
        for (int i = 0; i < dataList.size(); i++) {
            lop = classList.get(i);
            Class classTb = classRepository.findClassActiveByClassIdentifier(lop);
            if(classTb == null){
                System.out.println(notFindClass + lop);
                throw new MyException(notFindClass+ lop);
            }
            // check is adđitional
            if(classTb.getClassIdentifier().equalsIgnoreCase(preClass)){
                isAdd ++;
            }
            else{
                preClass = classTb.getClassIdentifier();
                isAdd = 0;
            }

            List<String> subData = dataList.get(i);
            for (int k = 0; k < subData.size(); k++) {
                String data = subData.get(k);
                if (data == null || data.trim().isEmpty()) {
                    continue;
                }
                //get mn,gv
                int vt = data.indexOf("-");
                String mh, gv = null;
                if (vt == -1) {
                    mh = data;
                } else {
                    mh = data.substring(0, vt);
                    gv = data.substring(vt + 1);
                }
                //check teacher
                Teacher teacherTb = null;
                if (gv != null) {
                    teacherTb = teacherRepository.findTeacherTeacherIdentifier(gv);
                    if(teacherTb == null) {
                        System.out.println(notFindTearcher + gv);
                        throw new MyException(notFindTearcher + gv);
                    }
                }
                //get slo
                slot = k % 5 + 1;
                //get day
                day = 1;
                if (k > 0) day = k / 5 + 1;
                // add data
                try {
                    TimeTable tb = new TimeTable();
                    if (teacherTb != null) {
                        tb.setTeacherId(teacherTb.getTeacherId());
                    }
                    tb.setClassId(classTb.getClassId());
                    tb.setSlot(slot);
                    tb.setDayId(day);
                    tb.setSubject(mh);
                    tb.setApplyDate(applyDate);
                    tb.setIsAfternoon(0);
                    tb.setIsAdditional(isAdd);
                    timetableRepository.save(tb);
                } catch (Exception e) {
                    day ++;
                    throw new MyException("không thêm được tkb sáng ở lớp: "
                            + lop + ", thứ:" + day + ", tiết: " + slot);
                }
            }
        }
        System.out.println("thành công");
        message = Constant.SUCCESS;
        return message;
    }

    public MessageDTO addTimetableAfternoon(Sheet worksheet,Date applyDate) throws Exception
    {
        MessageDTO message = new MessageDTO();
        List<String> classList = new ArrayList<>();
        List<List<String>> dataList = new ArrayList<List<String>>();
        int row = rowMaxAfternoon;
        int j = startCol;
        while (true) {
            String classItem = getData(worksheet, classRow, j);
            if (classItem == null || classItem.trim().isEmpty()) {
                break;
            }
            classList.add(classItem);
            List<String> subDatalist = new ArrayList<>();
            for (int i = classRow + 1; i < row; i++) {
                String s = getData(worksheet, i, j);
                subDatalist.add(s);
            }
            dataList.add(subDatalist);
            j++;
        }
        if(dataList.size() == 0){
            throw new MyException(TimeTableBlank);
        }

        String preClass = "";
        int isAdd = 0;
        String lop;
        int day, slot;
        for (int i = 0; i < dataList.size(); i++) {
            lop = classList.get(i);
            Class classTb = classRepository.findClassActiveByClassIdentifier(lop);
            if(classTb == null) {
                System.out.println(notFindClass+ lop);
                throw new MyException(notFindClass + lop);
            }
            // check is adđitional
            if(classTb.getClassIdentifier().equalsIgnoreCase(preClass)){
                isAdd ++;
            }
            else{
                preClass = classTb.getClassIdentifier();
                isAdd = 0;
            }
            List<String> subData = dataList.get(i);
            for (int k = 0; k < subData.size(); k++) {
                String data = subData.get(k);

                if (data == null || data.trim().isEmpty()) {
                    continue;
                }
                //get mh,gv
                int vt = data.indexOf("-");
                String mh, gv = null;
                if (vt == -1) {
                    mh = data;
                } else {
                    mh = data.substring(0, vt);
                    gv = data.substring(vt + 1);
                }
                //check teacher
                Teacher teacherTb = null;
                if (gv != null) {
                    teacherTb = teacherRepository.findTeacherTeacherIdentifier(gv);
                    if(teacherTb == null) {
                        System.out.println(notFindTearcher + gv);
                        throw new MyException(notFindTearcher + gv);
                    }
                }
                //get slot
                slot = k % 2 + 1;
                //get day
                day = 1;
                if (k > 0) day = k / 4 + 1;
                int isOdd = 1;
                if(k % 4 == 0 || k % 4 ==1 ){
                    isOdd = 0;
                }
                //add data
                try {
                    TimeTable tb = new TimeTable();
                    if (teacherTb != null) {
                        tb.setTeacherId(teacherTb.getTeacherId());
                    }
                    tb.setClassId(classTb.getClassId());
                    tb.setSlot(slot);
                    tb.setDayId(day);
                    tb.setSubject(mh);
                    tb.setApplyDate(applyDate);
                    tb.setIsAfternoon(1);
                    tb.setIsOddWeek(isOdd);
                    tb.setIsAdditional(isAdd);
                    timetableRepository.save(tb);
                } catch (Exception e) {
                    throw new MyException("không thêm được tkb chiều ở lớp: "
                            + lop + ", thứ:" + day + ", tiết: " + slot);
                }
            }
        }
        System.out.println("thành công");
        message = Constant.SUCCESS;
        return message;
    }



    public String getData(Sheet worksheet, int i, int j) {
        String data = null;
        try {
            data = worksheet.getRow(i).getCell(j).getStringCellValue();
        } catch (Exception e) {
            System.out.println(e);
        }
        return data;
    }
}
