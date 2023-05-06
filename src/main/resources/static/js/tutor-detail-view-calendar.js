let tagId;
let tutorId;
let pathURI;
let roles;

let selectedDate;
let year;
let month;
let day;
let hours;
let minutes;

let dateFormatted;
let timeFormatted;

$(document).ready(function () {

    const token = sessionStorage.getItem('accessToken');

    if (token != null) {
        roles = getRoles(token)
        setupHeaderAjax(token)
    }

    let today = new Date();
    $(".datepicker").datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        startDate: today,
        beforeShowDay: function (date) {
            let currentDate = new Date();
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

    selectedDate = today;

    // 년, 월, 일 추출
    year = selectedDate.getFullYear();
    month = selectedDate.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더해줌
    day = selectedDate.getDate();

    // 시, 분, 초 추출
    hours = selectedDate.getHours();
    minutes = selectedDate.getMinutes();

    // 년월일 포맷
    dateFormatted = year + '-' + pad(month, 2) + '-' + pad(day, 2);

    pathURI = window.location.pathname
    const regex = /\/(\d+)$/;
    const match = pathURI.match(regex);
    const number = match[1];

    tutorId = number;

    let getUrl = "/main/tutors/{tutor_id}/getInfo?calendarDate=";

    getUrl = getUrl.replace("{tutor_id}", number);
    getUrl += dateFormatted;

    $.ajax({
        type: "GET",
        url: getUrl,
        async: false,
        success: function (data, status, xhr) {
            getHeaderAjax(xhr)

            let jsonObject = JSON.parse(data);
            let dataBody = $("#reviewCard");

            console.log(jsonObject);

            $("#tutor-name").text(jsonObject.tutor_info.name);
            $("#introduce_content").text(jsonObject.tutor_info.introduce);
            $("#tutor-like").text(jsonObject.tutor_info.like);
            $("#tutor-nation").text(jsonObject.tutor_info.nationality);
            $("#first-interest").append(jsonObject.tutor_info.interest1);
            $("#second-interest").append(jsonObject.tutor_info.interest2);
            $("#profile-img").attr("src", jsonObject.tutor_info.picture);

            for (let i = 0; i < jsonObject.review.length; i++) {
                let reviewer = jsonObject.review[i];
                let div1 = $("<div>", {
                    class: "card bg-white border-light p-4 mb-4 col-8 col-lg-8",
                    style: "box-shadow: none"
                });

                let div2 = $("<div>", {class: "d-flex justify-content-between align-items-center mb-2"});
                let span1 = $("<span>", {class: "font-small"});
                let span2 = $("<span>", {
                    class: "font-weight-bold",
                    text: reviewer.tutee_name
                }); //name
                let span3 = $("<span>", {
                    class: "ml-2",
                    text: reviewer.review_date
                }); //date
                let p = $("<p>", {
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

            for (let i = 0; i < jsonObject.schedule.length; i++) {
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
        selectedDate = $(this).datepicker('getDate');

        // 년, 월, 일 추출
        year = selectedDate.getFullYear();
        month = selectedDate.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더해줌
        day = selectedDate.getDate();

        // 시, 분, 초 추출
        hours = selectedDate.getHours();
        minutes = selectedDate.getMinutes();

        // 년월일 포맷
        dateFormatted = year + '-' + pad(month, 2) + '-' + pad(day, 2);

        $(".cell").css("color", "");
        $(".cell").css("background-color", "");
        $(".cell").css("pointer-events", "none");

        let getUrl = "/main/tutors/{tutor_id}/getInfo?calendarDate=";
        getUrl = getUrl.replace("{tutor_id}", number);
        getUrl += dateFormatted;

        $.ajax({
            type: "GET",
            url: getUrl,
            success: function (data, status) {
                let jsonObject = JSON.parse(data);

                for (let i = 0; i < jsonObject.schedule.length; i++) {
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
        let reserveUrl = "/main/tutors/{tutor_id}/"
        reserveUrl = reserveUrl.replace("{tutor_id}", number);
        reserveUrl += "reserve";

        $.ajax({
            url: reserveUrl,
            type: "PUT",
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({
                tutorId: tutorId,
                tuteeId: getId(token),
                classDate: dateFormatted,
                classTime: tagId,
                test: "test",
            }),
            success: function (data, status) {
                console.log(tagId + "예약완료");
                alert(tagId + "에 예약되었습니다.");

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
    let s = num + "";
    while (s.length < size) s = "0" + s;
    return s;
}

function reservation(jsonObject, i) {
    if (jsonObject.schedule[i].tutee_id != null) {
        $("#" + jsonObject.schedule[i].class_time).css("color", "white");
        $("#" + jsonObject.schedule[i].class_time).css("background-color", "gray");
    } else if (jsonObject.schedule[i].tutee_id == null) {
        $("#" + jsonObject.schedule[i].class_time).css("color", "white");
        $("#" + jsonObject.schedule[i].class_time).css("background-color", "green");

        // tutee만 예약할 수 있게 설정
        if (roles == "tutee") {
            $("#" + jsonObject.schedule[i].class_time).css("pointer-events", "auto");
        } else {
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

function getHeaderAjax(xhr) {
    let accessToken = null
    //accesstoken 뽑아내기
    const authorization = xhr.getResponseHeader("Authorization");
    if (authorization != null) {
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
    let base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
    let payload = atob(base64Payload, 'base64');
    let result = JSON.parse(payload.toString())

    const expirationTime = result.exp * 1000 // exp는 초 단위이므로 밀리초 단위로 변환
    return expirationTime < Date.now();
}

function getId(accessToken) {
    let base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
    let payload = atob(base64Payload, 'base64');
    let result = JSON.parse(payload.toString())

    return result.sub;
}

function getRoles(accessToken) {
    let base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
    let payload = atob(base64Payload, 'base64');
    let result = JSON.parse(payload.toString())

    return result.roles;
}