/*Get giftedClass in combobox*/
$.ajax({
    url: '/api/assignRedStar/liststarclassdate',
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
            if (data.listClass.length != 0) {
                $("#classList").select2();
                $("#classList").html(`<option value="" selected="selected">Tất cả</option>`);
                $.each(data.listClass, function (i, item) {
                    $('#classList').append(
                        `<option value="` + item.classId + `">` + item.grade + ` ` + item.giftedClass.name + `</option>`
                    );
                });
            } else {
                $("#classList").html(`<option value="err" selected="selected">Danh sách lớp trống.</option>`);
            }
            if (data.listDate.length != 0) {
                $("#fromDate").select2();
                $("#fromDate").html("");
                $.each(data.listDate, function (i, item) {
                    if (i == 0) {
                        $('#fromDate').append(
                            `<option value="` + item + `" selected="selected">` + convertDate(item, '/') + `</option>
                        `);
                    } else {
                        $('#fromDate').append(
                            `<option value="` + item + `">` + convertDate(item, '/') + `</option>
                    `);
                    }
                });
            } else {
                $("#fromDate").html(`<option value="err" selected="selected">Danh sách ngày trống.</option>`);
            }
        } else {
            if (data.listClass.length != 0) {
                $("#classList").html(`<option value="err" selected="selected">` + message + `</option>`);
            } else {
                $("#fromDate").html(`<option value="err" selected="selected">` + message + `</option>`);
            }
        }
    },
    failure: function (errMsg) {
        if (data.listClass.length != 0) {
            $("#classList").html(`<option value="err" selected="selected">` + errMsg + `</option>`);
        } else {
            $("#fromDate").html(`<option value="err" selected="selected">` + errMsg + `</option>`);
        }
    },
    dataType: "json",
    contentType: "application/json"
});

setTimeout(search, 500);

/*Load data to list*/
function search() {
    var fromDate = $('#fromDate option:selected').val();
    var classId = $('#classList option:selected').val();
    var redStar = $('#redStarList').val().trim();
    var inforSearch = {
        fromDate: fromDate,
        classId: classId,
        redStar: redStar,
    };
    $('#deleteBtn').addClass('hide');
    $('#download').addClass('hide');
    console.log(JSON.stringify(inforSearch));
    if (fromDate == 'err' || classId == 'err') {
        $('tbody').html(`<tr><td colspan="3" class="text-center">Danh sách trống.</td></tr>`);
    } else {
        $('table').dataTable({
            destroy: true,
            searching: false,
            bInfo: false,
            paging: false,
            order: [],
            ajax: {
                url: "/api/assignRedStar/viewassigntask",
                type: "POST",
                data: function (d) {
                    return JSON.stringify(inforSearch);
                },
                dataType: "json",
                contentType: "application/json",
                failure: function (errMsg) {
                    $('tbody').html(`<tr><td colspan="3" class="text-center"> ` + errMsg + ` </td></tr>`)
                },
                dataSrc: function (data) {
                    var dataSrc = null;
                    var messageCode = data.message.messageCode;
                    var message = data.message.message;
                    if (messageCode == 0) {
                        if (data.listAssignTask.length != 0) {
                            dataSrc = data.listAssignTask;
                            if (roleID == 1) {
                                $('#deleteBtn').removeClass('hide');
                            }
                            $('#download').removeClass('hide');
                        } else {
                            return false;
                        }
                    } else {
                        $('tbody').html(`<tr><td colspan="3" class="text-center"> ` + message + ` </td></tr>`)
                        return false;
                    }
                    return dataSrc;
                }
            },
            columns: [
                {data: "className"},
                {data: "redStar1"},
                {data: "redStar2"},
            ],
            drawCallback: function (settings) {
                settings.oLanguage.sEmptyTable = "Danh sách trống."
            }
        })

    }
}

$("#search").click(function () {
    $('#violationList').html("");
    search();
});

/*Download button*/
$("#download").click(function () {
    var fromDate = $('#fromDate option:selected').val();
    if (fromDate == 'err') {
        messageModal('downloadModal', 'img/img-error.png', 'Chưa chọn ngày tải xuống!')
    } else {
        var download = {
            fromDate: fromDate,
            classId: $('#classList option:selected').val(),
            redStar: $('#redStarList').val().trim()
        }
        console.log(JSON.stringify(download))
        $.ajax({
            url: '/api/assignRedStar/download',
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
                var name = "Phân-công-trực-tuần-" + fromDate + '.xls';
                a.href = url;
                a.download = name;
                document.body.append(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
            },
            failure: function (errMsg) {
                messageModal('downloadModal', 'img/img-error.png', 'Không thể tải xuống!')
            },
            dataType: "binary",
            contentType: "application/json"
        });
    }
});

/*Check Date*/
function checkDate() {
    var request = {
        date: $('#dateApplied').val()
    }
    console.log(JSON.stringify(request));
    $.ajax({
        url: '/api/assignRedStar/checkDate',
        type: 'POST',
        data: JSON.stringify(request),
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
                $('#addAssign').modal('hide');
                createAssign(request);
            } else if (messageCode == 1) {
                $('#addAssign').modal('hide');
                messageModal('addSuccess', 'img/img-error.png', message)
            } else if (messageCode == 2) {
                $('#addAssign').modal('hide');
                $('#overrideConfirm .modal-body').html(`
                <img class="mb-3 mt-3" src="img/img-question.png"/>
                <h5>` + message + `</h5>`);
                $('#overrideConfirm').modal('show');
            } else {
                $('#addAssign').modal('hide');
                messageModal('addSuccess', 'img/img-error.png', message)
            }
        },
        failure: function (errMsg) {
            $('#overrideConfirm').modal('hide');
            messageModal('addSuccess', 'img/img-error.png', errMsg)
        },
        dataType: "json",
        contentType: "application/json"
    });

    $('#confirmApplied').unbind().click(function () {
        createAssign(request);
    })
}

function createAssign(request) {
    $.ajax({
        url: '/api/assignRedStar/create',
        type: 'POST',
        data: JSON.stringify(request),
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
                $('#overrideConfirm').modal('hide');
                $('#addSuccess .modal-footer').html(`<a href="assignEmulation" class="btn btn-primary">ĐÓNG</a>`);
                messageModal('addSuccess', 'img/img-success.png', 'Tạo phân công thành công!')
            } else {
                $('#overrideConfirm').modal('hide');
                messageModal('addSuccess', 'img/img-error.png', message)
            }
        },
        failure: function (errMsg) {
            $('#overrideConfirm').modal('hide');
            messageModal('addSuccess', 'img/img-error.png', errMsg)
        },
        dataType: "json",
        contentType: "application/json"
    });
}

/*Create Assign*/
$('#createAssignBtn').on('click', function () {
    var dateApplied = $('#dateApplied').val();
    if (dateApplied == '') {
        $('.addAssign-err').text('Hãy nhập ngày áp dụng.');
        return false;
    } else {
        checkDate();
    }
})

/*Delete Assign*/
function deleteAssign() {
    var date = $('#fromDate option:selected').val();
    if (date == 'err') {
        messageModal('addSuccess', 'img/img-error.png', 'Chưa chọn ngày xóa!')
    } else {
        var request = {
            date: date,
        }
        $('#overrideConfirm .modal-body').html(`
        <img class="mb-3 mt-3" src="img/img-question.png"/>
        <h5>Bạn có muốn <b>XÓA</b> phân công sau ngày ` + convertDate(date, '/') + ` không?</h5>`);
        $('#overrideConfirm').modal('show');
        $('#confirmApplied').unbind().click(function () {
            $.ajax({
                url: '/api/assignRedStar/delete',
                type: 'POST',
                data: JSON.stringify(request),
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
                        $('#overrideConfirm').modal('hide');
                        $('#addSuccess .modal-footer').html(`<a href="assignEmulation" class="btn btn-primary">ĐÓNG</a>`);
                        messageModal('addSuccess', 'img/img-success.png', 'Xóa phân công thành công!')
                    } else {
                        $('#overrideConfirm').modal('hide');
                        messageModal('addSuccess', 'img/img-error.png', message)
                    }
                },
                failure: function (errMsg) {
                    $('#overrideConfirm').modal('hide');
                    messageModal('addSuccess', 'img/img-error.png', errMsg)
                },
                dataType: "json",
                contentType: "application/json"
            });

        })
    }
}

if (roleID != 1) {
    $('.manageBtn').addClass('hide');
}