package com.example.webDemo3.service.impl.manageClassServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.*;
import com.example.webDemo3.dto.manageClassResponseDto.*;
import com.example.webDemo3.dto.request.*;
import com.example.webDemo3.dto.request.manageClassRequestDto.*;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.GiftedClass;
import com.example.webDemo3.entity.Role;
import com.example.webDemo3.entity.User;
import com.example.webDemo3.repository.ClassRepository;
import com.example.webDemo3.repository.GiftedClassRepository;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.service.manageClassService.ClassService;
import com.example.webDemo3.service.GenerateAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GiftedClassRepository giftedClassRepository;

    @Autowired
    private GenerateAccountService generateAccountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * kimpt142
     * 28/6
     * get list include classid, class name from 2 table CLASSES and GIFTED_CLASSES
     * @return ClassListResponseDto
     */
    @Override
    public ClassListResponseDto getClassList() {
        ClassListResponseDto responseDto = new ClassListResponseDto();
        MessageDTO message = new MessageDTO();
        List<Class> classList = classRepository.findAllOrderByClassName();
        List<ClassResponseDto> classResList = new ArrayList<>();

        if(classList!=null)
        {
            for(Class item : classList)
            {
                ClassResponseDto classDto = new ClassResponseDto();
                classDto.setClassID(item.getClassId());
                Integer grade = item.getGrade();
                String giftedName = item.getGiftedClass().getName();
                if(grade != null && !giftedName.trim().equals("")){
                    classDto.setClassName(grade.toString() + " " + giftedName);
                }
                else{
                    message = Constant.CLASSNAME_NOT_EXIT;
                    responseDto.setMessage(message);
                    return  responseDto;
                }
                classResList.add(classDto);
            }
            responseDto.setClassList(classResList);
            message = Constant.SUCCESS;
            responseDto.setMessage(message);
            return responseDto;
        }
        message = Constant.CLASSLIST_NOT_EXIT;
        responseDto.setMessage(message);
        return responseDto;
    }

    @Autowired
    private GiftedClassRepository giftedRepository;

    /**
     * kimpt142
     * 29/6
     * get gifted class list in db
     * @return a gifted class list
     */
    @Override
    public GiftedClassResponseDto getGiftedClassList() {
        GiftedClassResponseDto responseDto = new GiftedClassResponseDto();
        MessageDTO message = new MessageDTO();
        List<GiftedClass> giftedClasses = giftedRepository.findAll();

        if(giftedClasses!=null)
        {
            responseDto.setGiftedClassList(giftedClasses);
            message = Constant.SUCCESS;
            responseDto.setMessage(message);
            return responseDto;
        }
        message = Constant.GIFTEDCLASSLIST_NOT_EXIT;
        responseDto.setMessage(message);
        return responseDto;
    }

    /**
     * kimpt142
     * 29/6
     * add class into db
     * @param model include grade, giftedclassId, mappingname
     * @return message if success or fail
     */
    @Override
    public AddClassResponseDto addNewClass(AddClassRequestDto model) {
        AddClassResponseDto responseDto = new AddClassResponseDto();
        List<User> userList = new ArrayList<>();
        Class addClass = new Class();
        MessageDTO message = new MessageDTO();
        String classIdentifier = model.getClassIdentifier();
        Integer grade = model.getGrade();
        Integer giftedClassId = model.getGiftedClassId();

        if(classIdentifier.trim().equals("")){
            message = Constant.CLASSIDENTIFIER_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }

        if(grade == null){
            message = Constant.GRADE_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }

        if(giftedClassId == null){
            message = Constant.GIFTEDCLASSID_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }

        List<Class> classByClassIdentifier = classRepository.findClassListByClassIdentifier(classIdentifier);
        if(classByClassIdentifier.size() != 0){
            for(int i = 0; i < classByClassIdentifier.size(); i++){
                if(classByClassIdentifier.get(i).getStatus() == null || classByClassIdentifier.get(i).getStatus() == 0 ){
                    message = Constant.CLASSIDENTIFIER_EXIST;
                    responseDto.setMessage(message);
                    return responseDto;
                }
            }
        }

        Class classByGradeAndGiftedId = classRepository.searchClassByGradeAndGifedId(grade, giftedClassId);
        if(classByGradeAndGiftedId != null){
            if(classByGradeAndGiftedId.getStatus() != null && classByGradeAndGiftedId.getStatus() == 1 ){
                message = Constant.CLASS_EXIST_BLOCK;
                responseDto.setClassId(classByGradeAndGiftedId.getClassId());
                responseDto.setMessage(message);
                return responseDto;
            }
            else{
                message = Constant.CLASS_EXIST;
                responseDto.setMessage(message);
                return responseDto;
            }
        }

        addClass.setClassIdentifier(classIdentifier);
        addClass.setGrade(grade);
        addClass.setGiftedClass(new GiftedClass(giftedClassId));
        addClass.setStatus(0);

        try {
            classRepository.save(addClass);
            Class saveClass = classRepository.findClassActiveByClassIdentifier(classIdentifier);
            Integer classId = saveClass.getClassId();
            GenerateNameRequestDto requestDto;
            String password = "123@123a";
            String passwordEncode = passwordEncoder.encode(password);
            if(model.getIsRedStar()){
                requestDto = new GenerateNameRequestDto(3, classId);
                for(int i=0;i<2;i++){
                    User userRedStar;
                    String userName = generateAccountService.generateAccountName(requestDto).getUserName();
                    userRedStar = userRepository.findUserByUsername(userName);
                    if(userRedStar == null){
                        userRedStar = new User();
                    }
                    userRedStar.setUsername(userName);
                    userRedStar.setClassSchool(saveClass);
                    userRedStar.setPassword(passwordEncode);
                    userRedStar.setRole(new Role(3));
                    userRedStar.setStatus(0);
                    userRepository.save(userRedStar);
                    userRedStar.setPassword(password);
                    userList.add(userRedStar);
                }
            }
            if(model.getIsMonitor()){
                requestDto = new GenerateNameRequestDto(4, classId);
                String userName = generateAccountService.generateAccountName(requestDto).getUserName();
                User userMonitor;
                userMonitor = userRepository.findUserByUsername(userName);
                if(userMonitor == null){
                    userMonitor = new User();
                }

                userMonitor.setUsername(userName);
                userMonitor.setClassSchool(saveClass);
                userMonitor.setPassword(passwordEncode);
                userMonitor.setRole(new Role(4));
                userMonitor.setStatus(0);
                userRepository.save(userMonitor);
                userMonitor.setPassword(password);
                userList.add(userMonitor);
            }
        }
        catch (DataIntegrityViolationException e){
            message = Constant.CLASS_EXIST;
            responseDto.setMessage(message);
            return responseDto;
        }
        catch (Exception e)
        {
            message.setMessageCode(1);
            message.setMessage(e.toString());
            responseDto.setMessage(message);
            return responseDto;
        }

        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        responseDto.setUserList(userList);
        return responseDto;
    }


    /**
     * kimpt142
     * 30/6
     * add new gifted class to db
     * @param model include name
     * @return MessageDTO
     */
    @Override
    public MessageDTO addGiftedClass(AddGiftedClassRequestDto model) {
        GiftedClass gClass = new GiftedClass();
        MessageDTO message = new MessageDTO();
        String giftedClassName = model.getGiftedClassName().trim();

        if(giftedClassName == null || giftedClassName.trim().isEmpty()){
            message = Constant.GIFTEDCLASSID_EMPTY;
            return message;
        }

        List<String> giftedNameList = giftedClassRepository.findAllGiftedNameLower();
        if(!giftedNameList.contains(giftedClassName.toLowerCase())){
            gClass.setName(giftedClassName);
            try {
                giftedClassRepository.save(gClass);
                message = Constant.SUCCESS;
                return message;
            }
            catch (Exception e){
                message.setMessageCode(1);
                message.setMessage(e.toString());
                return message;
            }
        }

        message = Constant.GIFTEDCLASSID_EXIST;
        return message;
    }


    /**
     * kimpt142
     * 30/6
     * edit the information of a class
     * @param model include classidentifier, status
     * @return message
     */
    @Override
    public MessageDTO editClass(EditClassRequestDto model) {
        Integer classId = model.getClassId();
        String classIdentifier = model.getClassIdentifier().trim();
        Integer status = model.getStatus();
        MessageDTO message = new MessageDTO();

        if(classIdentifier == null || classIdentifier.trim().isEmpty()){
            message = Constant.CLASSIDENTIFIER_EMPTY;
            return message;
        }

        Class editClass = classRepository.findByClassId(classId);
        if(editClass == null){
            message = Constant.CLASS_NOT_EXIST;
            return message;
        }

        List<Class> classListByNewIdetifier = classRepository.findClassListByClassIdentifier(classIdentifier);
        if(classListByNewIdetifier.size() == 1){
            Class checkClass = classListByNewIdetifier.get(0);
            if(!editClass.getClassIdentifier().equalsIgnoreCase(classIdentifier)){
                if(status == 0){
                    if(checkClass.getStatus() == null || checkClass.getStatus() == 0){
                        message = Constant.CLASSIDENTIFIER_EXIST;
                        return message;
                    }
                }
            }
        }
        else if(classListByNewIdetifier.size() > 1){
            if(status == 0){
                for(int i = 0; i<classListByNewIdetifier.size() ;i++){
                    if(classListByNewIdetifier.get(i).getStatus() == null || classListByNewIdetifier.get(i).getStatus() == 0){
                        message = Constant.CLASSIDENTIFIER_EXIST;
                        return message;
                    }
                }
            }
        }

        try {
            editClass.setClassIdentifier(classIdentifier);
            if (status != null) {
                editClass.setStatus(status);
                List<User> userList = userRepository.findAllByClassSchoolClassId(classId);
                if (userList != null) {
                    for (User item : userList) {
                        item.setStatus(status);
                        userRepository.save(item);
                    }
                }
            }
            classRepository.save(editClass);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            return message;
        }
        message = Constant.SUCCESS;
        return message;
    }

    /**
     * kimpt142
     * 1/7
     * get class information from classId
     * @param model include classId
     * @return class information
     */
    @Override
    public ClassInforResponseDto getClassInfor(ClassInforRequestDto model) {
        ClassInforResponseDto responseDto = new ClassInforResponseDto();
        MessageDTO message = new MessageDTO();

        Class classInfor = classRepository.findByClassId(model.getClassId());
        if(classInfor == null) {
            message = Constant.CLASS_NOT_EXIST;
            responseDto.setMessage(message);
            return responseDto;
        }

        responseDto.setClassIdentifier(classInfor.getClassIdentifier());
        responseDto.setStatus(classInfor.getStatus());
        responseDto.setGrade(classInfor.getGrade());

        String giftedClassName = classInfor.getGiftedClass().getName();
        if(giftedClassName != null){
            responseDto.setGiftedClassName(giftedClassName);
        }
        else{
            message = Constant.GIFTEDCLASSNAME_NOT_EXIST;
            responseDto.setMessage(message);
            return responseDto;
        }

        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        return responseDto;
    }


    /**
     * kimpt142
     * 1/7
     * get list class to show in detail table
     * @return a detail of class
     */
    @Override
    public ClassTableResponseDto getClassTable(ClassTableRequestDto requestModel) {
        ClassTableResponseDto responseDto = new ClassTableResponseDto();
        MessageDTO message = new MessageDTO();
        Pageable paging;
        Integer orderBy = requestModel.getOrderBy();
        Integer pageNumber = requestModel.getPageNumber();
        String classIdentifier = requestModel.getClassIdentifier();
        Integer grade = requestModel.getGrade();
        Integer status = requestModel.getStatus();
        Page<Class> pagedResult;
        String orderByProperty;
        Integer pageSize = Constant.PAGE_SIZE;
        switch (orderBy){
            case 0: {
                orderByProperty = "classIdentifier";
                break;
            }
            case 1: {
                orderByProperty = "grade";
                break;
            }
            default: orderByProperty = "classIdentifier";
        }

        if(requestModel.getSortBy() == 0){
            paging = PageRequest.of(pageNumber, pageSize, Sort.by(orderByProperty).descending());
        }
        else {
            paging = PageRequest.of(pageNumber, pageSize, Sort.by(orderByProperty).ascending());
        }

        if(grade!=null){
            if(status==null){
                pagedResult = classRepository.searchClassByCondition(classIdentifier,grade, paging);
            }
            else if(status == 0){
                pagedResult = classRepository.searchActiveClassByCondition(classIdentifier,grade, paging);
            }
            else {
                pagedResult = classRepository.searchInactiveClassByCondition(classIdentifier, grade, paging);
            }
        }
        else{
            if(status==null){
                pagedResult = classRepository.searchClassByClassIdentifier(classIdentifier, paging);
            }
            else if(status == 0){
                pagedResult = classRepository.searchActiveClassByClassIdentifier(classIdentifier, paging);
            }
            else {
                pagedResult = classRepository.searchInactiveClassByClassIdentifier(classIdentifier, paging);
            }
        }

        //check result when get list
        if(pagedResult.getTotalElements() == 0){
            message = Constant.CLASSLIST_NOT_EXIT;
            responseDto.setMessage(message);
            return responseDto;
        }

        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        responseDto.setClassList(pagedResult);
        return responseDto;
    }

    /**
     * kimpt142
     * 3/7
     * delete a gifted class by id
     * @param model include gifted class id
     * @return message
     */
    @Override
    public MessageDTO deleteGiftedClassById(DelGifedClassRequestDto model) {
        Integer giftedClassId = model.getGiftedClassId();
        MessageDTO message = new MessageDTO();

        if(giftedClassId == null){
            message = Constant.GIFTEDCLASSID_EMPTY;
            return message;
        }

        try {
            giftedClassRepository.deleteById(giftedClassId);
        }catch (Exception e){
            message = Constant.DEL_GIFTEDCLASS_FAIL;
            return message;
        }
        message = Constant.SUCCESS;
        return message;
    }
}
