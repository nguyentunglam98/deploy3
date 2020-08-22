var inforSearch = {
    status: "",
    createDate: "",
    userName: "",
    pageNumber: 0
}
if (roleID != 1) {
    inforSearch.userName = username;
    $('#createBy').val(username);
    $('#createBy').prop('disabled', true);
}

search();

$('#search').on('click', function () {
    $('.panel-default').html('');
    search();
});

/*Load Homepage*/
function search() {
    inforSearch.status = $('#status option:selected').val();
    inforSearch.createDate = $('#inputDate').val();
    inforSearch.userName = $('#createBy').val();
    console.log(JSON.stringify(inforSearch));
    $.ajax({
        url: "/api/newsletter/searchconfirmnews",
        type: "POST",
        data: JSON.stringify(inforSearch),
        beforeSend: function () {
            $('body').addClass("loading")
        },
        complete: function () {
            $('body').removeClass("loading")
        },
        success: function (data) {
            var messageCode = data.message.messageCode;
            var message = data.message.message;
            $('.table-paging').html('');
            $('.panel-default').html('');
            if (messageCode == 0) {
                if (data.listLetter != null) {
                    var totalPages = data.listLetter.totalPages;
                    paging(inforSearch, totalPages);
                    if (data.listLetter.content.length != 0) {
                        $.each(data.listLetter.content, function (i, item) {
                            var status = item.status;
                            if (status == 1) {
                                status = "Đã ẩn";
                            } else if (status == 2) {
                                status = "Chờ duyệt";
                            } else if (status == 0) {
                                status = "Đã đăng";
                            }
                            $('.panel-default').append(`
                            <div class="panel-post mb-3">
                                <a href="postDetail?id=` + item.newsletterId + `" id="` + item.newsletterId + `">
                                    <div class="panel-post-content">
                                        <div class="post-img">
                                            <img class="lazy" data-original="` + item.headerImage + `">
                                        </div>
                                        <div class="post-description">
                                            <div class="post-title">` + item.header + `</div>
                                            <div class="post-date">` + item.createDate + `</div>
                                            <div class="post-shortDes"><span class="font-500">Tạo bởi: </span>` + item.userName + `</div>
                                            <div class="post-status font-500">Trạng thái: <span class="badge">` + status + `</span></div>
                                        </div>
                                    </div>
                                </a>
                            </div>`);
                            if (status == "Chờ duyệt") {
                                $('#' + item.newsletterId).find('.post-status .badge').addClass('badge-danger');
                            }
                            if (status == "Đã đăng") {
                                $('#' + item.newsletterId).find('.post-status .badge').addClass('badge-success');
                            }
                            if (status == "Đã ẩn") {
                                $('#' + item.newsletterId).find('.post-status .badge').addClass('badge-secondary');
                            }
                        });
                    } else {
                        $('.panel-default').html('<h3 class="text-center">Danh sách bài viết trống.</h3>');
                    }
                    pagingClick();
                    lazyLoad();
                } else {
                    $('.panel-default').html('<h3 class="text-center">Danh sách bài viết trống.</h3>');
                }
            } else {
                $('.panel-default').html(`<h3 class="text-center">` + message + `</h3>`);
            }
        },
        failure: function (errMsg) {
            $('.panel-default').html(`<h3 class="text-center">` + errMsg + `</h3>`);
        },
        dataType: "json",
        contentType: "application/json"
    });
}

