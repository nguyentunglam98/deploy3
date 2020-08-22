package com.example.webDemo3.controller;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.ListMonthSchoolRankResponseDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.*;
import com.example.webDemo3.service.manageSchoolRankSemesterService.CreateAndEditSchoolRankSemester;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.*;
import com.example.webDemo3.service.manageSchoolRankSemesterService.ViewSchoolRankSemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

/*
kimpt142 - 23/07
 */
@RestController
@RequestMapping("/api/ranksemester")
public class RankSemesterApiController {

    @Autowired
    private CreateAndEditSchoolRankSemester createAndEditSchoolRankSemester;

    @Autowired
    private ViewSchoolRankSemesterService viewSchoolRankSemesterService;
    /**
     * lamnt98
     * 23/07
     * catch request to get month list which has no ranked
     * @return ListMonthSchoolRankResponseDto
     */
    @PostMapping("/loadmonthlist")
    public ListMonthSchoolRankResponseDto getMonthList(@RequestBody ListMonthSchoolRankRequestDto module)
    {
        return createAndEditSchoolRankSemester.loadListMonth(module);
    }

    /**
     * lamnt98
     * 23/07
     * catch request to creat rank semester
     * @return MessageDTO
     */
    @PostMapping("/createranksemester")
    public MessageDTO createRankSemester(@RequestBody CreateRankSemesterRequestDto module)
    {
        return createAndEditSchoolRankSemester.createRankSemester(module);
    }

    /**
     * lamnt98
     * 23/07
     * catch request to get month list which has no ranked and month has ranked by semesterId
     * @return ListMonthSchoolRankResponseDto
     */
    @PostMapping("/loadeditranksemester")
    public ListMonthSchoolRankResponseDto getMonthList(@RequestBody ViewMonthOfEditRankSemesterRequestDto module) {
        return createAndEditSchoolRankSemester.loadEditListMonth(module);
    }

    /**
     * lamnt98
     * 23/07
     * catch request to edit rank semester
     * @return MessageDTO
     */
    @PostMapping("/editranksemester")
    public MessageDTO editRankSemester(@RequestBody EditRankSemesterRequestDto module)
    {
        return createAndEditSchoolRankSemester.editRankSemester(module);
    }

    /**
     * kimpt142
     * 24/07
     * catch request to get year list and semester list by year id
     * @return MessageDTO
     */
    @PostMapping("/loadranksemester")
    public LoadRankSemesterResponseDto loadRankSemester()
    {
        return viewSchoolRankSemesterService.loadRankSemesterPage();
    }

    /**
     * kimpt142
     * 24/07
     * catch request to get semester list by year id
     * @param model include year id
     * @return ViewWeekListResponseDto
     */
    @PostMapping("/getsemesterlist")
    public ViewSemesterListResponseDto getSemesterByYearId(@RequestBody LoadByYearIdRequestDto model)
    {
        return viewSchoolRankSemesterService.getSemesterListByYearId(model);
    }

    /**
     * kimpt142
     * 24/07
     * catch request to get rank semester list by semester id
     * @param model include semester id
     * @return RankSemesterListResponseDto
     */
    @PostMapping("/searchranksemester")
    public RankSemesterListResponseDto getSemesterByYearId(@RequestBody SearchRankSemesterRequestDto model)
    {
        return viewSchoolRankSemesterService.searchRankSemesterById(model);
    }

    /**
     * kimpt142
     * 24/07
     * catch request to download school rank semester list
     * @param model include yearid
     * @return ResponseEntity
     */
    @PostMapping("/download")
    public ResponseEntity<InputStreamResource> download(@RequestBody SearchRankSemesterRequestDto model) {
        ByteArrayInputStream in = viewSchoolRankSemesterService.downloadRankSemester(model);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Bangxephanghocky.xls");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    /**
     * lamnt98
     * 29/07
     * catch request to get history of schoolSemester
     * @return ViewSchoolWeekHistoryResponseDto
     */
    @PostMapping("/viewhistory")
    public ViewSchoolSemesterHistoryResponseDto viewSchoolSemesterHistory(@RequestBody ViewSchoolSemesterHistoryRequestDto module) {

        return createAndEditSchoolRankSemester.viewSchoolSemesterHistory(module);
    }
}
