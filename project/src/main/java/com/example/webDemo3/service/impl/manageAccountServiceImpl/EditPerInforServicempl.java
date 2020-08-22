package com.example.webDemo3.service.impl.manageAccountServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.manageAccountRequestDto.EditPerInforRequestDto;
import com.example.webDemo3.entity.User;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.service.manageAccountService.EditPerInforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * lamnt98 - 27/06
 */
@Service
public class EditPerInforServicempl implements EditPerInforService {
    @Autowired
    private UserRepository userRepository;

    /**
     * lamnt98
     * 26/06
     * find and edit information of user
     * @param editPerInforRequestDto
     * @return MessageDTO
     */
    @Override
    public MessageDTO editUserInformation(EditPerInforRequestDto editPerInforRequestDto) {
        MessageDTO message = new MessageDTO();
        User user = null;

        try {
            //check userName empty or not
            if(editPerInforRequestDto.getUserName().trim().isEmpty()){
                message = Constant.USERNAME_EMPTY;
            }
            //check and find user in database
            else if(userRepository.findUserByUsername(editPerInforRequestDto.getUserName()) != null){
                user = userRepository.findUserByUsername(editPerInforRequestDto.getUserName());
                user.setName(editPerInforRequestDto.getFullName());
                user.setPhone(editPerInforRequestDto.getPhone());
                user.setEmail(editPerInforRequestDto.getEmail());
                userRepository.save(user);
                message = Constant.EDIT_INFOR_SUCCESS;
            }else{
                message = Constant.USER_NOT_EXIT;
            }
        } catch (Exception e) {
            message.setMessageCode(1);
            message.setMessage(e.toString());
            return message;
        }
        return message;
    }
}
