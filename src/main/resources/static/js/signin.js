$(document).ready(function() {
$("#loginForm").submit(function (event) {
    event.preventDefault();
    var email = $("#email").val();
    var password = $("#password").val();

    $.ajax({
        type: "POST",
        url: "/members/signin.post",
        contentType: 'application/json',
        data: JSON.stringify({
            email: email,
            password: password
        }),
        success: function (data, textStatus, xhr) {
            //accesstoken 뽑아내기
            const authorization = xhr.getResponseHeader("Authorization");
            const accessToken = authorization.substring(7);
            //accesstoken 저장
            sessionStorage.setItem("accessToken", accessToken);

            //현재 페이지의 URL을 /main으로 변경하고, 변경된 URL을 새로고침
            history.pushState(null, null, "/main");
            location.reload();
            },
        error: function (data, textStatus) {
            $("#loginFail").text("로그인 정보가 틀렸습니다")
            $("#email").val("").removeClass("is-valid").addClass("is-invalid").focus()
            $("#password").val("").removeClass("is-valid").addClass("is-invalid")
            return false;
        },
    });
})
})