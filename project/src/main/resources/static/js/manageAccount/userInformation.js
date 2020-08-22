var oldFullName, oldPhone, oldEmail;

$(document).ready(function () {
    var account = {
        userName: username
    }
    if (roleID == 3 || roleID == 4) {
        $("#editInfo").addClass('hide');
        $("#fullNameDiv").addClass('hide');
        $("#phoneDiv").addClass('hide');
        $("#emailDiv").addClass('hide');
    }
    if (roleID != 3 && roleID != 4) {
        $('#classDiv').addClass('hide');
    }
    $.ajax({
        url: '/api/user/viewinformation',
        type: 'POST',
        data: JSON.stringify(account),
        beforeSend: function () {
            $('body').addClass("loading")
        },
        complete: function () {
            $('body').removeClass("loading")
        },
        success: function (data) {
            $('#fullName').attr('value', data.fullName);
            $('#username').attr('value', data.userName);
            $('#phone').attr('value', data.phone);
            $('#email').attr('value', data.email);
            $('#className').attr('value', data.className);
            $('#roleName').attr('value', data.roleName);
            oldFullName = $('#fullName').val();
            oldPhone = $('#phone').val();
            oldEmail = $('#email').val();
        },
        failure: function (errMsg) {
            $('.userInfo-err').text(errMsg);
        },
        dataType: "json",
        contentType: "application/json"
    });
});

$("#editInfo").click(function () {
    if (roleID != 3 && roleID != 4) {
        $('#fullName').prop('disabled', false);
        $('#phone').prop('disabled', false);
        $('#email').prop('disabled', false);
    }
    $('#editInfo').attr('value', 'XONG');

    $("#editInfo").unbind().click(function () {
        var emailRegex = '^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$';
        var phoneRegex = '^[0-9\\-\\+]{9,15}$';
        var fullName = $('#fullName').val().trim();
        var phone = $('#phone').val().trim();
        var email = $('#email').val().trim();
        if (fullName == oldFullName && phone == oldPhone && email == oldEmail) {
            $('.userInfo-err').text("Hãy thay đổi thông tin.");
            return false;
        } else if (phone != "" && !phone.match(phoneRegex)) {
            $('.userInfo-err').text("SĐT không đúng định dạng.");
            return false;
        } else if (email != "" && !email.match(emailRegex)) {
            $('.userInfo-err').text("Email không đúng định dạng.");
            return false;
        } else {
            $('.userInfo-err').text("");
            $('#fullName').prop('disabled', true);
            $('#phone').prop('disabled', true);
            $('#email').prop('disabled', true);
            var info = {
                userName: username,
                fullName: fullName,
                phone: phone,
                email: email
            }
            $.ajax({
                url: '/api/user/editinformation',
                type: 'POST',
                data: JSON.stringify(info),
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
                        messageModal('editInfoSuccess','img/img-success.png','Thông tin sửa thành công!')
                    } else {
                        messageModal('editInfoSuccess','img/img-error.png',message)
                    }
                },
                failure: function (errMsg) {
                    messageModal('editInfoSuccess','img/img-error.png',errMsg)
                },
                dataType: "json",
                contentType: "application/json"
            });
        }
    });
});

/*Check Role has create or not*/
if (roleID == null) {
    $('.userInfo-err').append(`Hãy <a href="login">ĐĂNG NHẬP</a> để có thể sửa thông tin!`);
    $('#editInfo').prop('disabled', true);
}