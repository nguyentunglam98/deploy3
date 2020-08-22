var spaceRegex = /^\S+$/;
var inforSearch = {
    userName: "",
    roleId: null,
    sortBy: "1",
    orderBy: "0",
    pageNumber: 0
}
var list = [];

/*Load role list*/
$.ajax({
    url: '/api/admin/rolelist',
    type: 'POST',
    success: function (data) {
        if (data.listRole != 0) {
            $.each(data.listRole, function (i, item) {
                $('#role-name').append(`<option value="` + item.roleId + `">` + item.roleName + `</option>`);
            });
        } else {
            $('tbody').html('');
            $('tbody').append(`
            <tr>
                <td colspan="7" class="userlist-result">Danh sách trống.</td>
            </tr>`);
        }
    },
    failure: function (errMsg) {
        $('tbody').html('');
        $('tbody').append(`
            <tr>
                <td colspan="7" class="userlist-result">` + errMsg + `</td>
            </tr>`);
    },
    dataType: "json",
    contentType: "application/json"
});
search();

/*Search button*/
$("#search").click(function () {
    var userName, roleId, sortBy, orderBy, pageNumber;
    userName = $('#searchByUsername input').val().trim();
    if ($('#role-name option:selected').val() == null) {
        roleId = null;
    } else {
        roleId = $('#role-name option:selected').val();
    }
    if ($('#sortBy option:selected').val() == null) {
        sortBy = "1";
    } else {
        sortBy = $('#sortBy option:selected').val();
    }
    if ($('#orderBy option:selected').val() == null) {
        orderBy = "0";
    } else {
        orderBy = $('#orderBy option:selected').val();
    }
    inforSearch = {
        userName: userName,
        roleId: roleId,
        sortBy: sortBy,
        orderBy: orderBy,
        pageNumber: 0
    }
    $('tbody').html("");
    $('.table-paging').html("");
    search();
});

/*Load user list*/
function search() {
    $.ajax({
        url: '/api/admin/userlist',
        type: 'POST',
        data: JSON.stringify(inforSearch),
        beforeSend: function () {
            $('body').addClass("loading")
        },
        complete: function () {
            $('body').removeClass("loading")
        },
        success: function (data) {
            if (data.message.messageCode == 0) {
                var totalPages = data.userList.totalPages;
                paging(inforSearch, totalPages);
                $.each(data.userList.content, function (i, item) {
                    var mappingName, phone, email, name, roleName;
                    if (item.name == null) {
                        name = "";
                    } else {
                        name = item.name;
                    }
                    if (item.phone == null) {
                        phone = "";
                    } else {
                        phone = item.phone;
                    }
                    if (item.email == null) {
                        email = "";
                    } else {
                        email = item.email;
                    }
                    if (item.classSchool == null) {
                        mappingName = "";
                    } else {
                        mappingName = item.classSchool.grade + " " + item.classSchool.giftedClass.name;
                    }
                    if (item.role == null) {
                        roleName = "";
                    } else {
                        roleName = item.role.roleName;
                    }
                    var selected = "";
                    var checked = ""
                    if (isCheck(item.username)) {
                        selected = "selected"
                        checked = "checked"
                    }
                    $('tbody').append(
                        `<tr class="` + selected + `">
                <td class="text-center">
                    <span class="custom-checkbox">
                        <input id="` + item.username + `"type="checkbox" name="options" value="` + item.username + `" ` + checked + `>
                        <label for="` + item.username + `"></label>
                    </span>
                </td>
                <td><span id="userName">` + item.username + `</span></td>
                <td><span id="fullName">` + name + `</span></td>
                <td><span id="roleName">` + roleName + `</span></td>
                <td><span id="className">` + mappingName + `</span></td>
                <td><span id="phone">` + phone + `</span></td>
                <td><span id="email">` + email + `</span></td>
                </tr>`);
                });
                selectCheckbox();
                pagingClick();
                manageBtn();
            } else {
                $('tbody').append(`<tr><td colspan="7" class="userlist-result">` + data.message.message + `</td></tr>`)
            }
        },
        failure: function (errMsg) {
            $('tbody').append(`<tr><td colspan="7" class="userlist-result">` + errMsg + ` </td></tr>`)
        },
        dataType: "json",
        contentType: "application/json"
    });
}

/*Delete account*/
$("#deleteAccount").click(function (e) {
    $('#deleteSuccess').modal('show');
    listUser = {
        listUser: list,
    }
    e.preventDefault();
    $.ajax({
        url: '/api/admin/deleteaccount',
        type: 'POST',
        data: JSON.stringify(listUser),
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
                messageModal('deleteSuccess', 'img/img-success.png', message)
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

/*Reset Account*/
$("#resetPassword").click(function (e) {
    $('.resetPass-err').text("");
    var newpassword = $('#new-password').val().trim();
    var confirmpassword = $('#confirm-password').val().trim();

    if (newpassword == "" && confirmpassword == "") {
        $('.resetPass-err').text("Hãy điền đầy đủ tất cả các trường.");
        return false;
    } else if (newpassword == "") {
        $('.resetPass-err').text("Hãy điền mật khẩu mới.");
        return false;
    } else if (confirmpassword == "") {
        $('.resetPass-err').text("Hãy xác nhận lại mật khẩu.");
        return false;
    } else if (newpassword != confirmpassword) {
        $('.resetPass-err').text("Mật khẩu xác nhận không đúng.");
        return false;
    } else if (newpassword.length < 6) {
        $('.resetPass-err').text("Mật khẩu phải chứa ít nhất 6 ký tự.");
        return false;
    } else if (!newpassword.match(spaceRegex)) {
        $('.resetPass-err').text("Mật khẩu không được chứa khoảng trắng.");
        return false;
    }  else {
        var resetPassword = {
            userNameList: list,
            passWord: newpassword,
        }
        e.preventDefault();
        $.ajax({
            url: '/api/admin/resetpassword',
            type: 'POST',
            data: JSON.stringify(resetPassword),
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
                    messageModal('resetSuccess', 'img/img-success.png', message);
                } else {
                    messageModal('resetSuccess', 'img/img-error.png', message);
                }
            },
            failure: function (errMsg) {
                messageModal('resetSuccess', 'img/img-error.png', errMsg);
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
});

/*Check user before delete account*/
function checkUser() {
    var userErr = localStorage.getItem("username");
    if (jQuery.inArray(userErr, list) != -1) {
        $('#deleteAccountModal .modal-footer .btn-danger').addClass('hide');
        $('#deleteAccountModal .modal-footer .btn-primary').attr('value', 'ĐÓNG');
        messageModal('deleteAccountModal', 'img/img-question.png', `Bạn không thể xoá tài khoản <b class="error">` + userErr + `</b>!`);
    } else if (list.length == 0) {
        $('#deleteAccountModal .modal-footer .btn-danger').addClass('hide');
        $('#deleteAccountModal .modal-footer .btn-primary').attr('value', 'ĐÓNG');
        messageModal('deleteAccountModal', 'img/img-error.png', 'Hãy chọn tài khoản mà bạn muốn xóa!');
    } else {
        $('#deleteAccountModal .modal-footer .btn-danger').removeClass('hide');
        $('#deleteAccountModal .modal-footer .btn-primary').attr('value', 'KHÔNG');
        messageModal('deleteAccountModal', 'img/img-question.png', `Bạn có chắc muốn <b>XÓA</b> tài khoản này không?`);
    }
}

/*Check user before reset password*/
function checkResetPassword() {
    if (list.length == 0) {
        $("#resetPasswordModal .modal-header").addClass('hide');
        $('#resetPasswordModal .modal-footer .btn-danger').addClass('hide');
        $('#resetPasswordModal .modal-footer .btn-primary').attr('value', 'ĐÓNG');
        messageModal('resetPasswordModal', 'img/img-error.png', 'Hãy chọn tài khoản mà bạn muốn đặt lại mật khẩu!');
    } else {
        $("#resetPasswordModal .modal-body").html("");
        $("#resetPasswordModal .modal-header").removeClass('hide');
        $('#resetPasswordModal .modal-body').append(`
        <div class="form-group text-left">
            <label for="new-password">Mật khẩu mới <span class="text-red">*</span></label>
            <input type="password" class="form-control" id="new-password" required>
        </div>
        <div class="form-group text-left">
            <label for="confirm-password">Xác nhận mật khẩu mới <span class="text-red">*</span></label>
            <input type="password" class="form-control" id="confirm-password" required>
        </div>
        <div class="error text-left resetPass-err"></div>
        `);
        $('#resetPasswordModal .modal-footer .btn-danger').removeClass('hide');
        $('#resetPasswordModal .modal-footer .btn-primary').attr('value', 'KHÔNG');
        $('#resetPasswordModal').modal('show');
    }
}

/*Show or hide button manage*/
function manageBtn() {
    if (roleID == 1) {
        $('.manageBtn').removeClass('hide');
        $('table thead th:first-child').removeClass('hide');
        $('tbody > tr > td:first-child').removeClass('hide')
    } else {
        $('.manageBtn').addClass('hide');
        $('table thead th:first-child').addClass('hide');
        $('tbody > tr > td:first-child').addClass('hide')
    }
}
