package com.example.webDemo3.service.impl.manageSchoolRankWeekImpl;

import com.example.webDemo3.service.manageSchoolRankWeek.AdditionFunctionSchoolRankService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class AdditionFunctionSchoolRankServiceImpl implements AdditionFunctionSchoolRankService {

    @Override
    public String addHistory(String oldhistory, String userName, Date date) {
        String history = "";
        String newDate = convertSqlDateToString(date);
        if(oldhistory == null || oldhistory.isEmpty()){
            history = "<ul> <li><span class=\"font-500\">" + "Tạo ngày: " + newDate +  " - " + "bởi: " + userName + ".</span> </li></ul>";
        }else{
            history = oldhistory +
                    "<ul> <li><span class=\"font-500\">" + "Sửa ngày: " + newDate +  " - " + "bởi: " + userName + ".</span> </li></ul>";
        }

        return history;
    }

    @Override
    public Date convertDateInComputerToSqlDate() {
        Calendar c = Calendar.getInstance();
        java.util.Date date = c.getTime();

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String string = simpleDateFormat.format(date);
        Date newdate = Date.valueOf(string);
        return newdate;
    }

    @Override
    public String convertSqlDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = formatter.format(date);

        return dateInString;
    }
}
