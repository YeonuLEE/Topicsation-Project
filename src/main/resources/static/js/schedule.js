var tagId = "";

//튜터 스케줄
$(document).ready(function () {
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
    var pathURI = window.location.pathname
    const regex = /\/mypage\/(\d+)\/schedule/;
    const match = pathURI.match(regex);
    if (match && match[1]) {
        const userId = match[1];

        var apiUrl2 = "/mypage/{user_id}";
        var apiUrl3 = "/mypage/{user_id}/schedule";

        apiUrl2 = apiUrl2.replace("{user_id}", userId);
        apiUrl3 = apiUrl3.replace("{user_id}", userId);
        var apiUrl = "/mypage/{user_id}/schedule/getCalendar?classDate=";

        apiUrl = apiUrl.replace("{user_id}", userId);
        apiUrl = apiUrl + dateFormatted;

    } else {
        console.log("매치되는 문자열이 없습니다.");
    }

    <!-- ajax get Date -->
    $.ajax({
        type: "GET",
        url: apiUrl,
        dataType: "json",
        success: function (data, status) {
            var jsonObject = JSON.parse(JSON.stringify(data));

            $("#information").attr("href", apiUrl2);
            $("#schedule").attr("href", apiUrl3);

            //링크 이동(수업입장)
            var link = "/lesson/";

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
            var tbody = $("#booking-list");
            for (var i = 0; i < jsonObject.schedule.length; i++) {
                var list = jsonObject.schedule[i];
                $("#" + list.class_time).addClass("select");
                var admission_link = link + jsonObject.schedule[i].class_id;

                if (list.tutee_id) {
                    var tr = $("<tr>");
                    var tno = $("<td>").attr("scope", "row").css("text-align", "center").text(count++);
                    var booking_time = $("<td>").css("text-align", "center").text(list.class_date + " " + list.class_time.substring(0, 2) + ":" + list.class_time.substring(2, 6));
                    var booking_tutee = $("<td>").css("text-align", "center").text(list.tutee_name);
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
            }
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });

    $('.cell').click(function () {
        if ($(this).hasClass("select")) {
            $(this).removeClass('select');
        } else {
            $(this).addClass('select');
        }
    });
    $("#save-btn").click(function () {
        $("#modal-default").modal();
    })
    $("#enter-password").click(function () {

        var tutor_id = "1234";
        var postlink = "/mypage/{user_id}/schedule/postCalender";
        var postUrl = postlink.replace("{user_id}", tutor_id);

        $.ajax({
            type: "POST",
            url: postUrl,
            contentType: 'application/json',
            data: JSON.stringify({
                $tutor_id: tutor_id,
                // $class_time: class_time,
                // $class_date: class_date,
                test: "test"
            }),
            success: function (data, status) {
                console.log(data)
            },
            error: function (data, textStatus) {
                alert("Error!")
            },
            complete: function (data, textStatus) {
            },
        });
    });
});

$('.datepicker').change(function () {
    var selectedDate = $(this).datepicker('getDate');

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

    // $(".cell").removeClass("select");

    $.ajax({
        type: "GET",
        url: apiUrl,
        success: function (data, status) {
            var jsonObject = JSON.parse(JSON.stringify(data));

            for (var i = 0; i < jsonObject.schedule.length; i++) {
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

// 숫자 앞에 0을 채우는 함수
function pad(num, size) {
    var s = num + "";
    while (s.length < size) s = "0" + s;
    return s;
}
