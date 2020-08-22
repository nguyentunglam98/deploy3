package com.example.webDemo3.service.manageSchoolRankWeek;

import com.example.webDemo3.dto.request.manageSchoolRankRequestDto.SearchRankWeekRequestDto;
import java.io.ByteArrayInputStream;

/*
kimpt142 - 21/07
 */
public interface DownloadRankWeekService {
    ByteArrayInputStream downloadRankWeek(SearchRankWeekRequestDto model);
}
