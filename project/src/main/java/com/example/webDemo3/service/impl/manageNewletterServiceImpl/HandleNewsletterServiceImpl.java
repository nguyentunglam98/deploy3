package com.example.webDemo3.service.impl.manageNewletterServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageNewsletterResponseDto.AddNewsletterResponseDto;
import com.example.webDemo3.dto.manageNewsletterResponseDto.NewsletterListResponseDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.AddNewsletterRequestDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.ConfirmRequestNewsletterDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.EditNewsletterRequestDto;
import com.example.webDemo3.dto.request.manageNewsletterRequestDto.SearchNewsByStatusAndDateRequestDto;
import com.example.webDemo3.entity.Newsletter;
import com.example.webDemo3.exception.MyException;
import com.example.webDemo3.repository.NewsletterRepository;
import com.example.webDemo3.service.manageNewsletterService.HandleNewsletterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.Column;
import java.sql.Date;
import java.util.List;

/*
kimpt142 - 27/07
 */
@Service
public class HandleNewsletterServiceImpl implements HandleNewsletterService {

    @Autowired
    private NewsletterRepository newsletterRepository;

    /**
     * kimpt142
     * 27/7
     * add a newsletter into db
     * @param model
     * @return
     */
    @Override
    @Transactional
    public AddNewsletterResponseDto addNewsletter(AddNewsletterRequestDto model) {
        AddNewsletterResponseDto responseDto = new AddNewsletterResponseDto();
        MessageDTO message;

        String username = model.getUsername();
        String header = model.getHeader();
        String headerImage = model.getHeaderImage();
        String content = model.getContent();
        Integer roleId = model.getRoleId();
        Integer gim = model.getGim();
        Integer status = 2;

        message = checkRequestNewsletter(username, header, headerImage, content, roleId);
        if(message.getMessageCode() == 1){
            responseDto.setMessage(message);
            return responseDto;
        }

        Date currentDate = new Date(System.currentTimeMillis());

        if(roleId == Constant.ROLEID_ADMIN){
            status = 0;
        }

        if(gim == null){
            gim = 0;
        }

        Newsletter newsletter = new Newsletter();
        newsletter.setUserName(username);
        newsletter.setCreateDate(currentDate);
        newsletter.setHeader(header);
        newsletter.setHeaderImage(headerImage);
        newsletter.setContent(content);
        newsletter.setGim(gim);
        newsletter.setStatus(status);

        try{
            if(gim == 1){
                message = updateGimTransaction();
            }
            responseDto = addNewsletterWithTran(newsletter);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            responseDto.setMessage(message);
            return responseDto;
        }

        responseDto.setNewsletterId(newsletter.getNewsletterId());
        message = Constant.SUCCESS;
        responseDto.setMessage(message);
        return responseDto;
    }

    /**
     * kimpt142
     * 27/07
     * edit newsletter into db
     * @param model include newsletter entity and roleid
     * @return message
     */
    @Override
    public MessageDTO editNewsletter(EditNewsletterRequestDto model) {
        MessageDTO message;
        Newsletter newsletter = model.getNewsletter();
        Integer newsletterId = newsletter.getNewsletterId();
        String userName = newsletter.getUserName();
        String header = newsletter.getHeader();
        String headerImage = newsletter.getHeaderImage();
        String content = newsletter.getContent();
        Integer roleId = model.getRoleId();

        if(newsletterId == null){
            message = Constant.NEWSLETTERID_EMPTY;
            return message;
        }

        message = checkRequestNewsletter(userName, header, headerImage, content, roleId);
        if(message.getMessageCode() == 1){
            return message;
        }

        try{
            if(newsletter.getGim() == 1){
                message = updateGimTransaction();
            }
            message = addNewsletterWithTran(newsletter).getMessage();
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return message;
        }

        message = Constant.SUCCESS;
        return message;
    }

    /**
     * kimpt142
     * 27/07
     * change status of newsletter
     * @param model include newsletter id and status
     * @return message
     */
    @Override
    public MessageDTO confirmRequestNewsletter(ConfirmRequestNewsletterDto model) {
        MessageDTO message;

        Integer newsletterId = model.getNewsletterId();
        Integer status = model.getStatus();

        if(newsletterId == null){
            message = Constant.NEWSLETTERID_EMPTY;
            return message;
        }

        if(status == null){
            message = Constant.NEWSLETTER_STATUS_NULL;
            return message;
        }

        Newsletter newsletter = newsletterRepository.findById(newsletterId).orElse(null);
        if(newsletter == null){
            message = Constant.NEWSLETTER_NULL;
            return message;
        }else {
            newsletter.setStatus(status);
        }

        try{
            newsletterRepository.save(newsletter);
        }catch (Exception e){
            message = Constant.EDIT_NEWSLETTER_FAIL;
            return message;
        }

        message = Constant.SUCCESS;
        return message;
    }

    /**
     * kimpt142
     * 27/07
     * find newsletter list by status and create date
     * @param model
     * @return
     */
    @Override
    public NewsletterListResponseDto searchNewsletterByStatusAndDate(SearchNewsByStatusAndDateRequestDto model) {
        NewsletterListResponseDto responseDto = new NewsletterListResponseDto();

        Integer status = model.getStatus();
        Date createDate = model.getCreateDate();
        String userName = model.getUserName();
        MessageDTO message;

        Page<Newsletter> pagedResult = null;
        Pageable paging;
        Integer pageSize = Constant.PAGE_SIZE;
        Integer pageNumber = model.getPageNumber();

        //check pageNumber null or not
        if(pageNumber == null){
            pageNumber = 0;
        }

        if(userName == null){
            userName = "";
        }

        paging = PageRequest.of(pageNumber, pageSize);
        pagedResult = newsletterRepository.findByStatusAndCreateDate(status, createDate, userName, paging);

        //check result when get list
        if(pagedResult==null || pagedResult.getTotalElements() == 0){
            message = Constant.NEWSLETTERLIST_EMPTY;
            responseDto.setMessage(message);
            return responseDto;
        }

        message = Constant.SUCCESS;
        responseDto.setListLetter(pagedResult);
        responseDto.setMessage(message);

        return responseDto;
    }

    /**
     * kimpt142
     * 27/07
     * check condition is not empty for newsletter list
     * @param username
     * @param header
     * @param headerImage
     * @param content
     * @param roleId
     * @return
     */
    private MessageDTO checkRequestNewsletter(String username, String header,
                                              String headerImage, String content, Integer roleId){
        MessageDTO message;
        if(username == null || username.trim().isEmpty()){
            message = Constant.USER_NOT_EXIT;
            return message;
        }

        if(header == null || header.trim().isEmpty()){
            message = Constant.HEADER_EMPTY;
            return message;
        }

        if(headerImage == null || headerImage.trim().isEmpty()){
            message = Constant.HEADERIMAGE_EMPTY;
            return message;
        }

        if(content == null || content.trim().isEmpty()){
            message = Constant.CONTENT_EMPTY;
            return message;
        }

        if(roleId == null || (roleId != Constant.ROLEID_ADMIN &&
                roleId != Constant.ROLEID_MONITOR && roleId != Constant.ROLEID_CLUBLEADER)){
            message = Constant.NOROLE_NEWSLETTER;
            return message;
        }

        return Constant.SUCCESS;
    }

    /**
     * kimpt142
     * 30/07
     * add/edit a newsletter into db
     * @param newsletter
     * @return
     * @throws Exception
     */
    private AddNewsletterResponseDto addNewsletterWithTran(Newsletter newsletter) throws Exception{
        AddNewsletterResponseDto responseDto = new AddNewsletterResponseDto();
        MessageDTO message;
        try {
            newsletter = newsletterRepository.save(newsletter);
        }
        catch (Exception e){
            message = Constant.ADD_NEWSLETTER_FAIL;
            throw new MyException(message.getMessage());
        }
        message = Constant.SUCCESS;
        responseDto.setNewsletterId(newsletter.getNewsletterId());
        responseDto.setMessage(message);
        return responseDto;
    }

    /**
     * kimpt142
     * 30/07
     * update all newsletter with 1 is 0
     * @return
     * @throws Exception
     */
    private MessageDTO updateGimTransaction() throws Exception{
        MessageDTO message;
        List<Newsletter> newsletterList = newsletterRepository.findAllNewsletterGim();
        try {
            for(Newsletter item : newsletterList){
                item.setGim(0);
                newsletterRepository.save(item);
            }
        }
        catch (Exception e){
            message = Constant.UPDATE_GIM_FAIL;
            throw new MyException(message.getMessage());
        }
        message = Constant.SUCCESS;
        return message;
    }
}
