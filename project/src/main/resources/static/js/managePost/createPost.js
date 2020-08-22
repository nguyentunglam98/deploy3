/*Valude default*/
var roleID = localStorage.getItem("roleID");
var username = localStorage.getItem("username");
var editor = CKEDITOR.replace('post-editor-text-content', {
    cloudServices_uploadUrl: 'https://73999.cke-cs.com/easyimage/upload/',
    cloudServices_tokenUrl: 'https://73999.cke-cs.com/token/dev/26d99879d9d20ba5d60497fc1556aa7f821ea78b8cc4c0df6f9f056e7b4e',
    width: '100%',
    height: 500,
    extraPlugins: 'easyimage',
});
var imageCover = CKEDITOR.replace('imageCover', {
    cloudServices_uploadUrl: 'https://73999.cke-cs.com/easyimage/upload/',
    cloudServices_tokenUrl: 'https://73999.cke-cs.com/token/dev/26d99879d9d20ba5d60497fc1556aa7f821ea78b8cc4c0df6f9f056e7b4e',
    width: 250,
    height: 200,
    extraPlugins: 'easyimage',
    removePlugins: 'image',
    removeDialogTabs: 'link:advanced',
    toolbar: [
        {
            name: 'insert',
            items: ['EasyImageUpload']
        }
    ],
});

if (roleID == 1) {
    $('.form-check').removeClass('hide');
}

/*Save button*/
$('#savePost').on('click', function () {
    var titleName = $('#titleName').val().trim();
    // var image = $('#imagePreview').attr('src');
    var image = imageCover.getData();
    var data = editor.getData();
    var gim;
    if ($('input[type="checkbox"]').prop("checked") == true) {
        gim = 1;
    } else {
        gim = 0;
    }
    if (titleName == "") {
        $('.createPost-err').text('Hãy nhập tiêu đề của bài viết.');
        return false;
    } else if (image.trim() == "") {
        $('.createPost-err').text('Hãy nhập ảnh bìa của bài viết.');
        return false;
    } else if (data == "") {
        $('.createPost-err').text('Hãy nhập nội dung của bài viết.');
        return false;
    } else {
        image = image.split('src=')[1].split('"')[1];
        console.log(image);
        var request = {
            username: username,
            header: titleName,
            headerImage: image,
            content: data,
            gim: gim,
            roleId: roleID,
        }
        if (gim == 1) {
            request.gim = 0;
            $('#saveModal .modal-footer').html(`
                    <input type="button" class="btn btn-danger" id="newGim" value="XÁC NHẬN">
                    <input type="button" class="btn btn-primary" id="closeBtn" data-dismiss="modal" value="ĐÓNG">
                `);
            messageModal('saveModal', 'img/img-question.png', 'Bạn có muốn <b>GHIM</b> bài viết này không?<h6>Bài viết được ghim trước đó sẽ bị bỏ ghim!</h6>');
            $('#newGim').on('click', function () {
                request.gim = 1;
                addNewPost(request);
            })
        } else {
            addNewPost(request);
        }
    }
})

function addNewPost(request) {
    console.log(JSON.stringify(request))
    $.ajax({
        url: "/api/newsletter/addnewsletter",
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
                $('#saveModal .modal-footer').html(`
                    <a href="postDetail?id=` + data.newsletterId + `" class="btn btn-danger" id="viewPost">XEM BÀI VIẾT</a>
                    <a href="createPost" class="btn btn-primary">ĐÓNG</a>
                `)
                messageModal('saveModal', 'img/img-success.png', "Tạo bài viết thành công!");
            } else {
                messageModal('saveModal', 'img/img-error.png', message);
            }
        },
        failure: function (errMsg) {
            messageModal('saveModal', 'img/img-error.png', errMsg);
        },
        dataType: "json",
        contentType: "application/json"
    });
}

/*Upload image*/
var loadFile = function (event) {
    var form = $('#form-post');
    var formData = new FormData(form[0]);
    // formData.append('ckCsrfToken', 'xaTDx3QM1m3tTc3Uk4OYgkqgd0g1ZtcEbfhQod2a7s2rxMSzf9RhlXjMJVSV');
    var CSTimestamp = Date.now();
    const apiSecret = 'xaTDx3QM1m3tTc3Uk4OYgkqgd0g1ZtcEbfhQod2a7s2rxMSzf9RhlXjMJVSV';
    $.ajax({
        type: "POST",
        url: "https://73999.cke-cs.com/api/v4/RQAfsTJxHLsH61eo6z1M/editors",
        data: formData,
        async: false,
        headers: {
            'X-CS-Signature': generateSignature(apiSecret, "POST", "https://73999.cke-cs.com/api/v4/RQAfsTJxHLsH61eo6z1M/editors", CSTimestamp, {
                bundle: formData,
                config: {
                    cloudServices: "2019-11-30-build-f4a4c2"
                }
            }),
            'X-CS-Timestamp': CSTimestamp
        },
        beforeSend: function () {
            $('body').addClass("loading")
        },
        complete: function (resp) {
            $('body').removeClass("loading");
            console.log(resp);
            // temp1[0].object["a"].authorization
        },
        success: function (data) {
            $('#imagePreview').prop('src', data.default);
            $('#imagePreview').prop('alt', 'Ảnh bìa bài viết');
            console.log(data.default);
        },
        failure: function (errMsg) {
            messageModal('overrideSuccess', 'img/img-error.png', errMsg);
        },
        cache: false,
        contentType: false,
        processData: false,
    });
};

function generateSignature(apiSecret, method, uri, timestamp, body) {
    const url = url.parse(uri).path;

    const hmac = crypto.createHmac('SHA256', apiSecret);

    hmac.update(`${method.toUpperCase()}${url}${timestamp}`);

    if (body) {
        hmac.update(Buffer.from(JSON.stringify(body)));
    }

    return hmac.digest('hex');
}
