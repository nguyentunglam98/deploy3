var giftedClassName;

// click button summit to create gifted class name
$("#submit").click(function (e) {
    giftedClassName = $('#giftedClassName').val().trim();
    if (giftedClassName == "") {
        $('.giftedClassName-err').text("Hãy nhập tên hệ chuyên !");
        return false;
    } else {
        $('.giftedClassName-err').text("");
        var addGiftedClass = {
            giftedClassName: giftedClassName
        }
        e.preventDefault();
        $.ajax({
            url: '/api/admin/addgifftedclass',
            type: 'POST',
            data: JSON.stringify(addGiftedClass),
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
                    $('#createSuccess .modal-footer').html(`
                        <a type="button" class="btn btn-danger" href="createClass">TẠO LỚP</a>
                        <a type="button" class="btn btn-primary" href="createGifftedClass">ĐÓNG</a>
                    `);
                    messageModal('createSuccess', 'img/img-success.png', 'Tạo hệ chuyên thành công!')
                } else {
                    $('#createSuccess .modal-footer').html(`<input type="button" class="btn btn-primary" data-dismiss="modal" value="ĐÓNG"/>`);
                    messageModal('createSuccess', 'img/img-error.png', message)
                }
            },
            failure: function (errMsg) {
                $('#createSuccess .modal-footer').html(`<input type="button" class="btn btn-primary" data-dismiss="modal" value="ĐÓNG"/>`);
                messageModal('createSuccess', 'img/img-error.png', errMsg)
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
});

/*Check Role has create or not*/
if (roleID != 1) {
    $('.giftedClassName-err').text("Bạn không có quyền thêm hệ chuyên!");
    $('#submit').prop('disabled', true);
}