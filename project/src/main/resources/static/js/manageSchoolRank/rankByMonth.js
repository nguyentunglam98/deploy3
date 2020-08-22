var listCreate = [];
var currentYearId
/*=============Set data================*/
/*Load year list*/
$.ajax({
    url: '/api/rankmonth/loadrankmonth',
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
        currentYearId = data.currentYearId;
        if (messageCode == 0 || messageCode == 1) {
            if (data.schoolYearList != null) {
                $('#byYear').html('');
                $.each(data.schoolYearList, function (i, item) {
                    if (currentYearId == item.schoolYearId) {
                        $('#byYear').append(`<option value="` + item.schoolYearId + `" selected="selected">` + item.yearName + `</option>`);
                    } else {
                        $('#byYear').append(`<option value="` + item.schoolYearId + `">` + item.yearName + `</option>`);
                    }
                })
                var yearId = $('#byYear option:selected').val();
                loadComboboxYear(yearId);
            } else {
                $('#byYear').html(`<option value="err">Danh sách năm học trống.</option>`);
                $('#byMonth').html(`<option value="err">Danh sách tháng đang trống.</option>`);
                $('#byMonth').prop('disabled', true);
            }
            if (data.classList != null) {
                $('#byClass').html(`<option value="" selected="selected">Tất cả</option>`);
                $("#byClass").select2();
                $.each(data.classList, function (i, item) {
                    $('#byClass').append(`<option value="` + item.classID + `">` + item.className + `</option>`);
                })
            } else {
                $('#byClass').html(`<option value="err">Danh sách lớp trống.</option>`);
            }
        } else {
            if (data.schoolYearList == null) {
                $('#byYear').html(`<option value="err">` + message + `</option>`);
            }
            if (data.schoolMonthList == null) {
                $('#byMonth').html(`<option value="err">` + message + `</option>`);
            }
            if (data.classList == null) {
                $('#byClass').html(`<option value="err">` + message + `</option>`);
            }
        }
    },
    failure: function (errMsg) {
        console.log(errMsg);
    },
    dataType: "json",
    contentType: "application/json"
});

/*Get week list when change year combobox*/
function loadComboboxYear(yearId) {
    $.ajax({
        url: '/api/rankmonth/getmonthlist',
        type: 'POST',
        data: JSON.stringify({yearId: yearId}),
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
                if (data.schoolMonthList != null) {
                    $('#byMonth').html('');
                    $.each(data.schoolMonthList, function (i, item) {
                        if (i == 0) {
                            $('#byMonth').append(`<option value="` + item.monthId + `" name="` + item.yearId + `" selected="selected">Tháng ` + item.month + `</option>`);
                        } else {
                            $('#byMonth').append(`<option value="` + item.monthId + `" name="` + item.yearId + `">Tháng ` + item.month + `</option>`);
                        }
                    });
                    if (sessionStorage.getItem('monthName') != null) {
                        var monthName = 'Tháng ' + sessionStorage.getItem('monthName');
                        $("#byMonth option").filter(function () {
                            return $(this).text() == monthName;
                        }).prop("selected", true);
                    }
                } else {
                    $('#byMonth').html(`<option value="err">Danh sách tuần đang trống.</option>`);
                }
            } else {
                $('#byMonth').html(`<option value="err">` + message + `</option>`);
            }
        },
        failure: function (errMsg) {
            $('#byMonth').html(`<option value="err">` + errMsg + `</option>`);
        },
        dataType: "json",
        contentType: "application/json"
    });
}

$('#byYear').change(function () {
    var yearId = $('#byYear option:selected').val();
    loadComboboxYear(yearId);
})

setTimeout(search, 1000);

/*Set data to table*/
function search() {
    var monthId = $('#byMonth option:selected').val();

    if (monthId != null && monthId != "" && monthId != "err") {
        $('#viewHistory').removeClass('hide');
    }
    if (roleID == 1) {
        $('#createRankBtn').removeClass('hide');
    }
    if ($('#byMonth option:selected').val() == 'err') {
        $('tbody').append(`<tr><td colspan="4" class="text-center">Danh sách xếp hạng của tháng trống.</td></tr>`);
        $('#editRankBtn').addClass('hide');
    } else {
        var infoSearch = {
            monthId: monthId,
            classId: $('#byClass option:selected').val(),
        }
        console.log(JSON.stringify(infoSearch));
        $('table').dataTable({
            destroy: true,
            searching: false,
            bInfo: false,
            paging: false,
            order: [],
            ajax: {
                url: "/api/rankmonth/searchrankmonth",
                type: "POST",
                data: function (d) {
                    return JSON.stringify(infoSearch);
                },
                dataType: "json",
                contentType: "application/json",
                failure: function (errMsg) {
                    $('tbody').append(
                        `<tr>
                        <td colspan="4" class="text-center"> ` + errMsg + ` </td>
                    </tr>`
                    )
                },
                dataSrc: function (data) {
                    var dataSrc = null;
                    var messageCode = data.message.messageCode;
                    var message = data.message.message;
                    var checkEdit = data.checkEdit;
                    if (messageCode == 0) {
                        if (roleID == 1) {
                            if (checkEdit != null && checkEdit == 0) {
                                $('#editRankBtn').removeClass('hide');
                            } else {
                                $('#editRankBtn').addClass('hide');
                            }
                        }
                        if (data.rankMonthList != null) {
                            dataSrc = data.rankMonthList;
                            $('#download').removeClass('hide');
                        } else {
                            return false;
                        }
                    } else {
                        $('tbody').append(
                            `<tr>
                            <td colspan="4" class="text-center"> ` + message + ` </td>
                        </tr>`
                        )
                        return false;
                    }
                    return dataSrc;
                }
            },
            columns: [
                {data: "className"},
                {data: "totalRankWeek"},
                {data: "totalGradeWeek"},
                {data: "rank"},
            ],
            columnDefs: [
                {
                    targets: 0,
                    createdCell: function (td, cellData, rowData, row, col) {
                        $(td).attr('data-column', rowData.classId);
                        $(td).css('min-width', '150px');
                    }
                },
                {
                    targets: 1,
                    createdCell: function (td, cellData, rowData, row, col) {
                        $(td).addClass('text-right');
                    }
                },
                {
                    targets: 2,
                    createdCell: function (td, cellData, rowData, row, col) {
                        $(td).addClass('text-right');
                    }
                },
                {
                    targets: 3,
                    createdCell: function (td, cellData, rowData, row, col) {
                        $(td).addClass('font-500 text-right');
                    }
                },
            ],
            drawCallback: function (settings) {
                settings.oLanguage.sEmptyTable = "Danh sách xếp hạng của tháng trống."
            }
        })
    }
    createRankBtn();
    editRankBtn();
    download();
    viewHistory();
}

/*Search button*/
$('#search').click(function (e) {
    $('table tbody').html('');
    $('#searchGroupButton').html(`                
    <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12 flex-wrap px-0">
        <input type="button" id="download" class="btn btn-success mr-2 my-1 hide" value="Tải xuống"/>
        <input type="button" id="viewHistory" class="btn btn-success mr-2 my-1 hide" value="Xem lịch sử"/>
    </div>
    <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12 flex-wrap px-0 text-right">
        <input type="button" id="editRankBtn" class="btn btn-success manageBtn ml-2 my-1 hide" value="Sửa xếp hạng tháng"/>
        <input type="button" id="createRankBtn" class="btn btn-success manageBtn ml-2 my-1 hide" value="Tạo xếp hạng tháng"/>
    </div>`);
    search();
});

/*==========Create rank===========*/

/*Load week list*/
function createRankBtn() {
    $('#createRankBtn').on('click', function () {
        $('#createNewRank').modal('show');
        listCreate = [];
        var currentYear = {
            currentYearId: currentYearId,
        }
        console.log(JSON.stringify(currentYear))
        $.ajax({
            url: '/api/rankmonth/loadweeklist',
            type: 'POST',
            data: JSON.stringify(currentYear),
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
                    console.log(data.weekList.length)
                    if (data.weekList.length != 0) {
                        $('#weekList').html('');
                        $('#weekList').append(`<h6>Các tuần áp dụng <span class="text-red">*</span></h6>`);
                        $.each(data.weekList, function (i, item) {
                            $('#weekList').append(`
                        <div class="form-check text-left my-1">
                            <span class="custom-checkbox">
                                <input type="checkbox" name="options" value="` + item.weekId + `">
                                <label for="` + item.weekId + `"></label>
                            </span>
                            <span class="ml-3"> Tuần ` + item.week + `</span>
                            <span class="hide weekName">` + item.week + `</span>
                            <span class="hide monthId">` + item.monthId + `</span>
                            <span class="hide isCheck">` + item.isCheck + `</span>
                        </div>
                    `);
                        });
                    } else {
                        $('#weekList').html(`<h6 class="text-red">Không có tuần áp dụng nào!</h6>`);
                    }
                } else {
                    $('#weekList').html(`<h6 class="text-red">` + message + `</h6>`);
                }
            },
            failure: function (errMsg) {
                $('#weekList').html(`<h6 class="text-red">` + errMsg + `</h6>`);
            },
            dataType: "json",
            contentType: "application/json"
        });
    })
}

/*Create rank month*/
$('#createNewRankBtn').on('click', function () {
    $('input[name=options]:checked').each(function (i) {
        listCreate.push({
            weekId: $(this).val(),
            week: $(this).parent().parent().find('.weekName').text(),
            monthId: $(this).parent().parent().find('.monthId').text(),
            isCheck: 1
        });
    });
    var monthName = $('#monthName').val().trim();
    if (monthName == "" || monthName == null) {
        $('.createNewRank-err').text('Hãy nhập tên tháng.');
        return false;
    } else if (listCreate.length == 0) {
        $('.createNewRank-err').text('Hãy chọn tuần áp dụng.')
        return false;
    } else {
        $('.createNewRank-err').text('');
        var createRank = {
            userName: username,
            month: monthName,
            currentYearId: currentYearId,
            weekList: listCreate
        }
        console.log(JSON.stringify(createRank));
        $.ajax({
            url: '/api/rankmonth/createrankmonth',
            type: 'POST',
            data: JSON.stringify(createRank),
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
                    $('#createNewRank').modal('hide');
                    messageModal('messageModal', 'img/img-success.png', 'Tạo xếp hạng tháng thành công!');
                    sessionStorage.removeItem('monthName');
                    sessionStorage.setItem('monthName', monthName);
                } else {
                    $('.createNewRank-err').text(message);
                }
            },
            failure: function (errMsg) {
                $('#createNewRank').modal('hide');
                messageModal('messageModal', 'img/img-error.png', errMsg)
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
})

/*=============Edit Rank=====================*/

/*Load edit rank month*/
function editRankBtn() {
    $('#editRankBtn').on('click', function () {
        listEdit = [];
        listEditOld = [];
        $('#editRank').modal('show');
        var monthName = $('#byMonth option:selected').text().slice(6);
        $('#newMonthName').val(monthName);
        var monthId = $('#byMonth option:selected').val();
        var data = {
            monthId: monthId,
            currentYearId: currentYearId,
        }
        console.log(JSON.stringify(data));
        $.ajax({
            url: '/api/rankmonth/loadeditrankmonth',
            type: 'POST',
            data: JSON.stringify(data),
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
                    if (data.weekList.length != 0) {
                        $('#weekListEdit').html('');
                        $('#weekListEdit').append(`<h6>Các tuần áp dụng <span class="text-red">*</span></h6>`);
                        $.each(data.weekList, function (i, item) {
                            $('#weekListEdit').append(`
                            <div class="form-check text-left my-1">
                                <span class="custom-checkbox">
                                    <input type="checkbox" name="editOptions" value="` + item.weekId + `">
                                    <label for="` + item.weekId + `"></label>
                                </span>
                                <span class="ml-3">Tuần ` + item.week + `</span>
                                <span class="hide week">` + item.week + `</span>
                                <span class="hide monthId">` + item.monthId + `</span>
                                <span class="hide isCheck">` + item.isCheck + `</span>
                            </div>
                        `);
                            if (item.isCheck == 1) {
                                $('input[value=' + item.weekId + ']').prop('checked', true);
                            }
                        });
                        $('input[name=editOptions]:checked').each(function (i) {
                            listEditOld.push({
                                weekId: $(this).val(),
                                week: $(this).parent().parent().find('.week').text(),
                                monthId: $(this).parent().parent().find('.monthId').text(),
                                isCheck: 1
                            });
                        });
                        $('input[name=editOptions]:not(:checked)').each(function (i) {
                            listEditOld.push({
                                weekId: $(this).val(),
                                week: $(this).parent().parent().find('.week').text(),
                                monthId: $(this).parent().parent().find('.monthId').text(),
                                isCheck: 0
                            });
                        });
                    } else {
                        $('#weekListEdit').html(`<h6 class="text-red">Không có tuần áp dụng nào!</h6>`);
                    }
                } else {
                    $('#weekListEdit').html(`<h6 class="text-red">` + message + `</h6>`);
                }
            },
            failure: function (errMsg) {
                $('#weekListEdit').html(`<h6 class="text-red">` + errMsg + `</h6>`);
            },
            dataType: "json",
            contentType: "application/json"
        });
    })
}

/*Edit rank month*/
$('#editRankBtnModal').on('click', function () {
    listEdit = [];
    var count = 0;
    $('input[name=editOptions]:checked').each(function (i) {
        count++;
        listEdit.push({
            weekId: $(this).val(),
            week: $(this).parent().parent().find('.week').text(),
            monthId: $(this).parent().parent().find('.monthId').text(),
            isCheck: 1
        });
    });
    $('input[name=editOptions]:not(:checked)').each(function (i) {
        listEdit.push({
            weekId: $(this).val(),
            week: $(this).parent().parent().find('.week').text(),
            monthId: $(this).parent().parent().find('.monthId').text(),
            isCheck: 0
        });
    });
    listEditOld.sort(function (a, b) {
        return a.weekId - b.weekId;
    });
    listEdit.sort(function (a, b) {
        return a.weekId - b.weekId;
    });
    var monthNameOld = $('#byMonth option:selected').text().slice(6);
    var monthName = $('#newMonthName').val().trim();

    if ((monthNameOld == monthName) && (JSON.stringify(listEditOld) == JSON.stringify(listEdit))) {
        $('.editRank-err').text('Hãy thay đổi thông tin.');
        return false;
    } else if (monthName == "" || monthName == null) {
        $('.editRank-err').text('Hãy nhập tên tháng.');
        return false;
    } else if (count == 0) {
        $('.editRank-err').text('Hãy chọn tuần áp dụng.')
        return false;
    } else {
        $('.editRank-err').text('');
        var editRank = {
            monthId: $('#byMonth option:selected').val(),
            month: monthName,
            userName: username,
            weekList: listEdit
        }
        console.log(JSON.stringify(editRank));
        $.ajax({
            url: '/api/rankmonth/editrankmonth',
            type: 'POST',
            data: JSON.stringify(editRank),
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
                    $('#editRank').modal('hide');
                    sessionStorage.removeItem('monthName');
                    sessionStorage.setItem('monthName', monthName);
                    messageModal('messageModal', 'img/img-success.png', 'Sửa xếp hạng tháng thành công!');
                } else {
                    $('.editRank-err').text(message);
                }
            },
            failure: function (errMsg) {
                $('#editRank').modal('hide');
                messageModal('messageModal', 'img/img-error.png', errMsg)
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
})

/*===============Download===================*/

/*Download button*/
function download() {
    $("#download").click(function () {
        var download = {
            monthId: $('#byMonth option:selected').val(),
            classId: $('#byClass option:selected').val()
        }
        console.log(JSON.stringify(download))
        $.ajax({
            url: '/api/rankmonth/download',
            type: 'POST',
            data: JSON.stringify(download),
            xhrFields: {
                responseType: 'blob'
            },
            beforeSend: function () {
                $('body').addClass("loading")
            },
            complete: function () {
                $('body').removeClass("loading")
            },
            success: function (data) {
                var a = document.createElement('a');
                var url = window.URL.createObjectURL(data);
                var name = 'XẾP-HẠNG-THI-ĐUA-THEO-THÁNG.xls';
                a.href = url;
                a.download = name;
                document.body.append(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
            },
            statusCode: {
                400: function (errMsg) {
                    messageModal('messageModal', 'img/img-error.png', 'Không thể tải được tập tin!')
                }
            },
            dataType: "binary",
            contentType: "application/json"
        });
    });
}

/*=====================================*/
/*Set role*/
if (roleID != 1) {
    $('.manageBtn').addClass('hide');
}

/*Remove checkbox and input when close modal*/
$(document).on('hidden.bs.modal', '#createNewRank', function () {
    $('input[name=options]').prop('checked', false);
    $('#monthName').val('');
    $('.createNewRank-err').text('');
});
$(document).on('hidden.bs.modal', '#editRank', function () {
    $('input[name=editOptions]').prop('checked', false);
    $('#newMonthName').val('');
    $('.editRank-err').text('');
});

/*===============View History===================*/

/*View history button*/
function viewHistory() {
    $("#viewHistory").click(function () {
        var viewHistory = {
            monthId: $('#byMonth option:selected').val(),
        };
        $.ajax({
            url: '/api/rankmonth/viewhistory',
            type: 'POST',
            data: JSON.stringify(viewHistory),
            beforeSend: function () {
                $('body').addClass("loading")
            },
            complete: function () {
                $('body').removeClass("loading")
            },
            success: function (data) {
                console.log(data)
                var messageCode = data.message.messageCode;
                var message = data.message.message;
                if (messageCode == 0) {
                    $('#historyModal .modal-body').html(data.history);
                    $('#historyModal').modal('show');
                } else {
                    messageModal('messageModal', 'img/img-error.png', message)
                }
            },
            failure: function (errMsg) {
                messageModal('messageModal', 'img/img-error.png', errMsg)
            },
            dataType: "json",
            contentType: "application/json"
        });
    });
}