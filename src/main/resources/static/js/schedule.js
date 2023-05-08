let link = "/lesson/";
let tbody;
let postUrl = "/mypage/{user_id}/schedule/postCalender";
let count = 1;
let userId;
let token;

let selectedDate;
let year;
let month;
let day;
let hours;
let minutes;

let dateFormatted;

//튜터 스케줄
$(document).ready(function () {
    // AJAX 에러 처리기로 설정
    $.ajaxSetup({
        error: moveToErrorPage
    });

    token = sessionStorage.getItem('accessToken');

    // access token 만료 기간 검증 및 req header에 삽입
    if (token != null) {
        setupHeaderAjax(token)
        userId = getId(token);
    }

    today = new Date();
    $('.datepicker').datepicker({
        format: 'dd-mm-yyyy',
        autoclose: true,
        startDate: '0d'
    });

    $('.datepicker').datepicker('setDate', today);

    selectedDate = today;

    // 년, 월, 일 추출
    year = selectedDate.getFullYear();
    month = selectedDate.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더해줌
    day = selectedDate.getDate();

    // 시, 분 추출
    hours = selectedDate.getHours();
    minutes = selectedDate.getMinutes();

    // 년월일 포맷
    dateFormatted = year + '-' + pad(month, 2) + '-' + pad(day, 2);

    if (userId) {

        var apiUrl2 = "/mypage/{user_id}";
        var apiUrl3 = "/mypage/{user_id}/schedule";


        apiUrl2 = apiUrl2.replace("{user_id}", userId);
        apiUrl3 = apiUrl3.replace("{user_id}", userId);

        var apiUrl = "/mypage/{user_id}/schedule/getCalendar?calendarDate=";

        apiUrl = apiUrl.replace("{user_id}", userId);
        apiUrl = apiUrl + dateFormatted;

    } else {
        console.log("매치되는 문자열이 없습니다.");
    }

    $.ajax({
        type: "GET",
        url: apiUrl,
        async: false,
        success: function (data, status, xhr) {
            getHeaderAjax(xhr)

            let jsonObject = JSON.parse(data);
            let person = jsonObject.tutor_info;


            $("#information").attr("href", apiUrl2);
            $("#schedule").attr("href", apiUrl3);

            //튜터 이름
            $("#tutor-name").text(person.name);

            //튜터 이미지

            let imgSrc = person.profileImg;
            let img = $("<img>", {

                src: imgSrc,
            }).addClass("card-img-top rounded-circle border-white");
            $("#tutor-img").append(img);

            let count = 1;
            //예약목록 출력
            tbody = $("#booking-list");
            for (var i = 0; i < jsonObject.schedule.length; i++) {
                scheduleList(jsonObject, i, tbody);
            }
        }
    });

    $('.cell').click(function () {
        if ($(this).css("background-color") === "rgb(0, 128, 0)") {
            $(this).css("background-color", "white");
            $(this).css("color", "black")
        } else {
            $(this).css("background-color", "green");
            $(this).css("color", "white");
        }
    });

    $('.datepicker').change(function () {
        tbody.empty();
        count = 1;
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

        // 시간 포맷
        timeFormatted = pad(hours, 2) + ':' + pad(minutes, 2);

        if (userId) {
            var apiUrl = "/mypage/{user_id}/schedule/getCalendar?calendarDate=".replace("{user_id}", userId);

            // apiUrl = apiUrl.replace("{user_id}", userId);
            apiUrl += dateFormatted;
        } else {
            console.log("매치되는 문자열이 없습니다.");
        }

        $(".cell").css("color", "");
        $(".cell").css("background-color", "");
        $(".cell").css("pointer-events", "auto");

        $.ajax({
            type: "GET",
            url: apiUrl,
            success: function (data, status) {
                let jsonObject = JSON.parse(data);
                let tbody = $("#booking-list");
                for (let i = 0; i < jsonObject.schedule.length; i++) {
                    scheduleList(jsonObject, i, tbody);
                }
            }
        });
    });

    $("#schedule-save").click(function () {
        $("#modal-default").modal();
        $("#modal-form").submit(function (event) {
            event.preventDefault();
            saveSchedule(postUrl, dateFormatted);
        })
    });

    $(document).on('click', '.admission-btn', function () {
        let classId = $(this).attr("id");
        let admission_link = "http://localhost:3000" + link + classId + "?token=" + token;
        const currentTime = new Date();

        const dateStr = classId.substring(classId.indexOf('_') + 1, classId.indexOf('_') + 11);
        const year = dateStr.substring(0, 4);
        const month = dateStr.substring(5, 7) - 1; // 월은 0부터 시작하므로 1을 뺍니다.
        const date = dateStr.substring(8, 10);

        const timeStr = classId.substring(classId.indexOf('_') + 12, classId.indexOf('_') + 16);
        const hours = timeStr.substring(0, 2);
        const minutes = timeStr.substring(2, 4);

        // Date 객체를 생성합니다.
        const classDate = new Date(year, month, date, hours, minutes);

        const timeDiff = (currentTime.getTime() - classDate.getTime()) / (1000 * 60);

        if (timeDiff < 0 || timeDiff < -30) {
            alert("수업 시작 전입니다.");
            return false;
        } else if (timeDiff > 30) {
            alert("수업이 종료되었습니다.");
            return false;
        } else if (timeDiff > 0 && timeDiff < 30) {
            // alert("수업 입장");
            location.href = admission_link;
        }
    });
});

function saveSchedule(postUrl, dateFormatted) {
    // 결과를 저장할 빈 객체 생성
    let selectCell = {};

    // .cell 클래스명을 가진 요소들을 가져옴
    const cells = document.querySelectorAll('.cell');

    // 각 셀을 반복하여 배경색이 green인 셀들의 id 값을 저장
    cells.forEach(cell => {
        if (cell.style.backgroundColor === "green") {
            selectCell[cell.id] = true;
        }
    });

// schedule 배열 생성
    const schedule = [];

// cellsWithGreenBG 객체의 프로퍼티를 반복하여 schedule 배열에 객체 추가
    for (let id in selectCell) {
        schedule.push({
            class_time: id
        });
    }

// user_info 객체 생성
    const userInfo = {
        class_date: dateFormatted,
        user_id: userId,
        password: $("#enter-password").val()
    };

// 최종 JSON 객체 생성
    const jsonData =
        {
            schedule,
            user_info: userInfo
        };

// 결과를 JSON 문자열로 변환
    const json = JSON.stringify(jsonData);

    $.ajax({
        type: "post",
        url: postUrl,
        data: json,
        contentType: "application/json",
        success: function (data, status) {
            if (data === "success") {
                $("#modal-default").modal('hide');
                location.reload()
            } else {
                $("#modal-default").modal('hide');
                location.reload()
            }
        },
        error: function (data, textStatus) {
            $("#modal-data").text("Invalid Password.").attr("class", "form-control is-invalid");
            $("#enter-password").val("");
        }
    });
}

// 숫자 앞에 0을 채우는 함수
function pad(num, size) {
    let s = num + "";
    while (s.length < size) s = "0" + s;
    return s;
}

function scheduleList(jsonObject, i, tbody) {
    if (jsonObject.schedule[i].tutee_id) {
        let tr = $("<tr>");
        let tno = $("<td>").attr("scope", "row").css("text-align", "center").text(count++);
        let booking_time = $("<td>").css("text-align", "center").text(jsonObject.schedule[i].class_date + " " + jsonObject.schedule[i].class_time.substring(0, 2) + ":" + jsonObject.schedule[i].class_time.substring(2, 6));
        let booking_tutee = $("<td>").css("text-align", "center").text(jsonObject.schedule[i].name);
        let admission_td = $("<td>").css("text-align", "center");
        let admission_div = $("<div>").css("text-align", "center");
        let admission_a = $("<button>")
            .addClass("btn btn-primary admission-btn")
            .attr("id", jsonObject.schedule[i].class_id)
            .text("Admission to class")
            .css("display", "inline-block");
        admission_div.append(admission_a);
        admission_td.append(admission_div);
        tr.append(tno);
        tr.append(booking_time);
        tr.append(booking_tutee);
        tr.append(admission_td);
        tbody.append(tr);

    }
    if (jsonObject.schedule[i].tutee_id != null) {
        $("#" + jsonObject.schedule[i].class_time).css("color", "white");
        $("#" + jsonObject.schedule[i].class_time).css("background-color", "gray");
        $("#" + jsonObject.schedule[i].class_time).css("pointer-events", "none");
    } else if (jsonObject.schedule[i].tutee_id == null) {
        $("#" + jsonObject.schedule[i].class_time).css("color", "white");
        $("#" + jsonObject.schedule[i].class_time).css("background-color", "green");
        $("#" + jsonObject.schedule[i].class_time).css("pointer-events", "auto");
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
    let result = JSON.parse(payload.toString());

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
    let base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
    let payload = atob(base64Payload, 'base64');
    let result = JSON.parse(payload.toString());

    return result.sub;
}

function getRoles(accessToken) {
    let base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
    let payload = atob(base64Payload, 'base64');
    let result = JSON.parse(payload.toString());

    return result.roles;
}

function moveToErrorPage (xhr){
    if(xhr.status == 401){
        window.location.href = "error/401"
    }else if(xhr.status == 404){
        window.location.href = "error/404"
    }else if(xhr.status == 500){
        window.location.href = "error/500"
    }
}