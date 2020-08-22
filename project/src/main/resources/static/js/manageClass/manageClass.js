var inforSearch = {
    classIdentifier: "",
    grade: "",
    sortBy: "1",
    orderBy: "0",
    status: "",
    pageNumber: 0
}

search();
/*Search button*/
$("#search").click(function () {
    var grade, classIdentifier, sortBy, orderBy, status;
    classIdentifier = $('#searchByIdentifier input').val().trim();
    if ($('#searchByGrade option:selected').val() == null || $('#searchByGrade option:selected').val() == "0") {
        grade = "";
    } else {
        grade = $('#searchByGrade option:selected').val();
    }
    if ($('#sortBy option:selected').val() == null) {
        sortBy = "1";
    } else {
        sortBy = $('#sortBy option:selected').val();
    }
    if ($('#orderBy option:selected').val() == null) {
        orderBy = "0";
    } else {
        orderBy = $('#orderBy option:selected').val();
    }
    if ($('#status option:selected').val() == null) {
        status = "";
    } else {
        status = $('#status option:selected').val();
    }
    inforSearch = {
        classIdentifier: classIdentifier,
        grade: grade,
        sortBy: sortBy,
        orderBy: orderBy,
        status: status,
        pageNumber: 0
    }
    $('tbody').html("");
    $('.table-paging').html("");
    search();

});

/*Load class list*/
function search() {
    $.ajax({
        url: '/api/admin/classtable',
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
                var totalPages = data.classList.totalPages;
                var id = 0 + inforSearch.pageNumber * 10;
                paging(inforSearch, totalPages);
                if (data.classList != null) {
                    $.each(data.classList.content, function (i, item) {
                        var grade, classIdentifier, status, giftedClassName;
                        id += 1;
                        if (item.grade == null) {
                            grade = "";
                        } else {
                            grade = item.grade;
                        }
                        if (item.classIdentifier == null) {
                            classIdentifier = "";
                        } else {
                            classIdentifier = item.classIdentifier;
                        }
                        if (item.status == null || item.status == 0) {
                            status = `<span class="status-active"><i class="fa fa-circle" aria-hidden="true"></i></span>`;
                        } else {
                            status = `<span class="status-deactive"><i class="fa fa-circle" aria-hidden="true"></i></span>`;
                        }
                        if (item.giftedClass == null) {
                            giftedClass = "";
                        } else {
                            giftedClassName = item.giftedClass.name;
                        }
                        $('tbody').append(
                            `<tr>
                                <td><span>` + id + `</span></td>
                                <td><span id="grade">` + grade + `</span></td>
                                <td><span id="giftedClassName">` + giftedClassName + `</span></td>
                                <td><span id="classIdentifier">` + classIdentifier + `</span></td>
                                <td><span id="status">` + status + `</span></td>
                                <td><span class="bt-table-field"><a href="editClass" id="${item.classId}" class="bt-table-edit">
                                 <i class="fa fa-pencil-square-o" aria-hidden="true"></i></a></span></td>
                            </tr>`
                        );
                    });
                    getClassID();
                    pagingClick();
                    manageBtn();
                }
            } else {
                $('tbody').append(`<tr><td colspan="6" class="userlist-result">` + data.message.message + `</td></tr>`)
            }
        },
        failure: function (errMsg) {
            $('tbody').append(`<tr><td colspan="6" class="userlist-result">` + errMsg + `</td></tr>`)
        },
        dataType: "json",
        contentType: "application/json"
    });
}

/*Edit class information by ID*/
function getClassID() {
    var classId = $('.bt-table-edit');
    $(classId).on('click', function (e) {
        classId = $(this).prop('id');
        sessionStorage.setItem("classId", classId);
    });
}

/*Show or hide button manage*/
function manageBtn() {
    if (roleID != 1) {
        $('.manageBtn').addClass('hide');
        $('table > thead > tr > th:last-child').addClass('hide');
        $('tbody > tr > td:last-child').addClass('hide');
    }
}
