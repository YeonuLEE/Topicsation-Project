var cell_id = "";
var tagId = "";
var tutorId = "";
var pathURI = "";
$(document).ready(function () {
    var today = new Date();
    $(".datepicker").datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        startDate: "+1d",

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

    console.log(dateFormatted)

    pathURI = window.location.pathname
    const regex = /\/(\d+)$/;
    const match = pathURI.match(regex);
    const number = match[1];

    tutorId = number;

    var apiUrl = "/main/tutors/{tutor_id}/getInfo?calendarDate=";

    apiUrl = apiUrl.replace("{tutor_id}", number);
    apiUrl = apiUrl + dateFormatted;
    console.log(apiUrl)
    $.ajax({
        type: "GET",
        url: apiUrl,
        success: function (data, status) {
            var jsonObject = JSON.parse(data);
            console.log(jsonObject);
            var dataBody = $("#reviewCard");

            $("#tutor-name").text(jsonObject.tutor_info.name);
            $("#introduce_content").text(jsonObject.tutor_info.introduce);
            $("#tutor-like").text(jsonObject.tutor_info.like);
            $("#tutor-nation").text(jsonObject.tutor_info.nationality);
            $("#first-interest").append(jsonObject.tutor_info.interest1);
            $("#second-interest").append(jsonObject.tutor_info.interest2);
            $("#profile-img").attr("src", "/"+jsonObject.tutor_info.picture);

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

        console.log(dateFormatted)

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

        console.log(apiUrl2)
        console.log(window.location.pathname)

        $.ajax({
            url: apiUrl2,
            type: "PUT",
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({
                $tutor_id: tutorId,
                $tutee_id: "1",
                $class_date: dateFormatted,
                $class_time: tagId,
                test: "test",
            }),
            success: function (data, status) {
                console.log(tagId + "예약완료");
            },
            error: function (data, textStatus) {
                alert("예약에 실패하였습니다. 다시 시도해 주세요");
                $("#" + jsonObject.schedule[i].class_time).css("color", "white");
                $("#" + jsonObject.schedule[i].class_time).css("background-color", "green");
                $("#" + jsonObject.schedule[i].class_time).css("pointer-events", "auto");
            },
            complete: function (data, textStatus) {
                alert(tagId+"에 예약되었습니다.");
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
    for (var i = 0; i < jsonObject.schedule.length; i++) {
        if (jsonObject.schedule[i].tutee_id != null) {
            $("#" + jsonObject.schedule[i].class_time).css("color", "white");
            $("#" + jsonObject.schedule[i].class_time).css("background-color", "gray");
        } else if (jsonObject.schedule[i].tutee_id == null) {
            $("#" + jsonObject.schedule[i].class_time).css("color", "white");
            $("#" + jsonObject.schedule[i].class_time).css("background-color", "green");
            $("#" + jsonObject.schedule[i].class_time).css("pointer-events", "auto");
        }
    }
}