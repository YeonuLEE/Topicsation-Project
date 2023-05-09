import {setupHeaderAjax, getId, getHeaderAjax} from './checkTokenExpiration.js';
import {moveToErrorPage} from "./error/MoveToErrorPage.js";

$(document).ready(function () {

    $.ajaxSetup({
        error: moveToErrorPage
    });

    let userId
    const token = sessionStorage.getItem('accessToken');

    // access token 만료 기간 검증 및 req header에 삽입
    if (token != null) {
        setupHeaderAjax(token)
        userId = getId(token);
    }

    let apiUrl = "/mypage/{user_id}";
    apiUrl = apiUrl.replace("{user_id}", userId);

    // 변수선언
    let dataParse;
    $.ajax({
        type: "GET",
        url: apiUrl + "/schedule/get",
        dataType: "json",
        async: false,
        success: function (data, status, xhr) {
            getHeaderAjax(xhr)

            $("#information").attr("href", apiUrl);
            $("#schedule").attr("href", apiUrl + "/schedule");
            $("#history").attr("href", apiUrl + "/history");

            dataParse = JSON.parse(JSON.stringify(data));
            let tbody = $("#tutee-schedule");

            // 프로필 이름 출력
            $("#tutee-name").text(dataParse.tutee_name);

            // 스케줄 테이블 출력
            for (let i = 0; i < dataParse.schedules.length; i++) {
                let tutee = dataParse.schedules[i];

                let tr = $("<tr>").css("padding", "0");
                let tno = $("<th scope=\"row\">")
                    .css("text-align", "center")
                    .css("vertical-align", "middle")
                    .text(i + 1);
                let classdate = $("<td>")
                    .css("text-align", "center")
                    .css("vertical-align", "middle")
                    .css("padding", "1")
                    .text(tutee.class_date + " " + tutee.class_time);
                let tutorname = $("<td>")
                    .css("text-align", "center")
                    .css("vertical-align", "middle")
                    .css("padding", "1")
                    .text(tutee.tutor_name);

                // 수업 입장 버튼
                let tdBtn = $("<td>")
                    .css("text-align", "center")
                    .css("padding", "1")
                    .css("vertical-align", "middle");
                let goToClassBtn = $("<button>")
                    .addClass("btn btn-primary goToClassBtn")
                    .attr("id", tutee.class_id)
                    .text("수업입장")
                    .css("display", "inline-block")
                    .css("vertical-align", "middle");
                    // .css("text-align", "center");

                let reserveDate = tutee.class_date
                let todayDate = new Date();
                let todayDateYear = todayDate.getFullYear();
                let todayDateMonth = todayDate.getMonth() + 1;
                let todayDateDay = todayDate.getDate();

                if (todayDateMonth < 10) {
                    todayDateMonth = "0" + todayDateMonth;
                }
                if (todayDateDay < 10) {
                    todayDateDay = "0" + todayDateDay;
                }

                let formattedDate = `${todayDateYear}-${todayDateMonth}-${todayDateDay}`;

                // 예약 취소 버튼
                let cancelReservationBtn = $("<button>")
                    .addClass("btn btn-danger cancelBtn")
                    .attr("id", "cancelReservationBtn" + i)
                    .text("예약취소")
                    .attr("data-toggle", "modal")
                    .attr("data-target", "#modal-default")
                    .on("click", function (event) {
                        event.preventDefault();
                        let id = $(this).attr("id");

                        const inputString = id;
                        var letters = "";
                        var numbers = "";
                        for (let i = 0; i < inputString.length; i++) {
                            const char = inputString.charAt(i);
                            if (isNaN(char)) {
                                letters += char;
                            } else {
                                numbers += char;
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

                        // 예약 취소 버튼을 클릭하면 실행될 함수
                        function cancelReservation() {
                            if ($("#cancel-reservation-message").val() == "예약을 취소하겠습니다") {
                                let class_id = dataParse.schedules[numbers].class_id;
                                ``
                                let postLink = "/mypage/{user_id}/schedule/cancel";
                                let apiUrl = postLink.replace("{user_id}", userId);

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
        }
    });

    $("#reset").click(function () {
        $("#cancel-reservation-message").val("");
    });

    $(document).on('click', '.goToClassBtn', function (event) {
        event.preventDefault();
        let classId = $(this).attr("id");
        let admission_link = "http://115.85.183.164:3000" + "/lesson/" + classId + "?token=" + token;

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

        if(timeDiff < 0 || timeDiff < -30) {
            alert("수업 시작 전입니다.");
            return false;
        }
        else if(timeDiff > 30) {
            alert("수업이 종료되었습니다.");
            return false;
        }
        else if(timeDiff > 0 && timeDiff < 30) {
            location.href = admission_link;
        }
    });



});

