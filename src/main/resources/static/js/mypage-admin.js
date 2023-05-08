import {setupHeaderAjax, getHeaderAjax, getId} from './checkTokenExpiration.js';
import { moveToErrorPage } from './error/MoveToErrorPage.js';

$(document).ready(function () {
    // AJAX 에러 처리기로 설정
    $.ajaxSetup({
        error: moveToErrorPage
    });

    let userId;
    // token 꺼내오기
    const token = sessionStorage.getItem('accessToken');

    // access token 만료 기간 검증 및 req header에 삽입
    if(token != null){
        // access token 만료 기간 검증 및 req header에 삽입
        setupHeaderAjax(token)
        userId = getId(token);
    }

    let apiUrl1 = "/mypage/{user_id}/get";
    apiUrl1 = apiUrl1.replace("{user_id}", userId);

    //mypage관련
    $.ajax({
        type: "GET",
        url: apiUrl1,
        dataType: "json",
        async:false,
        success: function (data, status, xhr) {
            getHeaderAjax(xhr)

            $("#manage-tutor").attr("href","/mypage/admin");

            let tbody = $("#spare-tutor");
            for (let i = 0; i < data.length; i++) {
                let spare = data[i];
                console.log(spare);
                let tr = $("<tr>");
                let tno = $("<th scope=\"row\">").text(i + 1);
                let tutorName = $("<td>", {text: spare.tutorName});
                let approlDate = $("<td>", {text: spare.approlDate});

                let link = $("<a>")
                    .text(spare.userId + ".pdf")
                    .attr("href", spare.file)
                    .attr("id", spare.userId);

                let file = $("<td>").append(link);

                let successBtn = $("<td><button>")
                    .addClass("btn btn-primary")
                    .attr("id", "successBtn" + i)
                    .text("승인")
                    .on("click", function () {
                        let id = $(this).attr("id");

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
                        let userId = data[numbers].userId;

                        $.ajax({
                            type: "post",
                            url: "/mypage/admin/success",
                            contentType: "application/String",
                            data: userId,
                            success: function (data) {
                                window.location.href = "/mypage/admin";
                            },
                            error: function (data) {
                                alert("튜터 승인에 실패했습니다. 테이블에서 튜터를 찾을 수 없습니다.")
                            }
                        });
                    });

                let failBtn = $("<td><button>")
                    .addClass("btn btn-danger")
                    .attr("id", "failBtn" + i)
                    .text("거부")
                    .on("click", function () {
                        let id = $(this).attr("id");

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
                        let userId = data[numbers].userId;

                        $.ajax({
                            type: "post",
                            url: "/mypage/admin/fail",
                            contentType: "application/String",
                            data: userId,
                            success: function (data) {
                                window.location.href = "/mypage/admin";
                            },
                            error: function (data) {
                                alert("튜터 거부에 실패했습니다. 테이블에서 튜터를 제거할 수 없습니다.")
                            }
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
        }
    });
});