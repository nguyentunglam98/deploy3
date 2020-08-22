var violationID, oldDescription, oldSubstractGrade, oldTypeId;
$(document).ready(function () {
    violationID = sessionStorage.getItem("violationId")
    var model = {
        violationId: violationID
    }
    if (violationID == null) {
        $('.violation-err').append(`Hãy chọn vi phạm cần chỉnh sửa <a href="violationList">TẠI ĐÂY</a>.`);
        $("#editInfo").prop('disabled', true);
    } else {
        $("#editInfo").prop('disabled', false);
        $.ajax({
            url: '/api/admin/getviolation',
            type: 'POST',
            data: JSON.stringify(model),
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
                    oldTypeId = data.currentViolation.typeId;
                    oldDescription = data.currentViolation.description;
                    oldSubstractGrade = data.currentViolation.substractGrade;
                    $.each(data.violationTypeList, function (i, item) {
                        $('#violationTypeName').append(`<option value="` + item.typeId + `">` + item.name + `</option>`);
                    });

                    $('#description').text(oldDescription);
                    $('#substractGrade').attr('value', oldSubstractGrade);
                    $("#violationTypeName").val(oldTypeId);
                } else {
                    $('#violation-err').text(message);
                }
            },
            failure: function (errMsg) {
                $('#violation-err').text(errMsg);
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
});

$("#editInfo").click(function (e) {
    var newTypeId = $('#violationTypeName').find('option:selected').val();
    var newDescription = $('#description').val().trim();
    var newSubstractGrade = $('#substractGrade').val().trim();

    if (newTypeId == null || newTypeId == 0) {
        $('.violation-err').text("Hãy lựa chọn nội quy!");
        return false;
    }else if(newDescription == ""){
        $('.violation-err').text("Hãy nhập mô tả lỗi vi phạm!");
        return false;
    }else if(newSubstractGrade == ""){
        $('.violation-err').text("Hãy nhập điểm trừ của lỗi vi phạm!");
        return false;
    }else if(newSubstractGrade <= 0){
        $('.violation-err').text("Hãy nhập điểm trừ của lỗi vi phạm lớn hơn 0!");
        return false;
    }else if (newTypeId == oldTypeId && newDescription == oldDescription && newSubstractGrade == oldSubstractGrade) {
        $('.violation-err').text("Hãy thay đổi thông tin !");
        return false;
    } else {
        var model = {
            typeId: newTypeId,
            violationId: violationID,
            description: newDescription,
            substractGrade: newSubstractGrade
        }
        e.preventDefault();
        $.ajax({
            url: '/api/admin/editviolation',
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
                    oldTypeId = newTypeId;
                    oldDescription = newDescription;
                    oldSubstractGrade = newSubstractGrade;
                    $('.violation-err').text("");
                    messageModal('editInfoSuccess', 'img/img-success.png', 'Thông tin sửa thành công!')
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