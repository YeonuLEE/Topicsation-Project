$(document).ready(function () {

    // 변수 초기화
    var apiUrl = "http://localhost:8081/lesson/{lesson_id}";

    const url = window.location.pathname;
    const regex = /\/lesson\/(.*?)\//; // 정규 표현식
    const match = url.match(regex);

    if (match && match[1]) {
        var classId = match[1];
    } else {
        console.log("매칭되는 문자열이 없습니다.");
    }

    apiUrl = apiUrl.replace("{lesson_id}", classId);


    window.onload = function () {
        setTimeout(function () {
            window.opener.location.href = "http://localhost:8081/main";
        }, 1000);
    };

    $('#like-btn').click(function () {
        $.ajax({
            url: apiUrl + "/evaluate.do",
            type: "PUT",
            contentType: 'application/json',
            data: JSON.stringify({
                $evaluate: 'like',
                $lesson_id: classId,
            }),
            success: function (result) {
                window.close();
            },
            error: function (xhr, status, error) {
                alert("좋아요에 실패했습니다!")
                console.error(error);
            }
        });
    });

    $('#dislike-btn').click(function () {
        $.ajax({
            url:  apiUrl + "/evaluate.do",
            type: "PUT",
            contentType: 'application/json',
            data: JSON.stringify({
                $evaluate: 'dislike',
                $lesson_id: classId,
            }),
            success: function (result) {
                window.close();
            },
            error: function (xhr, status, error) {
                alert("싫어요에 실패했습니다!")
                console.error(error);
            }
        });
    });
});