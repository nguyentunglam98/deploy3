package com.example.webDemo3.service.impl.manageAccountServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageAccountResponseDto.ViewPerInforResponseDto;
import com.example.webDemo3.dto.request.manageAccountRequestDto.ViewPerInforRequestDto;
import com.example.webDemo3.entity.User;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.service.manageAccountService.ViewPerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewPerInforServiceImpl implements ViewPerInfoService {

    @Autowired
    private UserRepository userRepository;

    /**
     * lamnt98
     * 25/06
     * find information of user
     * @param viewPerInforRequestDto
     * @return viewPerInforResponseDto
     */
    @Override
    public ViewPerInforResponseDto getUserInformation(ViewPerInforRequestDto viewPerInforRequestDto) {
        ViewPerInforResponseDto viewPerInforResponseDto = new ViewPerInforResponseDto();
        MessageDTO message = new MessageDTO();
        User user = null;

        try{
            //check userName empty or not
            if(viewPerInforRequestDto.getUserName().trim().isEmpty()){
                message = Constant.USERNAME_EMPTY;
            }else{
                //Find username in database
                user = userRepository.findUserByUsername(viewPerInforRequestDto.getUserName());

                //check user
                if(user==null) {
                    message = Constant.USER_NOT_EXIT;
                }
                else {
                    viewPerInforResponseDto.setFullName(user.getName());
                    viewPerInforResponseDto.setUserName(user.getUsername());
                    viewPerInforResponseDto.setEmail(user.getEmail());
                    viewPerInforResponseDto.setPhone(user.getPhone());

                    //check class of user
                    if(user.getClassSchool() != null){
                        viewPerInforResponseDto.setClassName(user.getClassSchool().getGrade().toString().concat(user.getClassSchool().getGiftedClass().getName()));
                    }
                    viewPerInforResponseDto.setRoleName(user.getRole().getRoleName());
                    message = Constant.SUCCESS;
                }
            }
        }catch(Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            viewPerInforResponseDto.setMessage(message);
            return viewPerInforResponseDto;
        }

        viewPerInforResponseDto.setMessage(message);
        return  viewPerInforResponseDto;
    }
}
