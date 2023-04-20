var cell_id = "";
var tagId = "";

$(document).ready(function () {
    var today = new Date();
    $(".datepicker").datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        startDate: "0d",

    });

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

    var tutorId = "1234";
    var apiUrl = "/main/tutors/{tutor_id}/getInfo?calendarDate=";

    apiUrl = apiUrl.replace("{tutor_id}", tutorId);
    apiUrl = apiUrl + dateFormatted

    $.ajax({
        type: "GET",
        url: apiUrl,
        success: function(data, status) {
            var jsonObject = JSON.parse(data);

            $("#tutor-name").text(jsonObject.tutor_info.name);
            $("#introduce_content").text(jsonObject.tutor_info.introduce);
            $("#tutor-like").text(jsonObject.tutor_info.like);
            $("#tutor-nation").text(jsonObject.tutor_info.nationality);
            $("#first-interest").append(jsonObject.tutor_info.interest1);
            $("#second-interest").append(jsonObject.tutor_info.interest2);
            $("#profile-img").attr("src",jsonObject.tutor_info.picture);

            for(var i=0;i<jsonObject.schedule.length;i++) {
                $("#" + jsonObject.schedule[i].class_time).addClass("select");
            }
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });

    $('.datepicker').change(function() {
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

        $(".cell").removeClass("select");

        $.ajax({
            type: "GET",
            url: apiUrl,
            success: function(data, status) {
                var jsonObject = JSON.parse(data);

                // $("#tutor-name").text(jsonObject.tutor_info.name);
                // $("#introduce_content").text(jsonObject.tutor_info.introduce);
                // $("#tutor-like").text(jsonObject.tutor_info.like);
                // $("#tutor-nation").text(jsonObject.tutor_info.nationality);
                // $("#first-interest").append(jsonObject.tutor_info.interest1);
                // $("#second-interest").append(jsonObject.tutor_info.interest2);
                // $("#profile-img").attr("src",jsonObject.tutor_info.picture);

                for(var i=0;i<jsonObject.schedule.length;i++) {
                    $("#" + jsonObject.schedule[i].class_time).addClass("select");
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
        $("#" + tagId).addClass("select");// 이 자리에 db로 데이터 보내는거 넣으면 됨

        $.ajax({
            type: "GET",
            url: apiUrl,
            success: function(data, status) {
                var jsonObject = JSON.parse(data);

                // $("#tutor-name").text(jsonObject.tutor_info.name);
                // $("#introduce_content").text(jsonObject.tutor_info.introduce);
                // $("#tutor-like").text(jsonObject.tutor_info.like);
                // $("#tutor-nation").text(jsonObject.tutor_info.nationality);
                // $("#first-interest").append(jsonObject.tutor_info.interest1);
                // $("#second-interest").append(jsonObject.tutor_info.interest2);
                // $("#profile-img").attr("src",jsonObject.tutor_info.picture);

                for(var i=0;i<jsonObject.schedule.length;i++) {
                    $("#" + jsonObject.schedule[i].class_time).addClass("select");
                }
            },
            error: function (data, textStatus) {
                alert("Error!")
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