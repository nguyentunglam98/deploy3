/*Set value default*/
var violationList = [];
var list = [];
$('#datetime').val(moment().format('YYYY-MM-DD'));
$('#datetime').prop('max', moment().format('YYYY-MM-DD'));

/*Get data in page*/
$.ajax({
    url: '/api/emulation/viewgradingemulation',
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
            if (data.classList.length != 0) {
                $("#classList").select2();
                $("#classList").html("");
                $.each(data.classList, function (i, item) {
                    $('#classList').append(
                        `<option value="` + item.classID + `">` + item.className + `</option>
                    `);
                });
            } else {
                $("#classList").html(`<option value="err">Không có lớp nào.</option>`);
            }
            if (data.vioTypeAndVioList != null) {
                $(".panel-default").html("");
                $.each(data.vioTypeAndVioList, function (i, item) {
                    var dataTarget = "collapse" + item.typeId;
                    $('.panel-default').append(
                        `<div class="panel-heading mt-3" data-toggle="collapse" data-target="#` + dataTarget + `" onclick="toggleClick()">
                             <div class="violationTypeName">
                                <span>
                                    <h5 class="my-0 mr-2 d-inline">Nội quy theo dõi: </h5>
                                    <span>` + item.name + `</span>
                                </span>
                                <div class="d-flex align-items-center">
                                    <h6 class="my-0 mr-2">Điểm:</h6>
                                    <span>` + item.totalGrade + `</span>
                                </div>
                            </div>
                            <button><i class="fa fa-chevron-down rotate up"></i></button>
                        </div>
                        <div class="panel-collapse collapse in" id="` + dataTarget + `">
                            <table class="table table-responsive-customer table-vertical-middle">
                                <thead>
                                    <td></td>
                                    <td>Vi phạm</td>
                                    <td class="text-center">Điểm trừ</td>
                                    <td class="text-center">Số lần</td>
                                    <td class="text-center">Tổng điểm trừ</td>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    `);
                    var dataTable = "#" + dataTarget + " tbody";
                    if (item.violation != null) {
                        $(dataTable).html("");
                        $.each(item.violation, function (i, list) {
                            $(dataTable).append(`
                            <tr>
                                <td style="vertical-align: top">
                                    <span class="custom-checkbox ">
                                        <input id="` + list.violationId + `" type="checkbox" name="options" value="` + list.violationId + `">
                                        <label class="mt-2" for="` + list.violationId + `"></label>
                                    </span>
                                </td>
                                <td>
                                    <div class="form-group my-0 text-left">
                                        <label class="violation-des">` + list.description + `</label>
                                        <input type="text" class="form-control text-red violation-note" placeholder="Ghi chú...">
                                    </div>
                                </td>
                                <td class="substract">` + list.substractGrade + `</td>
                                <td>
                                    <div class="quantity d-flex">
                                        <button class="btn-decrease"><i class="fa fa-minus" aria-hidden="true"></i></button>
                                        <input class="quantity-input" type="number" value="0" min="0">
                                        <button class="btn-increase"><i class="fa fa-plus" aria-hidden="true"></i></button>
                                    </div>
                                </td>
                                <td class="total">0</td>
                            </tr>`);
                        });
                    } else {
                        $(dataTable).html(`<tr><td colspan="4" class="userlist-result">Danh sách lỗi vi phạm trống.</td></tr>`);
                    }
                });
                selectCheckbox();
                getClass();
                increaseBtn();
                decreaseBtn();
            } else {
                $(".panel-default").html(`<h5>Không có lỗi vi phạm.</h5>`);
            }
        } else {
            $(".panel-default").html(`<h5>` + message + `</h5>`);
        }
    },
    failure: function (errMsg) {
        $(".panel-default").html(`<h5>` + errMsg + `</h5>`);
    },
    dataType: "json",
    contentType: "application/json"
});

/*Save Grading*/
$('#saveGrading').on('click', function () {
    var classId = $('#classList option:selected').val();
    getValueCheckbox();
    if (violationList.length != 0) {
        for (var i = 0; i < violationList.length; i++) {
            if (classId == "err") {
                messageModal('saveSuccess', "/img/img-error.png", "Hãy chọn lớp.");
                return false;
            } else if (violationList[i].note == "") {
                messageModal('saveSuccess', "/img/img-error.png", "Hãy điền đủ ghi chú cho các lỗi.");
                return false;
            } else {
                $('#confirmModal').modal('show');
                $('#confirmModal .modal-body').html('');
                $('#confirmModal .modal-body').append(`
                    <table class="table table-bordered">
                        <thead class="bg-light text-center">
                            <td>Mô tả lỗi</td>
                            <td>Điểm trừ</td>
                            <td>Số lần</td>
                            <td>Tổng điểm trừ</td>
                        </thead>
                        <tbody></tbody>
                    </table>
                `);
                for (var i = 0; i < violationList.length; i++) {
                    var total = parseFloat(violationList[i].substractGrade) * parseInt(violationList[i].quantity);
                    total = total.toFixed(1);
                    var description;
                    $('table tbody input[type="checkbox"]').each(function () {
                        if ($(this).val() == violationList[i].violationId) {
                            description = $(this).closest('td').parent().find('.violation-des').text();
                        }
                    });
                    $('#confirmModal .modal-body tbody').append(`
                        <tr>
                            <td>
                                <p class="mb-0">` + description + `</p>
                                <small class="text-red">` + violationList[i].note + `</small>
                            </td>
                            <td class="text-center">` + violationList[i].substractGrade + `</td>
                            <td class="text-center">` + violationList[i].quantity + `</td>
                            <td class="text-center">` + total + `</td>
                        </tr>                    
                    `);
                }
                $('#confirm').unbind().on('click', function () {
                    var infoSave = {
                        username: username,
                        classId: classId,
                        date: $('#datetime').val(),
                        roleId: roleID,
                        yearId: localStorage.getItem('currentYearId'),
                        violationList: violationList
                    }
                    console.log(JSON.stringify(infoSave));
                    $.ajax({
                        url: '/api/emulation/addgrademulation',
                        type: 'POST',
                        data: JSON.stringify(infoSave),
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
                                messageModal('saveSuccess', "/img/img-success.png", "Thông tin đã lưu thành công!");
                                $('#saveSuccess .modal-footer').html(`
                                    <a href="violationListOfClass" class="btn btn-danger">XEM VI PHẠM</a> 
                                    <a href="gradingToEmulation" class="btn btn-primary">ĐÓNG</a> `);
                                sessionStorage.setItem('classIdGrading', classId);
                                sessionStorage.setItem('dateGrading', $('#datetime').val());
                            } else {
                                messageModal('saveSuccess', "/img/img-error.png", message);
                            }
                        },
                        failure: function (errMsg) {
                            messageModal('saveSuccess', "/img/img-error.png", errMsg);
                        },
                        dataType: "json",
                        contentType: "application/json"
                    });
                })
            }
        }
    } else {
        messageModal('saveSuccess', "/img/img-error.png", 'Hãy chọn lỗi.');
    }
})

/*Get value checkbox*/
function getValueCheckbox() {
    if (list.length == 0) {
        violationList = [];
    }
    violationList = [];
    $('table tbody input[type="checkbox"]').each(function () {
        for (var i = 0; i < list.length; i++) {
            if ($(this).val() == list[i]) {
                var quantity = $(this).closest('td').parent().find('.quantity-input').val();
                if (quantity != "0") {
                    violationList.push({
                        violationId: $(this).val(),
                        quantity: $(this).closest('td').parent().find('.quantity-input').val(),
                        substractGrade: $(this).closest('td').parent().find('.substract').text(),
                        note: $(this).closest('td').parent().find('.violation-note').val()
                    });
                } else {
                    var removeItem = $(this).val();
                    violationList = $.grep(violationList, function (value) {
                        return value.violationId != removeItem;
                    });
                }
            }
        }
    });
};

/*Set class for red star*/
function getClass() {
    if (roleID == 3) {
        $('#datetime').prop('disabled', true);
        $('#classList').prop('disabled', true);
        var request = {
            username: username,
            applyDate: $('#datetime').val()
        }
        console.log(JSON.stringify(request))
        $.ajax({
            url: '/api/emulation/getClassIdOfRedStar',
            type: 'POST',
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
                    var classID = data.currentClassId;
                    if (classID != null) {
                        $("#classList").select2().val(classID).trigger('change');
                    }

                } else {
                    $("#classList option").remove();
                    $("#classList").append(`<option value="err" selected>` + message + `</option>`);
                }
            },
            failure: function (errMsg) {
                $("#classList option").remove();
                $("#classList").append(`<option value="err" selected>` + errMsg + `</option>`);
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
}

/*Button Increase*/
function increaseBtn() {
    $('.btn-increase').on('click', function () {
        var $qty = $(this).closest('div').find('.quantity-input');
        var currentVal = parseInt($qty.val());
        if (!isNaN(currentVal)) {
            $qty.val(currentVal + 1);
        }
        var $substract = $(this).closest('td').parent().find('.substract');
        var $total = $(this).closest('td').parent().find('.total');
        var total = parseFloat($substract.text()) * parseInt($qty.val());
        $total.text(total.toFixed(1));
    });
}

/*Button Decrease*/
function decreaseBtn() {
    $('.btn-decrease').on('click', function () {
        var $qty = $(this).closest('div').find('.quantity-input');
        var currentVal = parseInt($qty.val());
        if (!isNaN(currentVal) && currentVal > 0) {
            $qty.val(currentVal - 1);
        }
        var $substract = $(this).closest('td').parent().find('.substract');
        var $total = $(this).closest('td').parent().find('.total');
        var total = parseFloat($substract.text()) * parseInt($qty.val());
        $total.text(total.toFixed(2));
    });
}

/*Set role*/
if (roleID != 1 && roleID != 3 && roleID != 5) {
    $('#classList').prop('disabled', true);
    $('#datetime').prop('disabled', true);
    $('#saveGrading').prop('disabled', true);
    $('.gradingToEmulation-err').text('Bạn không có quyền chấm điểm thi đua!');
}

