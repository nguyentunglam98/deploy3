package com.example.webDemo3.service.impl.manageEmulationServiceImpl;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.manageEmulationResponseDto.ViewViolationClassListResponseDto;
import com.example.webDemo3.dto.manageEmulationResponseDto.ViolationClassResponseDto;
import com.example.webDemo3.dto.request.manageEmulationRequestDto.ViolationHistoryResquestDTO;
import com.example.webDemo3.entity.Class;
import com.example.webDemo3.entity.ViolationClass;
import com.example.webDemo3.repository.ClassRepository;
import com.example.webDemo3.repository.ViolationClassRepository;
import com.example.webDemo3.service.manageEmulationService.AdditionalFunctionViolationClassService;
import com.example.webDemo3.service.manageEmulationService.ViolationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViolationHistoryServiceImpl implements ViolationHistoryService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ViolationClassRepository violationClassRepository;

    @Autowired
    private AdditionalFunctionViolationClassService additionalFunctionService;

    @Override
    public ViewViolationClassListResponseDto getHistoryViolationOfClas(ViolationHistoryResquestDTO model) {
        ViewViolationClassListResponseDto output = new ViewViolationClassListResponseDto();
        List<Class> classList = classRepository.findByGifted(model.getGiftedId());
        Integer classId10 = -1;
        Integer classId11 = -1;
        Integer classId12 = -1;
        for (int i = 0; i < classList.size(); i++) {
            Class classSchool = classList.get(i);
            if (classSchool == null) break;
            
            if(classSchool.getGrade() == 10){
                classId10 = classSchool.getClassId();
            }
            else if(classSchool.getGrade() == 11){
                classId11 = classSchool.getClassId();
            }
            else {
                classId12 = classSchool.getClassId();
            }
        }

        Integer pageSize = Constant.PAGE_SIZE;
        Integer pageNumber = model.getPageNumber();

        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("date").descending());
        Page<ViolationClass> violationClassList = violationClassRepository.findAllHistoryOfClass(model.getFromDate(),model.getToDate()
                ,classId10,model.getFromYear(),classId11,model.getFromYear()+1,classId12, model.getFromYear()+2,paging);

        List<ViolationClassResponseDto> violationClassListDto = new ArrayList<>();
        if(violationClassList != null) {
            for (ViolationClass item : violationClassList) {
                ViolationClassResponseDto violationClassResponseDto
                    = additionalFunctionService.convertViolationClassFromEntityToDto(item);
                violationClassListDto.add(violationClassResponseDto);
            }
        }

        output.setTotalPage(violationClassList.getTotalPages());
        output.setViewViolationClassList(violationClassListDto);
        output.setMessage(Constant.SUCCESS);
        return output;
    }

}
