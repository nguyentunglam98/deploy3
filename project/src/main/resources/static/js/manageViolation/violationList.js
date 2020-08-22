$(document).ready(function () {
    $.ajax({
        url: '/api/admin/violationandviolationtype',
        type: 'POST',
        beforeSend: function () {
            $('body').addClass("loading")
        },
        complete: function () {
            $('body').removeClass("loading")
        },
        success: function (data) {
            if (data.messageDTO.messageCode == 0) {
                if (data.listViolationType.length != 0) {
                    $("#violationList").html("");
                    $.each(data.listViolationType, function (i, itemType) {
                        var typeId, totalGrade, name;
                        if (itemType.typeId == null) {
                            typeId = "-";
                        } else {
                            typeId = itemType.typeId;
                        }

                        if (itemType.name == null) {
                            name = "-";
                        } else {
                            name = itemType.name;
                        }

                        if (itemType.totalGrade == null) {
                            totalGrade = "-";
                        } else {
                            totalGrade = itemType.totalGrade;
                        }
                        $('#violationList').append(`
                            <div class="panel-heading mt-3" data-toggle="collapse" data-target="#collapse` + typeId + `" onclick="toggleClick()">
                                <div>
                                    <div class="d-flex align-items-center"><h5 class="my-0 mr-2">Nội quy theo dõi: </h5>` + name + `</div>
                                    <div class="d-flex align-items-center"><h6 class="my-0 mr-2">Điểm:</h6>` + totalGrade + `</div>
                                </div>
                                <div class="d-flex justify-content-end align-items-center">
                                    <span class="bt-table-field mx-2 manageBtn">
                                        <a href="editViolationType" class="bt-table-edit" title="Sửa" id="` + typeId + `">
                                            <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
                                        </a>
                                    </span>
                                    <span class="bt-table-field mr-5 manageBtn">
                                        <a title="Xóa" data-target="" data-toggle="modal" class="mx-2 bt-table-type-delete" id="` + typeId + `">
                                            <i class="fa fa-trash" aria-hidden="true"></i>
                                        </a>
                                    </span>
                                    <button><i class="fa fa-chevron-down rotate up"></i></button>
                                </div>
                            </div>
                            <div class="panel-collapse collapse in" id="collapse` + typeId + `">
                                <table class="table table-hover table-responsive">
                                    <thead>
                                    <th style="width: 5%"></th>
                                    <th>Vi phạm</th>
                                    <th style="width: 10%">Điểm trừ</th>
                                    <th style="width: 15%"></th>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        `);
                    });

                    $.each(data.listViolationType, function (i, itemType) {
                        var typeId = "#collapse" + itemType.typeId + " tbody";
                        if (itemType.violation != null && itemType.violation.length != 0) {
                            $.each(itemType.violation, function (j, itemVio) {
                                var description, substractGrade, violationId;
                                if (itemVio.description == null) {
                                    description = "-";
                                } else {
                                    description = itemVio.description;
                                }
                                if (itemVio.substractGrade == null) {
                                    substractGrade = "-";
                                } else {
                                    substractGrade = itemVio.substractGrade;
                                }
                                if (itemVio.violationId == null) {
                                    violationId = "-";
                                } else {
                                    violationId = itemVio.violationId;
                                }
                                $(typeId).append(`
                                <tr>
                                    <th style="width: 5%"></th>
                                    <td>` + description + `</td>
                                    <td>` + substractGrade + `</td>
                                    <td><span>
                                            <a href="editViolation" title="Sửa" class="mx-2 bt-table-edit-vio" id="` + violationId + `">
                                                <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
                                            </a>
                                            <a href="" title="Xóa" data-target="" data-toggle="modal" class="mx-2 bt-table-delete-vio" id="` + violationId + `">
                                                <i class="fa fa-trash" aria-hidden="true"></i>
                                            </a>                            
                                        </span></td>
                                </tr>
                            `);
                            });
                        } else {
                            $(typeId).append(
                                `<tr>
                                    <td colspan="4" class="userlist-result">Danh sách lỗi vi phạm trống.</td>
                                </tr>`
                            )
                        }
                    });
                    getViolationTypeID();
                    getViolationID();
                    deleteViolationType();
                    deleteViolation();
                    manageBtn();
                }
            } else {
                $('#violationList').append(`
                    <tr>
                        <td colspan="6" class="userlist-result">` + data.messageDTO.message + ` </td>
                    </tr> 
                `);
            }
        },
        failure: function (errMsg) {
            $('#violationList').append(
                `<tr>
                    <td colspan="6" class="userlist-result">` + errMsg + ` </td>
                </tr>`
            )
        },
        dataType: "json",
        contentType: "application/json"
    });
});

/*Edit violation type by ID*/
function getViolationTypeID() {
    var typeId = $('.bt-table-edit');
    $(typeId).on('click', function (e) {
        typeId = $(this).prop('id');
        sessionStorage.setItem("violationTypeID", typeId);
    });
};

/*Edit violation by ID*/
function getViolationID() {
    var violationId = $('.bt-table-edit-vio');
    $(violationId).on('click', function (e) {
        violationId = $(this).prop('id');
        sessionStorage.setItem("violationId", violationId);
    });
};

/*Delete violation type by ID*/
function deleteViolationType() {
    var deleteTypeId = $('.bt-table-type-delete');
    $(deleteTypeId).on('click', function (e) {
        deleteTypeId = $(this).prop('id');
        var model = {
            typeId: deleteTypeId,
        }
        e.preventDefault();
        messageModal('deleteModal', 'img/img-question.png', 'Bạn có chắc muốn <b>XÓA</b> nội quy này không?')
        $('.bt-table-type-delete').attr('data-target', '#deleteModal');
        $('#deleteViolation').on('click', function (e) {
            $.ajax({
                url: '/api/admin/deleteviolationtype',
                type: 'POST',
                data: JSON.stringify(model),
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
                        messageModal('deleteSuccess', 'img/img-success.png', message)
                    } else {
                        messageModal('deleteSuccess', 'img/img-error.png', message)
                    }
                },
                failure: function (errMsg) {
                    messageModal('deleteSuccess', 'img/img-error.png', errMsg)
                },
                dataType: "json",
                contentType: "application/json"
            });
        });
    });
};

/*Delete violation by ID*/
function deleteViolation() {
    var deleteId = $('.bt-table-delete-vio');
    $(deleteId).on('click', function (e) {
        deleteId = $(this).prop('id');
        var model = {
            violationId: deleteId,
        }
        e.preventDefault();
        messageModal('deleteModal', 'img/img-question.png', 'Bạn có chắc muốn <b>XÓA</b> vi phạm này không?')
        $('.bt-table-delete-vio').attr('data-target', '#deleteModal');
        $('#deleteViolation').on('click', function (e) {
            $.ajax({
                url: '/api/admin/deleteviolation',
                type: 'POST',
                data: JSON.stringify(model),
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
                        messageModal('deleteSuccess', 'img/img-success.png', message)
                    } else {
                        messageModal('deleteSuccess', 'img/img-error.png', message)
                    }
                },
                failure: function (errMsg) {
                    messageModal('deleteSuccess', 'img/img-error.png', errMsg)
                },
                dataType: "json",
                contentType: "application/json"
            });
        });
    });
};

function manageBtn() {
    if (roleID != 1) {
        $('.manageBtn').addClass('hide');
        $('thead th:last-child').addClass('hide');
        $('tbody tr td:last-child').addClass('hide');
        $('.table-title').addClass('pb-4');
    }
}
