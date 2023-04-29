import { setupAjax } from './checkTokenExpiration.js';

$(document).ready(function () {

    // window.addEventListener('beforeunload', function() {
    //     // 저장하고자 하는 값 가져오기
    //     const accessToken = sessionStorage.getItem('accessToken');
    //     console.log(accessToken);
    //
    //     // sessionStorage에 값 저장
    //     sessionStorage.setItem('accessToken', accessToken);
    // });

    const token = sessionStorage.getItem('accessToken');
    console.log("mypage.js 전달 토큰: "+token);

    // access token 만료 기간 검증 및 req header에 삽입
    setupAjax(token)


    // 로그인 로그아웃 버튼 바꾸기
    if (token != null) {
        $('#sign-btn').text('SIGN OUT');
    }

    //mypage관련
    $.ajax({
        type: "GET",
        url: "/mypage/admin/get",
        dataType: "json",
        success: function (data, status) {
            $("#manage-tutor").attr("href","/mypage/admin");

            console.log(data);
            var tbody = $("#spare-tutor");
            for (var i = 0; i < data.length; i++) {
                var spare = data[i];
                var tr = $("<tr>");
                var tno = $("<th scope=\"row\">").text(i + 1);
                var tutorName = $("<td>", {text: spare.tutorName});
                var approlDate = $("<td>", {text: spare.approlDate});
                var file = $("<td>", {text: spare.file});

                var successBtn = $("<td><button>")
                    .addClass("btn btn-primary")
                    .attr("id", "successBtn" + i)
                    .text("승인")
                    .on("click", function () {
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
                        var userId = data[numbers].userId;

                        $.ajax({
                            type: "post",
                            url: "/mypage/admin/success",
                            contentType: "application/String",
                            data: userId,
                            success: function (data) {
                                window.location.href = "/mypage/admin";
                            },
                            error: function (data) {
                                alert("Error!")
                            },
                            complete: function (data, textStatus) {
                            },
                        });
                    });

                var failBtn = $("<td><button>")
                    .addClass("btn btn-danger")
                    .attr("id", "failBtn" + i)
                    .text("거부")
                    .on("click", function () {
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
                        var userId = data[numbers].userId;

                        $.ajax({
                            type: "post",
                            url: "/mypage/admin/fail",
                            contentType: "application/String",
                            data: userId,
                            success: function (data) {
                                window.location.href = "/mypage/admin";
                            },
                            error: function (data) {
                                alert("Error!")
                            },
                            complete: function (data, textStatus) {
                            },
                        });
                    });
                tr.append(tno);
                tr.append(tutorName);
                tr.append(approlDate);
                tr.append(file);
                tr.append(successBtn);
                tr.append(failBtn);
                tbody.append(tr);
            }
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });
});