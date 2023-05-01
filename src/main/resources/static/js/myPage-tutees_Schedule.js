import { setupHeaderAjax, getId } from './checkTokenExpiration.js';

var id = "cancelReservationBtn";
$(document).ready(function () {

    const token = sessionStorage.getItem('accessToken');
    console.log(token)

    // access token 만료 기간 검증 및 req header에 삽입
    setupHeaderAjax(token)

    let userId = getId(token);

    // var pathURI = window.location.pathname
    // const regex = /\/mypage\/(\d+)\/schedule/;
    // const match = pathURI.match(regex);
    // const userId= match[1];

    var apiUrl1 = "/mypage/{user_id}/schedule/get";
    var apiUrl2 = "/mypage/{user_id}";
    var apiUrl3 = "/mypage/{user_id}/schedule";
    var apiUrl4 = "/mypage/{user_id}/history";

    apiUrl1 = apiUrl1.replace("{user_id}", userId);
    apiUrl2 = apiUrl2.replace("{user_id}", userId);
    apiUrl3 = apiUrl3.replace("{user_id}", userId);
    apiUrl4 = apiUrl4.replace("{user_id}", userId);

    // 변수선언
    var dataParse;
    $.ajax({
        type: "GET",
        url: apiUrl1,
        dataType: "json",
        success: function (data, status) {

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

                var classUrl = "/lesson/" + tutee.class_id;
                var tr = $("<tr>");
                var tno = $("<th scope=\"row\">").css("text-align", "center").text(i + 1);
                var classdate = $("<td>").css("text-align", "center").text(tutee.class_date + " " + tutee.class_time);
                var tutorname = $("<td>").css("text-align", "center").text(tutee.tutor_name);

                // 수업 입장 버튼
                var goToClassBtn = $("<a>", {href: "http://localhost:3000" + classUrl +"?token=" + token, text: "수업입장", class: "btn btn-primary"});

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
                tr.append(tno, classdate, tutorname, goToClassBtn, cancelReservationBtn);
                tbody.append(tr);
            }
        },

        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        }
    });

    $("#reset").click(function (){
        $("#cancel-reservation-message").val("");
    });
});
