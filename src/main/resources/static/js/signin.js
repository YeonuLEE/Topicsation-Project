$(document).ready(function() {
$("#loginForm").submit(function (event) {
    event.preventDefault();
    var email = $("#email").val()
    var password = $("#password").val()

    console.log(email);
    console.log(password);

    $.ajax({
        type: "POST",
        url: "/members/signin.post",
        contentType: 'application/json',
        data: JSON.stringify({
            email: email,
            password: password
        }),
        success: function (response, status) {
            console.log(response)
            var data = JSON.parse(response);
                if (data !== null) {
                    //accesstoken 저장
                    // sessionStorage.setItem("accessToken", data.accessToken);
                    // console.log(data.accessToken)


                    //refreshtoken 저장
                    document.cookie = "refreshToken=" + data.refreshToken + "; path=/; SameSite=Strict";
                    console.log(data.refreshToken)

                    history.pushState(null, null, "/main");
                    location.reload();
                } else {
                    console.log(status)
                    console.log(data)
                    $("#loginFail").text("로그인 정보가 틀렸습니다")
                    $("#email").val("").removeClass("is-valid").addClass("is-invalid")
                    $("#password").val("").removeClass("is-valid").addClass("is-invalid")
                }
            },
        error: function (data, textStatus) {
            console.log(textStatus)
            console.log(data)
            $("#loginFail").text("로그인 정보가 틀렸습니다")
            $("#email").val("").removeClass("is-valid").addClass("is-invalid")
            $("#password").val("").removeClass("is-valid").addClass("is-invalid")
            return false;
        },
    });
})
})