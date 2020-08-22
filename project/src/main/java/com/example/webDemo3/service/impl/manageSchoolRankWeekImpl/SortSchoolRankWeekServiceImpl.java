package com.example.webDemo3.service.impl.manageSchoolRankWeekImpl;

import com.example.webDemo3.entity.SchoolRankWeek;
import com.example.webDemo3.service.manageSchoolRankWeek.SortSchoolRankWeekService;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
kimpt142 - 21/07
 */
@Service
public class SortSchoolRankWeekServiceImpl implements SortSchoolRankWeekService {

    /**
     * kimpt142
     * 21/07
     * sort and set rank for the rank week list
     * @param schoolRankWeekList
     * @return
     */
    @Override
    public List<SchoolRankWeek> arrangeSchoolRankWeek(List<SchoolRankWeek> schoolRankWeekList) {
        Collections.sort(schoolRankWeekList, new Comparator<SchoolRankWeek>() {
            @Override
            public int compare(SchoolRankWeek o1, SchoolRankWeek o2) {
                return o2.getTotalGrade().compareTo(o1.getTotalGrade());
            }
        });
        int rank = 1;
        if(schoolRankWeekList.size() == 1){
            schoolRankWeekList.get(0).setRank(rank);
            return schoolRankWeekList;
        }
        else {
            int count = 0;
            for (int i = 0; i < schoolRankWeekList.size() - 1; i++) {
                SchoolRankWeek schoolRankWeek1 = schoolRankWeekList.get(i);
                schoolRankWeekList.get(i).setRank(rank);
                SchoolRankWeek schoolRankWeek2 = schoolRankWeekList.get(i + 1);
                if (schoolRankWeek2.getTotalGrade().compareTo(schoolRankWeek1.getTotalGrade()) == 0) {
                    schoolRankWeekList.get(i + 1).setRank(rank);
                    count++;
                } else {
                    rank += count + 1;
                    count = 0;
                    schoolRankWeekList.get(i + 1).setRank(rank);
                }
            }
        }
        return schoolRankWeekList;
    }
}
