package com.example.webDemo3.controller;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageAssignRedStarResponseDto.ListClassAndDateResponseDto;
import com.example.webDemo3.dto.manageAssignRedStarResponseDto.ViewAssignTaskResponseDto;
import com.example.webDemo3.dto.request.assignRedStarRequestDto.DownloadAssignRedStarRequestDto;
import com.example.webDemo3.dto.request.assignRedStarRequestDto.ViewAssignTaskRequestDto;
import com.example.webDemo3.dto.request.manageTimeTableRequestDto.CheckDateRequestDto;
import com.example.webDemo3.service.assignRedStarService.AssignRedStarService;
import com.example.webDemo3.service.assignRedStarService.CreateAssignRedStarService;
import com.example.webDemo3.service.assignRedStarService.DownloadAssignRedStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/assignRedStar")
public class AssignRedStarApiController {
    @Autowired
    private CreateAssignRedStarService assignRedStarService;

    @Autowired
    private DownloadAssignRedStarService downloadAssignRedStarService;

    @PostMapping("/delete")
    public MessageDTO deleteAssignRedStar(@RequestBody CheckDateRequestDto data)
    {
        return assignRedStarService.delete(data.getDate());
    }

    @PostMapping("/checkDate")
    public MessageDTO checkDate(@RequestBody CheckDateRequestDto data)
    {
        return assignRedStarService.checkDate(data.getDate());
    }

    @PostMapping("/create")
    public MessageDTO createAssignRedStar(@RequestBody CheckDateRequestDto data)
    {
        return assignRedStarService.create(data.getDate());
    }

    @Autowired
    private AssignRedStarService taskService;

    @PostMapping("/download")
    public ResponseEntity<InputStreamResource> downloadAssignRedStar(@RequestBody DownloadAssignRedStarRequestDto data)
    {
        ByteArrayInputStream in = downloadAssignRedStarService.download(data);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=phancong.xls");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));

    }

    /**
     * lamnt98
     * 14/07
     * catch request to get list star, class and date
     * @return ListClassAndDateResponseDto
     */
    @PostMapping("/liststarclassdate")
    public ListClassAndDateResponseDto getListStarClassDate()
    {
        return taskService.listStarClassDate();
    }

    /**
     * lamnt98
     * 14/07
     * catch request to get list assign task
     * @return ViewAssignTaskResponseDto
     */
    @PostMapping("/viewassigntask")
    public ViewAssignTaskResponseDto viewAssignTask(@RequestBody ViewAssignTaskRequestDto model)
    {
        return taskService.viewTask(model);
    }
}
