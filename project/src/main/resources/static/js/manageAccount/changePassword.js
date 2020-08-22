var spaceRegex = /^\S+$/;
$(document).ready(function () {
    $("#confirm").click(function (e) {
        $('.changePassword-err').text("");
        var oldpassword = $('#old-password').val().trim();
        var newpassword = $('#new-password').val().trim();
        var confirmpassword = $('#confirm-password').val().trim();

        if (oldpassword == "" && newpassword == "" && confirmpassword == "" ||
            oldpassword == "" && newpassword == "" ||
            newpassword == "" && confirmpassword == "" ||
            oldpassword == "" && confirmpassword == "") {
            $('.changePassword-err').text("Hãy điền đầy đủ tất cả các trường.");
            return false;
        } else if (oldpassword == "") {
            $('.changePassword-err').text("Hãy điền mật khẩu cũ.");
            return false;
        } else if (newpassword == "") {
            $('.changePassword-err').text("Hãy điền mật khẩu mới.");
            return false;
        } else if (confirmpassword == "") {
            $('.changePassword-err').text("Hãy xác nhận lại mật khẩu.");
            return false;
        } else if (newpassword != confirmpassword) {
            $('.changePassword-err').text("Mật khẩu xác nhận không đúng.");
            return false;
        } else if (newpassword.length < 6) {
            $('.changePassword-err').text("Mật khẩu phải chứa ít nhất 6 ký tự.");
            return false;
        } else if (!newpassword.match(spaceRegex)) {
            $('.changePassword-err').text("Mật khẩu không được chứa khoảng trắng.");
            return false;

        } else if (oldpassword == newpassword) {
            $('.changePassword-err').text("Mật khẩu chưa thay đổi.");
            return false;
        } else {
            $('.changePassword-err').text("");
            var password = {
                userName: username,
                oldPassword: oldpassword,
                newPassword: newpassword
            };
            e.preventDefault();
            $.ajax({
                type: 'POST',
                url: "/api/user/changepassword",
                data: JSON.stringify(password),
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
                        $('.changePassword-err').text("");
                        messageModal('changePassword', 'img/img-success.png', 'Mật khẩu thay đổi thành công!');
                    } else {
                        $('.changePassword-err').text(message);
                    }
                },
                failure: function (errMsg) {
                    messageModal('changePassword', 'img/img-success.png', errMsg);
                },
                dataType: "json",
                contentType: "application/json"
            });
        }
    })
});

$('#changePassword .modal-footer a').click(function () {
    localStorage.clear();
});

/*Check Role has create or not*/
if (roleID == null) {
    $('.changePassword-err').append(`Hãy <a href="login">ĐĂNG NHẬP</a> để có thể thay đổi mật khẩu!`);
    $('#confirm').prop('disabled', true);
}