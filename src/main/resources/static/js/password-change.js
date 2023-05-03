$(document).ready(function () {

    history.pushState(null, null, "/members/change");

    $("#changePasswordForm").submit(function (event) {
        event.preventDefault();
        var newPassword = $("#password").val()
        var newCofirmPassword = $("#password-confirm").val()

        if(newPassword === newCofirmPassword) {

            $.ajax({
                type: "POST",
                url: "/members/signin/change.post",
                contentType: 'application/json',
                data: JSON.stringify({
                    password: newCofirmPassword,
                }),
                success: function (data, status) {
                    alert("비밀번호가 수정되었습니다. 로그인해주세요.")
                    location.href = "/members/signin";
                },
                error: function (data, textStatus) {
                    alert("비밀번호 수정에 실패했습니다. 다시 시도해주세요.")
                    location.href = "/members/signin/change";
                },
                complete: function (data, textStatus) {
                },
            });
        }
        else {
            alert("비밀번호가 일치하지 않습니다. 다시 시도해주세요.\nPassword does not match!")
        }
    });
})