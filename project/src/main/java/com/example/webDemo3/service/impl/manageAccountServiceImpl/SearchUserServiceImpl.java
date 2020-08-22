package com.example.webDemo3.service.impl.manageAccountServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageAccountResponseDto.SearchUserResponseDto;
import com.example.webDemo3.dto.request.manageAccountRequestDto.SearchUserRequestDto;
import com.example.webDemo3.entity.User;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.service.manageAccountService.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SearchUserServiceImpl implements SearchUserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * kimpt142
     * 28/6
     * search username with multiple condition
     * @param requestModel
     * @return
     */
    @Override
    public SearchUserResponseDto searchUser(SearchUserRequestDto requestModel) {
        SearchUserResponseDto responseDto = new SearchUserResponseDto();
        MessageDTO message = new MessageDTO();
        Pageable paging;
        Integer orderBy = requestModel.getOrderBy();
        Integer pageNumber = requestModel.getPageNumber();
        String username = requestModel.getUserName();
        Integer roleId = requestModel.getRoleId();
        Page<User> pagedResult;
        String orderByProperty;
        Integer pageSize = Constant.PAGE_SIZE;
        switch (orderBy){
            case 0: {
                orderByProperty = "username";
                break;
            }
            case 1: {
                orderByProperty = "name";
                break;
            }
            default: orderByProperty = "username";
        }

        if(requestModel.getSortBy() == 0){
            paging = PageRequest.of(pageNumber, pageSize, Sort.by(orderByProperty).descending());
        }
        else {
            paging = PageRequest.of(pageNumber, pageSize, Sort.by(orderByProperty).ascending());
        }

        if(roleId!=null){
            pagedResult = userRepository.searchUserByCondition(username,roleId, paging);
        }
        else{
            pagedResult = userRepository.searchUserByUsername(username, paging);
        }

        //check result when get list
        if(pagedResult == null || pagedResult.getTotalElements() == 0){
            message = Constant.USERLIST_NULL;
            responseDto.setMessage(message);
            return responseDto;
        }

        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        responseDto.setUserList(pagedResult);
        return responseDto;
    }
}
