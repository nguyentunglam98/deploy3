var giftedClassId;

$(document).ready(function () {
    $.ajax({
        url: '/api/admin/giftedclasslist',
        type: 'POST',
        success: function (data) {
            $.each(data.giftedClassList, function (i, item) {
                $('#gifftedClass').append(`<option value="` + item.giftedClassId + `">` + item.name + `</option>`);
            });
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
        dataType: "json",
        contentType: "application/json"
    });
});

//change value of combobox
function changeSelected() {
    giftedClassId = $('#gifftedClass option:selected').val();
}

$('#deleteGC').click(function (e) {
    if (giftedClassId == null || giftedClassId == 0) {
        $('.giftedClass-err').text("Hãy chọn hệ chuyên để xóa!");
    } else {
        $('.giftedClass-err').text("");
        messageModal('deleteGifftedClassModal', 'img/img-question.png', 'Bạn có chắc muốn <b>XÓA</b> hệ chuyên này không?')
        $("#deleteGifftedClass").click(function (e) {
            deleteGifted(e);
        });
    }
});

//edit gifted class function
function deleteGifted(e) {
    $('.giftedClass-err').text("");
    var giftedClass = {
        giftedClassId: giftedClassId
    }
    e.preventDefault();
    $.ajax({
        url: '/api/admin/deletegiftedclass',
        type: 'POST',
        data: JSON.stringify(giftedClass),
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
                $("#deleteSuccess .modal-footer input").addClass('hide');
                $("#deleteSuccess .modal-footer a").removeClass('hide');
                messageModal('deleteSuccess', 'img/img-success.png', 'Xoá hệ chuyên thành công!')
            } else {
                $("#deleteSuccess .modal-footer input").removeClass('hide');
                $("#deleteSuccess .modal-footer a").addClass('hide');
                messageModal('deleteSuccess', 'img/img-error.png', message);
            }
        },
        failure: function (errMsg) {
            $("#deleteSuccess .modal-footer input").removeClass('hide');
            $("#deleteSuccess .modal-footer a").addClass('hide');
            messageModal('deleteSuccess', 'img/img-error.png', errMsg);
        },
        dataType: "json",
        contentType: "application/json"
    });
}

/*Check Role has create or not*/
if (roleID != 1) {
    $('.giftedClass-err').text("Bạn không có quyền xóa hệ chuyên!");
    $('#deleteGC').prop('disabled', true);
}