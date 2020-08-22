var infoSearch = {
    applyDate,
    classId
}
var classId, applyDate;
$(document).ready(function () {
    $("#class").select2();
});

/*Load years and list*/
$.ajax({
    url: '/api/timetable/getapplydateandclass',
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
            applyDate = data.currentDate;
            if (data.appyDateList == null) {
                $('#appyDateList').html(`<option>Không có ngày áp dụng nào.</option>`);
            } else {
                $('#appyDateList').html("");
                $.each(data.appyDateList, function (i, item) {
                    if (item == data.currentDate) {
                        $('#appyDateList').append(`<option value="` + item + `" selected>` + convertDate(item, '/') + `</option>`);
                    } else {
                        $('#appyDateList').append(`<option value="` + item + `">` + convertDate(item, '/') + `</option>`);
                    }
                });
                applyDate = $('#appyDateList option:selected').val();
            }
            if (data.classList == null) {
                $('#class').html(`<option value="">Không có lớp nào.</option>`);
            } else {
                $('#class').html("")
                $.each(data.classList, function (i, item) {
                    $('#class').append(`<option value="` + item.classId + `">`
                        + item.grade + ` ` + item.giftedClass.name +
                        `</option>`);
                });
                classId = $('#class option:selected').val();
            }
            infoSearch = {
                applyDate: applyDate,
                classId: classId
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
        console.log(errMsg);
    },
    dataType: "json",
    contentType: "application/json"
});

/*Load timetable*/
function loadTimetable() {
    $.ajax({
        url: '/api/timetable/searchclasstimetable',
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
                var afternoon, morning;
                if (data.afternoonTimeTableTableList.length > 1 ||
                    data.morningTimeTableList.length > 1) {
                    $('#timetablePlus').removeClass('hide');
                    var count;
                    if (data.afternoonTimeTableTableList.length > data.morningTimeTableList.length) {
                        count = data.afternoonTimeTableTableList.length;
                    } else {
                        count = data.morningTimeTableList.length;
                    }
                    $('#timetablePlus').html(`<h4 class="mt-5">Thời khóa biểu bổ sung</h4>`);
                    for (var i = 1; i < count; i++) {
                        addTimetable(i);
                        var idAfternoon = "#timetable" + i + " .afternoon";
                        var idMorning = "#timetable" + i + " .morning";
                        if (data.afternoonTimeTableTableList[i] == null) {
                            $(idMorning).removeClass('hide');
                            $(idAfternoon).addClass('hide');
                        }
                        if (data.morningTimeTableList[i] == null) {
                            $(idMorning).addClass('hide');
                            $(idAfternoon).removeClass('hide');
                        }
                    }
                } else {
                    $('#timetablePlus').addClass('hide');
                }
                if (data.morningTimeTableList.length <= 1) {
                    morning = data.morningTimeTableList[0];
                    if (morning == null) {
                        $('.morning .data').html('');
                    } else {
                        morningTimetable('tbody', morning);
                    }
                } else {
                    for (var i = 0; i < data.morningTimeTableList.length; i++) {
                        var idTimetable = "#timetable" + i + " tbody";
                        morning = data.morningTimeTableList[i];
                        if (i == 0) {
                            morningTimetable('tbody', morning);
                        } else {
                            morningTimetable(idTimetable, morning);
                        }
                    }
                }
                if (data.afternoonTimeTableTableList.length <= 1) {
                    $('#timetablePlus').addClass('hide');
                    afternoon = data.afternoonTimeTableTableList[0];
                    if (afternoon == null) {
                        $('.afternoon .data').html('');
                    } else {
                        afternoonTimetable('tbody', afternoon);
                    }
                } else {
                    for (var i = 0; i < data.afternoonTimeTableTableList.length; i++) {
                        var idTimetable = "#timetable" + i + " tbody";
                        afternoon = data.afternoonTimeTableTableList[i];
                        if (i == 0) {
                            afternoonTimetable('tbody', afternoon);
                        } else {
                            afternoonTimetable(idTimetable, afternoon);
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

/*Search timetable for Class*/
$('#search').click(function (e) {
    applyDate = $('#appyDateList option:selected').val();
    classId = $('#class option:selected').val();
    infoSearch = {
        applyDate: applyDate,
        classId: classId,
    }
    console.log(JSON.stringify(infoSearch));
    $('tbody .data').html('');
    loadTimetable();
})

/*Add timetable morning*/
function morningTimetable(pos, morning) {
    for (var i = 0; i < morning.length; i++) {
        var slot = morning[i].slotId;
        var dayId = morning[i].dayId;
        var subject = morning[i].subject;
        var teacher = morning[i].teacherIdentifier;
        if (teacher == null) {
            teacher = "";
        }
        if (slot == 1) {
            $(pos).find('tr').eq(slot - 1).find('td').eq(dayId + 1).html(
                `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + teacher + `</div>`
            )
        } else {
            $(pos).find('tr').eq(slot - 1).find('td').eq(dayId).html(
                `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + teacher + `</div>`
            )
        }
    }
}

/*Add timetable afternoon*/
function afternoonTimetable(pos, afternoon) {
    for (var i = 0; i < afternoon.length; i++) {
        var slot = afternoon[i].slotId;
        var dayId = afternoon[i].dayId;
        var subject = afternoon[i].subject;
        var teacher = afternoon[i].teacherIdentifier;
        var isOddWeek = afternoon[i].isOddWeek;
        if (teacher == null) {
            teacher = "";
        }
        if (isOddWeek == 0 || isOddWeek == null) {
            if (slot == 1) {
                $(pos).find('tr').eq(slot + 4).find('td').eq(dayId + 2).html(
                    `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + teacher + `</div>`
                )
            } else {
                $(pos).find('tr').eq(slot + 4).find('td').eq(dayId).html(
                    `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + teacher + `</div>`
                )
            }
        } else {
            if (slot == 1) {
                $(pos).find('tr').eq(slot + 6).find('td').eq(dayId + 1).html(
                    `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + teacher + `</div>`
                )
            } else {
                $(pos).find('tr').eq(slot + 6).find('td').eq(dayId).html(
                    `<div class="subject">` + subject + `</div>
                            <div class="teacher">` + teacher + `</div>`
                )
            }
        }
    }
}

function addTimetable(id) {
    $('#timetablePlus').append(`
    <div class="scrollable-table">
        <table class="timetable table-bordered" id="timetable` + id + `">
            <thead>
                <tr class="timtable-title">
                    <th colspan="2" style="width: 15%">Buổi</th>
                    <th style="width: 10%">Tiết</th>
                    <th>Thứ 2</th>
                    <th>Thứ 3</th>
                    <th>Thứ 4</th>
                    <th>Thứ 5</th>
                    <th>Thứ 6</th>
                    <th>Thứ 7</th>
                </tr>
            </thead>
            <tbody>
                <tr class="morning">
                    <td rowspan="5" colspan="2" class="session"><p>SÁNG</p></td>
                    <td class="slot">1</td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                </tr>
                <tr class="morning">
                    <td class="slot">2</td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                </tr>
                <tr class="morning">
                    <td class="slot">3</td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                </tr>
                <tr class="morning">
                    <td class="slot">4</td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                </tr>
                <tr class="morning">
                    <td class="slot">5</td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                </tr>
                <tr class="afternoon">
                    <td rowspan="4" class="session"><p>CHIỀU</p></td>
                    <td rowspan="2" class="week"><p></p></td>
                    <td class="slot">1</td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                </tr>
                <tr class="afternoon">
                    <td class="slot">2</td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                </tr>
                <tr class="afternoon isOdd">
                    <td rowspan="2" class="week"><p></p></td>
                    <td class="slot">1</td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                </tr>
                <tr class="afternoon isOdd">
                    <td class="slot">2</td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                    <td class="data"></td>
                </tr>
            </tbody>
        </table>
    </div>`)
}