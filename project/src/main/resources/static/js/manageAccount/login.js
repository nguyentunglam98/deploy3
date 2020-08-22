<!--Send data to JSON-->
$(document).ready(function () {
    $("#signin").click(function (e) {
        login();
    })
    $('input').keypress(function (e) {
        if (e.which == 13) {
            login();
            return false;
        }
    });
});

function login() {
    $('.login-err').text("");
    var username = $('#username').val().trim();
    var password = $('#password').val().trim();

    if (username == "" && password == "") {
        $('.login-err').text("Hãy điền tên đăng nhập và mật khẩu.");
        return false;
    } else if (username == "") {
        $('.login-err').text("Hãy điền tên đăng nhập.");
        return false;
    } else if (password == "") {
        $('.login-err').text("Hãy điền mật khẩu.");
        return false;
    } else {
        $('#error-username').text("");
        $('#error-password').text("");
        var user = {
            username: $('#username').val(),
            password: $('#password').val(),
        };
        console.log(JSON.stringify(user))
        $.ajax({
            type: 'POST',
            url: "/api/user/login",
            data: JSON.stringify(user),
            beforeSend: function () {
                $('body').addClass("loading")
            },
            complete: function () {
                $('body').removeClass("loading")
            },
            success: function (data) {
                var messageCode = data.message.messageCode;
                var message = data.message.message;
                var roleID = data.roleid;
                if (messageCode == 0) {
                    localStorage.setItem("username", username);
                    localStorage.setItem("loginSuccess", messageCode);
                    localStorage.setItem("roleID", roleID);
                    localStorage.setItem("currentYearId", data.currentYearId);
                    localStorage.setItem("accessToken", data.accessToken);
                    localStorage.setItem("asignedClass", data.asignedClass);
                    $("#loginSuccess-menu").addClass("show");
                    $('#login').css('display', 'none');
                    if (roleID == 1) {
                        $("#admin").addClass("show");
                    }
                    setTimeout(window.location.replace("/"),500);
                } else {
                    $('.login-err').text(message);
                }
            },
            failure: function (errMsg) {
                $('.login-err').text(errMsg);
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
}

/*Check Role has create or not*/
if (roleID != null) {
    $('fieldset').html(`<h5 class="text-red">Bạn đã đăng nhập!</h5>`);
}