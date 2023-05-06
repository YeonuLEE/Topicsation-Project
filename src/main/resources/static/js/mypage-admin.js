import {setupHeaderAjax, getHeaderAjax, getId} from './checkTokenExpiration.js';


$(document).ready(function () {

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
                let tr = $("<tr>");
                let tno = $("<th scope=\"row\">").text(i + 1);
                let tutorName = $("<td>", {text: spare.tutorName});
                let approlDate = $("<td>", {text: spare.approlDate});

                let pathURI = spare.file
                const regex = /\/certificate\/([a-zA-Z0-9]+)\.pdf$/;
                let userFile = null;
                if (regex.test(pathURI)) {
                    const match = pathURI.match(regex);
                    userFile = match[1];
                } else {
                    console.log("패턴에 일치하는 값이 없습니다.");
                }
                userFile = userFile+".pdf"; // 파일 이름
                let file = $("<td><a>")
                    .text(userFile)
                    .attr("id",spare.file)
                    .on("click",function (){
                        let url = "/mypage/download/{fileName}";
                        url = url.replace("{fileName}",userFile);

                        $.ajax({
                            url: url, // Spring Boot 애플리케이션의 다운로드 엔드포인트 URL
                            method: "GET",
                            xhrFields: {
                                responseType: "blob"
                            },
                            success: function (data, status, xhr) {
                                let fileName = xhr.getResponseHeader("Content-Disposition").split("filename=")[1].replace(/"/g, "");
                                let downloadUrl = URL.createObjectURL(data);

                                let link = document.createElement("a");
                                link.href = downloadUrl;
                                link.download = fileName;
                                document.body.appendChild(link);
                                link.click();
                                document.body.removeChild(link);
                            },
                            error: function (xhr, status, error) {
                                console.log("File download failed:", error);
                            }
                        });
                    });

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
                                alert("Error!")
                            },
                            complete: function (data, textStatus) {
                            },
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