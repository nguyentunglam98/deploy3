package com.example.webDemo3.service.impl.manageAccountServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.entity.User;
import com.example.webDemo3.exception.MyException;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.service.manageAccountService.ResetPassService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * kimpt142 - 27/6
 */
@Service
public class ResetPassServiceImpl implements ResetPassService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * kimpt142
     * 27/6
     * update new password for user list
     * @param userNameList is username list need to update
     * @param password is new password
     * @return message success
     */
    @Override
    @Transactional
    public MessageDTO resetMultiplePassword(String[] userNameList, String password) {
        MessageDTO message = new MessageDTO();

        try {
            message = resetPassword(userNameList, password);
        }
        catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return message;
    }

    /**
     * kimpt142
     * 22/07
     * update password for user with transaction
     * @param userNameList
     * @param password
     * @return
     * @throws Exception
     */
    private MessageDTO resetPassword(String[] userNameList, String password) throws Exception{
        MessageDTO message = new MessageDTO();
        password = passwordEncoder.encode(password);
        try {
            for (String username : userNameList) {
                User user = userRepository.findUserByUsername(username);
                if(user != null) {
                    user.setPassword(password);
                    userRepository.save(user);
                }
                else
                {
                    message = Constant.USER_NOT_EXIT;
                    throw new MyException(message.getMessage());
                }
            }
        }
        catch (Exception e){
            message = Constant.RESET_PASSWORD_FAIL;
            throw new MyException(message.getMessage());
        }

        message = Constant.RESET_PASS_SUCCESS;
        return message;
    }
}
