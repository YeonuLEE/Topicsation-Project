import { setupHeaderAjax, getId, getHeaderAjax } from './checkTokenExpiration.js';

var link = "/lesson/";
var tbody;
var postUrl = "/mypage/{user_id}/schedule/postCalender";
var count = 1;
let userId;
let token;

//튜터 스케줄
$(document).ready(function () {

    token = sessionStorage.getItem('accessToken');
    console.log(token)

    // access token 만료 기간 검증 및 req header에 삽입
    if(token != null){
        setupHeaderAjax(token)
        userId = getId(token);
    }


    var today = new Date();
    $('.datepicker').datepicker({
        format: 'dd-mm-yyyy',
        autoclose: true,
        startDate: '0d'
    });
    $('.datepicker').datepicker('setDate', today);

    var selectedDate = today;

    // 년, 월, 일 추출
    var year = selectedDate.getFullYear();
    var month = selectedDate.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더해줌
    var day = selectedDate.getDate();

    // 시, 분 추출
    var hours = selectedDate.getHours();
    var minutes = selectedDate.getMinutes();

    // 년월일 포맷
    var dateFormatted = year + '-' + pad(month, 2) + '-' + pad(day, 2);

    // 시간 포맷
    var timeFormatted = pad(hours, 2) + ':' + pad(minutes, 2);


    // uri 지정
    // var pathURI = window.location.pathname
    // const regex = /\/mypage\/(\d+)\/schedule/;
    // const match = pathURI.match(regex);
    if (userId) {
        //if ( match && match[1] )
        // userId = match[1]; <-- 연우한테 물어보기

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
        async:false,
        success: function (data, status, xhr) {
            getHeaderAjax(xhr)

            // var jsonObject = JSON.parse(JSON.stringify(data));
            var jsonObject = JSON.parse(data);

            $("#information").attr("href", apiUrl2);
            $("#schedule").attr("href", apiUrl3);

            //튜터 이름
            $("#tutor-name").text(jsonObject.name);

            //튜터 이미지
            var imgSrc = "/assets/img/team/";
            imgSrc += jsonObject.profileimg;

            var img = $("<img>", {
                src: imgSrc,
                alt: "Joseph Portrait"
            }).addClass("card-img-top rounded-circle border-white");
            $("#tutor-img").append(img);

            var count = 1;
            //예약목록 출력
            tbody = $("#booking-list");
            for (var i = 0; i < jsonObject.schedule.length; i++) {
                scheduleList(jsonObject, i, tbody);
            }
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
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
        var selectedDate = $(this).datepicker('getDate');

        // 년, 월, 일 추출
        var year = selectedDate.getFullYear();
        var month = selectedDate.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더해줌
        var day = selectedDate.getDate();

        // 시, 분, 초 추출
        var hours = selectedDate.getHours();
        var minutes = selectedDate.getMinutes();

        // 년월일 포맷
        dateFormatted = year + '-' + pad(month, 2) + '-' + pad(day, 2);

        // 시간 포맷
        var timeFormatted = pad(hours, 2) + ':' + pad(minutes, 2);

        // var pathURI = window.location.pathname
        // const regex = /\/mypage\/(\d+)\/schedule/;
        // const match = pathURI.match(regex);
        if (userId) {
            // if (match && match[1])
            // const userId = match[1];

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

        $(".cell").css("color", "");
        $(".cell").css("background-color", "");
        $(".cell").css("pointer-events", "auto");

        $.ajax({
            type: "GET",
            url: apiUrl,
            success: function (data, status) {
                var jsonObject = JSON.parse(data);
                var tbody = $("#booking-list");
                for (var i = 0; i < jsonObject.schedule.length; i++) {
                    scheduleList(jsonObject, i, tbody);
                }
            },
            error: function (data, textStatus) {
                alert("Error!")
            },
            complete: function (data, textStatus) {
            },
        });
    });

    $("#schedule-save").click(function () {
        $("#modal-default").modal();
        $("#modal-form").submit(function (event) {
            event.preventDefault();
            saveSchedule(postUrl, dateFormatted);
        })
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
            if(data === "success") {
                $("#modal-default").modal('hide');
                location.reload()
                // $("#enter-password").val("");
            }
            else{
                $("#modal-data").text("비밀번호가 틀렸습니다.");
                $("#enter-password").val("");
            }
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });
}

// 숫자 앞에 0을 채우는 함수
function pad(num, size) {
    var s = num + "";
    while (s.length < size) s = "0" + s;
    return s;
}

function scheduleList(jsonObject, i, tbody) {
    var admission_link = "http://localhost:3000" + link + jsonObject.schedule[i].class_id + "?token=" + token;

    if (jsonObject.schedule[i].tutee_id) {
        var tr = $("<tr>");
        var tno = $("<td>").attr("scope", "row").css("text-align", "center").text(count++);
        var booking_time = $("<td>").css("text-align", "center").text(jsonObject.schedule[i].class_date + " " + jsonObject.schedule[i].class_time.substring(0, 2) + ":" + jsonObject.schedule[i].class_time.substring(2, 6));
        var booking_tutee = $("<td>").css("text-align", "center").text(jsonObject.schedule[i].name);
        var admission_td = $("<td>").css("text-align", "center");
        var admission_div = $("<div>").css("text-align", "center");
        var admission_a = $("<a>").attr("href", admission_link)
            .addClass("btn btn-primary")
            .attr("id", "admission-btn")
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

