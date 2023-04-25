$(document).ready(function () {

    $("#changePasswordForm").submit(function (event) {
        event.preventDefault();
        var password = $("#password").val()
        var confirmPassword = $("#password_confirm").val()

        $.ajax({
            type: "POST",
            url: "/members/signin/change.post",
            contentType: 'application/json',
            data:JSON.stringify({
                $password : password,
                $confirmPassword : confirmPassword,
                test :"test",
            }),
            success: function(data, status) {
                console.log(data)
            },
            error: function (data, textStatus) {
                alert("Error!")
            },
            complete: function (data, textStatus) {
            },
        });
    });
})