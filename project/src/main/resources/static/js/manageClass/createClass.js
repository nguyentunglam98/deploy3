var grade, giftedClassId, classIdentifier, isRedStar, isMonitor;
gradeCombobox();

$.ajax({
    url: '/api/admin/giftedclasslist',
    type: 'POST',
    success: function (data) {
        $.each(data.giftedClassList, function (i, item) {
            $('#classIdentifier').append(`<option value="` + item.giftedClassId + `">` + item.name + `</option>`);
        });
    },
    failure: function (errMsg) {
        console.log(errMsg);
    },
    dataType: "json",
    contentType: "application/json"
});

function gradeCombobox() {
    $('#grade').append(`<option value="10">10</option>`);
    $('#grade').append(`<option value="11">11</option>`);
    $('#grade').append(`<option value="12">12</option>`);
}

function changeSelected() {
    grade = $('#grade option:selected').val();
    giftedClassId = $('#classIdentifier option:selected').val();
}

$("#submit").click(function (e) {
    isRedStar = false;
    isMonitor = false;
    classIdentifier = $('#identifier').val().trim();
    if ($('#isRedStar').is(":checked")) {
        isRedStar = true;
    }
    if ($('#isMonitor').is(":checked")) {
        isMonitor = true;
    }
    if (grade == 0 || grade == null) {
        $('.createClass-err').text("Hãy chọn khối lớp!");
        return false;
    } else if (giftedClassId == 0 || giftedClassId == null) {
        $('.createClass-err').text("Hãy chọn hệ chuyên!");
        return false;
    } else if (classIdentifier == "") {
        $('.createClass-err').text("Hãy nhập tên định danh!");
        return false;
    } else {
        $('.createClass-err').text('');
        var addClass = {
            classIdentifier: classIdentifier,
            grade: grade,
            giftedClassId: giftedClassId,
            isRedStar: isRedStar,
            isMonitor: isMonitor
        }
        e.preventDefault();
        $.ajax({
            url: '/api/admin/addclass',
            type: 'POST',
            data: JSON.stringify(addClass),
            beforeSend: function () {
                $('body').addClass("loading")
            },
            complete: function () {
                $('body').removeClass("loading")
            },
            success: function (data) {
                var messageCode = data.message.messageCode;
                var message = data.message.message;
                var listUser = data.userList;
                if (messageCode == 2) {
                    messageModal('createSuccess', 'img/img-error.png', message + `<h5>Nếu muốn tiếp tục sử dụng thì chỉnh sửa <a href="editClass" id="saveSession">TẠI ĐÂY</a></h5>`)
                    $('#saveSession').on('click', function () {
                        sessionStorage.setItem("classId", data.classId);
                    })
                } else if (messageCode == 0) {
                    if (listUser != null) {
                        var index = 1;
                        messageModal('createSuccess', 'img/img-success.png', 'Tạo lớp thành công!')
                        for (var i = 0; i < data.userList.length; i++) {
                            var roleAcc = data.userList[i].role.roleId;
                            var roleName;
                            if (roleAcc == 4) {
                                roleName = "Tài khoản Lớp trưởng:";
                            } else if (roleAcc == 3) {
                                roleName = "Tài khoản Cờ đỏ " + index + ":";
                                index++;
                            }
                            $("#createSuccess .modal-body").append(`
                                <div class="info-account">
                                    <div class="text-left"><b>` + roleName + `</b></div>
                                    <div class="account">
                                        <p><span class="roleName">Tên đăng nhập:</span> ` + data.userList[i].username + `</p>
                                        <p><span class="roleName">Mật khẩu:</span> ` + data.userList[i].password + `</p>
                                    </div>
                                </div>
                            `);
                        }
                    } else {
                        messageModal('createSuccess', 'img/img-success.png', 'Tạo lớp thành công!')
                    }
                } else {
                    $('.createClass-err').text(message);
                }
            },
            failure: function (errMsg) {
                $('.createClass-err').text('');
                messageModal('createSuccess', 'img/img-error.png', errMsg)
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
});

/*Check Role has create or not*/
if (roleID != 1) {
    $('.createClass-err').text("Bạn không có quyền thêm lớp!");
    $('#submit').prop('disabled', true);
}