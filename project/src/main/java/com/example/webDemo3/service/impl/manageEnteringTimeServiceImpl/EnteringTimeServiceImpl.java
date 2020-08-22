package com.example.webDemo3.service.impl.manageEnteringTimeServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.manageEnteringViolationResponseDto.ListDayResponseDto;
import com.example.webDemo3.dto.manageEnteringViolationResponseDto.ListEnteringTimeResponseDto;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageEnteringViolationResponseDto.ViolationEnteringTimeResponseDto;
import com.example.webDemo3.dto.request.manageAccountRequestDto.DeleteAccountRequestDto;
import com.example.webDemo3.dto.request.manageEnteringViolationRequestDto.AddVioEnTimeRequestDto;
import com.example.webDemo3.dto.request.manageEnteringViolationRequestDto.DeleteEnteringTimeRequestDto;
import com.example.webDemo3.entity.Day;
import com.example.webDemo3.entity.Role;
import com.example.webDemo3.entity.ViolationEnteringTime;
import com.example.webDemo3.exception.MyException;
import com.example.webDemo3.repository.DayRepository;
import com.example.webDemo3.repository.RoleRepository;
import com.example.webDemo3.repository.ViolationEnteringTimeRepository;
import com.example.webDemo3.service.manageEnteringTimeService.EnteringTimeService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * lamnt98
 * 07/07
 */
@Service
public class EnteringTimeServiceImpl implements EnteringTimeService {
    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private ViolationEnteringTimeRepository enteringTimeRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * lamnt98
     * 07/07
     * get All Day
     * @param
     * @return ListDayResponseDto
     */
    @Override
    public ListDayResponseDto getAllDay() {
        ListDayResponseDto listDayResponseDto = new ListDayResponseDto();
        List<Day> listDay = null;
        MessageDTO messageDTO = new MessageDTO();
        try{
            listDay = dayRepository.findAll();
            if(listDay.size() == 0){
                messageDTO = Constant.LIST_DAY_EMPTY;
                listDayResponseDto.setMessageDTO(messageDTO);
                return  listDayResponseDto;
            }
            listDayResponseDto.setListDay(listDay);
            messageDTO = Constant.SUCCESS;
            listDayResponseDto.setMessageDTO(messageDTO);
        }catch (Exception e){
            messageDTO.setMessageCode(1);
            messageDTO.setMessage(e.toString());
            listDayResponseDto.setMessageDTO(messageDTO);
            return  listDayResponseDto;
        }
        return  listDayResponseDto;
    }

    /**
     * lamnt98
     * 07/07
     * delete violation entering time
     * @param deleteEnteringTime
     * @return MessageDTO
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
    public MessageDTO deleteEnteringTime(DeleteEnteringTimeRequestDto deleteEnteringTime) {
        MessageDTO message = new MessageDTO();
        List<Integer> listEnteringTime = null;
        try{
            listEnteringTime = deleteEnteringTime.getListEnteringTime();
            //check size list entering time
            if(listEnteringTime.size() == 0){
                message = Constant.DELETE_ENTERING_TIME_EMPTY;
                return  message;
            }
            for(Integer enteringTimeId : listEnteringTime){
                ViolationEnteringTime violationEnteringTime = enteringTimeRepository.findById(enteringTimeId).orElse(null);
                //check violation entering time exists or not
                if(violationEnteringTime != null){
                    enteringTimeRepository.delete(violationEnteringTime);
                }else{
                    message = Constant.ENTERING_TIME_EMPTY;
                    return  message;
                }
            }
            message = Constant.DELETE_VIOLATION_ENTERING_TIME_SUCCESS;
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            return message;
        }
        return message;
    }

    /**
     * lamnt98
     * 07/07
     * get list violation entering time
     * @param
     * @return ListEnteringTimeResponseDto
     */
    @Override
    public ListEnteringTimeResponseDto getListEnteringTime() {
        ListEnteringTimeResponseDto enteringTimeResponseDto = new ListEnteringTimeResponseDto();
        List<ViolationEnteringTimeResponseDto> listEnteringTime = new ArrayList<>();
        List<ViolationEnteringTime> violationEnteringTimes = new ArrayList<>();
        Integer violationEnteringTimeId;
        String roleName;
        String dayName;
        String startTime;
        String endTime;
        MessageDTO message = new MessageDTO();
        try{
            violationEnteringTimes = enteringTimeRepository.findAll();

            //check violationEnteringTimes empty or not
            if(violationEnteringTimes.size() == 0){
                message = Constant.VIOLATION_ENTERING_TIME_NULL;
                enteringTimeResponseDto.setMessage(message);
            }

            //change violation entering time from entity to Dto
            for(int i = 0; i < violationEnteringTimes.size(); i++){
                ViolationEnteringTime violationEnteringTime = new ViolationEnteringTime();
                ViolationEnteringTimeResponseDto violationEnteringTimeResponseDto = new ViolationEnteringTimeResponseDto();
                violationEnteringTime = violationEnteringTimes.get(i);
                violationEnteringTimeId = violationEnteringTime.getViolationEnteringTimeId();
                roleName = roleRepository.findByRoleId(violationEnteringTime.getRoleId()).getRoleName();
                dayName = dayRepository.findByDayId(violationEnteringTime.getDayId()).getDayName();
                startTime = changeTimeToStringFormat(violationEnteringTime.getStartTime());
                endTime = changeTimeToStringFormat(violationEnteringTime.getEndTime());

                violationEnteringTimeResponseDto.setViolationEnteringTimeId(violationEnteringTimeId);
                violationEnteringTimeResponseDto.setRoleName(roleName);
                violationEnteringTimeResponseDto.setDayName(dayName);
                violationEnteringTimeResponseDto.setStartTime(startTime);
                violationEnteringTimeResponseDto.setEndTime(endTime);
                listEnteringTime.add(violationEnteringTimeResponseDto);
            }

            //check listEnteringTime empty or not
            if(listEnteringTime.size() == 0){
                message = Constant.VIOLATION_ENTERING_TIME_NULL;
                enteringTimeResponseDto.setMessage(message);
            }

            enteringTimeResponseDto.setListEmteringTime(listEnteringTime);
            message = Constant.SUCCESS;
            enteringTimeResponseDto.setMessage(message);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            enteringTimeResponseDto.setMessage(message);
            return  enteringTimeResponseDto;
        }
        return  enteringTimeResponseDto;
    }
    
    /**
     * lamnt98
     * 07/07
     * add list violation entering time
     * @param addVioEnTimeRequestDto
     * @return MessageDTO
     */
    @Override
    @Transactional
    public MessageDTO addEnteringTime(AddVioEnTimeRequestDto addVioEnTimeRequestDto) {
        MessageDTO message = new MessageDTO();
        try {
            message = addEnteringTimeTransaction(addVioEnTimeRequestDto);
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return message;
    }

    private MessageDTO addEnteringTimeTransaction(AddVioEnTimeRequestDto addVioEnTimeRequestDto) throws Exception {
        MessageDTO message = new MessageDTO();
        Role role;
        Integer roleId;
        List<Integer> listDayId;
        Time startTime;
        Time endTime;
        boolean checkListDay = true;
        try{
            roleId = addVioEnTimeRequestDto.getRoleId();
            //check roleId null or not
            if(roleId == null) {
                message = Constant.ROLE_ID_NULL;
                throw new MyException(message.getMessage());
            }

            role = roleRepository.findByRoleId(roleId);
            //check role exist or not
            if(role == null){
                message = Constant.ROLE_NOT_EXIST;
                throw new MyException(message.getMessage());
            }

            listDayId = addVioEnTimeRequestDto.getListDayId();
            if(listDayId.size() == 0){
                message = Constant.LIST_DAY_EMPTY;
                throw new MyException(message.getMessage());
            }
            for(int i = 0; i < listDayId.size(); i++){
                Day day = dayRepository.findByDayId(listDayId.get(i));
                if(day == null){
                    checkListDay = false;
                    break;
                }
            }

            //check all day in list day exist or not
            if(!checkListDay){
                message = Constant.DAY_NOT_EXIST;
                throw new MyException(message.getMessage());
            }

            //check start time empty or not
            if(addVioEnTimeRequestDto.getStartTime().trim().isEmpty()){
                message = Constant.START_TIME_EMPTY;
                throw new MyException(message.getMessage());
            }
            startTime = changeStringToTimeFormat(addVioEnTimeRequestDto.getStartTime());

            //check end time empty or not
            if(addVioEnTimeRequestDto.getEndTime().trim().isEmpty()){
                message = Constant.END_TIME_EMPTY;
                throw new MyException(message.getMessage());
            }
            endTime = changeStringToTimeFormat(addVioEnTimeRequestDto.getEndTime());

            for(int i = 0; i < listDayId.size(); i++){
                List<ViolationEnteringTime> checkEnteringTime = new ArrayList<>();

                checkEnteringTime = enteringTimeRepository.findEnteringTimeByRoleIdAndDayId(roleId,listDayId.get(i));

                for(ViolationEnteringTime enteringTime : checkEnteringTime){
                    //check start time or end time has between time in database of role and day or not
                    if((startTime.after(enteringTime.getStartTime()) && startTime.before(enteringTime.getEndTime()))
                            || (endTime.after(enteringTime.getStartTime()) && endTime.before(enteringTime.getEndTime()))){
                        Day day = dayRepository.findByDayId(listDayId.get(i));
                        message.setMessageCode(1);
                        message.setMessage("Không thể thêm thời gian trực tuần cho " + role.getRoleName() + " do đã tồn tại thời gian trực tuần từ "
                                        + enteringTime.getStartTime() + " - " + enteringTime.getEndTime() + " của " + day.getDayName());
                        throw new MyException(message.getMessage());
                    }
                }
                ViolationEnteringTime enteringTime = new ViolationEnteringTime();
                enteringTime.setRoleId(roleId);
                enteringTime.setDayId(listDayId.get(i));
                enteringTime.setStartTime(startTime);
                enteringTime.setEndTime(endTime);
                enteringTimeRepository.save(enteringTime);
            }
            message = Constant.ADD_VIOLATION_ENTERING_TIME_SUCCESS;
        }catch (Exception e){
            message.setMessageCode(1);
            message.setMessage(e.toString());
            throw new MyException(message.getMessage());
        }
        return message;
    }

    /**
     * lamnt98
     * 07/07
     * change Time to String
     * @param time
     * @return String
     */
    public String changeTimeToStringFormat(Time time){
        String newTime = time.toString();
        String endTime = "";
        String[] split = newTime.split(":");
        //check array split empty or not
        if(split.length != 0){
            char[] cArray = split[0].toCharArray();
            //check format of HH
            if(cArray[0] == '0'){
                endTime = String.valueOf(cArray[1]) + "h";
            }else{
                endTime = split[0] + "h";
            }
            //check mm exist or not
            if(!split[1].equals("00")){
                endTime += split[1];
            }
        }
        return  endTime;
    }
    /**
     * lamnt98
     * 07/07
     * change String to Time
     * @param time
     * @return Time
     */
    public Time changeStringToTimeFormat(String time){
        return  java.sql.Time.valueOf(time);
    }

}
