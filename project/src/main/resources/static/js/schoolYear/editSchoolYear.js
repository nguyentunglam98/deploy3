var schoolYearId, oldFromYear, oldFromDate, oldToDate;
var currentDate = moment().format('YYYY-MM-DD');
var currentYear = moment().format('YYYY');

$(document).ready(function () {
    schoolYearId = sessionStorage.getItem("schoolYearId")
    var info = {
        schoolYearId: schoolYearId
    }
    if (schoolYearId == null) {
        $('.editSchoolYear-err').append(`Hãy chọn năm học cần chỉnh sửa <a href="schoolYearList">TẠI ĐÂY</a>.`);
        $("#editInfo").prop('disabled', true);
    } else {
        $("#editInfo").prop('disabled', false);
        $.ajax({
            url: '/api/admin/getschoolyear',
            type: 'POST',
            data: JSON.stringify(info),
            beforeSend: function () {
                $('body').addClass("loading")
            },
            complete: function () {
                $('body').removeClass("loading")
            },
            success: function (data) {
                var messageCode = data.message.messageCode;
                var message = data.message.message;
                if (messageCode == 0) {
                    oldFromYear = data.fromYear;
                    oldToYear = data.toYear;
                    oldFromDate = data.fromDate;
                    oldToDate = data.toDate;
                    $('#fromYear').val(oldFromYear);
                    $('#toYear').val(oldToYear);
                    $('#fromDate').val(oldFromDate);
                    $("#toDate").val(oldToDate);
                    if (Date.parse(oldFromDate) <= Date.parse(currentDate)) {
                        $('#fromDate').prop('disabled', true);
                    }
                    if (Date.parse(oldToDate) <= Date.parse(currentDate)) {
                        $('#toDate').prop('disabled', true);
                    }
                    if (oldFromYear <= currentYear) {
                        $('#fromYear').prop('disabled', true);
                    }
                } else {
                    $('#editSchoolYear-err').text(message);
                }
            },
            failure: function (errMsg) {
                $('#editSchoolYear-err').text(errMsg);
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
});

$(document).change(function () {
    limitedDate();
});

$("#editInfo").click(function (e) {
    var fromYear = $('#fromYear').val().trim();
    var toYear = $('#toYear').val().trim();
    var fromDate = $('#fromDate').val().trim();
    var toDate = $('#toDate').val().trim();

    if (fromYear == "") {
        $('.editSchoolYear-err').text("Hãy điền năm học.");
        return false;
    } else if (fromDate == "") {
        $('.editSchoolYear-err').text("Hãy điền thời gian bắt đầu năm học.");
        return false;
    } else if (toDate == "") {
        $('.editSchoolYear-err').text("Hãy điền thời gian kết thúc năm học.");
        return false;
    } else if (fromYear == oldFromYear && fromDate == oldFromDate && toDate == oldToDate) {
        $('.editSchoolYear-err').text("Hãy thay đổi thông tin.");
        return false;
    } else {
        $('.editSchoolYear-err').text('')
        var newInfo = {
            schoolYearId: schoolYearId,
            fromDate: fromDate,
            toDate: toDate,
            fromYear: fromYear,
            toYear: toYear
        }
        e.preventDefault();
        $.ajax({
            url: '/api/admin/editschoolyear',
            type: 'POST',
            data: JSON.stringify(newInfo),
            beforeSend: function () {
                $('body').addClass("loading")
            },
            complete: function () {
                $('body').removeClass("loading")
            },
            success: function (data) {
                var messageCode = data.messageCode;
                var message = data.message;
                if (messageCode == 0) {
                    messageModal('editInfoSuccess', 'img/img-success.png', 'Thông tin sửa thành công!')
                } else {
                    $('.editSchoolYear-err').text(message);
                }
            },
            failure: function (errMsg) {
                messageModal('editInfoError', 'img/img-error.png', errMsg);
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
});