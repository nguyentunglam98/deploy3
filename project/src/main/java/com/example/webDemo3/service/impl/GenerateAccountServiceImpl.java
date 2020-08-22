package com.example.webDemo3.service.impl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.GenerateNameResponseDto;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.GenerateNameRequestDto;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.GiftedClass;
import com.example.webDemo3.entity.Role;
import com.example.webDemo3.repository.ClassRepository;
import com.example.webDemo3.repository.GiftedClassRepository;
import com.example.webDemo3.repository.RoleRepository;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.service.GenerateAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

/*
kimpt142 - 30/6
 */
@Service
public class GenerateAccountServiceImpl implements GenerateAccountService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GiftedClassRepository giftedClassRepository;
    /**
     * kimpt142
     * 30/6
     * generate name form roleid, classid
     * @param model
     * @return
     */
    @Override
    public GenerateNameResponseDto generateAccountName(GenerateNameRequestDto model) {
        GenerateNameResponseDto responseDto = new GenerateNameResponseDto();
        MessageDTO message = new MessageDTO();
        String userName = null;
        String convertClassName = null;
        String giftedClassName;
        Integer roleId = model.getRoleId();
        Integer classId = model.getClassId();

        if(roleId == null){
            message = Constant.ROLE_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }

        if(classId == null){
            message = Constant.CLASS_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }
        Role role = roleRepository.findByRoleId(roleId);
        Class genClass = classRepository.findByClassId(classId);

        if(role == null){
            message = Constant.ROLE_NOT_EXIST;
            responseDto.setMessage(message);
            return responseDto;
        }

        if(genClass == null){
            message = Constant.CLASS_NOT_EXIST;
            responseDto.setMessage(message);
            return responseDto;
        }

        GiftedClass giftedClass = giftedClassRepository.findById(genClass.getGiftedClass().getGiftedClassId()).orElse(null);
        if(giftedClass == null){
            message = Constant.GIFTEDCLASSNAME_NOT_EXIST;
            responseDto.setMessage(message);
            return responseDto;
        }

        String rollName = role.getRoleName();
        giftedClassName = giftedClass.getName();
        String className = genClass.getGrade().toString() + giftedClassName;

        if(rollName != null && !rollName.isEmpty()){
            userName = stripAccents(rollName);
        }

        Integer indexAccount = 1;
        List<String> userNameList = userRepository.findAllUsernameActive();

        if(!className.isEmpty()) {
            convertClassName = stripAccents(className);
        }

        while (userNameList.contains(userName + indexAccount + "_"+ convertClassName)){
            indexAccount+=1;
        }

        userName += indexAccount+ "_"+ convertClassName;

        responseDto.setUserName(userName);
        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        return responseDto;
    }

    private String stripAccents(String s)
    {
        s = s.toLowerCase();
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        s = s.replaceAll("\\s", "");
        s = s.replaceAll("đ", "d");
        return s;
    }
}
