var editClassId, oldIdentifierName, oldStatus, newStatus;

$(document).ready(function () {
    editClassId = sessionStorage.getItem("classId")
    var classRequest = {
        classId: editClassId
    }
    if (editClassId == null) {
        $('.classInfo-err').append(`Hãy quay lại chọn lớp mà bạn muốn sửa thông tin tại <a href="manageClass">ĐÂY</a>`);
        $("#editInfo").prop('disabled', true);
    } else {
        $("#editInfo").prop('disabled', false);
        $.ajax({
            url: '/api/admin/viewclassinfor',
            type: 'POST',
            data: JSON.stringify(classRequest),
            beforeSend: function () {
                $('body').addClass("loading")
            },
            complete: function () {
                $('body').removeClass("loading")
            },
            success: function (data) {
                if (data.message.messageCode == 0) {
                    oldIdentifierName = data.classIdentifier;
                    $('#identifier').attr('value', data.classIdentifier);
                    $('#giftedClassName').attr('value', data.giftedClassName);
                    $('#grade').attr('value', data.grade);
                    if (data.status != null && data.status == 1) {
                        $("input[name=optradio][value='1']").prop('checked', true);
                        oldStatus = 1;
                    } else {
                        $("input[name=optradio][value='0']").prop('checked', true);
                        oldStatus = 0;
                    }
                } else {
                    $('tbody').append(
                        `<tr>
                    <td colspan="7" class="userlist-result">
                        ` + data.message.message + `
                    </td>
                </tr>`
                    )
                }

            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
})

//edit information of class
$("#editInfo").click(function (e) {
    newStatus = 1;
    newClassIdentifier = $('#identifier').val().trim();
    var radioValue = $("input[name=optradio]:checked").val();
    if (radioValue == '0') {
        newStatus = 0;
    }

    if (newClassIdentifier == "") {
        $('.classInfo-err').text("Hãy nhập tên định danh!");
        return false;
    } else if (newStatus == oldStatus && newClassIdentifier == oldIdentifierName) {
        $('.classInfo-err').text("Hãy thay đổi thông tin !");
        return false;
    } else {
        if (newStatus != oldStatus) {
            $('#confirmEditModal .modal-footer .btn-danger').removeClass('hide');
            $('#confirmEditModal .modal-footer .btn-primary').attr('value', 'KHÔNG');
            messageModal('confirmEditModal', 'img/img-error.png', 'Bạn có chắc muốn <b>CẬP NHẬT</b> trạng thái của lớp này không?')
            $('#editClassModal').click(function (e) {
                editClass(e);
            })
        } else {
            editClass(e);
        }
    }
});

function editClass(e) {
    var editClass = {
        classId: editClassId,
        classIdentifier: newClassIdentifier,
        status: newStatus.toString()
    }
    e.preventDefault();
    $.ajax({
        url: '/api/admin/editclass',
        type: 'POST',
        data: JSON.stringify(editClass),
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
                $('.classInfo-err').text("");
                $('#editInfoSuccess .modal-body').html(`
                    <img class="my-3" src="img/img-success.png"/>
                    <h5>Thông tin sửa thành công!</h5>
                `)
                if (oldStatus != newStatus) {
                    $('#editInfoSuccess .modal-body').append(`
                        <h5>Trạng thái của tài khoản cờ đỏ và lớp trưởng của lớp cũng đã được thay đổi.</h5>
                    `);
                }
                $('#editInfoSuccess').modal('show');
                oldIdentifierName = newClassIdentifier;
                oldStatus = newStatus;
            } else {
                $('.classInfo-err').text(message);
            }
        },
        failure: function (errMsg) {
            $('.classInfo-err').text('');
            messageModal('editInfoSuccess', 'img/img-error.png', errMsg)
        },
        dataType: "json",
        contentType: "application/json"
    });
}