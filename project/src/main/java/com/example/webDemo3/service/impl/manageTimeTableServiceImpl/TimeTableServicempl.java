package com.example.webDemo3.service.impl.manageTimeTableServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.*;
import com.example.webDemo3.dto.manageTimeTableResponseDto.*;
import com.example.webDemo3.dto.request.manageTimeTableRequestDto.ClassTimeTableRequestDto;
import com.example.webDemo3.dto.request.manageTimeTableRequestDto.TeacherTimeTableRequestDto;
import com.example.webDemo3.entity.*;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.repository.*;
import com.example.webDemo3.service.manageTimeTableService.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * lamnt98
 * 01/07
 */
@Service
public class TimeTableServicempl implements TimeTableService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private TeacherRepository teacherRepository;


    /**
     * lamnt98
     * 01/07
     * Get applyDate list, class list, currentId, classId
     * @param
     * @return ListApplyDateAndClassResponseDto
     */
    @Override
    public ListApplyDateAndClassResponseDto getApplyDateAndClassList() {
        ListApplyDateAndClassResponseDto timeTabel = new ListApplyDateAndClassResponseDto();
        MessageDTO messageDTO = new MessageDTO();
        Date currentDate = null;
        Integer classId = null;
        List<Date> appyDateList = null;
        List<Class> classList = null;

        try {
            appyDateList = timetableRepository.getAllDate();

            //check applyDateList empty or not
            if(appyDateList.size() != 0){
                currentDate = appyDateList.get(0);
                timeTabel.setCurrentDate(currentDate);
                timeTabel.setAppyDateList(appyDateList);
            }

            classList = classRepository.findAll();

            //check classList empty or not
            if(classList.size() != 0){
                timeTabel.setClassList(classList);
                classId = classList.get(0).getClassId();
                timeTabel.setClassId(classId);
            }

            //check applyDateList and classTeacher empty or not
            if(appyDateList.size() == 0 && classList.size() == 0 ){
                messageDTO.setMessageCode(1);
                timeTabel.setMessage(messageDTO);
                return  timeTabel;
            }

            messageDTO = Constant.SUCCESS;
            timeTabel.setMessage(messageDTO);
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            timeTabel.setMessage(messageDTO);
            return  timeTabel;
        }
        return timeTabel;
    }

    /**
     * lamnt98
     * 07/09
     * Get applyDate list, teacher list, currentId, teacherId
     * @param
     * @return ListApplyDateandTeacherResponseDto
     */
    @Override
    public ListApplyDateandTeacherResponseDto getApplyDateAndTeacherList() {
        ListApplyDateandTeacherResponseDto timeTabel = new ListApplyDateandTeacherResponseDto();
        MessageDTO messageDTO = new MessageDTO();
        Date currentDate = null;
        Integer teacherId = null;
        List<Date> appyDateList;
        List<Teacher> teacherList = null;

        try {
            appyDateList = timetableRepository.getAllDate();

            //check dateList empty or not
            if(appyDateList.size() != 0){
                currentDate = appyDateList.get(0);
                timeTabel.setCurrentDate(currentDate);
                timeTabel.setAppyDateList(appyDateList);
            }

            teacherList = teacherRepository.findAll();

            //check teacher list empty or not
            if(teacherList.size() != 0){
                teacherId = teacherList.get(0).getTeacherId();
                timeTabel.setTeacherId(teacherId);
                timeTabel.setTeacherList(teacherList);
            }

            //check applyDateList and teacherList empty or not
            if(appyDateList.size() == 0 && teacherList.size() == 0 ){
                messageDTO.setMessageCode(1);
                timeTabel.setMessage(messageDTO);
                return  timeTabel;
            }

            messageDTO = Constant.SUCCESS;
            timeTabel.setMessage(messageDTO);
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            timeTabel.setMessage(messageDTO);
            return  timeTabel;
        }
        return timeTabel;
    }

    /**
     * lamnt98
     * 01/07
     * search timetable of class by currentDate and classId
     * @param classTimeTable
     * @return SearchTimeTableResponseDto
     */
    @Override
    public SearchTimeTableResponseDto searchClassTimeTable(ClassTimeTableRequestDto classTimeTable) {
        SearchTimeTableResponseDto timeTabel = new SearchTimeTableResponseDto();
        List<List<MorInforTimeTableDto>> morningTimeTableList = new ArrayList<>();
        List<List<AfterInforTimeTableDto>> afternoonTimeTableTableList = new ArrayList<>();
        MessageDTO messageDTO = new MessageDTO();
        Date currentDate = classTimeTable.getApplyDate();
        Integer classId = classTimeTable.getClassId();
        List<TimeTable> morningTimeTable = null;
        List<TimeTable> afternoonTimeTable = null;


        try {
            //check currentDate null or not
            if(currentDate == null){
                messageDTO = Constant.FROMDATE_EMPTY;
                timeTabel.setMessage(messageDTO);
                return timeTabel;
            }

            //check classId null or not
            if(classId == null){
                messageDTO = Constant.CLASS_ID_NULL;
                timeTabel.setMessage(messageDTO);
                return timeTabel;
            }
            //check class exists or not
            if(classRepository.findByClassId(classId) == null){
                messageDTO = Constant.CLASS_NOT_EXIST;
                timeTabel.setMessage(messageDTO);
                return  timeTabel;
            }

            for(int i = 0; i < 4; i++){
                morningTimeTable = timetableRepository.getMorningClassTimeTable(currentDate,classId,i);
                afternoonTimeTable = timetableRepository.getAfternoonClassTimeTable(currentDate,classId,i);

                if(morningTimeTable.size() == 0 && afternoonTimeTable.size() == 0 ){
                    break;
                }

                if(morningTimeTable.size() != 0){
                    morningTimeTableList.add(changeMorningTimeTable(morningTimeTable));
                }

                if(afternoonTimeTable.size() != 0){
                    afternoonTimeTableTableList.add(changeAfternoonTimeTable(afternoonTimeTable));
                }
            }

            //check timetable empty or not
            if(morningTimeTableList.size() == 0 && afternoonTimeTableTableList.size() == 0 ){
                messageDTO = Constant.TIMETABLE_NULL;
                timeTabel.setMessage(messageDTO);
                return  timeTabel;
            }

            timeTabel.setMorningTimeTableList(morningTimeTableList);
            timeTabel.setAfternoonTimeTableTableList(afternoonTimeTableTableList);
            messageDTO = Constant.SUCCESS;
            timeTabel.setMessage(messageDTO);
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            timeTabel.setMessage(messageDTO);
            return  timeTabel;
        }
        return timeTabel;
    }

    /**
     * lamnt98
     * 01/07
     * search timetable of teacher by currentDate and teacherId
     * @param teacherTimeTable
     * @return SearchTimeTableResponseDto
     */
    @Override
    public SearchTimeTableResponseDto searchTeacherTimeTable(TeacherTimeTableRequestDto teacherTimeTable) {
        SearchTimeTableResponseDto timeTabel = new SearchTimeTableResponseDto();
        List<List<MorInforTimeTableDto>> morningTimeTableList = new ArrayList<>();
        List<List<AfterInforTimeTableDto>> afternoonTimeTableTableList = new ArrayList<>();
        MessageDTO messageDTO = new MessageDTO();
        Date currentDate = teacherTimeTable.getApplyDate();
        Integer teacherId = teacherTimeTable.getTeacherId();
        List<TimeTable> morningTimeTable = null;
        List<TimeTable> afternoonTimeTable = null;
        Teacher teacher = null;

        try {

            //check currentDate null or not
            if(currentDate == null){
                messageDTO = Constant.FROMDATE_EMPTY;
                timeTabel.setMessage(messageDTO);
                return timeTabel;
            }

            //check classId null or not
            if(teacherId == null){
                messageDTO = Constant.TEACHER_ID_NULL;
                timeTabel.setMessage(messageDTO);
                return timeTabel;
            }

            teacher = teacherRepository.findById(teacherId).orElse(null);
            //check teacher exists or not
            if( teacher== null){
                messageDTO = Constant.TEACHER_NOT_EXIT;
                timeTabel.setMessage(messageDTO);
                return  timeTabel;
            }


            morningTimeTable = timetableRepository.getMorningTeacherTimeTable(currentDate,teacherId);
            afternoonTimeTable = timetableRepository.getAfternoonTeacherTimeTable(currentDate,teacherId);

            //check morningTimeTable empty or not
            if(morningTimeTable.size() != 0){
                morningTimeTableList.add(changeMorningTimeTable(morningTimeTable));
            }

            //check afternoonTimeTable empty or not
            if(afternoonTimeTable.size() != 0){
                afternoonTimeTableTableList.add(changeAfternoonTimeTable(afternoonTimeTable));
            }

            //check timetable emty or not
            if(morningTimeTableList.size() == 0 && afternoonTimeTableTableList.size() == 0 ){
                messageDTO = Constant.TIMETABLE_NULL;
                timeTabel.setMessage(messageDTO);
                return  timeTabel;
            }

            timeTabel.setMorningTimeTableList(morningTimeTableList);
            timeTabel.setAfternoonTimeTableTableList(afternoonTimeTableTableList);
            messageDTO = Constant.SUCCESS;
            timeTabel.setMessage(messageDTO);
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            timeTabel.setMessage(messageDTO);
            return  timeTabel;
        }
        return timeTabel;
    }

    /**
     * lamnt98
     * 03/07
     * change MorningTimetable from Timetable to ResponseDto
     * @param
     * @return List<MorInforTimeTableDto>
     */
    public List<MorInforTimeTableDto> changeMorningTimeTable(List<TimeTable> list){
        List<MorInforTimeTableDto> listMorning = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            Teacher teacher = null;
            TimeTable timeTable = list.get(i);
            MorInforTimeTableDto morInforTimeTable = new MorInforTimeTableDto();

            Integer teacherId = timeTable.getTeacherId();

            //check teacherId empty or not
            if(teacherId != null){
                teacher = teacherRepository.findById(teacherId).orElse(null);
            }

            //cheack teacher rmpty or not
            if(teacher != null){
                morInforTimeTable.setTeacherIdentifier(teacher.getTeacherIdentifier());
            }

            Class newClass = classRepository.findById(timeTable.getClassId()).orElse(null);

            //check class empty or not
            if(newClass != null){
                morInforTimeTable.setClassIdentifier(newClass.getClassIdentifier());
            }

            morInforTimeTable.setSlotId(timeTable.getSlot());
            morInforTimeTable.setDayId(timeTable.getDayId());
            morInforTimeTable.setSubject(timeTable.getSubject());
            morInforTimeTable.setIsAdditional(timeTable.getIsAdditional());

            listMorning.add(morInforTimeTable);
        }
        return  listMorning;
    }

    /**
     * lamnt98
     * 03/07
     * change AfternoonTimeTable from Timetable to ResponseDto
     * @param
     * @return List<AfterInforTimeTableDto>
     */
    public List<AfterInforTimeTableDto> changeAfternoonTimeTable(List<TimeTable> list){
        List<AfterInforTimeTableDto> listAfternoon = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            TimeTable timeTable = list.get(i);
            AfterInforTimeTableDto afternoonTimeTable = new AfterInforTimeTableDto();
            Teacher teacher = null;

            Integer teacherId = timeTable.getTeacherId();

            //check teacherId empty or not
            if(teacherId != null){
                teacher = teacherRepository.findById(teacherId).orElse(null);
            }

            //check teacher emmpty or not
            if(teacher != null){
                afternoonTimeTable.setTeacherIdentifier(teacher.getTeacherIdentifier());
            }

            Class newClass = classRepository.findById(timeTable.getClassId()).orElse(null);

            //check class empty or not
            if(newClass != null){
                afternoonTimeTable.setClassIdentifier(newClass.getClassIdentifier());
            }

            afternoonTimeTable.setSlotId(timeTable.getSlot());
            afternoonTimeTable.setDayId(timeTable.getDayId());
            afternoonTimeTable.setSubject(timeTable.getSubject());
            afternoonTimeTable.setIsOddWeek(timeTable.getIsOddWeek());
            afternoonTimeTable.setIsAdditional(timeTable.getIsAdditional());
            listAfternoon.add(afternoonTimeTable);
        }
        return  listAfternoon;
    }
}
