var violationTypeID, oldName, oldTotalGrade, newName, newTotalGrade;
$(document).ready(function () {
    violationTypeID = sessionStorage.getItem("violationTypeID")
    var editRequest = {
        typeId: violationTypeID
    }
    if (violationTypeID == null) {
        $('.violation-err').append(`Hãy chọn nội quy cần chỉnh sửa <a href="violationList">TẠI ĐÂY</a>.`);
        $("#editInfo").prop('disabled', true);
    } else {
        $("#editInfo").prop('disabled', false);
        $.ajax({
            url: '/api/admin/getviolationtype',
            type: 'POST',
            data: JSON.stringify(editRequest),
            beforeSend: function () {
                $('body').addClass("loading")
            },
            complete: function () {
                $('body').removeClass("loading")
            },
            success: function (data) {
                var messageCode = data.messageDTO.messageCode;
                var message = data.messageDTO.message;
                if (messageCode == 0) {
                    oldName = data.name;
                    oldTotalGrade = data.totlaGrade;
                    $('#description').attr('value', oldName);
                    $('#totalGrade').attr('value', oldTotalGrade);
                } else {
                    $('.violation-err').text(message);
                }
            },
            failure: function (errMsg) {
                $('.violation-err').text(errMsg);
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
});

$("#editInfo").click(function (e) {
    newName = $('#description').val().trim();
    newTotalGrade = $('#totalGrade').val().trim();

    if (newName == "") {
        $('.violation-err').text("Hãy nhập mô tả nội quy!");
        return false;
    } else if (newTotalGrade == "") {
        $('.violation-err').text("Hãy nhập điểm cho nội quy!");
        return false;
    }else if (newTotalGrade == oldTotalGrade && newName == oldName) {
        $('.violation-err').text("Hãy thay đổi thông tin!");
        return false;
    }else if(newTotalGrade <= 0){
        $('.violation-err').text("Hãy nhập điểm của nội quy lớn hơn 0!");
        return false;
    } else {
        editViolationType(e);
    }
});

//edit violation type using typeid
function editViolationType(e){
    var model ={
        typeId : violationTypeID,
        name : newName,
        totlaGrade : newTotalGrade
    }
    e.preventDefault();
    $.ajax({
        url: '/api/admin/editviolationtype',
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
                oldTotalGrade = newTotalGrade;
                oldName = newName;
                $('.violation-err').text("");
                messageModal('editInfoSuccess', 'img/img-success.png', message)
            } else {
                $('.violation-err').text(message);
            }
        },
        failure: function (errMsg) {
            $('.violation-err').text(errMsg);
        },
        dataType: "json",
        contentType: "application/json"
    });
}
