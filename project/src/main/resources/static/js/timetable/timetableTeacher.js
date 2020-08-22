var infoSearch = {
    applyDate,
    teacherId
}
var applyDate, teacherId;

$(document).ready(function () {
    $("#teacher").select2();
});

/*Load years and list*/
$.ajax({
    url: '/api/timetable/getapplydateandteacher',
    type: 'POST',
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
            if (data.appyDateList == null) {
                $('#appyDateList').html(`<option>Không có ngày áp dụng nào.</option>`);
            } else {
                $('#appyDateList').html("");
                $.each(data.appyDateList, function (i, item) {
                    if (item == data.currentDate) {
                        $('#appyDateList').append(`<option value="` + item + `" selected>` + convertDate(item,'/') + `</option>`);
                    } else {
                        $('#appyDateList').append(`<option value="` + item + `">` + convertDate(item,'/') + `</option>`);
                    }
                });
                applyDate = $('#appyDateList option:selected').val();
            }
            if (data.teacherList == null) {
                $('#teacher').html(`<option value="">Không có giáo viên nào</option>`);
            } else {
                $('#teacher').html("")
                $.each(data.teacherList, function (i, item) {
                    $('#teacher').append(`<option value="` + item.teacherId + `">` + item.fullName + `</option>`);
                });
                teacherId = $('#teacher option:selected').val();
            }
            infoSearch = {
                applyDate: applyDate,
                teacherId: teacherId
            }
            loadTimetable();
        } else {
            $('.timetable').addClass('hide');
            $('.table-err').removeClass('hide');
            $('.table-err').html(
                ` <tr><td colspan="8" class="userlist-result">` + message + `</td> </tr> `
            )
        }
    },
    failure: function (errMsg) {
        $('.timetable').addClass('hide');
        $('.table-err').removeClass('hide');
        $('.table-err').html(
            ` <tr><td colspan="8" class="userlist-result">` + errMsg + `</td> </tr> `
        )
    },
    dataType: "json",
    contentType: "application/json"
});

loadTimetable();

/*Load timetable*/
function loadTimetable() {
    $.ajax({
        url: '/api/timetable/searchteachertimetable',
        type: 'POST',
        data: JSON.stringify(infoSearch),
        beforeSend: function () {
            $('body').addClass("loading")
        },
        complete: function () {
            $('body').removeClass("loading")
        },
        success: function (data) {
            $('.timetable').removeClass('hide');
            $('.table-err').addClass('hide');
            var messageCode = data.message.messageCode;
            var message = data.message.message;

            if (messageCode == 0) {
                $('.timetable').removeClass('hide');
                $('.table-err').addClass('hide');
                var morning = data.morningTimeTableList[0];
                var afternoon = data.afternoonTimeTableTableList[0];
                if (morning == null) {
                    $('.morning .data').html('');
                } else {
                    for (var i = 0; i < morning.length; i++) {
                        var slot = morning[i].slotId;
                        var dayId = morning[i].dayId;
                        var subject = morning[i].subject;
                        var className = morning[i].classIdentifier;
                        if (className == null) {
                            className = "";
                        }
                        if (slot == 1) {
                            $('tbody').find('tr').eq(slot - 1).find('td').eq(dayId + 1).html(
                                `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + className + `</div>`
                            )
                        } else {
                            $('tbody').find('tr').eq(slot - 1).find('td').eq(dayId).html(
                                `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + className + `</div>`
                            )
                        }

                    }
                }
                if (afternoon == null) {
                    $('.afternoon .data').html('');
                } else {
                    for (var i = 0; i < afternoon.length; i++) {
                        var slot = afternoon[i].slotId;
                        var dayId = afternoon[i].dayId;
                        var subject = afternoon[i].subject;
                        var className = afternoon[i].classIdentifier;
                        var isOddWeek = afternoon[i].isOddWeek;
                        if (className == null) {
                            className = "";
                        }
                        if (isOddWeek == 0 || isOddWeek == null) {
                            if (slot == 1) {
                                $('tbody').find('tr').eq(slot + 4).find('td').eq(dayId + 2).html(
                                    `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + className + `</div>`
                                )
                            } else {
                                $('tbody').find('tr').eq(slot + 4).find('td').eq(dayId).html(
                                    `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + className + `</div>`
                                )
                            }
                        } else {
                            if (slot == 1) {
                                $('tbody').find('tr').eq(slot + 6).find('td').eq(dayId + 1).html(
                                    `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + className + `</div>`
                                )
                            } else {
                                $('tbody').find('tr').eq(slot + 6).find('td').eq(dayId).html(
                                    `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + className + `</div>`
                                )
                            }
                        }
                    }
                }
            } else {
                $('.timetable').addClass('hide');
                $('.table-err').removeClass('hide');
                $('.table-err').html(
                    ` <tr><td colspan="8" class="userlist-result">` + message + `</td> </tr> `
                )
            }
        },
        failure: function (errMsg) {
            $('.timetable').addClass('hide');
            $('.table-err').removeClass('hide');
            $('.table-err').html(
                ` <tr><td colspan="8" class="userlist-result">` + errMsg + `</td> </tr> `
            )
        },
        dataType: "json",
        contentType: "application/json"
    });
}

/*Search timetable for Teacher*/
$('#search').click(function (e) {
    applyDate = $('#appyDateList option:selected').val();
    teacherId = $('#teacher option:selected').val();
    infoSearch = {
        applyDate: applyDate,
        teacherId: teacherId,
    }
    console.log(JSON.stringify(infoSearch));
    $('tbody .data').html('');
    loadTimetable();
})

