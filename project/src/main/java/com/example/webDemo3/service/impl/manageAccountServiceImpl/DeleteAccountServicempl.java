package com.example.webDemo3.service.impl.manageAccountServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.request.manageAccountRequestDto.DeleteAccountRequestDto;
import com.example.webDemo3.entity.User;
import com.example.webDemo3.exception.MyException;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.service.manageAccountService.DeleteAccountService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * lamnt98 - 27/06
 */
@Service
public class DeleteAccountServicempl implements DeleteAccountService {
    @Autowired
    private UserRepository userRepository;

    /**
     * lamnt98
     * 27/06
     * find and delete account
     * @param deleteAccount
     * @return MessageDTO
     */
    @Override
    @Transactional
    public MessageDTO deleteAccount(DeleteAccountRequestDto deleteAccount) {
        MessageDTO message = new MessageDTO();
        try {
            message = delete(deleteAccount);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return message;
    }


    private MessageDTO delete(DeleteAccountRequestDto deleteAccount) throws Exception{
        MessageDTO messageDTO = new MessageDTO();
        try{
            List<String> listUser = deleteAccount.getListUser();
            for(String userName : listUser){
                User user = userRepository.findUserByUsername(userName);
                //check user exists or not
                if(user != null && (user.getStatus() == null || user.getStatus() != 1)){
                    user.setStatus(1);
                    userRepository.save(user);
                }else{
                    messageDTO = Constant.USER_NOT_EXIT;
                    throw new MyException(messageDTO.getMessage());
                }
            }
            messageDTO = Constant.DELETE_ACCOUNT_SUCCESS;
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            throw new MyException(messageDTO.getMessage());
        }
        return messageDTO;
    }
}
