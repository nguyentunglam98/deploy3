package com.example.webDemo3.service.impl.manageViolationServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.*;
import com.example.webDemo3.dto.manageViolationResponseDto.*;
import com.example.webDemo3.dto.request.manageViolationRequestDto.*;
import com.example.webDemo3.entity.Violation;
import com.example.webDemo3.entity.ViolationType;
import com.example.webDemo3.repository.ViolationRepository;
import com.example.webDemo3.repository.ViolationTypeRepository;
import com.example.webDemo3.service.manageViolationService.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * lamnt98
 * 06/07
 */
@Service
public class ViolationServiceImpl implements ViolationService {

    @Autowired
    private ViolationRepository violationRepository;

    @Autowired
    private ViolationTypeRepository violationTypeRepository;

    /**
     * lamnt98
     * 06/07
     * get all list violation and violation type
     * @param
     * @return ListVioAndTypeResponseDto
     */
    @Override
    public ListVioAndTypeResponseDto getListViolationAndType() {
        ListVioAndTypeResponseDto list = new ListVioAndTypeResponseDto();
        List<ViolationTypeResponseDto> listViolationType = new ArrayList<>();
        List<Violation> violationList = null;
        List<ViolationType> violationTypeList = null;
        MessageDTO messageDTO = new MessageDTO();

        try{
            violationTypeList = getListViolationType();

            //check violationTypeList empty or not
            if(violationTypeList.size() == 0){
                messageDTO = Constant.VIOLATION_TYPE_EMPTY;
                list.setMessageDTO(messageDTO);
                return  list;
            }

            //change from ViolationType to ViolationTypeResponseDto
            for(int i = 0; i < violationTypeList.size(); i++){
                ViolationTypeResponseDto violationTypeResponseDto = new ViolationTypeResponseDto();
                ViolationType violationType = violationTypeList.get(i);
                violationTypeResponseDto.setTypeId(violationType.getTypeId());
                violationTypeResponseDto.setName(violationType.getName());
                violationTypeResponseDto.setTotalGrade(violationType.getTotalGrade());
                violationTypeResponseDto.setStatus(violationType.getStatus());

                violationList = violationRepository.findViolationByTypeId(violationType.getTypeId());

                //check violationList empty or not
                if(violationList.size() != 0){
                    violationTypeResponseDto.setViolation(violationList);
                }
                listViolationType.add(violationTypeResponseDto);
            }

            //check listViolationType empty or not
            if(listViolationType.size() != 0){
                list.setListViolationType(listViolationType);
                list.setMessageDTO(Constant.SUCCESS);
            }
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            list.setMessageDTO(messageDTO);
            return  list;
        }
        return  list;
    }

    /**
     * lamnt98
     * 06/07
     * get violation by id
     * @param violationRequestDto
     * @return ViewViolationResponseDto
     */
    @Override
    public ViewViolationResponseDto getViolationById(ViewViolationRequestDto violationRequestDto) {
        ViewViolationResponseDto violationResponseDto = new ViewViolationResponseDto();
        List<ViolationType> violationTypeList = null;
        Violation violation = null;
        MessageDTO messageDTO = new MessageDTO();
        Integer violationId = null;
        try{
            violationTypeList = getListViolationType();

            //check violationTypeList empty or not
            if(violationTypeList.size() == 0){
                messageDTO = Constant.VIOLATION_TYPE_EMPTY;
                violationResponseDto.setMessageDTO(messageDTO);
                return  violationResponseDto;
            }
            violationResponseDto.setViolationTypeList(violationTypeList);

            violationId = violationRequestDto.getViolationId();

            if(violationId == null){
                messageDTO = Constant.VIOLATION_ID_NULL;
                violationResponseDto.setMessageDTO(messageDTO);
                return violationResponseDto;
            }
            violation = violationRepository.findViolationByViolationId(violationId);
            if(violation == null){
                messageDTO = Constant.VIOLATION_EMPTY;
                violationResponseDto.setMessageDTO(messageDTO);
                return violationResponseDto;
            }
            violationResponseDto.setCurrentViolation(violation);
            messageDTO = Constant.SUCCESS;
            violationResponseDto.setMessageDTO(messageDTO);
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            violationResponseDto.setMessageDTO(messageDTO);
            return  violationResponseDto;
        }
        return  violationResponseDto;
    }

    /**
     * lamnt98
     * 06/07
     * edit violation
     * @param editViolationRequestDto
     * @return MessageDTO
     */
    @Override
    public MessageDTO editViolation(EditViolationRequestDto editViolationRequestDto) {
        Violation violation = null;
        ViolationType violationType =null;
        Integer violationId = null;
        Integer typeId = null;
        String description = null;
        Float substractGrade = null;
        MessageDTO messageDTO = new MessageDTO();
        try{
            violationId = editViolationRequestDto.getViolationId();

            //check violationId empty or not
            if(violationId.toString().trim().isEmpty()){
                messageDTO = Constant.VIOLATION_ID_NULL;
                return  messageDTO;
            }
            violation = violationRepository.findViolationByViolationId(violationId);

            //check violation empty or not
            if(violation == null){
                messageDTO = Constant.VIOLATION_EMPTY;
                return  messageDTO;
            }
            typeId = editViolationRequestDto.getTypeId();

            //check typeId empty or not
            if(typeId == null){
                messageDTO = Constant.VIOLATION_TYPE_ID_NULL;
                return  messageDTO;
            }
            violationType = violationTypeRepository.searchViolationTypeByTypeId(typeId);

            //check violationType empty or not
            if(violationType == null){
                messageDTO = Constant.VIOLATION_TYPE_EMPTY;
                return  messageDTO;
            }

            //check description empty or not
            description = editViolationRequestDto.getDescription().trim();
            if(description.isEmpty()){
                messageDTO = Constant.VIOLATION_DESCIPTION_EMPTY;
                return  messageDTO;
            }

            //check substractGrade empty or not
            substractGrade = editViolationRequestDto.getSubstractGrade();
            if(substractGrade == null){
                messageDTO = Constant.VIOLATION_SUBSTRACT_GRADE_EMPTY;
                return  messageDTO;
            }
            violation.setTypeId(typeId);
            violation.setDescription(description);
            violation.setSubstractGrade(substractGrade);
            violationRepository.save(violation);
            messageDTO = Constant.EDIT_VIOLATION_SUCCESS;
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            return  messageDTO;
        }
        return  messageDTO;
    }

    /**
     * lamnt98
     * 06/07
     * add violation
     * @param violationRequestDto
     * @return MessageDTO
     */
    @Override
    public MessageDTO addViolation(AddViolationRequestDto violationRequestDto) {
        MessageDTO messageDTO = new MessageDTO();
        Violation violation = new Violation();
        ViolationType violationType;
        Integer typeId;
        String description = null;
        Float substractGrade = null;
        try{
            //check typeId empty or not
            typeId = violationRequestDto.getTypeId();
            if(typeId == null){
                messageDTO = Constant.VIOLATION_TYPE_ID_NULL;
                return  messageDTO;
            }

            violationType = violationTypeRepository.searchViolationTypeByTypeId(typeId);
            if(violationType == null){
                messageDTO = Constant.VIOLATION_TYPE_EMPTY;
                return  messageDTO;
            }

            //check description empty or not
            description = violationRequestDto.getDescription().trim();
            if(description.isEmpty()){
                messageDTO = Constant.VIOLATION_DESCIPTION_EMPTY;
                return  messageDTO;
            }

            //check substractGrade empty or not
            substractGrade = violationRequestDto.getSubstractGrade();
            if(substractGrade == null){
                messageDTO = Constant.VIOLATION_SUBSTRACT_GRADE_EMPTY;
                return  messageDTO;
            }
            violation.setTypeId(violationRequestDto.getTypeId());
            violation.setDescription(description);
            violation.setSubstractGrade(substractGrade);
            violationRepository.save(violation);
            messageDTO = Constant.ADD_VIOLATION_SUCCESS;
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            return  messageDTO;
        }
        return  messageDTO;
    }

    /**
     * lamnt98
     * 06/07
     * delete violation
     * @param violationRequestDto
     * @return MessageDTO
     */
    @Override
    public MessageDTO deleteViolation(DeleteViolationRequestDio violationRequestDto) {
        MessageDTO messageDTO = new MessageDTO();
        Violation violation = new Violation();
        Integer violationID;
        try{
            //check violationId empty or not
            violationID = violationRequestDto.getViolationId();
            if(violationID == null){
                messageDTO = Constant.VIOLATION_ID_NULL;
                return  messageDTO;
            }

            violation = violationRepository.findViolationByViolationId(violationID);
            if(violation == null){
                messageDTO = Constant.VIOLATION_EMPTY;
                return  messageDTO;
            }
            violation.setStatus(1);
            violationRepository.save(violation);
            messageDTO = Constant.DELETE_VIOLATION_SUCCESS;
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            return  messageDTO;
        }
        return  messageDTO;
    }

    /**
     * lamnt98
     * 06/07
     * get violation type
     * @param viewViolatoinTypeRequestDto
     * @return ViewViolationTypeResponseDto
     */
    @Override
    public ViewViolationTypeResponseDto getViolationTypeById(ViewViolatoinTypeRequestDto viewViolatoinTypeRequestDto) {
        ViewViolationTypeResponseDto violationTypeResponseDto = new ViewViolationTypeResponseDto();
        ViolationType violationType = null;
        MessageDTO messageDTO = new MessageDTO();
        Integer typeId = null;
        try{
            typeId = viewViolatoinTypeRequestDto.getTypeId();

            if(typeId == null){
                messageDTO = Constant.VIOLATION_TYPE_ID_NULL;
                violationTypeResponseDto.setMessageDTO(messageDTO);
                return violationTypeResponseDto;
            }

            violationType = violationTypeRepository.searchViolationTypeByTypeId(typeId);
            //check violationType empty or not
            if(violationType == null){
                messageDTO = Constant.VIOLATION_TYPE_EMPTY;
                violationTypeResponseDto.setMessageDTO(messageDTO);
                return  violationTypeResponseDto;
            }

            violationTypeResponseDto.setTypeId(typeId);
            violationTypeResponseDto.setName(violationType.getName());
            violationTypeResponseDto.setTotlaGrade(violationType.getTotalGrade());
            messageDTO = Constant.SUCCESS;
            violationTypeResponseDto.setMessageDTO(messageDTO);
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            violationTypeResponseDto.setMessageDTO(messageDTO);
            return  violationTypeResponseDto;
        }
        return  violationTypeResponseDto;
    }


    /**
     * lamnt98
     * 06/07
     * edit violation type
     * @param violationTypeRequestDto
     * @return MessageDTO
     */
    @Override
    public MessageDTO editViolationType(EditViolationTypeRequestDto violationTypeRequestDto) {
        ViolationType violationType =null;
        Integer typeId = null;
        String name = null;
        Float totalGrade = null;
        MessageDTO messageDTO = new MessageDTO();
        try{
            typeId = violationTypeRequestDto.getTypeId();

            //check typeId empty or not
            if(typeId.toString().trim().isEmpty()){
                messageDTO = Constant.VIOLATION_TYPE_ID_NULL;
                return  messageDTO;
            }
            violationType = violationTypeRepository.searchViolationTypeByTypeId(typeId);

            //check violation empty or not
            if(violationType == null){
                messageDTO = Constant.VIOLATION_EMPTY;
                return  messageDTO;
            }

            //check violation type name empty or not
            name = violationTypeRequestDto.getName();
            if(name.toString().trim().isEmpty()){
                messageDTO = Constant.VIOLATION_TYPE_NAME_EMPTY;
                return  messageDTO;
            }

            //check totalGrade empty or not
            totalGrade = violationTypeRequestDto.getTotlaGrade();
            if(totalGrade == null){
                messageDTO = Constant.TOTAL_GRAGE_EMPTY;
                return  messageDTO;
            }
            violationType.setName(name);
            violationType.setTotalGrade(violationTypeRequestDto.getTotlaGrade());
            violationTypeRepository.save(violationType);
            messageDTO = Constant.EDIT_VIOLATION_TYPE_SUCCESS;
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            return  messageDTO;
        }
        return  messageDTO;
    }

    /**
     * lamnt98
     * 06/07
     * add violation type
     * @param violationRequestDto
     * @return MessageDTO
     */
    @Override
    public MessageDTO addViolationType(AddViolationTypeRequestDto violationRequestDto) {
        MessageDTO messageDTO = new MessageDTO();
        ViolationType violationType = new ViolationType();
        Integer typeId;
        String name = null;
        Float totalGrade = null;
        try{

            //check name empty or not
            name = violationRequestDto.getName().trim();
            if(name.isEmpty()){
                messageDTO = Constant.VIOLATION_TYPE_NAME_EMPTY;
                return  messageDTO;
            }

            //check totalGrade empty or not
            totalGrade = violationRequestDto.getTotalGrade();
            if(totalGrade == null){
                messageDTO = Constant.TOTAL_GRAGE_EMPTY;
                return  messageDTO;
            }

            violationType.setName(name);
            violationType.setTotalGrade(totalGrade);
            violationTypeRepository.save(violationType);
            messageDTO = Constant.ADD_VIOLATION_TYPE_SUCCESS;
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            return  messageDTO;
        }
        return  messageDTO;
    }

    /**
     * lamnt98
     * 06/07
     * delete violation type
     * @param violationRequestDto
     * @return MessageDTO
     */
    @Override
    public MessageDTO deleteViolationType(DeleteViolationTypeRequestDto violationRequestDto) {
        MessageDTO messageDTO = new MessageDTO();
        List<Violation> violationList= new ArrayList<>();
        ViolationType violationType = null;
        Integer typeId;
        try{
            //check violationId empty or not
            typeId = violationRequestDto.getTypeId();
            if(typeId == null){
                messageDTO = Constant.VIOLATION_TYPE_ID_NULL;
                return  messageDTO;
            }

            violationType = violationTypeRepository.searchViolationTypeByTypeId(typeId);
            //check violationType exist or not
            if(violationType == null){
                messageDTO = Constant.VIOLATION_TYPE_EMPTY;
                return  messageDTO;
            }

            //check violationList empty or not
            violationList = violationRepository.findViolationByTypeId(typeId);
            if(violationList.size() != 0){
                messageDTO = Constant.VIOLATION_EXISTS;
                return  messageDTO;
            }

            violationType.setStatus(1);
            violationTypeRepository.save(violationType);
            messageDTO = Constant.DELETE_VIOLATION_TYPE_SUCCESS;
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            return  messageDTO;
        }
        return  messageDTO;
    }

    /**
     * lamnt98
     * 06/07
     * get all violation type
     * @param
     * @return ListViolationTypeResponseDto
     */
    @Override
    public ListViolationTypeResponseDto getAllViolationType() {
        ListViolationTypeResponseDto list = new ListViolationTypeResponseDto();
        List<ViolationType> violationTypeList = null;
        MessageDTO message = new MessageDTO();
        try{
            violationTypeList = getListViolationType();
            //check violationTypeList empty or not
            if(violationTypeList.size() == 0){
                message = Constant.VIOLATION_TYPE_EMPTY;
                list.setMessage(message);
                return  list;
            }
            list.setViolationTypeList(violationTypeList);
            message = Constant.SUCCESS;
            list.setMessage(message);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            list.setMessage(message);
            return  list;
        }
        return list;
    }

    /**
     * lamnt98
     * 06/07
     * function get all violation type
     * @param
     * @return List<ViolationType>
     */
    private List<ViolationType> getListViolationType() {
        List<ViolationType> violationTypeList = null;
        violationTypeList = violationTypeRepository.selectAllViolationTypeActive();
        return violationTypeList;
    }
}
