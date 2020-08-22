package com.example.webDemo3.service.impl.manageAccountServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.manageAccountRequestDto.AddAccResquestDTO;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.Role;
import com.example.webDemo3.entity.User;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.service.manageAccountService.AddAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddAccountServiceImpl implements AddAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * kimpt142
     * 27/6
     * add account to user tables
     * @param resquestDTO include username, password, roleid, classid, phone, email
     * @return message success or fail
     */
    @Override
    public MessageDTO addAccount(AddAccResquestDTO resquestDTO) {
        User newUser = new User();
        String userName = resquestDTO.getUserName();
        String passWord = resquestDTO.getPassWord();
        Integer roleId = resquestDTO.getRoleId();
        Integer classId = resquestDTO.getClassId();
        MessageDTO message = new MessageDTO();

        if(userName.trim().isEmpty()) {
            message = Constant.USERNAME_EMPTY;
            return message;
        }

        if(passWord.trim().isEmpty()) {
            message = Constant.PASSWORD_EMPTY;
            return message;
        }

        newUser = userRepository.findUserByUsername(userName);

        //check username existed in user table
        if(newUser != null && (newUser.getStatus() == null || newUser.getStatus() == 0)) {
            message = Constant.USERNAME_EXIST;
            return message;
        }
        else{
            passWord = passwordEncoder.encode(passWord);

            if(newUser == null){
                newUser = new User();
            }

            newUser.setUsername(userName);
            newUser.setPassword(passWord);
            newUser.setName(resquestDTO.getFullName());
            newUser.setPhone(resquestDTO.getPhone());
            newUser.setEmail(resquestDTO.getEmail());
            newUser.setStatus(0);

            if(roleId != null){
                newUser.setRole(new Role(roleId));
            }

            if(classId != null){
                newUser.setClassSchool(new Class(classId));
            }

            try {
                userRepository.save(newUser);
            }
            catch (Exception e)
            {
                message.setMessageCode(1);
                message.setMessage(e.toString());
                return message;
            }

            message = Constant.SUCCESS;
            return message;
        }
    }
}
