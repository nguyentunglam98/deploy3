var inforSearch = {
    fullName: "",
    orderBy: "1",
    pageNumber: 0
}
var list = [];
search();

/*Search button*/
$("#search").click(function () {
    var fullName, orderBy, pageNumber;
    fullName = $('#searchByFullName input').val().trim();
    if ($('#orderBy option:selected').val() == null) {
        orderBy = "1";
    } else {
        orderBy = $('#orderBy option:selected').val();
    }
    inforSearch = {
        fullName: fullName,
        orderBy: orderBy,
        pageNumber: 0
    }
    $('tbody').html("");
    $('.table-paging').html("");
    search();
});

/*Load teacher list*/
function search() {
    $.ajax({
        url: '/api/admin/teacherlist',
        type: 'POST',
        data: JSON.stringify(inforSearch),
        beforeSend: function () {
            $('body').addClass("loading")
        },
        complete: function () {
            $('body').removeClass("loading")
        },
        success: function (data) {
            if (data.message.messageCode == 0) {
                var totalPages = data.teacherList.totalPages;
                paging(inforSearch, totalPages);
                if (data.teacherList != null) {
                    $.each(data.teacherList.content, function (i, item) {
                        var phone, email;
                        if (item.phone == null) {
                            phone = "";
                        } else {
                            phone = item.phone;
                        }
                        if (item.email == null) {
                            email = "";
                        } else {
                            email = item.email;
                        }
                        var selected = "";
                        var checked = ""
                        if (isCheck(item.teacherId)) {
                            selected = "selected"
                            checked = "checked"
                        }
                        $('tbody').append(
                            `<tr class="` + selected + `">
                                <td class="text-center">
                                    <span class="custom-checkbox ">
                                        <input id="` + item.teacherId + `"type="checkbox" name="options" value="` + item.teacherId + `" ` + checked + `>
                                        <label for="` + item.teacherId + `"></label>
                                    </span>
                                </td>
                                <td><span id="fullName">` + item.fullName + `</span></td>
                                <td><span id="teacherIdentifier">` + item.teacherIdentifier + `</span></td>
                                <td><span id="phone">` + phone + `</span></td>
                                <td><span id="email">` + email + `</span></td>
                                <td><span class="bt-table-field">
                                    <a href="teacherInformation" id="` + item.teacherId + `" class="bt-table-edit">
                                    <i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
                                    </span>
                                </td>
                            </tr>`
                        );
                    });
                } else {
                    $('tbody').append(
                        `<tr>
                        <td colspan="7" class="userlist-result">
                            ` + data.message.message + `
                        </td>
                    </tr>`
                    )
                }
                getTeacherID();
                selectCheckbox();
                pagingClick();
                manageBtn();
            } else {
                $('tbody').append(`<tr><td colspan="7" class="userlist-result">` + data.message.message + `</td></tr>`);
            }
        },
        failure: function (errMsg) {
            $('tbody').append(`<tr><td colspan="7" class="userlist-result">` + errMsg + `</td></tr>`);
        },
        dataType: "json",
        contentType: "application/json"
    });
}

/*Delete teacher*/
$("#deleteTeacher").click(function (e) {
    listTeacher = {
        listTeacher: list,
    }
    e.preventDefault();
    $.ajax({
        url: '/api/admin/deleteteacher',
        type: 'POST',
        data: JSON.stringify(listTeacher),
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

/*Check teacher before delete account*/
function checkTeacher() {
    $('#deleteTeacherModal').modal('show');
    $('#deleteTeacherModal .modal-body').html("");
    if (list.length == 0) {
        $('#deleteTeacherModal .modal-footer .btn-danger').addClass('hide');
        $('#deleteTeacherModal .modal-footer .btn-primary').attr('value', 'ĐÓNG');
        messageModal('deleteTeacherModal', 'img/img-error.png', 'Hãy chọn giáo viên mà bạn muốn xóa!')
    } else {
        $('#deleteTeacherModal .modal-footer .btn-danger').removeClass('hide');
        $('#deleteTeacherModal .modal-footer .btn-primary').attr('value', 'KHÔNG');
        messageModal('deleteTeacherModal', 'img/img-question.png', `Bạn có chắc muốn <b>XÓA</b> giáo viên này không?`)
    }
}

/*Edit teacher information by ID*/
function getTeacherID() {
    var teacherId = $('.bt-table-edit');
    $(teacherId).on('click', function (e) {
        teacherId = $(this).prop('id');
        sessionStorage.setItem("teacherId", teacherId);
    });
}

/*Show or hide button manage*/
function manageBtn() {
    if (roleID != 1) {
        $('.manageBtn').addClass('hide');
        $('table thead th:first-child').addClass('hide');
        $('tbody > tr > td:first-child').addClass('hide');
        $('table thead th:last-child').addClass('hide');
        $('tbody > tr > td:last-child').addClass('hide')
    }
}