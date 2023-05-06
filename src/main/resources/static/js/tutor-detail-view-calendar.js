// import {getRoles, setupHeaderAjax, getHeaderAjax} from './checkTokenExpiration.js';

var cell_id = "";
var tagId = "";
var tutorId = "";
var pathURI = "";
let roles = "";
$(document).ready(function () {

    const token = sessionStorage.getItem('accessToken');

    if(token != null){
        roles = getRoles(token)
        setupHeaderAjax(token)
    }

    var today = new Date();
    $(".datepicker").datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        startDate: today,
        beforeShowDay: function (date) {
            var currentDate = new Date();
            currentDate.setHours(0, 0, 0, 0);

            if (date.valueOf() < currentDate.valueOf() + 1) {
                return false; // 오늘 이전의 날짜를 비활성화합니다.
            } else {
                return true; // 오늘 이후의 날짜를 활성화합니다.
            }
        },
    });

    today.setDate(today.getDate() + 1)

    $(".datepicker").datepicker("setDate", today);

    var selectedDate = today;

    // 년, 월, 일 추출
    var year = selectedDate.getFullYear();
    var month = selectedDate.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더해줌
    var day = selectedDate.getDate();

    // 시, 분, 초 추출
    var hours = selectedDate.getHours();
    var minutes = selectedDate.getMinutes();

    // 년월일 포맷
    var dateFormatted = year + '-' + pad(month, 2) + '-' + pad(day, 2);

    // 시간 포맷
    var timeFormatted = pad(hours, 2) + ':' + pad(minutes, 2);

    pathURI = window.location.pathname
    const regex = /\/(\d+)$/;
    const match = pathURI.match(regex);
    const number = match[1];

    tutorId = number;

    var apiUrl = "/main/tutors/{tutor_id}/getInfo?calendarDate=";

    apiUrl = apiUrl.replace("{tutor_id}", number);
    apiUrl = apiUrl + dateFormatted;

    $.ajax({
        type: "GET",
        url: apiUrl,
        async:false,
        success: function (data, status, xhr) {
            getHeaderAjax(xhr)

            var jsonObject = JSON.parse(data);
            var dataBody = $("#reviewCard");

            console.log(jsonObject);

            $("#tutor-name").text(jsonObject.tutor_info.name);
            $("#introduce_content").text(jsonObject.tutor_info.introduce);
            $("#tutor-like").text(jsonObject.tutor_info.like);
            $("#tutor-nation").text(jsonObject.tutor_info.nationality);
            $("#first-interest").append(jsonObject.tutor_info.interest1);
            $("#second-interest").append(jsonObject.tutor_info.interest2);
            $("#profile-img").attr("src", jsonObject.tutor_info.picture);

            for(var i = 0; i < jsonObject.review.length; i++){
                var reviewer = jsonObject.review[i];

                var div1 = $("<div>", {
                    class: "card bg-white border-light p-4 mb-4 col-8 col-lg-8",
                    style: "box-shadow: none"
                });
                var div2 = $("<div>", {class: "d-flex justify-content-between align-items-center mb-2"});
                var span1 = $("<span>",{class: "font-small"});
                var span2 = $("<span>",{
                    class: "font-weight-bold",
                    text: reviewer.tutee_name
                }); //name
                var span3 = $("<span>",{
                    class: "ml-2",
                    text: reviewer.review_date
                }); //date
                var p = $("<p>", {
                    class: "m-0",
                    text: reviewer.review_content
                }); //reviewContent

                dataBody.append(div1);
                div1.append(div2);
                div2.append(span1);
                span1.append(span2);
                span1.append(span3);
                div1.append(p);
            }

            for (var i = 0; i < jsonObject.schedule.length; i++) {
                reservation(jsonObject, i);
            }
        },
        error: function (data, textStatus) {
            location.href = "/error/404";
        },
        complete: function (data, textStatus) {
        },
    });

    $('.datepicker').change(function () {
        var selectedDate = $(this).datepicker('getDate');

        // 년, 월, 일 추출
        var year = selectedDate.getFullYear();
        var month = selectedDate.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더해줌
        var day = selectedDate.getDate();

        // 시, 분, 초 추출
        var hours = selectedDate.getHours();
        var minutes = selectedDate.getMinutes();

        // 년월일 포맷
        var dateFormatted = year + '-' + pad(month, 2) + '-' + pad(day, 2);

        // 시간 포맷
        var timeFormatted = pad(hours, 2) + ':' + pad(minutes, 2);

        apiUrl = "/main/tutors/{tutor_id}/getInfo?calendarDate=";

        apiUrl = apiUrl.replace("{tutor_id}", number);
        apiUrl = apiUrl + dateFormatted;


        $(".cell").css("color","");
        $(".cell").css("background-color","");
        $(".cell").css("pointer-events","none");

        $.ajax({
            type: "GET",
            url: apiUrl,
            success: function (data, status) {
                var jsonObject = JSON.parse(data);

                for (var i = 0; i < jsonObject.schedule.length; i++) {
                    reservation(jsonObject, i);
                }
            },
            error: function (data, textStatus) {
                alert("Error!")
            },
            complete: function (data, textStatus) {
            },
        });
    });


    $(".cell").click(function () {
        if (!$(this).hasClass("select")) {
            tagId = $(this).attr("id");

            // 모달 띄우기
            $("#modal-default").modal();
        }
    });

    $("#booking").click(function () {
        $("#" + tagId).css("color", "white");
        $("#" + tagId).css("background-color", "gray");
        $("#" + tagId).css("pointer-events", "none");
        var apiUrl2 = "/main/tutors/{tutor_id}/"
        apiUrl2 = apiUrl2.replace("{tutor_id}", number);
        apiUrl2 = apiUrl2 + "reserve";

        $.ajax({
            url: apiUrl2,
            type: "PUT",
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({
                $tutor_id: tutorId,
                $tutee_id: getId(token),
                $class_date: dateFormatted,
                $class_time: tagId,
                test: "test",
            }),
            success: function (data, status) {

                console.log(tagId + "예약완료");
                alert(tagId+"에 예약되었습니다.");

            },
            error: function (data, textStatus) {
                alert("예약에 실패하였습니다. 다시 시도해 주세요");
                $("#" + tagId).css("color", "white");
                $("#" + tagId).css("background-color", "green");
                $("#" + tagId).css("pointer-events", "auto");
            },
            complete: function (data, textStatus) {
            },
        });
    });
});

// 숫자 앞에 0을 채우는 함수
function pad(num, size) {
    var s = num + "";
    while (s.length < size) s = "0" + s;
    return s;
}

function reservation(jsonObject, i){
    if (jsonObject.schedule[i].tutee_id != null) {
        $("#" + jsonObject.schedule[i].class_time).css("color", "white");
        $("#" + jsonObject.schedule[i].class_time).css("background-color", "gray");
    } else if (jsonObject.schedule[i].tutee_id == null) {
        $("#" + jsonObject.schedule[i].class_time).css("color", "white");
        $("#" + jsonObject.schedule[i].class_time).css("background-color", "green");

        // tutee만 예약할 수 있게 설정
        if(roles == "tutee"){
            $("#" + jsonObject.schedule[i].class_time).css("pointer-events", "auto");
        }else{
            $("#" + jsonObject.schedule[i].class_time).css("pointer-events", "none");
        }

    }
}

function setupHeaderAjax(token) {
    $.ajaxSetup({
        beforeSend: function (xhr) {
            if (!checkTokenExp(token)) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + token)
            } else {
                xhr.setRequestHeader('Authorization', null)
            }
        }
    });
}

function getHeaderAjax(xhr){
    let accessToken = null
    //accesstoken 뽑아내기
    const authorization = xhr.getResponseHeader("Authorization");
    if(authorization != null){
        accessToken = authorization.substring(7);
        //accesstoken 저장
        sessionStorage.setItem("accessToken", accessToken);
    }
}

function setupHeader(token) {
    let xhr = new XMLHttpRequest();
    if (!checkTokenExp(token)) {
        xhr.setRequestHeader('Authorization', 'Bearer ' + token)
    } else {
        xhr.setRequestHeader('Authorization', null)
    }
}

function checkTokenExp(accessToken) {
    var base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
    var payload = atob(base64Payload, 'base64');
    var result = JSON.parse(payload.toString())

    const expirationTime = result.exp * 1000 // exp는 초 단위이므로 밀리초 단위로 변환
    if (expirationTime < Date.now()) {
        // AccessToken이 만료된 경우
        return true
    } else {
        // AccessToken이 유효한 경우
        return false
    }
}

function getId(accessToken) {
    var base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
    var payload = atob(base64Payload, 'base64');
    var result = JSON.parse(payload.toString())

    return result.sub;
}

function getRoles(accessToken) {
    var base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
    var payload = atob(base64Payload, 'base64');
    var result = JSON.parse(payload.toString())

    return result.roles;
}





