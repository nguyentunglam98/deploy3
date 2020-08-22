package com.example.webDemo3.service.impl.manageAccountServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.manageAccountRequestDto.LoginRequestDto;
import com.example.webDemo3.entity.ClassRedStar;
import com.example.webDemo3.entity.SchoolYear;
import com.example.webDemo3.entity.User;
import com.example.webDemo3.repository.ClassRedStarRepository;
import com.example.webDemo3.repository.SchoolYearRepository;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.dto.manageAccountResponseDto.LoginResponseDto;
import com.example.webDemo3.service.manageAccountService.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolYearRepository schoolYearRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClassRedStarRepository classRedStarRepository;

    /**
     * kimpt142
     * 23/6/2020
     * check username and password exist
     * @param u include username and password
     * @return logindto(1,success) if success and (0,fail) is fail
     */
    @Override
    public LoginResponseDto checkLoginUser(LoginRequestDto u) {
        LoginResponseDto loginDto = new LoginResponseDto();
        MessageDTO message = new MessageDTO();
        User user = null;

        /**
         * Find username in database
         */
        try {
            user = userRepository.findUserByUsername(u.getUsername());
        }
        catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            loginDto.setMessage(message);
            return loginDto;
        }

        /**
         * check username, status and password
         */
        if(user==null){
            message = Constant.USER_NOT_EXIT;
        }
        else if(user.getStatus() != null && user.getStatus() == 1){
            message = Constant.USER_INACTIVE;
        }
        else if(!passwordEncoder.matches(u.getPassword(),user.getPassword())){
            //if(!u.getPassword().equals(user.getPassword())){
            message = Constant.WRONG_PASSWORD;
        }
        else{
            message = Constant.SUCCESS;
            loginDto.setRoleid(user.getRole().getRoleId());
            Date dateCurrent = new Date(System.currentTimeMillis());
            SchoolYear schoolCurrent = schoolYearRepository.findSchoolYearsByDate(dateCurrent);
            if(schoolCurrent != null) {
                loginDto.setCurrentYearId(schoolCurrent.getYearID());
            }
            if(loginDto.getRoleid() == Constant.ROLEID_REDSTAR){
                Date fromDate = classRedStarRepository.getBiggestClosetDate(dateCurrent);
                if( fromDate != null){
                    ClassRedStar cs = classRedStarRepository.findByRedStar(u.getUsername(),fromDate);
                    if(cs != null){
                        loginDto.setAsignedClass(cs.getClassSchool().getGrade()+" "+cs.getClassSchool().getGiftedClass().getName());
                    }
                }
            }
        }

        loginDto.setMessage(message);
        return loginDto;
    }
}
