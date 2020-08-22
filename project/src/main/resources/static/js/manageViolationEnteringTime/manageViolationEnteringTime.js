var list = [];

$.ajax({
    url: '/api/admin/viewenteringtime',
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
            if (data.listEmteringTime.length != 0) {
                $('#deleteBtn').removeClass('hide');
                $('tbody').html("");
                $.each(data.listEmteringTime, function (i, item) {
                    var violationEnteringTimeId, roleName, dayName, startTime, endTime;
                    if (item.violationEnteringTimeId == null) {
                        violationEnteringTimeId = "-";
                    } else {
                        violationEnteringTimeId = item.violationEnteringTimeId;
                    }
                    if (item.roleName == null) {
                        roleName = "-";
                    } else {
                        roleName = item.roleName;
                    }
                    if (item.dayName == null) {
                        dayName = "-";
                    } else {
                        dayName = item.dayName;
                    }
                    if (item.startTime == null) {
                        startTime = "-";
                    } else {
                        startTime = item.startTime;
                    }
                    if (item.endTime == null) {
                        endTime = "-";
                    } else {
                        endTime = item.endTime;
                    }

                    $('tbody').append(
                        `<tr>
                            <td>
                                <span class="custom-checkbox">
                                    <input type="checkbox" name="options" value="` + violationEnteringTimeId + `">
                                    <label></label>
                                </span>
                            </td>
                            <td><span>` + roleName + `</span></td>
                            <td><span>` + dayName + `</span></td>
                            <td><span>` + startTime + `</span></td>
                            <td><span>` + endTime + `</span></td>
                        </tr>
                    `);
                });
            } else {
                $('#deleteBtn').addClass('hide');
                $('tbody').html(`<tr><td colspan="5" id="table-err" class="text-center">Chưa có thời gian chấm điểm.</td></tr>`)
            }
        } else {
            $('tbody').html(`<tr><td colspan="5" id="table-err" class="text-center">` + message + `</td></tr>`)
        }
        selectCheckbox();
        manageBtn();
    },
    failure: function (errMsg) {
        $('tbody').html(`<tr><td colspan="5" id="table-err" class="text-center">` + errMsg + `</td></tr>`)
    },
    dataType: "json",
    contentType: "application/json"
});

/*Check select*/
function checkSelect() {
    if (list.length == 0) {
        $('#deleteModal .modal-footer .btn-danger').addClass('hide');
        $('#deleteModal .modal-footer .btn-primary').attr('value', 'ĐÓNG');
        messageModal('deleteModal', 'img/img-error.png', 'Hãy chọn thời gian mà bạn muốn xóa!')
    } else {
        $('#deleteModal .modal-footer .btn-danger').removeClass('hide');
        $('#deleteModal .modal-footer .btn-primary').attr('value', 'KHÔNG');
        messageModal('deleteModal', 'img/img-question.png', 'Bạn có muốn <b>XÓA</b> thời gian chấm này không?')
    }
}

/*Delete School Year*/
$('#deleteTime').on('click', function () {
    console.log(list)
    listEnteringTime = {
        listEnteringTime: list,
    }
    console.log(JSON.stringify(listEnteringTime))
    $.ajax({
        url: '/api/admin/deleteenteringtime',
        type: 'POST',
        data: JSON.stringify(listEnteringTime),
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
                messageModal('deleteSuccess', 'img/img-success.png', 'Xóa thời gian chấm thành công!')
            } else {
                messageModal('deleteSuccess', 'img/img-error.png', message)
            }
        },
        failure: function (errMsg) {
            messageModal('deleteSuccess', 'img/img-error.png', errMsg)
        },
        dataType: "json",
        contentType: "application/json"
    });
});

/*Show or hide button manage*/
function manageBtn() {
    if (roleID != 1) {
        $('.manageBtn').addClass('hide');
        $('table > thead > tr > th:first-child').addClass('hide');
        $('tbody > tr > td:first-child').addClass('hide');
        $('.table-title').addClass('pb-3');
    }
    $('#table-err').removeClass('hide');
}