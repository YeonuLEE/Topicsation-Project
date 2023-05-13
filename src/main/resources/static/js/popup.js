$(document).ready(function () {
    let datatitle = $("#evaluate-h3");
    let databody = $("#evaluate-div");
    // 변수 초기화

    let apiUrl = "https://www.topicsation.site/lesson/{lesson_id}";
    let classId

    const url = window.location.pathname;
    const regex = /\/lesson\/(.*?)\//; // 정규 표현식
    const match = url.match(regex);

    if (match && match[1]) {
        classId = match[1];
    } else {
        console.log("매칭되는 문자열이 없습니다.");
    }

    apiUrl = apiUrl.replace("{lesson_id}", classId);

    window.onload = function () {
        setTimeout(function () {
            window.opener.location.href = "https://www.topicsation.site/main";
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
                review_add();
            },
            error: function (xhr, status, error) {
                alert("좋아요에 실패했습니다!")
            }
        });
    });

    $('#dislike-btn').click(function () {
        $.ajax({
            url: apiUrl + "/evaluate.do",
            type: "PUT",
            contentType: 'application/json',
            data: JSON.stringify({
                $evaluate: 'dislike',
                $lesson_id: classId,
            }),
            success: function (result) {
                review_add();
            },
            error: function (xhr, status, error) {
                alert("싫어요에 실패했습니다!");
            }
        });
    });

    function review_add() {
        let div1 = $("<div>", {class: "form-group"});
        let textarea1 = $("<textarea>", {
            class: "form-control",
            id: "review-content",
            rows: "3",
            maxLength: "120"
        });
        let button = $("<button>", {
            type: "button",
            class: "btn mt-3 mb-2 mr-2 btn-primary form-control",
            id: "review-btn",
            text: "리뷰 등록하기"
        });

        databody.empty();
        datatitle.text("리뷰를 남겨주세요!");
        databody.append(div1);
        div1.append(textarea1);
        div1.append(button);
        // textarea 태그 요소를 참조합니다.
        const textarea = document.getElementById("review-content");

        // maxlength 속성값을 가져옵니다.
        const maxLength = textarea.getAttribute("maxlength");

        // 입력 길이를 검사하는 함수를 작성합니다.
        function checkLength() {
            // 입력된 텍스트의 길이를 계산합니다.
            const textLength = textarea.value.length;

            // 입력 길이가 maxlength보다 큰 경우, 입력을 막습니다.
            if (textLength > maxLength) {
                textarea.value = textarea.value.substring(0, maxLength);
            }
        }

        // 입력이 발생할 때마다 checkLength 함수를 실행합니다.
        textarea.addEventListener("input", checkLength);
        $("#review-btn").click(function () {
            $.ajax({
                url: apiUrl + "/evaluate.review",
                type: "POST",
                contentType: 'application/json',
                data: JSON.stringify({
                    $review_content: $("#review-content").val(),
                    $lesson_id: classId,
                }),
                success: function (result) {
                    alert("리뷰 등록 완료");
                    window.close();
                },
                error: function (xhr, status, error) {
                    alert("리뷰 등록에 실패했습니다!");
                }
            });
        });
    }
});


