/*Value defaul*/
$('#datetime').val(moment().format('YYYY-MM-DD'));
var classId, date;
if (sessionStorage.getItem('classIdGrading') == null) {
    classId = 1;
} else {
    classId = sessionStorage.getItem('classIdGrading');
}
if (sessionStorage.getItem('dateGrading') == null) {
    date = moment().format('YYYY-MM-DD');
    $('#datetime').val(date);
} else {
    date = sessionStorage.getItem('dateGrading');
    $('#datetime').val(date);
}
var infoSearch = {
    username: username,
    classId: classId,
    date: date,
    roleId: roleID
}
var editViolation = "";

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
                $("#classList").html('');
                $.each(data.classList, function (i, item) {
                    if (classId != null) {
                        if (item.classID == classId) {
                            $('#classList').append(`<option value="` + item.classID + `" selected="selected">` + item.className + `</option>`);
                        } else {
                            $('#classList').append(`<option value="` + item.classID + `">` + item.className + `</option>`);
                        }
                    } else {
                        if (item.classID == 1) {
                            $('#classList').append(`<option value="` + item.classID + `" selected="selected">` + item.className + `</option>`);
                        } else {
                            $('#classList').append(`<option value="` + item.classID + `">` + item.className + `</option>`);
                        }
                    }
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

/*Search button*/
$("#search").click(function () {
    date = $('#datetime').val();
    if ($('#classList option:selected').val() == null || $('#classList option:selected').val() == "") {
        classId = "";
    } else {
        classId = $('#classList option:selected').val();
    }
    infoSearch = {
        username: username,
        classId: classId,
        date: date,
        roleId: roleID
    }
    console.log(JSON.stringify(infoSearch))
    $(".violation-by-date").html("");
    search();
});

search();

/*Set data to container*/
function search() {
    $.ajax({
        url: '/api/emulation/viewviolationofclass',
        type: 'POST',
        data: JSON.stringify(infoSearch),
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
                if (data.viewViolationClassList != null) {
                    $(".violation-by-date").html("");
                    $.each(data.viewViolationClassList, function (i, item) {
                        var violationClassId, requestId, substractGrade, createBy, totals, note, history, checkHistory,
                            reason, quantityNew;
                        var classId = item.classId;
                        var className = item.className;
                        var createDate = item.createDate;
                        var violationDate = item.dayName + " - " + convertDate(createDate, '/');
                        var description = item.description;
                        var quantity = item.quantity;
                        var checkEdit = item.checkEdit;
                        if (item.note == null || item.note == "") {
                            note = "";
                        } else {
                            note = item.note;
                        }
                        if (item.history == null || item.history == "") {
                            history = null;
                            checkHistory = 0;
                        } else {
                            history = item.history;
                            checkHistory = 1;
                        }
                        if (item.violationClassRequest == null) {
                            violationClassId = item.violationClassId;
                            createBy = item.createBy;
                            substractGrade = item.substractGrade;
                            totals = parseFloat(parseFloat(substractGrade) * parseInt(quantity)).toFixed(1);
                            reason = "";
                            quantityNew = null;
                        } else {
                            requestId = item.violationClassRequest.requestId;
                            violationClassId = item.violationClassRequest.violationClassId;
                            createBy = item.violationClassRequest.createBy;
                            substractGrade = item.violationClassRequest.substractGrade;
                            totals = parseFloat(parseFloat(substractGrade) * parseInt(quantity)).toFixed(1);
                            reason = item.violationClassRequest.reason;
                            quantityNew = item.violationClassRequest.quantityNew;
                        }
                        $('.violation-by-date').append(`
                            <div class="violation-description mt-3">
                                <div class="violation-date">
                                    <span>` + violationDate + ` - Lớp ` + className + `</span>
                                </div>
                                <div class="violation-details">
                                    <div class="violation-name">
                                        <span class="font-500">Mô tả lỗi: </span>
                                        <span>` + description + `</span>
                                    </div>
                                    <p class="violation-note my-0">
                                        <span class="font-500">Ghi chú: </span>
                                        <span>` + note + `</span>
                                    </p>
                                </div>
                                <div class="violation-create-by">
                                    <span class="font-500">Tạo bởi: </span>
                                    <span>` + createBy + `</span>
                                </div>
                                <div class="violation-substract-grade">
                                    <span class="font-500">Điểm trừ: </span>
                                    <span>` + substractGrade + `</span>
                                </div>
                                <div class="violation-quantity">
                                    <span class="font-500">Số lần: </span>
                                    <span>` + quantity + `</span>
                                </div>
                                <div class="violation-total">
                                    <span class="font-500">Tổng điểm trừ: </span>
                                    <span>` + totals + `</span>
                                </div>
                                <div class="violation-action">
                                    <div class="hide violationClassId">` + violationClassId + `</div>
                                    <div class="hide requestId">` + requestId + `</div>
                                    <div class="hide classId">` + classId + `</div>
                                    <div class="hide className">` + className + `</div>
                                    <div class="hide createDate">` + createDate + `</div>
                                    <div class="hide violationDate">` + violationDate + `</div>
                                    <div class="hide createBy">` + createBy + `</div>
                                    <div class="hide description">` + description + `</div>
                                    <div class="hide note">` + note + `</div>
                                    <div class="hide substractGrade">` + substractGrade + `</div>
                                    <div class="hide reason">` + reason + `</div>
                                    <div class="hide quantityNew">` + quantityNew + `</div>
                                    <div class="hide quantity">` + quantity + `</div>
                                    <div class="hide history">` + history + `</div>
                                    <input type="button" class="btn btn-danger edit-btn my-1" id="editBtn" data-toggle="modal" name="` + checkEdit + `" value="CHỈNH SỬA"/>
                                    <input type="button" class="btn btn-primary history-btn my-1" data-toggle="modal" name="` + checkHistory + `" value="LỊCH SỬ SỬA"/>
                                </div>
                            </div>
                        `);
                        if (checkHistory == 0) {
                            $('.violation-action .history-btn[name="0"]').addClass('hide');
                        } else {
                            $('.violation-action .history-btn[name="1"]').removeClass('hide');
                        }
                        if (checkEdit == 0) {
                            $('.violation-action .edit-btn[name="0"]').removeClass('hide');
                            $('.violation-action .edit-btn[name="0"]').val('CHỈNH SỬA');
                        } else if (checkEdit == 2) {
                            $('.violation-action .edit-btn[name="2"]').removeClass('hide');
                            $('.violation-action .edit-btn[name="2"]').val('XEM YÊU CẦU CHỈNH SỬA');

                        } else if (checkEdit == 1) {
                            $('.violation-action .edit-btn[name="1"]').addClass('hide');
                        }
                    });
                    editBtn()
                    historyBtn();
                    totalSubstractGrade();
                } else {
                    $(".violation-by-date").html(`<h3 class="text-center mt-3">` + message + `</h3>`);
                    $('.violation-total-grade').addClass('hide');
                    $('.violation-total-grade .totalGrade').text('');
                }
            } else {
                $(".violation-by-date").html(`<h3 class="text-center mt-3">` + message + `</h3>`);
                $('.violation-total-grade').addClass('hide');
                $('.violation-total-grade .totalGrade').text('');
            }
        },
        failure: function (errMsg) {
            $(".violation-by-date").html(`<h3 class="text-center mt-3">` + errMsg + `</h3>`);
            $('.violation-total-grade').addClass('hide');
            $('.violation-total-grade .totalGrade').text('');
        },
        dataType: "json",
        contentType: "application/json"
    });
}

/*Edit button*/
function editBtn() {
    var editBtn = $('.violation-action .edit-btn[name="0"]');
    $(editBtn).unbind("click").click(function () {
        var violationClassId = $(this).parent().find('.violationClassId').text();
        var classId = $(this).parent().find('.classId').text();
        var className = $(this).parent().find('.className').text();
        var createDate = $(this).parent().find('.createDate').text();
        var violationDate = $(this).parent().find('.violationDate').text();
        var createBy = $(this).parent().find('.createBy').text();
        var description = $(this).parent().find('.description').text();
        var note = $(this).parent().find('.note').text();
        var substract = $(this).parent().find('.substractGrade').text();
        var quantity = $(this).parent().find('.quantity').text();
        var total = parseFloat(parseFloat(substract) * parseInt(quantity)).toFixed(1);
        $('#confirmEdit .modal-title').text('Xác nhận thay đổi');
        $('#confirmEdit .modal-footer .btn-danger').val('XÁC NHẬN');
        $('#confirmEdit .modal-footer .btn-danger').prop('id', 'confirmEditBtn');
        $('#confirmEdit .modal-footer .btn-danger').removeClass('confirmDeleteBtn');
        $('#confirmEdit .modal-footer .btn-primary').val('HỦY');
        editModal(violationDate, className, description, note, createBy, substract, quantity, total)
        var $total = $('.total');
        increaseBtn(substract, total, $total);
        decreaseBtn(substract, total, $total);
        var $newQuantity = $('.quantity-input');
        var $reason = $('#reason');
        editModalBtn(violationClassId, classId, createDate, description, substract, note, $reason, quantity, $newQuantity);
    })
    var viewEditBtn = $('.violation-action .edit-btn[name="2"]');
    $(viewEditBtn).on('click', function () {
        var requestId = $(this).parent().find('.requestId').text();
        var description = $(this).parent().find('.description').text();
        var note = $(this).parent().find('.note').text();
        var substract = $(this).parent().find('.substractGrade').text();
        var reason = $(this).parent().find('.reason').text();
        var quantityNew = $(this).parent().find('.quantityNew').text();
        var quantity = $(this).parent().find('.quantity').text();
        var total = parseFloat(parseFloat(substract) * parseInt(quantity)).toFixed(1);
        $('#confirmEdit .modal-title').text('Yêu cầu chỉnh sửa');
        $('#confirmEdit .modal-footer .btn-danger').val('HỦY YÊU CẦU CHỈNH SỬA');
        $('#confirmEdit .modal-footer .btn-danger').prop('id', requestId);
        $('#confirmEdit .modal-footer .btn-danger').addClass('confirmDeleteBtn');
        $('#confirmEdit .modal-footer .btn-primary').val('ĐÓNG');
        var newTotal = parseFloat(parseFloat(substract) * parseInt(quantityNew)).toFixed(1);
        confirmEditModal(description, substract, note, reason, quantity, quantityNew, total, newTotal)
        deleteRequest();
    })
}

/*Edit Modal Button*/
function editModalBtn(violationClassId, classId, createDate, description, substract, note, $reason, quantity, $newQuantity) {
    var editBtn = $('.violation-action .edit-btn[name="0"]');
    $('#editModalBtn').unbind("click").click(function () {
        var newQuantity = $newQuantity.val();
        var reason = $reason.val();
        $('.editInfo-err').text('');
        if (quantity == newQuantity) {
            $('.editInfo-err').text('Hãy thay đổi thông tin.');
            return false;
        } else if (reason == null || reason == "") {
            $('.editInfo-err').text('Hãy điền lý do thay đổi.');
            return false;
        } else {
            $('.editInfo-err').text('');
            editViolation = {
                violationClassId: violationClassId,
                username: username,
                classId: classId,
                editDate: moment().format('YYYY-MM-DD'),
                createDate: createDate,
                roleId: roleID,
                newQuantity: newQuantity,
                reason: reason,
                oldQuantity: quantity
            }
            var total = parseFloat(parseFloat(substract) * parseInt(quantity)).toFixed(1);
            var newTotal = parseFloat(parseFloat(substract) * parseInt(newQuantity)).toFixed(1);

            confirmEditModal(description, substract, note, reason, quantity, newQuantity, total, newTotal)
            confirmEditBtn();
        }
    });
}

/*Confirm Edit Modal*/
function confirmEditModal(description, substract, note, reason, quantity, newQuantity, total, newTotal) {
    $('#editModal').modal('hide');
    $('#confirmEdit .modal-body').html('');
    $('#confirmEdit .modal-body').append(`
        <div class="panel-title text-left">
            <h6 class="violationName">` + description + `</h6>
            <h6 class="substract-grade">Điểm trừ: ` + substract + `</h6>
        </div>
        <div class="panel-body">
            <div class="note">
                <span class="title">Ghi chú: </span> 
                <span class="info ml-4">` + note + `</span>
            </div>
            <div class="reason-change">
                <span class="title">Lý do thay đổi: </span> 
                <span class="info ml-4">` + reason + `</span>
            </div>
            <div class="quantity">
                <span class="title">Số lần: </span> 
                <span class="info ml-4">` + quantity + ` -> <span class="text-red font-500">` + newQuantity + `</span></span>
            </div>
            <div class="totals">
                <span class="title">Tổng điểm trừ: </span> 
                <span class="info ml-4">` + total + ` -> <span class="text-red font-500">` + newTotal + `</span></span>
            </div>
        </div>
    `);
    $('#confirmEdit').modal('show');
}

/*Confirm Edit Button*/
function confirmEditBtn() {
    $('#confirmEditBtn').unbind("click").click(function () {
        console.log(JSON.stringify(editViolation));
        $.ajax({
            url: '/api/emulation/requesteditviolation',
            type: 'POST',
            data: JSON.stringify(editViolation),
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
                    messageModal("saveSuccess","img/img-success.png", "Tạo yêu cầu thay đổi thành công!");
                } else {
                    messageModal("saveSuccess","img/img-error.png", message);
                }
            },
            failure: function (errMsg) {
                messageModal("saveSuccess","img/img-error.png", errMsg);
            },
            dataType: "json",
            contentType: "application/json"
        });
    });
}

/*Edit modal template*/
function editModal(violationDate, className, description, note, createBy, substract, quantity, total) {
    $('#editModal').modal('show');
    $('#editModal .modal-body').html('')
    $('#editModal .modal-body').append(`
        <p class="font-500">` + violationDate + ` - Lớp ` + className + `</p>
        <div class="ml-3">
            <p class="mb-1">
                <span class="font-500">Mô tả lỗi: </span>
                <span>` + description + `</span>
            </p>
            <p class="mb-1">
                <span class="font-500">Ghi chú: </span>
                <span class="text-red">` + note + `</span>
            </p>
        </div>
        <div class="ml-3">
            <p class="mb-1">
                <span class="font-500">Tạo bởi: </span>
                <span class="substract">` + createBy + `</span>
            </p>
        </div>
        <div class="ml-3">
            <p class="mb-1">
                <span class="font-500">Điểm trừ: </span>
                <span class="substract">` + substract + `</span>
            </p>
        </div>
        <div class="ml-3">
            <p class="mb-1 d-flex align-items-center">
                <span class="font-500">Số lần: </span>
                <span class="quantity d-flex ml-3">
                    <a role="button" class="btn-decrease"><i class="fa fa-minus" aria-hidden="true"></i></a>
                    <input class="quantity-input" type="number" value="` + quantity + `" min="0">
                    <a role="button" class="btn-increase"><i class="fa fa-plus" aria-hidden="true"></i></a>
                </span>
            </p>
        </div>
        <div class="ml-3">
            <p class="mb-1">
                <span class="font-500">Tổng điểm trừ: </span>
                <span class="total">` + total + `</span>
            </p>
        </div>
        <div class="form-group mx-3">
            <label class="font-500" for="reason">Lý do: </label>
            <textarea type="text" class="form-control" id="reason" placeholder="Lý do..."></textarea>
            <span class="text-red editInfo-err"></span>
        </div>
    `)
    if (roleID == 1 || roleID == 3) {
        $('#editModalBtn').val("CHỈNH SỬA");
    }
    if (roleID == 4) {
        $('#editModalBtn').val("TẠO YÊU CẦU THAY ĐỔI");
    }
}

/*History button*/
function historyBtn() {
    $('.history-btn').on('click', function () {
        var history = $(this).parent().find('.history').html();
        $('#historyModal .modal-body').html(history);
        $('#historyModal').modal('show');
    });
}

/*Delete request button*/
function deleteRequest() {
    $('#confirmEdit .confirmDeleteBtn').unbind("click").click(function () {
        var requestId = $(this).prop('id');
        var requestId = {
            requestId: requestId
        }
        console.log(JSON.stringify(requestId));
        $.ajax({
            url: '/api/emulation/deleteViolationClassRequest',
            type: 'POST',
            data: JSON.stringify(requestId),
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
                    messageModal("saveSuccess","img/img-success.png", "Hủy yêu cầu thay đổi thành công!");
                } else {
                    messageModal("saveSuccess","img/img-error.png", message);
                }
            },
            failure: function (errMsg) {
                messageModal("saveSuccess","img/img-error.png", errMsg);
            },
            dataType: "json",
            contentType: "application/json"
        });
    })
}

/*Button Increase*/
function increaseBtn(substract, total, $total) {
    $('.btn-increase').on('click', function () {
        var $qty = $(this).closest('div').find('.quantity-input');
        var currentVal = parseInt($qty.val());
        if (!isNaN(currentVal)) {
            $qty.val(currentVal + 1);
        }
        total = parseFloat(parseFloat(substract) * parseInt($qty.val())).toFixed(1);
        $total.text(total);
    });
}

/*Button Decrease*/
function decreaseBtn(substract, total, $total) {
    $('.btn-decrease').on('click', function () {
        var $qty = $(this).closest('div').find('.quantity-input');
        var currentVal = parseInt($qty.val());
        if (!isNaN(currentVal) && currentVal > 0) {
            $qty.val(currentVal - 1);
        }
        total = parseFloat(parseFloat(substract) * parseInt($qty.val())).toFixed(1);
        $total.text(total);
    });
}

/*Calculation total subtract*/
function totalSubstractGrade() {
    $('.violation-total-grade').removeClass('hide');
    var total = 0;
    var substractGrade = $('.violation-total span:last-child');
    $(substractGrade).each(function () {
        var substract = $(this).text();
        total = parseFloat(total) + parseFloat(substract);
    })
    $('.violation-total-grade .totalGrade').text(total.toFixed(1));
}

$('.closeModal').on('click', function () {
    $('#saveSuccess').modal('hide');
    date = $('#datetime').val();
    if ($('#classList option:selected').val() == null || $('#classList option:selected').val() == "") {
        classId = "";
    } else {
        classId = $('#classList option:selected').val();
    }
    infoSearch = {
        username: username,
        classId: classId,
        date: date,
        roleId: roleID
    }
    console.log(JSON.stringify(infoSearch))
    $(".violation-by-date").html("");
    search();
})

if (roleID != 1) {
    $('.manageBtn').addClass('hide');
}