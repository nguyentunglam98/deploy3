package com.example.webDemo3.service.impl.manageTeacherServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageTeacherResponseDto.ViewTeaInforResponseDto;
import com.example.webDemo3.dto.manageTeacherResponseDto.ViewTeaListResponseDto;
import com.example.webDemo3.dto.request.manageTeacherRequestDto.*;
import com.example.webDemo3.entity.Teacher;
import com.example.webDemo3.repository.TeacherRepository;
import com.example.webDemo3.service.manageTeacherService.TeacherService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherServicempl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    /**
     * lamnt98
     * 01/07
     * add teacher
     * @param addTeacher
     * @return MessageDTO
     */
    @Override
    public MessageDTO addTeacher(AddTeacherRequestDto addTeacher) {
        MessageDTO messageDTO = new MessageDTO();
        Teacher teacher = new Teacher();
        String teacherIdentifier = addTeacher.getTeacherIdentifier().trim();
        String fullName = addTeacher.getFullName();
        String phone = addTeacher.getPhone();
        String email = addTeacher.getEmail();

        try {
            if (teacherIdentifier.isEmpty()) {
                messageDTO = Constant.TEACHER_IDENTIFIER_EMPTY;
                return messageDTO;
            }
            teacher = teacherRepository.findTeacherTeacherIdentifier(teacherIdentifier);
            if (teacher == null) {
                teacher = new Teacher();
                teacher.setFullName(fullName);
                teacher.setTeacherIdentifier(teacherIdentifier);

                teacher.setEmail(email);
                teacher.setPhone(phone);
                teacherRepository.save(teacher);
            } else {
                messageDTO = Constant.TEACHER_EXIT;
                return messageDTO;
            }
            messageDTO = Constant.ADD_TEACHER_SUCCESS;
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            return messageDTO;
        }
        return messageDTO;
    }

    /**
     * lamnt98
     * 01/07
     * delete teacher
     * @param deleteTeacher
     * @return MessageDTO
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
    public MessageDTO deleteTeacher(DeleteTeacherRequestDto deleteTeacher) {
        MessageDTO messageDTO = new MessageDTO();
        try{
            List<Integer> listTeacher = deleteTeacher.getListTeacher();
            //check size list teacher
            if(listTeacher.size() == 0){
                messageDTO = Constant.TEACHER_NOT_EXIT;
                return  messageDTO;
            }
            for(Integer teacherId : listTeacher){
                Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
                //check teacher exists or not
                if(teacher != null && (teacher.getStatus() == null || teacher.getStatus() != 1)){
                    teacher.setStatus(1);
                    teacherRepository.save(teacher);
                }else{
                    messageDTO = Constant.TEACHER_NOT_EXIT;
                    return  messageDTO;
                }
            }
            messageDTO = Constant.DELETE_TEACHER_SUCCESS;
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            return messageDTO;
        }
        return messageDTO;
    }

    /**
     * lamnt98
     * 01/07
     * edit teacher information
     * @param editTeaInforRequestDto
     * @return MessageDTO
     */
    @Override
    public MessageDTO editTeacherInformation(EditTeaInforRequestDto editTeaInforRequestDto) {
        MessageDTO message = new MessageDTO();
        Teacher teacher = null;
        Teacher newTeacher = null;

        try {
            Integer teacherId = editTeaInforRequestDto.getTeacherId();

            //check teacherId null or not
            if(teacherId == null){
                message = Constant.TEACHER_ID_NULL;
                return message;
            }

            //check teacherIdentifier empty or not
            if(editTeaInforRequestDto.getTeacherIdentifier().trim().isEmpty()){
                message = Constant.TEACHER_IDENTIFIER_EMPTY;
                return message;
            }

            //check and find teacher in database
            teacher = teacherRepository.findById(teacherId).orElse(null);
            if(teacher != null)
            {
                //check teacherIdentifier exist or not
                newTeacher = teacherRepository.findTeacherTeacherIdentifier(editTeaInforRequestDto.getTeacherIdentifier());
                if(newTeacher != null && newTeacher.getTeacherId() != teacherId){
                    message = Constant.TEACHER_EXIT;
                    return message;
                }
                teacher.setFullName(editTeaInforRequestDto.getFullName());
                teacher.setPhone(editTeaInforRequestDto.getPhone());
                teacher.setEmail(editTeaInforRequestDto.getEmail());
                teacher.setTeacherIdentifier(editTeaInforRequestDto.getTeacherIdentifier());
                teacherRepository.save(teacher);
                message = Constant.EDIT_TEACHER_INFORMATION_SUCCESS;
            }else{
                message = Constant.TEACHER_NOT_EXIT;
            }
        } catch (Exception e) {
            message.setMessageCode(1);
            message.setMessage(e.toString());
            return message;
        }
        return message;
    }

    /**
     * lamnt98
     * 01/07
     * search teacher list
     * @param viewTeacherList
     * @return ViewTeaListResponseDto
     */
    @Override
    public ViewTeaListResponseDto searchTeacher(ViewTeaListRequestDto viewTeacherList) {
        ViewTeaListResponseDto viewListResponse = new ViewTeaListResponseDto();
        MessageDTO message = new MessageDTO();
        Pageable paging;
        Integer orderBy = viewTeacherList.getOrderBy();
        Integer pageNumber = viewTeacherList.getPageNumber();
        String fullName = viewTeacherList.getFullName().trim();
        String orderByProperty = "fullName";

        Page<Teacher> pagedResult;
        Integer pageSize = Constant.PAGE_SIZE;

        //check orby asc or desc
        if(orderBy == 0){
            paging = PageRequest.of(pageNumber, pageSize, Sort.by(orderByProperty).descending());
        }
        else {
            paging = PageRequest.of(pageNumber, pageSize, Sort.by(orderByProperty).ascending());
        }

        //check fullName empty or not
        if(fullName.trim().isEmpty()){
            pagedResult = teacherRepository.selectAll(paging);
        }else{
            pagedResult = teacherRepository.searchTeacherBy(fullName,paging);
        }

        //check result when get list
        if(pagedResult==null || pagedResult.getTotalElements() == 0){
            message = Constant.TEACHERLIST_NULL;
            viewListResponse.setMessage(message);
            return viewListResponse;
        }

        message = Constant.SUCCESS;
        viewListResponse.setMessage(message);
        viewListResponse.setTeacherList(pagedResult);
        return viewListResponse;
    }

    /**
     * lamnt98
     * 01/07
     * view teacher information
     * @param viewTeaInforRequestDto
     * @return ViewTeaInforResponseDto
     */
    @Override
    public ViewTeaInforResponseDto viewTeacherInfor(ViewTeaInforRequestDto viewTeaInforRequestDto) {
        Teacher teacher = null;
        ViewTeaInforResponseDto viewTeaInforResponseDto = new ViewTeaInforResponseDto();
        MessageDTO messageDTO = new MessageDTO();
        try {
            Integer teacherId = viewTeaInforRequestDto.getTeacherId();

            //check teacherId empty or not
            if (teacherId == null) {
                messageDTO = Constant.TEACHER_ID_NULL;
                viewTeaInforResponseDto.setMessageDTO(messageDTO);
                return viewTeaInforResponseDto;
            }

            //check and find teacher in database
            teacher = teacherRepository.findById(teacherId).orElse(null);
            if (teacher != null) {
                viewTeaInforResponseDto.setTeacherId(teacher.getTeacherId());
                viewTeaInforResponseDto.setTeacherIdentifier(teacher.getTeacherIdentifier());
                viewTeaInforResponseDto.setEmail(teacher.getEmail());
                viewTeaInforResponseDto.setPhone(teacher.getPhone());
                viewTeaInforResponseDto.setFullName(teacher.getFullName());
                messageDTO = Constant.SUCCESS;
            } else {
                messageDTO = Constant.TEACHER_NOT_EXIT;
            }
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            viewTeaInforResponseDto.setMessageDTO(messageDTO);
            return  viewTeaInforResponseDto;
        }

        viewTeaInforResponseDto.setMessageDTO(messageDTO);
        return  viewTeaInforResponseDto;
    }
}
