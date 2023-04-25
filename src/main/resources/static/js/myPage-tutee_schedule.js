$(document).ready(function() {
    var pathURI = window.location.pathname
    const regex = /\/mypage\/(\d+)\/schedule/;
    const match = pathURI.match(regex);
    const userId= match[1];
    console.log(userId)
    var apiUrl1 = "/mypage/{tutor_id}/schedule/get";
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
        dataType:"json",
        success: function(data, status) {

            $("#information").attr("href", apiUrl2);
            $("#schedule").attr("href", apiUrl3);
            $("#history").attr("href", apiUrl4);

            dataParse = JSON.parse(JSON.stringify(data));
            console.log(dataParse);
            console.log(status);
            var tbody = $("#tutee-schedule");

            // 프로필 이름 출력
            $("#tutee-name").text(dataParse[0].tutee_name);

            // 스케줄 테이블 출력
            for(var i = 0; i < dataParse[0].schedules.length; i++) {
                var tutee = dataParse[0].schedules[i];
                var classUrl = "/lesson/" + tutee.class_id;
                var tr = $("<tr>");
                var tno = $("<th scope=\"row\">").css("text-align", "center").text(i + 1);
                var classdate = $("<td>").css("text-align", "center").text(tutee.class_date +" "+tutee.class_time);
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
                    .on("click", function(event) {
                            event.preventDefault();
                        }
                    );
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

    var id;
    $('button').click(function() {
        id = $(this).attr('id');
        alert(id);
    });

    $("#" + id).click(function(event) {
        event.preventDefault();
        // $("#modal-default").modal();

        alert("skd");
        // 예약을 취소하겠습니다 입력 확인
        if ($("#cancel-reservation-massage").val() === "예약을 취소하겠습니다") {
            var user_id = dataParse[0].user_id;
            var class_id = dataParse[0].class_id;
            var postLink = "mypage/{user_id}/schedule/cancel";
            var apiUrl = postLink.replace("{user_id}", user_id);
            alert(apiUrl);

            $("#cancel-reservation").click(function(event){
                event.preventDefault();
                $.ajax({
                    type: "PUT",
                    url: apiUrl,
                    contentType: "application/json",
                    data: JSON.stringify({$class_id: class_id}),
                    success: function(data) {
                        console.log(data);
                    },
                    error: function(data){
                        alert("Error!")
                    },
                    complete: function (data, textStatus) {
                    }
                });
            })
        } else {
            $("#cancel-reservation-massage").focus();
            return false;
        }
    })

});