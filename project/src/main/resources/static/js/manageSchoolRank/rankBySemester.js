var listCreate = [];
var currentYearId;
/*=============Set data================*/
/*Load year list*/
$.ajax({
    url: '/api/ranksemester/loadranksemester',
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
                $('#bySemester').html(`<option value="err">Danh sách học kỳ đang trống.</option>`);
                $('#bySemester').prop('disabled', true);
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
            if (data.schoolSemesterList == null) {
                $('#bySemester').html(`<option value="err">` + message + `</option>`);
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

/*Get semester list when change year combobox*/
function loadComboboxYear(yearId) {
    $.ajax({
        url: '/api/ranksemester/getsemesterlist',
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
                if (data.schoolSemesterList != null) {
                    $('#bySemester').html('');
                    $.each(data.schoolSemesterList, function (i, item) {
                        if (i == 0) {
                            $('#bySemester').append(`<option value="` + item.semesterId + `" name="` + item.yearId + `" selected="selected">Học kỳ ` + item.semester + `</option>`);
                        } else {
                            $('#bySemester').append(`<option value="` + item.semesterId + `" name="` + item.yearId + `">Học kỳ ` + item.semester + `</option>`);
                        }
                    });
                    if (sessionStorage.getItem('semesterName') != null) {
                        var semesterName = 'Học kỳ ' + sessionStorage.getItem('semesterName');
                        $("#bySemester option").filter(function () {
                            return $(this).text() == semesterName;
                        }).prop("selected", true);
                    }
                } else {
                    $('#bySemester').html(`<option value="err">Danh sách học kỳ đang trống.</option>`);
                }
            } else {
                $('#bySemester').html(`<option value="err">` + message + `</option>`);
            }
        },
        failure: function (errMsg) {
            $('#bySemester').html(`<option value="err">` + errMsg + `</option>`);
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
    var semesterId = $('#bySemester option:selected').val();
    if (semesterId != null && semesterId != "" && semesterId != "err") {
        $('#viewHistory').removeClass('hide');
    }
    if (roleID == 1) {
        $('#createRankBtn').removeClass('hide');
    }
    if ($('#bySemester option:selected').val() == 'err') {
        $('tbody').append(`<tr><td colspan="4" class="text-center">Danh sách xếp hạng của học kỳ trống.</td></tr>`);
        $('#editRankBtn').addClass('hide');
    } else {
        var infoSearch = {
            semesterId: semesterId,
            classId: $('#byClass option:selected').val()
        }
        console.log(JSON.stringify(infoSearch));
        $('table').dataTable({
            destroy: true,
            searching: false,
            bInfo: false,
            paging: false,
            order: [],
            ajax: {
                url: "/api/ranksemester/searchranksemester",
                type: "POST",
                data: function (d) {
                    return JSON.stringify(infoSearch);
                },
                dataType: "json",
                contentType: "application/json",
                failure: function (errMsg) {
                    $('tbody').append(`<tr><td colspan="4" class="text-center"> ` + errMsg + ` </td></tr>`)
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
                        if (data.rankSemesterList != null) {
                            dataSrc = data.rankSemesterList;
                            $('#download').removeClass('hide');
                        } else {
                            return false;
                        }
                    } else {
                        $('tbody').append(`<tr><td colspan="4" class="text-center"> ` + message + ` </td></tr>`)
                        return false;
                    }
                    return dataSrc;
                }
            },
            columns: [
                {data: "className"},
                {data: "totalRankMonth"},
                {data: "totalGradeMonth"},
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
                settings.oLanguage.sEmptyTable = "Danh sách xếp hạng của học kỳ trống."
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
        <input type="button" id="editRankBtn" class="btn btn-success manageBtn ml-2 my-1 hide" value="Sửa xếp hạng kỳ"/>
        <input type="button" id="createRankBtn" class="btn btn-success manageBtn ml-2 my-1 hide" value="Tạo xếp hạng kỳ"/>
    </div>`);
    search();
});

/*==========Create rank===========*/

/*Load month list*/
function createRankBtn() {
    $('#createRankBtn').on('click', function () {
        $('#createNewRank').modal('show');
        listCreate = [];
        var currentYear = {
            currentYearId: currentYearId,
        }
        console.log(JSON.stringify(currentYear))
        $.ajax({
            url: '/api/ranksemester/loadmonthlist',
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
                    if (data.monthList.length != 0) {
                        $('#monthList').html('');
                        $('#monthList').append(`<h6>Các tháng áp dụng <span class="text-red">*</span></h6>`);
                        $.each(data.monthList, function (i, item) {
                            $('#monthList').append(`
                        <div class="form-check text-left my-1">
                            <span class="custom-checkbox">
                                <input type="checkbox" name="options" value="` + item.monthId + `">
                                <label for="` + item.monthId + `"></label>
                            </span>
                            <span class="ml-3"> Tháng ` + item.month + `</span>
                            <span class="hide monthName">` + item.month + `</span>
                            <span class="hide semesterId">` + item.semesterId + `</span>
                            <span class="hide isCheck">` + item.isCheck + `</span>
                        </div>
                    `);
                        });
                    } else {
                        $('#monthList').html(`<h6 class="text-red">Không có tháng áp dụng nào!</h6>`);
                    }
                } else {
                    $('#monthList').html(`<h6 class="text-red">` + message + `</h6>`);
                }
            },
            failure: function (errMsg) {
                $('#monthList').html(`<h6 class="text-red">` + errMsg + `</h6>`);
            },
            dataType: "json",
            contentType: "application/json"
        });
    })
}

/*Create rank semester*/
$('#createNewRankBtn').on('click', function () {
    $('input[name=options]:checked').each(function (i) {
        listCreate.push({
            monthId: $(this).val(),
            month: $(this).parent().parent().find('.monthName').text(),
            semesterId: $(this).parent().parent().find('.semesterId').text(),
            isCheck: 1
        });
    });
    var semesterName = $('#semesterName').val().trim();
    if (semesterName == "" || semesterName == null) {
        $('.createNewRank-err').text('Hãy nhập tên học kỳ.');
        return false;
    } else if (listCreate.length == 0) {
        $('.createNewRank-err').text('Hãy chọn tháng áp dụng.')
        return false;
    } else {
        $('.createNewRank-err').text('');
        var createRank = {
            userName: username,
            semester: semesterName,
            currentYearId: currentYearId,
            monthList: listCreate
        }
        console.log(JSON.stringify(createRank));
        $.ajax({
            url: '/api/ranksemester/createranksemester',
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
                    messageModal('messageModal', 'img/img-success.png', 'Tạo xếp hạng học kỳ thành công!');
                    sessionStorage.removeItem('semesterName');
                    sessionStorage.setItem('semesterName', semesterName);
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

/*Load edit rank semester*/
function editRankBtn() {
    $('#editRankBtn').on('click', function () {
        listEdit = [];
        listEditOld = [];
        $('#editRank').modal('show');
        var semesterName = $('#bySemester option:selected').text().slice(7);
        $('#newSemesterName').val(semesterName);
        var semesterId = $('#bySemester option:selected').val();
        var data = {
            semesterId: semesterId,
            currentYearId: currentYearId,
        }
        console.log(JSON.stringify(data));
        $.ajax({
            url: '/api/ranksemester/loadeditranksemester',
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
                    if (data.monthList.length != 0) {
                        $('#monthListEdit').html('');
                        $('#monthListEdit').append(`<h6>Các tháng áp dụng <span class="text-red">*</span></h6>`);
                        $.each(data.monthList, function (i, item) {
                            $('#monthListEdit').append(`
                            <div class="form-check text-left my-1">
                                <span class="custom-checkbox">
                                    <input type="checkbox" name="editOptions" value="` + item.monthId + `">
                                    <label for="` + item.monthId + `"></label>
                                </span>
                                <span class="ml-3">Tháng ` + item.month + `</span>
                                <span class="hide monthName">` + item.month + `</span>
                                <span class="hide semesterId">` + item.semesterId + `</span>
                                <span class="hide isCheck">` + item.isCheck + `</span>
                            </div>
                        `);
                            if (item.isCheck == 1) {
                                $('input[value=' + item.monthId + ']').prop('checked', true);
                            }
                        });
                        $('input[name=editOptions]:checked').each(function (i) {
                            listEditOld.push({
                                monthId: $(this).val(),
                                month: $(this).parent().parent().find('.monthName').text(),
                                semesterId: $(this).parent().parent().find('.semesterId').text(),
                                isCheck: 1
                            });
                        });
                        $('input[name=editOptions]:not(:checked)').each(function (i) {
                            listEditOld.push({
                                monthId: $(this).val(),
                                month: $(this).parent().parent().find('.monthName').text(),
                                semesterId: $(this).parent().parent().find('.semesterId').text(),
                                isCheck: 0
                            });
                        });
                    } else {
                        $('#monthListEdit').html(`<h6 class="text-red">Không có tháng áp dụng nào!</h6>`);
                    }
                } else {
                    $('#monthListEdit').html(`<h6 class="text-red">` + message + `</h6>`);
                }
            },
            failure: function (errMsg) {
                $('#monthListEdit').html(`<h6 class="text-red">` + errMsg + `</h6>`);
            },
            dataType: "json",
            contentType: "application/json"
        });
    })
}

/*Edit rank semester*/
$('#editRankBtnModal').on('click', function () {
    listEdit = [];
    var count = 0;
    $('input[name=editOptions]:checked').each(function (i) {
        count++;
        listEdit.push({
            monthId: $(this).val(),
            month: $(this).parent().parent().find('.monthName').text(),
            semesterId: $(this).parent().parent().find('.semesterId').text(),
            isCheck: 1
        });
    });
    $('input[name=editOptions]:not(:checked)').each(function (i) {
        listEdit.push({
            monthId: $(this).val(),
            month: $(this).parent().parent().find('.monthName').text(),
            semesterId: $(this).parent().parent().find('.semesterId').text(),
            isCheck: 0
        });
    });
    listEditOld.sort(function (a, b) {
        return a.monthId - b.monthId;
    });
    listEdit.sort(function (a, b) {
        return a.monthId - b.monthId;
    });
    var semesterNameOld = $('#bySemester option:selected').text().slice(7);
    var semesterName = $('#newSemesterName').val().trim();

    if ((semesterNameOld == semesterName) && (JSON.stringify(listEditOld) == JSON.stringify(listEdit))) {
        $('.editRank-err').text('Hãy thay đổi thông tin.');
        return false;
    } else if (semesterName == "" || semesterName == null) {
        $('.editRank-err').text('Hãy nhập tên học kỳ.');
        return false;
    } else if (count == 0) {
        $('.editRank-err').text('Hãy chọn tháng áp dụng.')
        return false;
    } else {
        $('.editRank-err').text('');
        var editRank = {
            semesterId: $('#bySemester option:selected').val(),
            semester: semesterName,
            userName: username,
            monthList: listEdit
        }
        console.log(JSON.stringify(editRank));
        $.ajax({
            url: '/api/ranksemester/editranksemester',
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
                    sessionStorage.removeItem('semesterName');
                    sessionStorage.setItem('semesterName', semesterName);
                    messageModal('messageModal', 'img/img-success.png', 'Sửa xếp hạng học kỳ thành công!');
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
            semesterId: $('#bySemester option:selected').val(),
            classId: $('#byClass option:selected').val()
        }
        console.log(JSON.stringify(download))
        $.ajax({
            url: '/api/ranksemester/download',
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
                var name = 'XẾP-HẠNG-THI-ĐUA-THEO-HỌC-KỲ.xls';
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
    $('#semesterName').val('');
    $('.createNewRank-err').text('');
});
$(document).on('hidden.bs.modal', '#editRank', function () {
    $('input[name=editOptions]').prop('checked', false);
    $('#newSemesterName').val('');
    $('.editRank-err').text('');
});

/*===============View History===================*/

/*View history button*/
function viewHistory() {
    $("#viewHistory").click(function () {
        var viewHistory = {
            semesterId: $('#bySemester option:selected').val(),
        }
        $.ajax({
            url: '/api/ranksemester/viewhistory',
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