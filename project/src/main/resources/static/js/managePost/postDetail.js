/*Value default*/
var newsletterId = $('#newsletterId').text();
var request = {
    newsletterId: newsletterId
}

/*View detail post*/
$.ajax({
    url: "/api/newsletter/viewletter",
    type: "POST",
    data: JSON.stringify(request),
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
            if (data.newsletter.length != 0) {
                document.title = '.::' + data.newsletter.header + '::.';
                $('.table-title h2').text(data.newsletter.header);
                $('.table-title .post-date').text(data.newsletter.createDate);
                $('.post-content-text').html(data.newsletter.content);
                $('.post-action .status').text(data.newsletter.status);
                $('.post-action .author').text(data.newsletter.userName);
                var imgContent = $('.post-content-text img');
                imgContent.each(function () {
                    $(this).addClass('lazy');
                    var src = $(this).attr('src')
                    $(this).prop('src', 'http://placehold.it/600x350&text=Chờ ảnh');
                    $(this).prop('srcset', '');
                    $(this).attr('data-original', src);
                })
                lazyLoad();
            } else {
                $('.table-title h2').text('');
                $('.table-title .post-date').text('');
                $('.post-content-text').html(`
                <div class="text-center mb-5">
                    <img class="my-3" src="img/img-error.png" alt="Không tìm thấy bài viết.">
                    <h5>Không tìm thấy bài viết.</h5>
                </div>`);
                $('.post-action').addClass('hide');
            }
            displayButton();
        } else {
            $('.table-title h2').text('');
            $('.table-title .post-date').text('');
            $('.post-content-text').html(`
                <div class="text-center mb-5">
                    <img class="my-3" src="img/img-error.png" alt="Không tìm thấy bài viết.">
                    <h5>` + message + `</h5>
                </div>`);
            $('.post-action').addClass('hide');
        }
    },
    failure: function (errMsg) {
        $('.table-title h2').text('');
        $('.table-title .post-date').text('');
        $('.post-content-text').html(`
        <div class="text-center mb-5">
            <img class="my-3" src="img/img-error.png" alt="Không tìm thấy bài viết.">
            <h5>` + errMsg + `</h5>
        </div>`);
        $('.post-action').addClass('hide');
    },
    dataType: "json",
    contentType: "application/json"
});

/*Display button action*/
function displayButton() {
    var status = $('.post-action .status').text();
    var author = $('.post-action .author').text();
    if (roleID == 1) {
        if (status == 2) {
            $('.btn-delete').removeClass('hide');
            $('.btn-edit').addClass('hide');
            $('.btn-accept').removeClass('hide');
        } else if (status == 0) {
            $('.btn-delete').removeClass('hide');
            $('.btn-edit').removeClass('hide');
            $('.btn-accept').addClass('hide');
        } else if (status == 1) {
            $('.btn-delete').addClass('hide');
            $('.btn-edit').removeClass('hide');
            $('.btn-accept').removeClass('hide');
        }
    } else if (username == author) {
        if (status == 2) {
            $('.btn-delete').removeClass('hide');
            $('.btn-edit').removeClass('hide');
        } else {
            $('.btn-delete').addClass('hide');
            $('.btn-edit').addClass('hide');
        }
    } else {
        $('.btn-delete').addClass('hide');
    }
}

/*Button action*/
$('.btn-delete').on('click', function () {
    $('#confirmModal').modal('show');
    $('#confirmModal .modal-body').html(`
        <img class="my-3" src="img/img-question.png" alt="img-question">
        <h5>Bạn có muốn <b>ẨN</b> bài viết này không?</h5>
    `);
    $('#confirmBtn').on('click', function () {
        $('#confirmModal').modal('hide');
        confirmRequest(1, 'Ẩn bài viết thành công!');
    })
})

$('.btn-edit').on('click', function () {
    sessionStorage.setItem('newsletterIdEdit', newsletterId);
    location.href = 'editPost';
})

$('.btn-reject').on('click', function () {
    confirmRequest(1, 'Ẩn bài đăng thành công!')
});

$('.btn-accept').on('click', function () {
    confirmRequest(0, 'Đăng bài thành công!')
})

function confirmRequest(status, messageSuccess) {
    var request = {
        newsletterId: newsletterId,
        status: status,
    }
    console.log(JSON.stringify(request));
    $.ajax({
        url: "/api/newsletter/confirmnewsletter",
        type: "POST",
        data: JSON.stringify(request),
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
                $('#modalSuccess .modal-footer').html(`<a href="managePost" class="btn btn-primary">ĐÓNG</a>`);
                messageModal('modalSuccess', 'img/img-success.png', messageSuccess)
            } else {
                messageModal('modalSuccess', 'img/img-error.png', message);
            }
        },
        failure: function (errMsg) {
            messageModal('modalSuccess', 'img/img-error.png', errMsg);
        },
        dataType: "json",
        contentType: "application/json"
    });
}