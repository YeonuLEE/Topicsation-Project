$(document).ready(function () {

        $("#send-reset-password").click(function (event) {
            event.preventDefault();

            let email = $("#email").val();

            $.ajax({
                type: "POST",
                url: "/members/singin/find/post",
                contentType: 'application/json',
                data: JSON.stringify({
                    email: email
                }),
                success: function (data, status) {
                    $("#emailCheck").text("Check your Email!").css("color", "green")
                    $("#enter-password").attr("class", "form-control is-valid");

                    $.ajax({
                        type: "POST",
                        url: "/members/signin/find/email.send",
                        contentType: 'application/json',
                        data: JSON.stringify({
                            email: email
                        }),
                        success: function (data, status) {
                            alert("이메일 전송 성공하였습니다.")
                        },
                        error: function (data, textStatus) {
                            alert("이메일 전송에 실패했습니다.")
                            location.href="/members/signin";
                        },
                        complete: function (data, textStatus) {
                        },
                    });
                },
                error: function (data, textStatus) {
                    $("#emailCheck").text("Not registed Email!").css("color", "#ff253a")
                },
                complete: function (data, textStatus) {
                },
            });
        });
});