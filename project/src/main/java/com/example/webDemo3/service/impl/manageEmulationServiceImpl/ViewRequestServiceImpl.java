package com.example.webDemo3.service.impl.manageEmulationServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageEmulationResponseDto.ViewViolationClassListResponseDto;
import com.example.webDemo3.dto.manageEmulationResponseDto.ViolationClassRequestResponseDto;
import com.example.webDemo3.dto.manageEmulationResponseDto.ViolationClassResponseDto;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.ViewRequestDto;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.Violation;
import com.example.webDemo3.entity.ViolationClass;
import com.example.webDemo3.entity.ViolationClassRequest;
import com.example.webDemo3.repository.*;
import com.example.webDemo3.service.manageEmulationService.AdditionalFunctionViolationClassService;
import com.example.webDemo3.service.manageEmulationService.ValidateEmulationService;
import com.example.webDemo3.service.manageEmulationService.ViewRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * lamnt98
 * 14/07
 * Search request of change
 */
@Service
public class ViewRequestServiceImpl implements ViewRequestService {

    @Autowired
    private ValidateEmulationService validateEmulationService;

    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private ViolationRepository violationRepository;

    @Autowired
    private ViolationClassRepository violationClassRepository;

    @Autowired
    private ViolationClassRequestRepository violationClassRequestRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private AdditionalFunctionViolationClassService additionalFunctionViolationClassService;

    @Override
    public ViewViolationClassListResponseDto viewRequest(ViewRequestDto viewRequest) {
        ViewViolationClassListResponseDto responseDto = new ViewViolationClassListResponseDto();
        ViolationClassResponseDto violationClass = new ViolationClassResponseDto();
        List<ViolationClassResponseDto> viewViolationClassList = new ArrayList<>();
        ViolationClassRequestResponseDto violationClassRequest = new ViolationClassRequestResponseDto();

        MessageDTO message = new MessageDTO();
        Page<ViolationClass> pagedResult = null;
        Page<ViolationClassRequest> pagedResultRequest = null;
        Integer totalPage = 0;

        Integer classId = viewRequest.getClassId();
        Integer status = viewRequest.getStatus();
        Date createDate = viewRequest.getCreateDate();
        String createBy = viewRequest.getCreateBy();

        if(createBy == null){
            createBy = "";
        }

        Class newClass = null;
        Integer pageSize = Constant.PAGE_SIZE;
        Integer pageNumber = viewRequest.getPageNumber();

        Pageable paging;
        try {

            //check classId null or not
            if(classId != null){
                newClass = classRepository.findByClassId(classId);

                //check class exists or not
                if (newClass == null) {
                    message = Constant.CLASS_NOT_EXIST;
                    responseDto.setMessage(message);
                    return responseDto;
                }
            }

            paging = PageRequest.of(pageNumber, pageSize, Sort.by("dateChange").descending());

            pagedResultRequest = getListPageViolationClassRequestWithCondition(createDate, status, classId, createBy, paging);

            //check pagedResultRequest null or not
            if(pagedResultRequest != null){
                //Run to change from ViolationClassEntity và ViolationClassRequest to Dto
                for(ViolationClassRequest newViolatinClassRequest : pagedResultRequest){
                    violationClassRequest = additionalFunctionViolationClassService.convertViolationClassRequestFromEntityToDto(newViolatinClassRequest);

                    ViolationClass violationClass1 = violationClassRepository.findById(violationClassRequest.getViolationClassId()).orElse(null);
                    violationClass = additionalFunctionViolationClassService.convertViolationClassFromEntityToDto(violationClass1);
                    violationClass.setViolationClassRequest(violationClassRequest);
                    viewViolationClassList.add(violationClass);
                }
                totalPage = pagedResultRequest.getTotalPages();
                responseDto.setViewViolationClassList(viewViolationClassList);
            }

            responseDto.setTotalPage(totalPage);
            responseDto.setViewViolationClassList(viewViolationClassList);

            //check response empty or not
            if(responseDto.getViewViolationClassList().size() == 0){
                message = Constant.VIEW_CHANGE_REQUEST_NULL;
                responseDto.setMessage(message);
                return  responseDto;
            }

            message = Constant.SUCCESS;
            responseDto.setMessage(message);
        } catch (Exception e) {
            message.setMessageCode(1);
            message.setMessage(e.toString());
            responseDto.setMessage(message);
        }
        return responseDto;
    }

    public Page<ViolationClassRequest> getListPageViolationClassRequestWithCondition(Date createDate, Integer status, Integer classId, String createBy, Pageable paging) {
        Page<ViolationClassRequest> pagedResultRequest = null;
        try {
            switch (status) {
                case 0: {
                    pagedResultRequest = violationClassRequestRepository.findViolationClassRequestByConditionAndStatus(classId, createDate, createBy, 0, paging);
                    break;
                }
                case 1: {
                    pagedResultRequest = violationClassRequestRepository.findViolationClassRequestByConditionAndStatus(classId, createDate, createBy, 2, paging);
                    break;
                }
                case 2: {
                    pagedResultRequest = violationClassRequestRepository.findViolationClassRequestByConditionAndStatus(classId, createDate, createBy, 1, paging);
                    break;
                }
                default: {
                    pagedResultRequest = violationClassRequestRepository.findViolationClassRequestByCondition(classId,createDate,createBy, paging);
                    //pagedResultRequest = violationClassRequestRepository.findAllByCondition(paging);
                }
            }
            return pagedResultRequest;
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return pagedResultRequest;
    }
}

