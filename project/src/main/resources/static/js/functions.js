/*Value default*/
var roleID = localStorage.getItem("roleID");
var username = localStorage.getItem("username");
var pathname = $(location).attr('pathname');

/*Set navigation bar*/
$(document).ready(function () {
    getAuthen();
    var loginSuccess = localStorage.getItem("loginSuccess");
    var asignedClass = localStorage.getItem("asignedClass");
    if (loginSuccess == 0) {
        $("#loginSuccessMenu").removeClass("hide");
        $('#loginMenu').css('display', 'none');
        //ROLEID_REDSTAR
        if (roleID == 3) {
            if (asignedClass != "null") {
                $("#loginSuccessMenu .nav-link").html(`<span><p class="m-0" style="font-size: 15px">` + username + `</p><p class="m-0" style="font-size: 12px">(Chấm lớp ` + asignedClass + `)</p></span><i class="fa fa-caret-down"></i>`);
            } else {
                $("#loginSuccessMenu .nav-link").html(`<span><p class="m-0" style="font-size: 15px">` + username + `</p><p class="m-0" style="font-size: 12px">(Không có lớp chấm)</p></span><i class="fa fa-caret-down"></i>`);
            }
            $('#loginSuccessMenu .nav-link').attr('style', 'padding-top: 0!important; padding-bottom: 0!important');
        } else {
            $("#loginSuccessMenu .nav-link").html(username + `<i class="fa fa-caret-down"></i>`);
        }
        //ROLEID_ADMIN
        if (roleID == 1) {
            $("#adminMenu").removeClass("hide");
        }
        //ROLEID_TIMETABLE_MANAGER || ROLEID_ADMIN
        if (roleID == 2 || roleID == 1) {
            $("#scheduleManagerMenu").addClass("show");
        }
        //ROLEID_REDSTAR || ROLEID_SUMMERIZEGROUP || ROLEID_ADMIN
        if (roleID == 3 || roleID == 5 || roleID == 1) {
            $('#gradingToEmulationMenu').removeClass('hide');
        }
        //ROLEID_MONITOR || ROLEID_CLUBLEADER
        if (roleID == 4 || roleID == 6) {
            $('#sendPostMenu').removeClass('hide');
        }
        //ROLEID_MONITOR || ROLEID_ADMIN
        if (roleID == 4 || roleID == 1) {
            $('#viewRequestMenu').removeClass('hide');
        }
    } else {
        $('#loginMenu').css('display', 'block');
        $("#loginSuccessMenu").addClass("hide");
        $("#adminMenu").addClass("hide");
        $("#scheduleManagerMenu").removeClass("show");
    }
    $("#logout").click(function () {
        //localStorage.clear();
        logout();
    })

    // Responsive menu
    // $(document).on('click', '.dropdown-menu', function (e) {
    //     e.stopPropagation();
    // });
    if ($(window).width() < 992) {
        $('.submenu').prev().append(`<i class="fa fa-caret-down"></i>`);
        $('.dropdown-menu a').click(function (e) {
            if ($(this).next('.submenu').length) {
                e.stopPropagation();
                $(this).next('.submenu').toggleClass('show');
                $(this).find('.fa').toggleClass('up');
            }
            $('.dropdown').on('hide.bs.dropdown', function () {
                $(this).find('.submenu').hide();
                $(this).find('.fa').removeClass('up');
            })
        });

        var divHeight = $('header .navbar').height();
        $('header').css('height', divHeight + 'px');
    }

    /*Add active class on menu*/
    $('#navbarMenu li a').each(function () {
        var href = '/' + $(this).attr('href');
        if (href == '//') {
            href = '/';
        }
        if (href == pathname) {
            if (pathname == '/') {
                $('.homePage').addClass('active');
            } else {
                $('.homePage').removeClass('active');
                $(this).closest('.nav-item').find('.nav-link').addClass('active');
                $(this).addClass('active');
            }
        }
    });

    menuClick();
});

function menuClick() {
    $('.dropdown').on('hide.bs.dropdown', function () {
        $('.nav-link .fa').not($(this).find('.fa')).removeClass('up');
        $(this).find('.fa').removeClass('up');
    });
    $('.dropdown').on('show.bs.dropdown', function () {
        $('.nav-link .fa').not($(this).find('.fa')).removeClass('up');
        $(this).find('.nav-link').find('.fa').addClass('up');
        $(this).find('.submenu').prev().removeClass('up');
    });
}

/*Loading page*/
$(document).on({
    ajaxStart: function () {
        $('body').addClass("loading");
    },
    ajaxComplete: function () {
        $('body').removeClass("loading");
    }
})

/*Select Checkbox*/
function selectCheckbox() {
    var checkbox = $('table tbody input[type="checkbox"]');
    $(checkbox).on('change', function (e) {
        row = $(this).closest('tr');
        if ($(this).is(':checked')) {
            row.addClass('selected');
            if (jQuery.inArray($(this).val(), list) == -1) {
                list.push($(this).val());
            }
        } else {
            row.removeClass('selected');
            var removeItem = $(this).val();
            list = $.grep(list, function (value) {
                return value != removeItem;
            });
        }
    });

    $("#selectAll").click(function () {
        var checkbox = $('table tbody input[type="checkbox"]');
        if (this.checked) {
            checkbox.each(function () {
                this.checked = true;
                $('tbody tr').addClass('selected');
                if (jQuery.inArray($(this).val(), list) == -1) {
                    list.push($(this).val());
                }
            });
        } else {
            checkbox.each(function () {
                this.checked = false;
                $('tbody tr').removeClass('selected');
                var removeItem = $(this).val();
                list = $.grep(list, function (value) {
                    return value != removeItem;
                });
            });
        }
    });
    checkbox.click(function () {
        if (!this.checked) {
            $("#selectAll").prop("checked", false);
        }
    });
}

/*Check user is selected or not*/
function isCheck(user) {
    if (list == null || list.length == 0) {
        return false;
    }
    for (var i = 0; i < list.length; i++) {
        if (list[i] == user) {
            return true;
        }
    }
    return false;
}

/*Pagination*/
function pagingClick() {
    $('.table-paging input').on('click', function (event) {
        $("#selectAll").prop("checked", false);
        var value = ($(this).val() - 1);
        if ($(this).val() == "Sau") {
            value = $('.table-paging__page_cur').val();
        } else if ($(this).val() == "Trước") {
            value = $('.table-paging__page_cur').val() - 2;
        }
        inforSearch.pageNumber = value;
        $('tbody').html("");
        $('.table-paging').html("");
        search();
    })
}

function pagingHomepage() {
    $('.table-paging input').on('click', function (event) {
        var value = ($(this).val() - 1);
        if ($(this).val() == "Sau") {
            value = $('.table-paging__page_cur').val();
        } else if ($(this).val() == "Trước") {
            value = $('.table-paging__page_cur').val() - 2;
        }
        homePage.pageNumber = value;
        console.log(JSON.stringify(homePage))
        $('tbody').html("");
        $('.table-paging').html("");
        loadHomepage();
    })
}

function paging(inforSearch, totalPages) {
    var pageNumber = parseInt(inforSearch.pageNumber)
    $('.table-paging').append(
        `<input type="button" class="table-paging__page btn-prev" id="prevPage" value="Trước"/>`
    );

    if(pageNumber == 0){
        $('.table-paging').append(
            `<input type="button" value="` + (1) + `" class="table-paging__page table-paging__page_cur"/>`
        );
    }
    else {
        $('.table-paging').append(
            `<input type="button" value="` + (1) + `" class="table-paging__page"/>`
        );
    }
    if(pageNumber > 2) {
        $('.table-paging').append(
            `<input type="button" value="..." class="table-paging__page" disabled/>`
        );
    }
    for (var i = pageNumber; i <= pageNumber + 2; i++) {
        if(i <= 1 || i >= totalPages) continue;
        if (i - 1 == pageNumber) {
            $('.table-paging').append(
                `<input type="button" value="` + (i) + `" class="table-paging__page table-paging__page_cur"/>`
            );
        } else {
            $('.table-paging').append(
                `<input type="button" value="` + (i) + `" class="table-paging__page"/>`
            );
        }
    }

    if(pageNumber + 2 < totalPages -1) {
        $('.table-paging').append(
            `<input type="button" value="..." class="table-paging__page" disabled/>`
        );
    }

    if(totalPages > 1){
        if(pageNumber == totalPages -1 ){
            $('.table-paging').append(
                `<input type="button" value="` + (totalPages) + `" class="table-paging__page table-paging__page_cur"/>`
            );
        }
        else{
            $('.table-paging').append(
                `<input type="button" value="` + (totalPages) + `" class="table-paging__page"/>`
            );
        }
    }

    $('.table-paging').append(
        `<input type="button" class="table-paging__page btn-next" id="nextPage" value="Sau"/>`
    );
    var pageNumInfo = parseInt(inforSearch.pageNumber);
    if ( pageNumInfo == 0) {
        $('#prevPage').prop('disabled', true);
    } else {
        $('#prevPage').prop('disabled', false);
    }
    if ((pageNumInfo + 1) == totalPages) {
        $('#nextPage').prop('disabled', true);
    } else {
        $('#nextPage').prop('disabled', false);
    }

};

/*Convert string to form date*/
function convertDate(str, value) {
    str = str.split("-");
    str = str[2].concat(value + str[1] + value + str[0]);
    return str;
}

function toggleClick() {
    $(".panel-heading").on('click', function () {
        $(this).find(".fa-chevron-down").addClass("up");
    })
    $(".panel-heading.collapsed").on('click', function () {
        $(this).find(".fa-chevron-down").removeClass("up");
    })
}

/*Set limited date in year*/
function limitedDate() {
    var fromYear = $('#fromYear').val();
    $('#toYear').val(parseInt(fromYear) + 1);
    var toYear = $('#toYear').val();
    $('#fromDate').attr('min', fromYear + '-01-01');
    $('#fromDate').attr('max', fromYear + '-12-31');
    $('#toDate').attr('min', toYear + '-01-01');
    $('#toDate').attr('max', toYear + '-12-31');
}

/*Limited text*/
function limitedText(str) {
    str = $(str).text();
    if (str.length > 300) {
        str = str.substring(0, 300) + '...'
    }
    return str;
}

/*Lazy Load*/
function lazyLoad() {
    $("img.lazy").lazyload({
        effect: "fadeIn"
    });
}

/*Dialog message*/
function messageModal(modalName, img, message) {
    $('#' + modalName + ' .modal-body').html(`
        <img class="my-3" src="` + img + `"/>
        <h5>` + message + `</h5>
    `)
    $('#' + modalName).modal('show');
}

/*Logout*/
function logout() {
    $.ajax({
        type: 'POST',
        url: "/api/user/logout",
        // data: JSON.stringify(user),
        beforeSend: function () {
            $('body').addClass("loading")
        },
        complete: function () {
            $('body').removeClass("loading")
        },
        success: function (data) {
            if (data != null && data.messageCode === 0) {
                localStorage.clear();
                // localStorage.removeItem("userInfo")
                // this.$cookies.remove("access_token")
                // this.$cookies.remove("token_provider")
                // window.location.href = "/"
            }
        },
        failure: function (errMsg) {
            $('.login-err').text(errMsg);
        },
        dataType: "json",
        contentType: "application/json"
    });
}

/*Authen*/
function getAuthen() {
    $.ajax({
        type: 'POST',
        url: "/api/user/get-authentication",
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem("accessToken")
        },

        // data: JSON.stringify(user),
        beforeSend: function () {
            $('body').addClass("loading")
        },
        complete: function () {
            $('body').removeClass("loading")
        },
        success: function (data) {
            if (data != null && data.messageCode == 1) {
                localStorage.clear();
            }
        },
        failure: function (errMsg) {
            $('.login-err').text(errMsg);
        },
        dataType: "json",
        contentType: "application/json"
        // Authorization: 'Bearer ' + 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTU5NjA5NzYxMSwiZXhwIjoxNTk2NzAyNDExfQ.bCFuTQOk1Kl9ARmrT83HTOQOMOFbFb6aLY_uEXiJTXBMz3tdlh3RMFPz70-sCKzNgTZ8bFJlzGeeHs5PgGAeNQ'
    });
}

/*Clear session when leaving page*/
if (pathname != '/teacherInformation') {
    sessionStorage.removeItem('teacherId');
}
if (pathname != '/editClass') {
    sessionStorage.removeItem('classId');
}
if (pathname != '/editViolationType') {
    sessionStorage.removeItem('violationTypeID');
}
if (pathname != '/editViolation') {
    sessionStorage.removeItem('violationId');
}
if (pathname != '/editSchoolYear') {
    sessionStorage.removeItem('schoolYearId');
}
if (pathname != '/violationListOfClass') {
    sessionStorage.removeItem('classIdGrading');
    sessionStorage.removeItem('dateGrading');
}
if (pathname != '/rankByWeek') {
    sessionStorage.removeItem('weekName');
    sessionStorage.removeItem('weekId');
}
if (pathname != '/rankByMonth') {
    sessionStorage.removeItem('monthName');
}
if (pathname != '/rankBySemester') {
    sessionStorage.removeItem('semesterName');
}
if (pathname != '/rankByYear') {
    sessionStorage.removeItem('yearId');
}
if (pathname != '/postDetail') {
    sessionStorage.removeItem('newsletterId');
}
if (pathname != '/editPost') {
    sessionStorage.removeItem('newsletterIdEdit');
}

