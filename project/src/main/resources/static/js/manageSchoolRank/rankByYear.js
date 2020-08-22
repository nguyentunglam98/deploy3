var listCreate = [];
var currentYearId = localStorage.getItem('currentYearId');
/*=============Set data================*/
/*Load year list*/
$.ajax({
    url: '/api/rankyear/loadrankyear',
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
            if (data.schoolYearList != null) {
                $('#byYear').html('');
                $.each(data.schoolYearList, function (i, item) {
                    if (item.schoolYearId == currentYearId) {
                        $('#byYear').append(`<option value="` + item.schoolYearId + `" selected="selected">` + item.yearName + `</option>`);
                    } else {
                        $('#byYear').append(`<option value="` + item.schoolYearId + `">` + item.yearName + `</option>`);
                    }
                })
                if (sessionStorage.getItem('yearId') != null) {
                    $('#byYear').val(sessionStorage.getItem('yearId')).change();
                }
            } else {
                $('#byYear').html(`<option value="err">Danh sách năm học trống.</option>`);
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

setTimeout(search, 1000);

/*Set data to table*/
function search() {
    $('#editRankBtn').addClass('hide');
    $('#download').addClass('hide');
    $('#viewHistory').addClass('hide');
    if ($('#byYear option:selected').val() == 'err') {
        $('tbody').append(`<tr><td colspan="4" class="text-center">Danh sách xếp hạng của năm học trống.</td></tr>`);
    } else {
        var infoSearch = {
            yearId: $('#byYear option:selected').val(),
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
                url: "/api/rankyear/searchrankyear",
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
                    if (messageCode == 0) {
                        if (data.rankYearList != null) {
                            dataSrc = data.rankYearList;
                            $('#editRankBtn').removeClass('hide');
                            $('#download').removeClass('hide');
                            $('#viewHistory').removeClass('hide');
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
                {data: "totalRankSemester"},
                {data: "totalGradeSemester"},
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
                settings.oLanguage.sEmptyTable = "Danh sách xếp hạng của năm học trống."
            }
        })
    }
}

/*Search button*/
$('#search').click(function (e) {
    $('table tbody').html('');
    search();
});

/*==========Create rank===========*/
/*Load month list*/
$('#createRankBtn').on('click', function () {
    $('#createNewRank').modal('show');
    listCreate = [];
    var currentYear = {
        currentYearId: currentYearId,
    }
    console.log(JSON.stringify(currentYear))
    $.ajax({
        url: '/api/rankyear/loadsemesterlist',
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
            if (messageCode == 0 || messageCode == 1) {
                if (data.schoolYearList == null || data.schoolYearList.length == 0) {
                    $('#yearName').html(`<option value="err" selected>Không có năm học nào chưa được xếp hạng.</option>`)
                } else {
                    $('#yearName').html('');
                    $.each(data.schoolYearList, function (i, item) {
                        if (item.schoolYearId == currentYearId) {
                            $('#yearName').append(`<option value="` + item.schoolYearId + `" selected="selected">` + item.yearName + `</option>`);
                        } else {
                            $('#yearName').append(`<option value="` + item.schoolYearId + `">` + item.yearName + `</option>`);
                        }
                    });
                }
                if (data.semesterList.length != 0) {
                    $('#semesterList').html('');
                    $('#semesterList').append(`<h6>Các học kỳ áp dụng <span class="text-red">*</span></h6>`);
                    $.each(data.semesterList, function (i, item) {
                        $('#semesterList').append(`
                        <div class="form-check text-left my-1">
                            <span class="custom-checkbox">
                                <input type="checkbox" name="options" value="` + item.semesterId + `">
                                <label for="` + item.semesterId + `"></label>
                            </span>
                            <span class="ml-3"> Học kỳ ` + item.semester + `</span>
                            <span class="hide semester">` + item.semester + `</span>
                            <span class="hide yearId">` + item.yearId + `</span>
                            <span class="hide isCheck">` + item.isCheck + `</span>
                        </div>
                    `);
                    });
                } else {
                    $('#semesterList').html(`<h6 class="text-red">Không có học kỳ áp dụng nào!</h6>`);
                }
            } else {
                $('#semesterList').html(`<h6 class="text-red">` + message + `</h6>`);
            }
        },
        failure: function (errMsg) {
            $('#semesterList').html(`<h6 class="text-red">` + errMsg + `</h6>`);
        },
        dataType: "json",
        contentType: "application/json"
    });
})

/*Create rank semester*/
$('#createNewRankBtn').on('click', function () {
    $('input[name=options]:checked').each(function (i) {
        listCreate.push({
            semesterId: $(this).val(),
            semester: $(this).parent().parent().find('.semester').text(),
            yearId: $(this).parent().parent().find('.yearId').text(),
            isCheck: 1
        });
    });
    var yearId = $('#yearName option:selected').val();
    if (listCreate.length == 0) {
        $('.createNewRank-err').text('Hãy chọn học kỳ áp dụng.')
        return false;
    } else {
        $('.createNewRank-err').text('');
        var createRank = {
            yearId: yearId,
            userName: username,
            semesterList: listCreate
        }
        console.log(JSON.stringify(createRank));
        $.ajax({
            url: '/api/rankyear/createrankyear',
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
                    messageModal('messageModal', 'img/img-success.png', 'Tạo xếp hạng năm học thành công!');
                    sessionStorage.removeItem('yearId');
                    sessionStorage.setItem('yearId', yearId);
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
$('#editRankBtn').on('click', function () {
    listEdit = [];
    listEditOld = [];
    $('#editRank').modal('show');
    var newYearName = $('#byYear option:selected').text();
    $('#newYearName').val(newYearName);
    var yearId = $('#byYear option:selected').val();
    var data = {
        yearId: yearId,
    }
    console.log(JSON.stringify(data));
    $.ajax({
        url: '/api/rankyear/loadeditrankyear',
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
                if (data.semesterList.length != 0) {
                    $('#semesterListEdit').html('');
                    $('#semesterListEdit').append(`<h6>Các học kỳ áp dụng <span class="text-red">*</span></h6>`);
                    $.each(data.semesterList, function (i, item) {
                        $('#semesterListEdit').append(`
                            <div class="form-check text-left my-1">
                                <span class="custom-checkbox">
                                    <input type="checkbox" name="editOptions" value="` + item.semesterId + `">
                                    <label for="` + item.semesterId + `"></label>
                                </span>
                                <span class="ml-3">Học kỳ ` + item.semester + `</span>
                                <span class="hide semester">` + item.semester + `</span>
                                <span class="hide yearId">` + item.yearId + `</span>
                                <span class="hide isCheck">` + item.isCheck + `</span>
                            </div>
                        `);
                        if (item.isCheck == 1) {
                            $('input[value=' + item.semesterId + ']').prop('checked', true);
                        }
                    });
                    $('input[name=editOptions]:checked').each(function (i) {
                        listEditOld.push({
                            semesterId: $(this).val(),
                            semester: $(this).parent().parent().find('.semester').text(),
                            yearId: $(this).parent().parent().find('.yearId').text(),
                            isCheck: 1
                        });
                    });
                    $('input[name=editOptions]:not(:checked)').each(function (i) {
                        listEditOld.push({
                            semesterId: $(this).val(),
                            semester: $(this).parent().parent().find('.semester').text(),
                            semesterId: $(this).parent().parent().find('.semesterId').text(),
                            isCheck: 0
                        });
                    });
                } else {
                    $('#semesterListEdit').html(`<h6 class="text-red">Không có học kỳ áp dụng nào!</h6>`);
                }
            } else {
                $('#semesterListEdit').html(`<h6 class="text-red">` + message + `</h6>`);
            }
        },
        failure: function (errMsg) {
            $('#semesterListEdit').html(`<h6 class="text-red">` + errMsg + `</h6>`);
        },
        dataType: "json",
        contentType: "application/json"
    });
})

/*Edit rank semester*/
$('#editRankBtnModal').on('click', function () {
    listEdit = [];
    var count = 0;
    $('input[name=editOptions]:checked').each(function (i) {
        count++;
        listEdit.push({
            semesterId: $(this).val(),
            semester: $(this).parent().parent().find('.semester').text(),
            yearId: $(this).parent().parent().find('.yearId').text(),
            isCheck: 1
        });
    });
    $('input[name=editOptions]:not(:checked)').each(function (i) {
        listEdit.push({
            semesterId: $(this).val(),
            semester: $(this).parent().parent().find('.semester').text(),
            yearId: $(this).parent().parent().find('.yearId').text(),
            isCheck: 0
        });
    });
    listEditOld.sort(function (a, b) {
        return a.semesterId - b.semesterId;
    });
    listEdit.sort(function (a, b) {
        return a.semesterId - b.semesterId;
    });
    if (JSON.stringify(listEditOld) == JSON.stringify(listEdit)) {
        $('.editRank-err').text('Hãy thay đổi thông tin.');
        return false;
    } else if (count == 0) {
        $('.editRank-err').text('Hãy chọn học kỳ áp dụng.')
        return false;
    } else {
        $('.editRank-err').text('');
        var editRank = {
            yearId: $('#byYear option:selected').val(),
            userName: username,
            semesterList: listEdit
        }
        console.log(JSON.stringify(editRank));
        $.ajax({
            url: '/api/rankyear/editrankyear',
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
                    sessionStorage.removeItem('yearId');
                    sessionStorage.setItem('yearId', $('#byYear option:selected').val());
                    messageModal('messageModal', 'img/img-success.png', 'Sửa xếp hạng năm học thành công!');
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
$("#download").click(function () {
    var download = {
        yearId: $('#byYear option:selected').val(),
        classId: $('#byClass option:selected').val()
    }
    console.log(JSON.stringify(download))
    $.ajax({
        url: '/api/rankyear/download',
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
            var name = 'XẾP-HẠNG-THI-ĐUA-THEO-NĂM-HỌC.xls';
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

/*=====================================*/
/*Set role*/
if (roleID != 1) {
    $('.manageBtn').addClass('hide');
}

/*Remove checkbox and input when close modal*/
$(document).on('hidden.bs.modal', '#createNewRank', function () {
    $('input[name=options]').prop('checked', false);
    $('#yearName').val('')
    $('.createNewRank-err').text('');
});
$(document).on('hidden.bs.modal', '#editRank', function () {
    $('input[name=editOptions]').prop('checked', false);
    $('#newYearName').val('');
    $('.editRank-err').text('');
});

/*===============View History===================*/
/*View history button*/
$("#viewHistory").click(function () {
    var viewHistory = {
        yearId: $('#byYear option:selected').val(),
    };
    $.ajax({
        url: '/api/rankyear/viewhistory',
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