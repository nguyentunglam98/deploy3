package com.example.webDemo3.controller;

import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageSchoolRankResponseDto.*;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.CreateRankWeekRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.ViewWeekAnDateListRequestDto;
import com.example.webDemo3.service.manageSchoolRankWeek.CreateAndEditSchoolRankWeekService;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.SearchRankWeekRequestDto;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.UpdateSchoolRankWeekRequestDto;
import com.example.webDemo3.service.manageSchoolRankWeek.DownloadRankWeekService;
import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.*;
import com.example.webDemo3.service.manageSchoolRankWeek.UpdateSchoolRankWeekService;
import com.example.webDemo3.service.manageSchoolRankWeek.ViewSchoolRankWeekService;
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
kimpt142 - 21/07
 */
@RestController
@RequestMapping("/api/rankweek")
public class RankWeekApiController {

    @Autowired
    private CreateAndEditSchoolRankWeekService createAndEditSchoolRankWeekService;

    @Autowired
    private ViewSchoolRankWeekService viewSchoolRankWeekService;

    @Autowired
    private UpdateSchoolRankWeekService updateSchoolRankWeekService;

    @Autowired
    private DownloadRankWeekService downloadRankWeekService;

    /**
     * lamnt98
     * 21/07
     * catch request to get date list which has no ranked
     * @return reponseDTO with a class list and messagedto
     */
    @PostMapping("/loaddatelist")
    public ListDateResponseDto getDateList()
    {
        return createAndEditSchoolRankWeekService.loadListDate();
    }

    /**
     * lamnt98
     * 21/07
     * catch request to get create rank week
     * @return reponseDTO
     */
    @PostMapping("/createrankweek")
    public MessageDTO createRankWeek(@RequestBody CreateRankWeekRequestDto module) {

        return createAndEditSchoolRankWeekService.createRankWeek(module);
    }

    /**
     * lamnt98
     * 21/07
     * catch request to get list edit date
     * @return reponseDTO
     */
    @PostMapping("/loadeditrankweek")
    public ListDateResponseDto getEditDateList(@RequestBody ViewWeekAnDateListRequestDto module) {

        return createAndEditSchoolRankWeekService.loadEditListDate(module);
    }

    /**
     * lamnt98
     * 21/07
     * catch request to get create rank week
     * @return reponseDTO
     */
    @PostMapping("/editrankweek")
    public MessageDTO editRankWeek(@RequestBody EditRankWeekRequestDto module) {

        return createAndEditSchoolRankWeekService.editRankWeek(module);
    }

    /**
     * kimpt142
     * 21/07
     * catch request to get the week list and class list
     * @param
     * @return ViewWeekAndClassListResponseDto
     */
    @PostMapping("/viewweekandclasslist")
    public ViewWeekAndClassListResponseDto getSchoolYearList()
    {
        return viewSchoolRankWeekService.loadRankWeekPage();
    }

    /**
     * kimpt142
     * 21/07
     * catch request to get school rank week list by week id and class id
     * @param model include week id and class id
     * @return RankWeekListResponseDto
     */
    @PostMapping("/searchrankweek")
    public RankWeekListResponseDto searchRankWeekByWeekAndClass(@RequestBody SearchRankWeekRequestDto model)
    {
        return viewSchoolRankWeekService.searchRankWeekByWeekAndClass(model);
    }

    /**
     * kimpt142
     * 21/07
     * catch request to update school rank week list
     * @param model include rank week list
     * @return MessageDTO
     */
    @PostMapping("/updatescorerankweek")
    public MessageDTO updateRankWeek(@RequestBody UpdateSchoolRankWeekRequestDto model)
    {
        return updateSchoolRankWeekService.updateSchoolRankWeek(model);
    }

    /**
     * kimpt142
     * 21/07
     * catch request to download rank week
     * @param model include weekid and classid
     * @return ResponseEntity
     */
    @PostMapping("/download")
    public ResponseEntity<InputStreamResource> download(@RequestBody SearchRankWeekRequestDto model)
    {
        ByteArrayInputStream in = downloadRankWeekService.downloadRankWeek(model);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=bangxephangtuan.xls");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));

    }

    /**
     * kimpt142
     * 21/07
     * catch request to get the week list by year id
     * @param model include year id
     * @return ViewWeekListResponseDto
     */
    @PostMapping("/getweeklist")
    public ViewWeekListResponseDto getWeekListByYearId(@RequestBody LoadByYearIdRequestDto model)
    {
        return viewSchoolRankWeekService.getWeekListByYearId(model);
    }

    /**
     * lamnt98
     * 21/07
     * catch request to get history of schoolWeek
     * @return ViewSchoolWeekHistoryResponseDto
     */
    @PostMapping("/viewhistory")
    public ViewSchoolWeekHistoryResponseDto viewSchoolWeekHistory(@RequestBody ViewSchoolWeekHistoryRequestDto module) {

        return createAndEditSchoolRankWeekService.viewSchoolWeekHistory(module);
    }
}
