package com.example.webDemo3.constant;

import com.example.webDemo3.dto.MessageDTO;

public class Constant {
    /**
     * Constant value
     */
    public static final Integer PAGE_SIZE = 10;
    public static final Integer ROLEID_ADMIN = 1;
    public static final Integer ROLEID_TIMETABLE_MANAGER = 2;
    public static final Integer ROLEID_REDSTAR = 3;
    public static final Integer ROLEID_MONITOR = 4;
    public static final Integer ROLEID_SUMMERIZEGROUP = 5;
    public static final Integer ROLEID_CLUBLEADER = 6;
    public static final Double LEARNING_GRADE = 20.0;
    public static final Double MOVEMENT_GRADE =  0.2;
    public static final Double LABOR_GRADE = 0.2;
    /**
     * Fail message
     */
    public static final MessageDTO USER_NOT_EXIT =
            new MessageDTO(1,"Tên đăng nhập không tồn tại.");
    public static final MessageDTO WRONG_PASSWORD =
            new MessageDTO(1,"Mật khẩu không đúng.");
    public static final MessageDTO USERNAME_EXIST =
            new MessageDTO(1,"Tên tài khoản đã tồn tại.");
    public static final MessageDTO USERNAME_EMPTY =
            new MessageDTO(1,"Hãy nhập tên đăng nhập.");
    public static final MessageDTO PASSWORD_EMPTY =
            new MessageDTO(1,"Hãy nhập mật khẩu.");
    public static final MessageDTO CLASSNAME_NOT_EXIT =
            new MessageDTO(1,"Tên lớp không tồn tại.");
    public static final MessageDTO CLASSLIST_NOT_EXIT =
            new MessageDTO(1,"Danh sách lớp trống.");
    public static final MessageDTO DELETE_FALSE =
            new MessageDTO(1,"Tài khoản xóa không thành công!");
    public static final MessageDTO USER_INACTIVE =
            new MessageDTO(1,"Tài khoản này đã bị khóa!");
    public static final MessageDTO GIFTEDCLASSLIST_NOT_EXIT =
            new MessageDTO(1,"Danh sách hệ chuyên trống.");
    public static final MessageDTO GRADE_EMPTY =
            new MessageDTO(1,"Hãy lựa chọn khối lớp.");
    public static final MessageDTO GIFTEDCLASSID_EMPTY =
            new MessageDTO(1,"Hãy lựa chọn hệ chuyên.");
    public static final MessageDTO CLASSIDENTIFIER_EMPTY =
            new MessageDTO(1,"Hãy nhập tên định danh cho lớp.");
    public static final MessageDTO CLASSIDENTIFIER_EXIST =
            new MessageDTO(1,"Tên định danh này đã tồn tại với một lớp đang hoạt động.");
    public static final MessageDTO CLASSNAME_EXIST =
            new MessageDTO(1,"Tên lớp này tồn tại.");
    public static final MessageDTO USERLIST_NULL =
            new MessageDTO(1,"Danh sách tài khoản trống.");
    public static final MessageDTO GIFTEDCLASSID_EXIST =
            new MessageDTO(1,"Tên hệ chuyên đã tồn tại.");

    public static final MessageDTO FULLNAME_EMPTY =
            new MessageDTO(1,"Hãy nhập tên giáo viên.");
    public static final MessageDTO TEACHER_IDENTIFIER_EMPTY =
            new MessageDTO(1,"Hãy nhập định danh.");
    public static final MessageDTO TEACHER_NOT_EXIT =
            new MessageDTO(1,"Giáo viên không tồn tại.");
    public static final MessageDTO TEACHER_EXIT =
            new MessageDTO(1,"Tên định danh của giáo viên đã tồn tại.");
    public static final MessageDTO TEACHER_ID_INVALID =
            new MessageDTO(1,"Thông tin không đúng định dạng.");
    public static final MessageDTO TEACHERLIST_NULL =
            new MessageDTO(1,"Danh sách giáo viên trống.");
    public static final MessageDTO CLASS_NOT_EXIST =
            new MessageDTO(1,"Lớp này không tồn tại.");
    public static final MessageDTO ROLE_EMPTY =
            new MessageDTO(1,"Hãy nhập chức vụ.");
    public static final MessageDTO CLASS_EMPTY =
            new MessageDTO(1,"Hãy nhập lớp.");
    public static final MessageDTO ROLE_NOT_EXIST =
            new MessageDTO(1,"Chức vụ không tồn tại.");
    public static final MessageDTO CLASS_EXIST =
            new MessageDTO(1,"Lớp này đã tồn tại.");
    public static final MessageDTO GIFTEDCLASSNAME_NOT_EXIST =
            new MessageDTO(1,"Tên hệ chuyên không tồn tại.");
    public static final MessageDTO YEAR_ID_NULL =
            new MessageDTO(1,"Không có năm hiện tại.");
    public static final MessageDTO LIST_WEEK_NULL =
            new MessageDTO(1,"Danh sách tuần đang trống.");
    public static final MessageDTO LIST_YEAR_NULL =
            new MessageDTO(1,"Không có danh sách năm.");
    public static final MessageDTO WEEK_ID_NULL =
            new MessageDTO(1,"Hãy chọn tuần.");
    public static final MessageDTO TEACHER_ID_NULL =
            new MessageDTO(1,"Hãy chọn giáo viên.");
    public static final MessageDTO CLASS_ID_NULL =
            new MessageDTO(1,"Hãy chọn lớp");
    public static final MessageDTO TIMETABLE_NULL =
            new MessageDTO(1,"Không có kết quả.");
    public static final MessageDTO CLASSIDENTIFIER_EXIST_BLOCK =
            new MessageDTO(2,"Tên định danh này đã tồn tại nhưng bị khóa.");
    public static final MessageDTO CLASS_EXIST_BLOCK =
            new MessageDTO(2,"Lớp này đã tồn tại nhưng bị khóa.");
    public static final MessageDTO DEL_GIFTEDCLASS_FAIL =
            new MessageDTO(1,"Không thể xóa hệ chuyên này.");
    public static final MessageDTO VIOLATION_TYPE_EMPTY =
            new MessageDTO(1,"Không có kết quả.");
    public static final MessageDTO VIOLATION_ID_NULL =
            new MessageDTO(1,"Hãy chọn lỗi.");
    public static final MessageDTO VIOLATION_EMPTY =
            new MessageDTO(1,"Không có lỗi như vậy.");
    public static final MessageDTO VIOLATION_TYPE_ID_NULL =
            new MessageDTO(1,"Hãy chọn nội quy.");

    public static final MessageDTO VIOLATION_TYPE_NAME_EMPTY =
            new MessageDTO(1,"Hãy điền nội quy.");

    public static final MessageDTO TOTAL_GRAGE_EMPTY =
            new MessageDTO(1,"Hãy nhập điểm.");

    public static final MessageDTO VIOLATION_DESCIPTION_EMPTY =
            new MessageDTO(1,"Hãy điền mô tả.");

    public static final MessageDTO VIOLATION_SUBSTRACT_GRADE_EMPTY =
            new MessageDTO(1,"Hãy nhập điểm trừ.");

    public static final MessageDTO VIOLATION_EXISTS =
            new MessageDTO(1,"Không thể xóa loại lỗi này! Vẫn còn các lỗi đang hoạt động.");
    public static final MessageDTO SCHOOLYEARLIST_EMPTY =
            new MessageDTO(1,"Danh sách năm học trống.");
    public static final MessageDTO SCHOOLYEARID_EMPTY =
            new MessageDTO(1,"Hãy lựa chọn năm học.");
    public static final MessageDTO DELETESCHOOLYEAR_FAIL =
            new MessageDTO(1,"Không thể xóa năm học này.");
    public static final MessageDTO FROMYEAR_EMPTY =
            new MessageDTO(1,"Hãy nhập năm bắt đầu.");
    public static final MessageDTO TOYEAR_EMPTY =
            new MessageDTO(1,"Hãy nhập năm kết thúc.");
    public static final MessageDTO FROMDATE_EMPTY =
            new MessageDTO(1,"Hãy nhập ngày bắt đầu.");
    public static final MessageDTO TODATE_EMPTY =
            new MessageDTO(1,"Hãy nhập ngày kết thúc.");
    public static final MessageDTO FROMDATE_GREATER_TODATE =
            new MessageDTO(1,"Hãy chọn ngày kết thúc xảy ra sau ngày bắt đầu.");
    public static final MessageDTO FROMDATE_EXIST =
            new MessageDTO(1,"Ngày bắt đầu đã nằm trong một năm học khác.");
    public static final MessageDTO TODATE_EXIST =
            new MessageDTO(1,"Ngày kết thúc đã nằm trong một năm học khác.");
    public static final MessageDTO FROMDATE_GREATER_CURRENTDATE =
            new MessageDTO(1,"Ngày bắt đầu của năm học hiện tại phải nhỏ hơn ngày hôm nay.");
    public static final MessageDTO LIST_DAY_EMPTY =
            new MessageDTO(1,"Không có danh sách ngày.");
    public static final MessageDTO VIOLATION_ENTERING_TIME_NULL =
            new MessageDTO(1,"Không có kết quả.");
    public static final MessageDTO DELETE_ENTERING_TIME_EMPTY =
            new MessageDTO(1,"Hãy chọn thời gian thực thi để xóa.");
    public static final MessageDTO ENTERING_TIME_EMPTY =
            new MessageDTO(1,"Thời gian thực thi này không tồn tại.");
    public static final MessageDTO ROLE_ID_NULL =
            new MessageDTO(1,"Hãy chọn chức vụ.");
    public static final MessageDTO DAY_NOT_EXIST =
            new MessageDTO(1,"Không có ngày thỏa mãn.");
    public static final MessageDTO START_TIME_EMPTY =
            new MessageDTO(1,"Hãy chọn thời gian bắt đầu.");
    public static final MessageDTO END_TIME_EMPTY =
            new MessageDTO(1,"Hãy chọn thời gian kết thúc.");
    public static final MessageDTO CLASS_RED_STAR_EMPTY =
            new MessageDTO(1,"Không có kết quả.");
    public static final MessageDTO SCHOOLYEAR_EMPTY =
            new MessageDTO(1,"Không tồn tại năm học này.");
    public static final MessageDTO LIST_DATE_EMPTY =
            new MessageDTO(1,"Danh sách ngày trống.");
    public static final MessageDTO LIST_CLASS_EMPTY =
            new MessageDTO(1,"Danh sách lớp trống.");
    public static final MessageDTO DATE_EMPTY =
            new MessageDTO(1,"Hãy lựa chọn ngày.");
    public static final MessageDTO LIST_REDSTAR_EMPTY =
            new MessageDTO(1,"Danh sách sao đỏ trống.");
    public static final MessageDTO VIEW_CHANGE_REQUEST_NULL =
            new MessageDTO(1,"Không có kết quả.");
    public static final MessageDTO EMULATE_FAIL =
            new MessageDTO(1,"Bạn không có quyền thêm/chỉnh sửa với lớp này.");
    public static final MessageDTO DATE_RANKED =
            new MessageDTO(1,"Điểm thi đua của lớp vào ngày này đã được xếp hạng.");
    public static final MessageDTO OVERDATE_EMULATE =
            new MessageDTO(1,"Bạn không thể chấm điểm ngày ở tương lai.");
    public static final MessageDTO TYPE_REQUEST_NULL =
            new MessageDTO(1,"Hãy chọn loại yêu cầu.");
    public static final MessageDTO VIOLATION_CLASS_ID_NULL =
            new MessageDTO(1,"Hãy chọn yêu cầu thay đổi.");
    public static final MessageDTO VIOLATION_CLASS_NULL =
            new MessageDTO(1,"Thông tin chấm điểm không tồn tại.");
    public static final MessageDTO VIOLATIONOFCLASS_EMPTY =
            new MessageDTO(1,"Danh sách lỗi của lớp đang trống.");
    public static final MessageDTO VIOLATIONCLASSID_EMPTY =
            new MessageDTO(1,"Hãy lựa chọn lỗi của lớp học.");
    public static final MessageDTO REASON_EMPTY =
            new MessageDTO(1,"Hãy nhập lý do của bạn.");
    public static final MessageDTO QUANTITY_EMPTY =
            new MessageDTO(1,"Hãy nhập số lượng mà bạn muốn.");
    public static final MessageDTO REQUEST_ID_NULL =
            new MessageDTO(1,"Hãy chọn yêu cầu thay đổi.");
    public static final MessageDTO NOT_ACCEPT_EDIT =
            new MessageDTO(1,"Bạn không có quyền chỉnh sửa.");
    public static final MessageDTO NOT_ACCEPT_DAY_EDIT =
            new MessageDTO(1,"Bạn không có quyền thêm/chỉnh sửa vào ngày được chọn.");
    public static final MessageDTO NOT_ACCEPT_TIME_EDIT =
            new MessageDTO(1,"Đã quá thời gian để thêm/chỉnh sửa lỗi.");
    public static final MessageDTO MONITOR_NOT_EDIT_TODAY =
            new MessageDTO(1,"Bạn chỉ có thể yêu cầu thay đổi trong ngày hôm nay.");
    public static final MessageDTO ADD_VIOLATION_NOT_CURRENTYEAR =
            new MessageDTO(1,"Bạn chỉ có thể thêm lỗi vi phạm của lớp trong năm học này.");
    public static final MessageDTO NOT_ACCEPT_REQUEST_CHANGE =
            new MessageDTO(1,"Bạn không có quyền chấp nhận yêu cầu thay đổi.");
    public static final MessageDTO NOT_REJECT_REQUEST_CHANGE =
            new MessageDTO(1,"Bạn không có quyền từ chối yêu cầu thay đổi.");
    public static final MessageDTO DATE_LIST_EMPTY =
            new MessageDTO(1,"Không có ngày chưa được xếp hạng.");
    public static final MessageDTO NOT_ACCEPT_CREATE_RANK_WEEK =
            new MessageDTO(1,"Bạn không có quyền tạo xếp hạng tuần.");
    public static final MessageDTO NOT_ACCEPT_CREATE_RANK_MONTH =
            new MessageDTO(1,"Bạn không có quyền tạo xếp hạng tháng.");
    public static final MessageDTO NOT_ACCEPT_CREATE_RANK_SEMESTER =
            new MessageDTO(1,"Bạn không có quyền tạo xếp hạng kỳ.");
    public static final MessageDTO NOT_ACCEPT_CREATE_RANK_YEAR =
            new MessageDTO(1,"Bạn không có quyền tạo xếp hạng năm.");
    public static final MessageDTO NOT_ACCEPT_EDIT_RANK_WEEK =
            new MessageDTO(1,"Bạn không có quyền sửa xếp hạng tuần.");
    public static final MessageDTO NOT_ACCEPT_EDIT_RANK_MONTH =
            new MessageDTO(1,"Bạn không có quyền sửa xếp hạng tháng.");
    public static final MessageDTO NOT_ACCEPT_EDIT_RANK_SEMESTER =
            new MessageDTO(1,"Bạn không có quyền sửa xếp hạng kỳ.");
    public static final MessageDTO NOT_ACCEPT_EDIT_RANK_YEAR =
            new MessageDTO(1,"Bạn không có quyền sửa xếp hạng năm.");
    public static final MessageDTO WEEK_NAME_EMPTY =
            new MessageDTO(1,"Hãy điền tên tuần.");
    public static final MessageDTO SCHOOL_WEEK_EXISTS =
            new MessageDTO(1,"Tên tuần trong năm hiện tại đã tồn tại.");
    public static final MessageDTO SCHOOL_RANK_WEEK_NULL =
            new MessageDTO(1,"Danh sách xếp hạng lớp theo tuần trống.");
    public static final MessageDTO RANKLIST_EMPTY =
            new MessageDTO(1,"Danh sách xếp hạng tuần trống.");
    public static final MessageDTO LEARNINGGRADE_GREATER =
            new MessageDTO(1,"Điểm học tập không thể nhập quá số điểm cho phép.");
    public static final MessageDTO MOVEMENTGRADE_GREATER =
            new MessageDTO(1,"Điểm phong trào không thể nhập quá số điểm cho phép.");
    public static final MessageDTO LABORGRADE_GREATER =
            new MessageDTO(1,"Điểm lao động không thể nhập quá số điểm cho phép.");
    public static final MessageDTO RANKWEEK_NOT_EDIT =
            new MessageDTO(1,"Bạn không thể sửa tuần đã được xếp hạng ở tháng.");
    public static final MessageDTO SCHOOL_WEEK_NOT_EXIST =
            new MessageDTO(1,"Tuần không tồn tại.");
    public static final MessageDTO VIOLATIONREQUEST_DELETED =
            new MessageDTO(1,"Yêu cầu thay đổi này đã được hủy.");
    public static final MessageDTO ACCEPT_REQUEST_DELETE =
            new MessageDTO(1,"Không thể hủy yêu cầu thay đổi đã được chấp nhận.");
    public static final MessageDTO REJECT_REQUEST_DELETE =
            new MessageDTO(1,"Không thể hủy yêu cầu thay đổi đã bị từ chối.");
    public static final MessageDTO DELETE_REQUEST_FAIL =
            new MessageDTO(1,"Không thể xóa yêu cầu thay đổi này.");
    public static final MessageDTO INCORRECT_FILE_FORMAT =
            new MessageDTO(1,"Không đúng định dạng file");
    public static final MessageDTO UPDATE_SCHOOL_RANK_FAIL =
            new MessageDTO(1,"Cập nhật điểm xếp hạng cho tuần xảy ra lỗi.");
    public static final MessageDTO RESET_PASSWORD_FAIL =
            new MessageDTO(1,"Đặt lại mật khẩu xảy ra lỗi.");
    public static final MessageDTO SCHOOL_WEEK_ID_NULL =
            new MessageDTO(1,"Tuần này chưa tồn tại.");
    public static final MessageDTO RANK_HAS_VIOLATION_CLASS_REQUEST_NOT_EXCEPT_EXIST =
            new MessageDTO(1,"Vẫn còn yêu cầu thay đổi chưa được xử lý, không thể tạo xếp hạng.");
    public static final MessageDTO  MONTHLIST_EMPTY=
            new MessageDTO(1,"Danh sách tháng đang trống.");
    public static final MessageDTO  MONTHID_EMPTY=
            new MessageDTO(1,"Hãy chọn tháng cần tìm kiếm.");
    public static final MessageDTO  RANKMONTHLIST_EMPTY=
            new MessageDTO(1,"Danh sách xếp hạng của tháng này trống.");
    public static final MessageDTO MONTH_NAME_EMPTY =
            new MessageDTO(1,"Hãy điền tên tháng.");
    public static final MessageDTO WEEK_LIST_EMPTY =
            new MessageDTO(1,"Không có tuần chưa được xếp hạng.");
    public static final MessageDTO SCHOOL_MONTH_EXISTS =
            new MessageDTO(1,"Tên tháng trong kỳ đã tồn tại.");
    public static final MessageDTO SCHOOL_MONTH_ID_NULL =
            new MessageDTO(1,"Hãy nhập tên tháng.");
    public static final MessageDTO SCHOOL_MONTH_NOT_EXISTS =
            new MessageDTO(1,"Tên tháng trong kỳ không tồn tại.");
    public static final MessageDTO LIST_MONTH_NULL =
            new MessageDTO(1,"Danh sách tháng đang trống.");
    public static final MessageDTO SEMESTER_NAME_EMPTY =
            new MessageDTO(1,"Hãy điền tên kỳ.");
    public static final MessageDTO MONTH_LIST_EMPTY =
            new MessageDTO(1,"Không có tháng chưa được xếp hạng.");
    public static final MessageDTO SCHOOL_SEMESTER_EXISTS =
            new MessageDTO(1,"Tên kỳ trong năm hiện tại đã tồn tại.");
    public static final MessageDTO SCHOOL_RANK_SEMESTER_NULL =
            new MessageDTO(1,"Danh sách xếp hạng lớp theo kỳ trống.");
    public static final MessageDTO SCHOOL_SEMESTER_ID_NULL =
            new MessageDTO(1,"Hãy nhập tên kỳ.");
    public static final MessageDTO SCHOOL_SEMESTER_NOT_EXISTS =
            new MessageDTO(1,"Tên kỳ trong năm không tồn tại.");
    public static final MessageDTO  SEMESTERLIST_EMPTY=
            new MessageDTO(1,"Danh sách học kỳ đang trống.");
    public static final MessageDTO  SEMESTERID_EMPTY=
            new MessageDTO(1,"Hãy chọn học kỳ cần tìm kiếm.");
    public static final MessageDTO  RANKYEARLIST_EMPTY=
            new MessageDTO(1,"Danh sách xếp hạng của năm học này trống.");
    public static final MessageDTO  RANKSEMESTERLIST_EMPTY=
            new MessageDTO(1,"Danh sách xếp hạng của kỳ này trống.");
    public static final MessageDTO LIST_SEMESTER_NULL =
            new MessageDTO(1,"Danh sách kỳ đang trống.");
    public static final MessageDTO SEMESTER_LIST_EMPTY =
            new MessageDTO(1,"Không có kỳ chưa được xếp hạng.");
    public static final MessageDTO SCHOOL_RANK_YEAR_EMPTY =
            new MessageDTO(1,"Không có năm chưa được xếp hạng.");
    public static final MessageDTO SCHOOL_RANK_YEAR_NULL =
            new MessageDTO(1,"Danh sách xếp hạng lớp theo năm trống.");
    public static final MessageDTO SCHOOL_YEAR_NOT_EXISTS =
            new MessageDTO(1,"Tên năm không tồn tại.");
    public static final MessageDTO HEADER_EMPTY =
            new MessageDTO(1,"Hãy nhập tiêu đề của bài viết.");
    public static final MessageDTO HEADERIMAGE_EMPTY =
            new MessageDTO(1,"Hãy nhập ảnh bìa của bài viết.");
    public static final MessageDTO CONTENT_EMPTY =
            new MessageDTO(1,"Hãy nhập nội dung của bài viết.");
    public static final MessageDTO NOROLE_NEWSLETTER =
            new MessageDTO(1,"Bạn không có quyền thêm/chỉnh sửa bài viết.");
    public static final MessageDTO NEWSLETTERID_EMPTY =
            new MessageDTO(1,"Hãy lựa chọn bài viết.");
    public static final MessageDTO ADD_NEWSLETTER_FAIL =
            new MessageDTO(1,"Tạo bài viết xảy ra lỗi.");
    public static final MessageDTO EDIT_NEWSLETTER_FAIL =
            new MessageDTO(1,"Chỉnh sửa bài viết xảy ra lỗi.");
    public static final MessageDTO NEWSLETTER_STATUS_NULL =
            new MessageDTO(1,"Hãy lựa chọn trạng thái.");
    public static final MessageDTO NEWSLETTER_NULL =
            new MessageDTO(1,"Không tìm thấy bài viết");
    public static final MessageDTO PAGE_NUMBER_NULL =
            new MessageDTO(1,"Hãy chọn trang.");
    public static final MessageDTO NEW_LETTER_ID_NULL =
            new MessageDTO(1,"Hãy chọn bài viết.");
    public static final MessageDTO NEW_LETTER_NOT_EXISTS =
            new MessageDTO(1,"Bài viết không tồn tại.");
    public static final MessageDTO NEWSLETTERLIST_EMPTY =
            new MessageDTO(1,"Danh sách bài viết trống.");
    public static final MessageDTO CREATE_DATE_NULL =
            new MessageDTO(1,"Hãy điền ngày tạo.");
    public static final MessageDTO HISTORY_IS_EMPTY =
            new MessageDTO(1,"Không có lịch sử.");
    public static final MessageDTO ADD_FROMDATE_SMALLER_CURRENT =
            new MessageDTO(1,"Bạn không thể tạo năm với ngày nhỏ hơn hiện tại.");
    public static final MessageDTO EDIT_FROMDATE_SMALLER_CURRENT =
            new MessageDTO(1,"Bạn không thể sửa năm với ngày nhỏ hơn hiện tại.");
    public static final MessageDTO EDIT_TODATE_SMALLER_CURRENT =
            new MessageDTO(1,"Bạn không thể sửa ngày của năm học nhỏ hơn ngày hôm nay.");
    public static final MessageDTO NO_EDIT_STARTDATE_SCHOOLYEAR =
            new MessageDTO(1,"Bạn không thể chỉnh sửa ngày bắt đầu.");
    public static final MessageDTO NO_EDIT_ENDDATE_SCHOOLYEAR =
            new MessageDTO(1,"Bạn không thể chỉnh sửa ngày kết thúc.");
    public static final MessageDTO RANKMONTH_NOT_EDIT =
            new MessageDTO(1,"Bạn không thể sửa tháng đã được xếp hạng ở học kỳ.");
    public static final MessageDTO RANKSEMESTER_NOT_EDIT =
            new MessageDTO(1,"Bạn không thể sửa học kỳ đã được xếp hạng ở năm học.");
    public static final MessageDTO ADD_HISTORY_WEEK_FAIL =
            new MessageDTO(1,"Thêm lịch sử cho chỉnh sửa xảy ra lỗi.");
    public static final MessageDTO UPDATE_GIM_FAIL =
            new MessageDTO(1,"Cập nhật bài đăng ghim xảy ra lỗi.");
    public static final MessageDTO NOT_ADD_ASSIGN_REDSTAR =
            new MessageDTO(1,"Không thể thêm phân công có ngày áp dụng nhỏ hơn hoặc bằng ngày hiện tại.");
    public static final MessageDTO NOT_DELETE_ASSIGN_REDSTAR =
            new MessageDTO(1,"Không thể xóa phân công có ngày áp dụng nhỏ hơn hoặc bằng ngày hiện tại.");
    /**
     * Success message
     */
    public static final MessageDTO SUCCESS =
            new MessageDTO(0,"Thành công.");

    //Success message edit information of user
    public static final MessageDTO EDIT_INFOR_SUCCESS =
            new MessageDTO(0,"Thông tin sửa thành công!");

    //Success message delete account
    public static final MessageDTO DELETE_ACCOUNT_SUCCESS =
            new MessageDTO(0,"Tài khoản đã bị xóa!");


    //Success message change account password
    public static final MessageDTO CHANGE_PASS_SUCCESS =
            new MessageDTO(0,"Mật khẩu đã được đặt lại!");

    public static final MessageDTO RESET_PASS_SUCCESS =
            new MessageDTO(0,"Mật khẩu đã được đặt lại.");

    //Success message edit information of teacher
    public static final MessageDTO EDIT_TEACHER_INFORMATION_SUCCESS =
            new MessageDTO(0,"Sửa thông tin Giáo viên thành công!");

    //Success message delete account
    public static final MessageDTO DELETE_TEACHER_SUCCESS =
            new MessageDTO(0,"Giáo viên đã bị xóa!");

    //Success add teacher
    public static final MessageDTO ADD_TEACHER_SUCCESS =
            new MessageDTO(0,"Thêm Giáo viên thành công!");

    //Success edit violation
    public static final MessageDTO EDIT_VIOLATION_SUCCESS =
            new MessageDTO(0,"Thông tin sửa thành công!");

    //Success add violation
    public static final MessageDTO ADD_VIOLATION_SUCCESS =
            new MessageDTO(0,"Thêm lỗi thành công!");

    //Success delete violation
    public static final MessageDTO DELETE_VIOLATION_SUCCESS =
            new MessageDTO(0,"Lỗi đã bị xóa!");

    //Success edit violation type
    public static final MessageDTO EDIT_VIOLATION_TYPE_SUCCESS =
            new MessageDTO(0,"Thông tin sửa thành công!");

    //Success add violation type
    public static final MessageDTO ADD_VIOLATION_TYPE_SUCCESS =
            new MessageDTO(0,"Thêm loại lỗi thành công!");

    //Success delete violation type
    public static final MessageDTO DELETE_VIOLATION_TYPE_SUCCESS =
            new MessageDTO(0,"Loại lỗi đã bị xóa!");

    //Success delete entering time
    public static final MessageDTO DELETE_VIOLATION_ENTERING_TIME_SUCCESS =
            new MessageDTO(0,"Xóa thành công!");

    //Success add violation type
    public static final MessageDTO ADD_VIOLATION_ENTERING_TIME_SUCCESS =
            new MessageDTO(0,"Thêm thời gian trực tuần thành công!");

    //dulicate date update timetable
    public static final MessageDTO CONFIRM_UPDATE_TIMTABLE=
            new MessageDTO(2,"ngày áp dụng này đã tồn tại bạn có muốn ghi đè không");

    //accept request emulation success
    public static final MessageDTO ACCEPT_REQUEST_SUCCESS =
            new MessageDTO(0,"Yêu cầu thay đổi được chấp nhận.");

    //reject request emulation success
    public static final MessageDTO REJECT_REQUEST_SUCCESS =
            new MessageDTO(0,"Từ chối thành công.");

}
