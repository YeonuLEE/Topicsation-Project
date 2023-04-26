var id = "cancelReservationBtn";
$(document).ready(function () {
    // 변수선언
    var dataParse;
    $.ajax({
        type: "GET",
        url: "/mypage/{tutor_id}/schedule/get",
        dataType: "json",
        success: function (data, status) {
            dataParse = JSON.parse(JSON.stringify(data));
            console.log(dataParse);
            console.log(status);
            var tbody = $("#tutee-schedule");

            // 프로필 이름 출력
            $("#tutee-name").text(dataParse[0].tutee_name);

            // 스케줄 테이블 출력
            for (var i = 0; i < dataParse[0].schedules.length; i++) {
                var tutee = dataParse[0].schedules[i];
                console.log("tutee : " + tutee.class_id);
                classId_Val = tutee.class_id;
                var classUrl = "/lesson/" + tutee.class_id;
                var tr = $("<tr>");
                var tno = $("<th scope=\"row\">").css("text-align", "center").text(i + 1);
                var classdate = $("<td>").css("text-align", "center").text(tutee.class_date + " " + tutee.class_time);
                var tutorname = $("<td>").css("text-align", "center").text(tutee.tutor_name);

                // 수업 입장 버튼
                var goToClassBtn = $("<a>", {href: classUrl, text: "수업입장", class: "btn btn-primary"});

                // 예약 취소 버튼
                var cancelReservationBtn = $("<button>")
                    .addClass("btn btn-danger cancelBtn")
                    .attr("id", "cancelReservationBtn" + i)
                    .text("예약취소")
                    .attr("data-toggle", "modal")
                    .attr("data-target", "#modal-default")
                    .on("click", function (event) {
                        event.preventDefault();


                        // 예약 취소 버튼을 클릭하면 실행될 함수
                        function cancelReservation() {
                            if ($("#cancel-reservation-message").val() == "예약을 취소하겠습니다") {
                                var user_id = dataParse[0].user_id;
                                // var index = parseInt(id.replace("cancelReservationBtn", ""));
                                var class_id = dataParse[0].schedules[0].class_id;
                                console.log("user_id : " + user_id)
                                console.log("class_id : " + class_id)
                                var postLink = "/mypage/{user_id}/schedule/cancel";
                                var apiUrl = postLink.replace("{user_id}", user_id);
                                alert(apiUrl);

                                $.ajax({
                                    type: "PUT",
                                    url: apiUrl,
                                    contentType: "application/json",
                                    data: JSON.stringify({
                                        $class_id: class_id,
                                    }),
                                    success: function (data) {
                                        $("#modal-default").modal('hide'); // 모달 창 닫기
                                        console.log(data);
                                        $("#cancel-reservation-message").val("");
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
                console.log(tutee.class_date);
                console.log(tutee.tutor_name);
                console.log($('.cancelBtn').attr("id"));
            }
        },

        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        }
    });
});