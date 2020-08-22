$('#inputDate').prop('max', moment().format('YYYY-MM-DD'));
var inforSearch = {
    classId: null,
    status: 0,
    createDate: $('#inputDate').val(),
    createBy: $('#createBy').val(),
    pageNumber: 0
}

/*Search button*/
$("#search").click(function () {
    var classId, status, createDate, createBy;
    createDate = $('#inputDate').val();
    createBy = $('#createBy').val().trim();
    if ($('#classList option:selected').val() == null || $('#classList option:selected').val() == "" || $('#classList option:selected').val() == 0) {
        classId = null;
    } else {
        classId = $('#classList option:selected').val();
    }
    if ($('#status option:selected').val() == null || $('#status option:selected').val() == 3 || $('#status option:selected').val() == "") {
        status = 3;
    } else {
        status = $('#status option:selected').val();
    }
    inforSearch = {
        classId: classId,
        status: status,
        createDate: createDate,
        createBy: createBy,
        pageNumber: 0
    }
    console.log(JSON.stringify(inforSearch))
    $(".panel-default").html("");
    search();

});

/*Set data classList to combobox*/
$.ajax({
    url: '/api/admin/classlist',
    type: 'POST',
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
            if (data.classList != null) {
                $("#classList").select2();
                $("#classList").html(`<option value="0" selected>Tất cả</option>`);
                $.each(data.classList, function (i, item) {
                    $('#classList').append(
                        `<option value="` + item.classID + `">` + item.className + `</option>
                    `);
                });
            } else {
                $("#classList").html(`<option selected>` + message + `</option>`);
            }
        } else {
            $("#classList").html(`<option selected>` + message + `</option>`);
        }
    },
    failure: function (errMsg) {
        $("#classList").html(`<option selected>` + errMsg + `</option>`);
    },
    dataType: "json",
    contentType: "application/json"
});

search();

/*Set data to container*/
function search() {
    if (roleID == 4) {
        inforSearch.createBy = username;
        $('#createBy').val(username);
        $('#createBy').prop('disabled', true);
    }
    console.log(JSON.stringify(inforSearch))
    $.ajax({
        url: '/api/emulation/viewrequest',
        type: 'POST',
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
            if (messageCode == 0) {
                var totalPages = data.totalPage;
                if (totalPages > 1) {
                    $('.table-paging').removeClass('hide');
                    $('.table-paging').html('');
                    paging(inforSearch, totalPages);
                } else {
                    $('.table-paging').addClass('hide');
                }
                if (data.viewViolationClassList.length != 0) {
                    $(".panel-default").html("");
                    $.each(data.viewViolationClassList, function (i, item) {
                        if (item.violationClassRequest != null) {
                            var status, requestId, dataTarget, createBy, totals, quantityNew,
                                totalsNew, reason;
                            var violationDate = item.dayName + " - " + convertDate(item.createDate, '/');
                            var substractGrade = item.substractGrade;
                            var quantity = item.violationClassRequest.quantityOld;
                            totals = parseFloat(parseFloat(substractGrade) * parseInt(quantity)).toFixed(1);
                            requestId = item.violationClassRequest.requestId;
                            dataTarget = "collapse" + requestId;
                            createBy = item.violationClassRequest.createBy;
                            quantityNew = item.violationClassRequest.quantityNew;
                            totalsNew = parseFloat(parseFloat(substractGrade) * parseInt(quantityNew)).toFixed(1);
                            reason = item.violationClassRequest.reason;
                            if (item.violationClassRequest.status == 0) {
                                status = "Chưa duyệt";
                            } else if (item.violationClassRequest.status == 2) {
                                status = "Chấp nhận";
                            } else if (item.violationClassRequest.status == 1) {
                                status = "Từ chối";
                            }
                            $('.panel-default').append(`
                                <div class="panel-heading mt-3 mb-0" data-toggle="collapse" data-target="#` + dataTarget + `" onclick="toggleClick()">
                                    <div class="w-100">
                                        <div class="violation-date violationTypeName">
                                            <span>` + violationDate + `</span>
                                        </div>
                                        <div class="violation-class violationTypeName">
                                            <span class="font-500">Lớp: </span>
                                            <span>` + item.className + `</span>
                                        </div>
                                        <div class="violation-details violationTypeName">
                                            <span class="font-500">Mô tả lỗi: </span>
                                            <span>` + item.description + `</span>
                                        </div>
                                        <div class="violation-author violationTypeName">
                                            <span class="font-500">Người yêu cầu thay đổi: </span>
                                            <span>` + createBy + `</span>
                                        </div>
                                        <div class="violation-status violationTypeName">
                                            <span class="font-500">Trạng thái: </span>
                                            <span class="badge">` + status + `</span>
                                        </div>
                                    </div>
                                    <button class="violation-btn"><i class="fa fa-chevron-down rotate up"></i></button>
                                </div>

                                <div class="panel-collapse collapse" id="` + dataTarget + `">
                                    <div class="modal-body text-center mx-auto">
                                        <div class="panel-body">
                                            <div class="col-md-6 px-0 d-inline-block">
                                                <div class="violationName">
                                                    <span class="title">Mô tả lỗi: </span>
                                                    <span class="info ml-4">` + item.description + `</span>
                                                </div>
                                                <div class="note">
                                                    <span class="title">Ghi chú: </span>
                                                    <span class="info ml-4">` + item.note + `</span>
                                                </div>
                                                <div class="reason-change">
                                                    <span class="title">Lý do thay đổi: </span>
                                                    <span class="info ml-4">` + reason + `</span>
                                                </div>
                                            </div>
                                            <div class="d-inline-block">
                                                <div class="substract-grade">
                                                    <span class="title">Điểm trừ: </span>
                                                    <span class="info ml-4">` + substractGrade + `</span>
                                                </div>
                                                <div class="quantity">
                                                    <span class="title">Số lần: </span>
                                                    <span class="info ml-4">` + quantity + ` -> <span class="text-red font-500">` + quantityNew + `</span></span>
                                                </div>
                                                <div class="totals">
                                                    <span class="title">Tổng điểm trừ: </span>
                                                    <span class="info ml-4">` + totals + ` -> <span class="text-red font-500">` + totalsNew + `</span></span>
                                                </div>
                                            </div>
                                            <div class="hide violationClassId">` + item.violationClassId + `</div>
                                            <div class="col-md-12 text-right mt-4">
                                                <input type="submit" name="` + requestId + `" class="btn btn-primary accept-request my-1" data-toggle="modal" value="XÁC NHẬN">
                                                <input type="button" name="` + requestId + `" class="btn btn-danger ml-3 reject-request my-1" data-toggle="modal" value="TỪ CHỐI">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            `);
                            if (status == "Chấp nhận" || status == "Từ chối" || roleID != 1) {
                                var dataTargetID = "#" + dataTarget;
                                $(dataTargetID).find('.accept-request').parent().addClass('hide');
                            }
                            if (status == "Từ chối") {
                                var dataTargetID = "#" + dataTarget;
                                $(dataTargetID).find('.quantity').html(`
                                    <span class="title">Số lần: </span>
                                    <span class="info ml-4">` + quantity
                                );
                                $(dataTargetID).find('.totals').html(`
                                    <span class="title">Tổng điểm trừ: </span>
                                    <span class="info ml-4">` + totals
                                );
                            }
                            if (status == "Chưa duyệt") {
                                var dataTargetID = "#" + dataTarget;
                                // $(dataTargetID).prev().find('.violation-status span:last-child').css('color', '#ff0000');
                                $(dataTargetID).prev().find('.violation-status .badge').addClass('badge-danger');
                            }
                            if (status == "Chấp nhận") {
                                var dataTargetID = "#" + dataTarget;
                                // $(dataTargetID).prev().find('.violation-status span:last-child').css('color', '#339933');
                                $(dataTargetID).prev().find('.violation-status .badge').addClass('badge-success');

                            }
                            if (status == "Từ chối") {
                                var dataTargetID = "#" + dataTarget;
                                // $(dataTargetID).prev().find('.violation-status span:last-child').css('color', '#808080');
                                $(dataTargetID).prev().find('.violation-status .badge').addClass('badge-secondary');
                            }
                        }
                    });

                } else {
                    $(".panel-default").html(`<h3 class="text-center mt-3">Danh sách yêu cầu trống.</h3>`);
                }
            } else {
                $(".panel-default").html(`<h3 class="text-center mt-3">` + message + `</h3>`);
            }
            acceptRequest();
            rejectRequest();
            pagingClick();
        },
        failure: function (errMsg) {
            $(".panel-default").html(`<h3 class="text-center mt-3">` + errMsg + `</h3>`);
        },
        dataType: "json",
        contentType: "application/json"
    });
}

/*Accept Request*/
function acceptRequest() {
    $('.accept-request').on('click', function () {
        $('#confirmModal').modal('show');
        $('#confirmModal .modal-body').html(`
            <img class="mb-3 mt-3" src="img/img-question.png"/>
            <h5>Bạn có muốn <b>CHẤP NHẬN</b> yêu cầu này không?</h5>
        `);
        var violationClassId = $(this).parent().parent().find('.violationClassId').text();
        var requestId = $(this).prop('name')
        $('#confirmBtn').on('click', function () {
            $('#confirmModal').modal('hide');
            var accept = {
                violationClassId: violationClassId,
                requestId: requestId,
                userName: username,
            }
            console.log(JSON.stringify(accept));
            $.ajax({
                url: '/api/emulation/acceptrequest',
                type: 'POST',
                data: JSON.stringify(accept),
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
                        $('#modalSuccess').modal('show');
                        $('#modalSuccess .modal-body').html(`
                        <img class="mb-3 mt-3" src="/img/img-success.png"/>
                        <h5>` + message + `</h5>
                    `);
                    } else {
                        $('#modalSuccess').modal('show');
                        $('#modalSuccess .modal-body').html(`
                        <img class="mb-3 mt-3" src="/img/img-error.png"/>
                        <h5>` + message + `</h5>
                    `);
                    }
                },
                failure: function (errMsg) {
                    $('#modalSuccess').modal('show');
                    $('#modalSuccess .modal-body').html(`
                    <img class="mb-3 mt-3" src="/img/img-error.png"/>
                    <h5>` + errMsg + `</h5>
                `);
                },
                dataType: "json",
                contentType: "application/json"
            });
        })

    });
}

/*Reject Request*/
function rejectRequest() {
    $('.reject-request').on('click', function () {
        $('#confirmModal').modal('show');
        $('#confirmModal .modal-body').html(`
            <img class="mb-3 mt-3" src="img/img-question.png"/>
            <h5>Bạn có muốn <b>TỪ CHỐI</b> yêu cầu này không?</h5>
        `);
        var violationClassId = $(this).parent().parent().find('.violationClassId').text();
        var requestId = $(this).prop('name');
        $('#confirmBtn').on('click', function () {
            $('#confirmModal').modal('hide');
            var accept = {
                violationClassId: violationClassId,
                requestId: requestId,
                userName: username,
            }
            console.log(JSON.stringify(accept));
            $.ajax({
                url: '/api/emulation/rejectrequest',
                type: 'POST',
                data: JSON.stringify(accept),
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
                        $('#modalSuccess').modal('show');
                        $('#modalSuccess .modal-body').html(`
                        <img class="mb-3 mt-3" src="/img/img-success.png"/>
                        <h5>` + message + `</h5>
                    `);
                    } else {
                        $('#modalSuccess').modal('show');
                        $('#modalSuccess .modal-body').html(`
                        <img class="mb-3 mt-3" src="/img/img-error.png"/>
                        <h5>` + message + `</h5>
                    `);
                    }
                },
                failure: function (errMsg) {
                    $('#modalSuccess').modal('show');
                    $('#modalSuccess .modal-body').html(`
                    <img class="mb-3 mt-3" src="/img/img-error.png"/>
                    <h5>` + errMsg + `</h5>
                `);
                },
                dataType: "json",
                contentType: "application/json"
            });
        });

    });
}

$('.btn-reload').on('click', function () {
    var classId, status, createDate;
    createDate = $('#inputDate').val();
    if ($('#classList option:selected').val() == null || $('#classList option:selected').val() == "" || $('#classList option:selected').val() == 0) {
        classId = null;
    } else {
        classId = $('#classList option:selected').val();
    }
    if ($('#status option:selected').val() == null || $('#status option:selected').val() == 3 || $('#status option:selected').val() == "") {
        status = 3;
    } else {
        status = $('#status option:selected').val();
    }
    inforSearch = {
        classId: classId,
        status: status,
        createDate: createDate,
        pageNumber: 0
    }
    console.log(JSON.stringify(inforSearch))
    $(".panel-default").html("");
    search();
})