var identifier, fullName, phone, email;
var emailRegex = '^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$';
var phoneRegex = '^[0-9\\-\\+]{9,15}$';

$("#submit").click(function (e) {
    fullName = $('#fullName').val().trim();
    identifier = $('#identifier').val().trim();
    phone = $('#phone').val().trim();
    email = $('#email').val().trim();

    if (identifier == "") {
        $('.createTeacher-err').text("Hãy điền tên định danh.");
        return false;
    } else if (phone != "" && !phone.match(phoneRegex)) {
        $('.createTeacher-err').text("SĐT không đúng định dạng.");
        return false;
    } else if (email != "" && !email.match(emailRegex)) {
        $('.createTeacher-err').text("Email không đúng định dạng.");
        return false;
    } else {
        var teacher = {
            teacherIdentifier: identifier,
            fullName: fullName,
            phone: phone,
            email: email,
        }
        e.preventDefault();
        $.ajax({
            url: '/api/admin/addteacher',
            type: 'POST',
            data: JSON.stringify(teacher),
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
                    $('.createTeacher-err').text("");
                    messageModal('createSuccess', 'img/img-success.png', 'Thêm giáo viên thành công!')
                } else {
                    $('.createTeacher-err').text(message);
                }
            },
            failure: function (errMsg) {
                messageModal('createSuccess', 'img/img-error.png', errMsg);
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
});

/*Check Role has create or not*/
if (roleID != 1) {
    $('.createTeacher-err').text("Bạn không có quyền thêm giáo viên!");
    $('#submit').prop('disabled', true);
}
