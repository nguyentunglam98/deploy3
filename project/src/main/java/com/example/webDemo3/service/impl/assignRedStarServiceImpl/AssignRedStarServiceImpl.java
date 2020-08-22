package com.example.webDemo3.service.impl.assignRedStarServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageAssignRedStarResponseDto.ListClassAndDateResponseDto;
import com.example.webDemo3.dto.manageAssignRedStarResponseDto.ViewAssignTaskResponseDto;
import com.example.webDemo3.dto.manageEmulationResponseDto.ClassRedStarResponseDto;
import com.example.webDemo3.dto.request.assignRedStarRequestDto.ViewAssignTaskRequestDto;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.ClassRedStar;
import com.example.webDemo3.repository.ClassRedStarRepository;
import com.example.webDemo3.repository.ClassRepository;
import com.example.webDemo3.service.assignRedStarService.AssignRedStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * lamnt98
 * 14/07
 * View assign task
 */
@Service
public class AssignRedStarServiceImpl implements AssignRedStarService {
    @Autowired
    private ClassRedStarRepository classRedStarRepository;

    @Autowired
    private ClassRepository classRepository;

    @Override
    public ViewAssignTaskResponseDto viewTask(ViewAssignTaskRequestDto data) {
        ViewAssignTaskResponseDto assignTaskResponseDto = new ViewAssignTaskResponseDto();
        MessageDTO message = Constant.SUCCESS;
        if (data.getRedStar() == null) {
            data.setRedStar("");
        }
        try {
            List<ClassRedStar> assignList = classRedStarRepository.findAllByCondition(data.getClassId()
                    , "", data.getFromDate());
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
            assignTaskResponseDto.setListAssignTask(listAssignTask);
        } catch (Exception e) {
            message.setMessageCode(1);
            message.setMessage(e.toString());
            assignTaskResponseDto.setMessage(message);
            return assignTaskResponseDto;
        }
        assignTaskResponseDto.setMessage(message);
        return assignTaskResponseDto;
    }


    @Override
    public ListClassAndDateResponseDto listStarClassDate() {
        ListClassAndDateResponseDto list = new ListClassAndDateResponseDto();
        List<Class> listClass = new ArrayList<>();
        List<Date> listDate = new ArrayList<>();
        MessageDTO message = new MessageDTO();
        try{
            listClass = classRepository.findAll();
            listDate = classRedStarRepository.findDistinctByClassRedStarId_FROM_DATE();

            list.setListClass(listClass);
            list.setListDate(listDate);

            //check list class emptu or not
            if(listClass.size() == 0){
                message = Constant.LIST_CLASS_EMPTY;
                list.setMessage(message);
                return list;
            }

            //check list date empty or not
            if(listDate.size() == 0){
                message = Constant.LIST_DATE_EMPTY;
                list.setMessage(message);
                return list;
            }

            message = Constant.SUCCESS;
            list.setMessage(message);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            list.setMessage(message);
            return list;
        }
        return list;
    }
}
