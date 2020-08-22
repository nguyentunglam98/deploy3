$('#fromYear').change(function () {
    var fromYear = $(this).val();
    $('#toYear').val(parseInt(fromYear) + 3);
});
/*Setup value */
$('#fromDate').val(moment().format('YYYY-MM-DD'));
$('#toDate').val(moment().format('YYYY-MM-DD'));
$('#fromYear').val(moment().year());
$('#toYear').val(parseInt($('#fromYear').val()) + 3)
var inforSearch = {
    fromDate: $('#fromDate').val(),
    toDate: $('#toDate').val(),
    giftedId: $('#gifftedClass option:selected').val(),
    fromYear: $('#fromYear').val(),
    toYear: $('#toYear').val(),
    pageNumber: 0
}

/*Get giftedClass in combobox*/
$.ajax({
    url: '/api/admin/giftedclasslist',
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
            if (data.giftedClassList != null) {
                $("#gifftedClass").select2();
                $("#gifftedClass").html("");
                $.each(data.giftedClassList, function (i, item) {
                    $('#gifftedClass').append(
                        `<option value="` + item.giftedClassId + `">` + item.name + `</option>
                    `);
                });
            } else {
                $("#gifftedClass").html(`<option>Không có hệ chuyên nào.</option>`);
            }
        } else {
            $("#gifftedClass").html(`<option>` + message + `</option>`);
        }
    },
    failure: function (errMsg) {
        $("#gifftedClass").html(`<option>` + errMsg + `</option>`);
    },
    dataType: "json",
    contentType: "application/json"
});
search();

/*Search button*/
$("#search").click(function () {
    var giftedId, fromYear;
    if ($('#gifftedClass option:selected').val() == null || $('#gifftedClass option:selected').val() == "0") {
        giftedId = "";
    } else {
        giftedId = $('#gifftedClass option:selected').val();
    }
    if ($('#fromYear').val() == null) {
        fromYear = "";
    } else {
        fromYear = $('#fromYear').val();
    }
    inforSearch = {
        fromDate: $('#fromDate').val(),
        toDate: $('#toDate').val(),
        giftedId: giftedId,
        fromYear: fromYear,
        toYear: $('#toYear').val(),
        pageNumber: 0
    }
    $('#violationList').html("");
    search();
});

/*Load data to list*/
function search() {
    console.log(JSON.stringify(inforSearch))
    $.ajax({
        url: '/api/violationClass/history/view',
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
                var totalPage = parseInt(data.totalPage);
                if (totalPage > 1) {
                    $('.table-paging').removeClass('hide');
                    $('.table-paging').html('');
                    paging(inforSearch, totalPage);
                } else {
                    $('.table-paging').addClass('hide');
                }
                if (data.viewViolationClassList.length != 0) {
                    $('#violationList').html("");
                    $.each(data.viewViolationClassList, function (i, item) {
                        var quantity, note, dayName;
                        if (item.dayName == null) {
                            dayName = "";
                        } else {
                            dayName = item.dayName + " - ";
                        }
                        if (item.quantity == null) {
                            quantity = 0;
                        } else {
                            quantity = item.quantity;
                        }
                        if (item.note == null) {
                            note = "";
                        } else {
                            note = item.note;
                        }
                        $('#violationList').append(
                            `<div class="violation-description my-2">
                                <div class="violation-date">
                                    <span>` + dayName + convertDate(item.createDate, '/') + `</span>
                                </div>
                                <div class="violation-details">
                                    <div class="violation-name">
                                        <span class="font-500">Mô tả lỗi: </span>
                                        <span>` + item.description + `</span>
                                    </div>
                                    <p class="violation-note my-0">
                                        <span class="font-500">Ghi chú: </span>
                                        <span>` + note + `</span>
                                    </p>
                                </div>
                                <div class="violation-substract-grade">
                                    <span class="font-500">Điểm trừ: </span>
                                    <span>` + item.substractGrade + `</span>
                                </div>
                                <div class="violation-quantity">
                                    <span class="font-500">Số lần: </span>
                                    <span>` + quantity + `</span>
                                </div>
                                <div class="violation-total">
                                    <span class="font-500">Tổng điểm trừ: </span>
                                    <span>` + parseFloat(parseFloat(item.substractGrade) * parseInt(quantity)) + `</span>
                                </div>
                            </div>`
                        );
                    });
                } else {
                    $('#violationList').html(
                        `<div class="violation-description my-2">
                            <div class="violation-date w-100 text-center">
                                <span>Không có kết quả.</span>
                            </div>
                        </div>
                    `)
                }
            } else {
                $('#violationList').html(
                    `<div class="violation-description my-2">
                        <div class="violation-date w-100 text-center">
                            <span>` + message + `</span>
                        </div>
                    </div>
                `)
            }
            pagingClick();
        },
        failure: function (errMsg) {
            $('#violationList').html(
                `<div class="violation-description my-2">
                    <div class="violation-date w-100 text-center">
                        <span>` + errMsg + `</span>
                    </div>
                </div>
            `)
        },
        dataType: "json",
        contentType: "application/json"
    });
}