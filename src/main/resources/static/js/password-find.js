$(document).ready(function () {

    $("#email").click(function () {

        var user_id = "1234";
        var email = "kk@gmail.com";

        $.ajax({
            type: "POST",
            url: "/members/singin/find/post",
            contentType: 'application/json',
            data: JSON.stringify({
                $user_id: user_id,
                $email: email,
                test: "test"
            }),
            success: function (data, status) {
                console.log(data)
            },
            error: function (data, textStatus) {
                alert("Error!")
            },
            complete: function (data, textStatus) {
            },
        });
    });
});