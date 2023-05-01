$(document).ready(function () {


    // var token = sessionStorage.getItem('token');
    // // console.log(token)
    // if (token != null) {
    //     $.ajaxSetup({
    //         beforeSend: function(xhr) {
    //             xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    //         }
    //         // headers: {
    //         //     'Authorization': 'Bearer' + token
    //         // }
    //     });
    //     $('#sign-btn').text('SIGN OUT');
    // }

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

                var pathURI = spare.file
                const regex = /\/certificate\/([a-zA-Z0-9]+)\.pdf$/;
                let userFile = null;
                if (regex.test(pathURI)) {
                    const match = pathURI.match(regex);
                    userFile = match[1];
                } else {
                    console.log("패턴에 일치하는 값이 없습니다.");
                }
                userFile = userFile+".pdf"; // 파일 이름
                console.log(userFile);
                var file = $("<td><a>")
                    .text(userFile)
                    .attr("id",spare.file)
                    .on("click",function (){
                        var url = "/mypage/download/{fileName}";
                        url = url.replace("{fileName}",userFile);
                        console.log(url);

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