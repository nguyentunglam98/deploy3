package com.example.webDemo3.service.impl.manageAccountServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.manageAccountRequestDto.ChangePasswordRequestDto;
import com.example.webDemo3.entity.User;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.service.manageAccountService.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordmpl implements ChangePasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * lamnt98
     * 23/06
     * check password and update new password
     * @param user
     * @return MessageDTO
     */
    @Override
    public MessageDTO checkChangePasswordUser(ChangePasswordRequestDto user) {
        MessageDTO message = new MessageDTO();
        User newUser = null;

        /**
         * Validate userName, oldPassword and newPassword
         */
        try {
            if(user.getUserName().trim().isEmpty()){
                message = Constant.USERNAME_EMPTY;
            }else if(user.getOldPassword().trim().isEmpty()  || user.getNewPassword().trim().isEmpty()){
                message = Constant.PASSWORD_EMPTY;
            }else{
                /**
                 * find user in database
                 */
                if(userRepository.findUserByUsername(user.getUserName()) != null){
                    newUser = userRepository.findUserByUsername(user.getUserName());
                    /**
                     * check oldpassword and update newpassword
                     */
                    if(!passwordEncoder.matches(user.getOldPassword(),newUser.getPassword())){
                    //if(!newUser.getPassword().equals(user.getOldPassword())){
                        message = Constant.WRONG_PASSWORD;
                    }
                    else{
                        newUser.setPassword(passwordEncoder.encode(user.getNewPassword()));
                        userRepository.save(newUser);
                        message = Constant.CHANGE_PASS_SUCCESS;
                    }
                }else {
                    message = Constant.USER_NOT_EXIT;
                }
            }
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            return message;
        }

        return message;
    }
}
