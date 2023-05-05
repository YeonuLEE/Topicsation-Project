import {setupHeaderAjax, getId, getHeaderAjax} from './checkTokenExpiration.js';

let userId

$(document).ready(function () {

    const token = sessionStorage.getItem('accessToken');

    // access token 만료 기간 검증 및 req header에 삽입
    if (token != null) {
        setupHeaderAjax(token)
        userId = getId(token);
    }

    var apiUrl1 = "/mypage/{user_id}/schedule/get";
    var apiUrl2 = "/mypage/{user_id}";
    var apiUrl3 = "/mypage/{user_id}/schedule";
    var apiUrl4 = "/mypage/{user_id}/history";

    apiUrl1 = apiUrl1.replace("{user_id}", userId);
    apiUrl2 = apiUrl2.replace("{user_id}", userId);
    apiUrl3 = apiUrl3.replace("{user_id}", userId);
    apiUrl4 = apiUrl4.replace("{user_id}", userId);

    var classUrl;
    // 변수선언
    var dataParse;
    $.ajax({
        type: "GET",
        url: apiUrl1,
        dataType: "json",
        async: false,
        success: function (data, status, xhr) {
            getHeaderAjax(xhr)

            $("#information").attr("href", apiUrl2);
            $("#schedule").attr("href", apiUrl3);
            $("#history").attr("href", apiUrl4);

            dataParse = JSON.parse(JSON.stringify(data));
            var tbody = $("#tutee-schedule");

            // 프로필 이름 출력
            $("#tutee-name").text(dataParse.tutee_name);

            // 스케줄 테이블 출력
            for (var i = 0; i < dataParse.schedules.length; i++) {
                var tutee = dataParse.schedules[i];

                classUrl = "/lesson/" + tutee.class_id;
                var tr = $("<tr>").css("padding", "0");
                var tno = $("<th scope=\"row\">")
                    .css("text-align", "center")
                    .css("vertical-align", "middle")
                    .text(i + 1);
                var classdate = $("<td>")
                    .css("text-align", "center")
                    .css("vertical-align", "middle")
                    .css("padding", "1")
                    .text(tutee.class_date + " " + tutee.class_time);
                var tutorname = $("<td>")
                    .css("text-align", "center")
                    .css("vertical-align", "middle")
                    .css("padding", "1")
                    .text(tutee.tutor_name);

                // 수업 입장 버튼
                // var goToClassBtn = $("<a>", {href: "http://localhost:3000" + classUrl +"?token=" + token, text: "수업입장", class: "btn btn-primary"});
                var tdBtn = $("<td>")
                    .css("text-align", "center")
                    .css("padding", "1")
                    .css("vertical-align", "middle");
                var goToClassBtn = $("<button>")
                    .addClass("btn btn-primary goToClassBtn")
                    .attr("id", tutee.class_id)
                    .text("수업입장")
                    .css("display", "inline-block")
                    .css("vertical-align", "middle");
                    // .css("text-align", "center");

                var reserveDate = tutee.class_date
                var todayDate = new Date();
                var todayDateYear = todayDate.getFullYear();
                var todayDateMonth = todayDate.getMonth() + 1;
                var todayDateDay = todayDate.getDate();

                if (todayDateMonth < 10) {
                    todayDateMonth = "0" + todayDateMonth;
                }
                if (todayDateDay < 10) {
                    todayDateDay = "0" + todayDateDay;
                }

                var formattedDate = `${todayDateYear}-${todayDateMonth}-${todayDateDay}`;

                console.log(formattedDate);

                // 예약 취소 버튼
                var cancelReservationBtn = $("<button>")
                    .addClass("btn btn-danger cancelBtn")
                    .attr("id", "cancelReservationBtn" + i)
                    .text("예약취소")
                    .attr("data-toggle", "modal")
                    .attr("data-target", "#modal-default")
                    .on("click", function (event) {
                        event.preventDefault();
                        var id = $(this).attr("id");

                        const inputString = id;
                        let letters = "";
                        let numbers = "";
                        for (let i = 0; i < inputString.length; i++) {
                            const char = inputString.charAt(i);
                            if (isNaN(char)) {
                                letters += char;
                            } else {
                                numbers += char;
                            }
                        }

                        // 예약 취소 버튼을 클릭하면 실행될 함수
                        function cancelReservation() {
                            if ($("#cancel-reservation-message").val() == "예약을 취소하겠습니다") {
                                var class_id = dataParse.schedules[numbers].class_id;

                                var postLink = "/mypage/{user_id}/schedule/cancel";
                                var apiUrl = postLink.replace("{user_id}", userId);

                                $.ajax({
                                    type: "PUT",
                                    url: apiUrl,
                                    contentType: "application/json",
                                    data: JSON.stringify({
                                        $class_id: class_id,
                                    }),
                                    success: function (data) {
                                        $("#modal-default").modal('hide'); // 모달 창 닫기
                                        $("#cancel-reservation-message").val("");
                                        // 실행창 초기화
                                        location.reload();

                                    },
                                    error: function (data) {
                                        alert("Error!")
                                    },
                                    complete: function (data, textStatus) {
                                    },
                                });
                            } else {
                                $("#cancel-reservation-message").focus();
                                return false;
                            }
                        }

                        // 모달을 띄우기 위한 속성 설정
                        $("#modal-default").modal({
                            show: true
                        });

                        // 모달 내부의 버튼 클릭 이벤트 핸들러 등록
                        $("#cancel-reservation").on("click", function (event) {
                            event.preventDefault();
                            cancelReservation();
                        });
                    });

                if (reserveDate == formattedDate) {
                    tr.append(tno, classdate, tutorname, tdBtn);
                    tdBtn.append(goToClassBtn);
                } else {
                    tr.append(tno, classdate, tutorname, tdBtn);
                    tdBtn.append(cancelReservationBtn);
                }
                tbody.append(tr);
            }
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        }
    });

    $("#reset").click(function () {
        $("#cancel-reservation-message").val("");
    });

    $(document).on('click', '.goToClassBtn', function (event) {
        event.preventDefault();
        var classId = $(this).attr("id");
        var admission_link = "http://localhost:3000" + "/lesson/" + classId + "?token=" + token;

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
        // alert(timeDiff);
        if(timeDiff < 0 || timeDiff < -30) {
            alert("수업 시작 전입니다.");
            return false;
        }
        else if(timeDiff > 30) {
            alert("수업이 종료되었습니다.");
            return false;
        }
        else if(timeDiff > 0 && timeDiff < 30) {
            // alert("수업 입장");
            location.href = admission_link;
        }
    });
});
