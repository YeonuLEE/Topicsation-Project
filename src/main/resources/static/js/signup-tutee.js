// POST로 tutee 회원가입 정보 보내기
$(document).ready(function () {
    $("#signUpForm").on('submit',function(event) {
        event.preventDefault();
        var data = {
            email: $('input[name="email"]').val(),
            password: $('input[name="password"]').val(),
            passwordConfirm: $('input[name="passwordConfirm"]').val(),
            name: $('input[name="name"]').val(),
            firstInterest: $('select[name="firstInterest"]').val(),
            secondInterest: $('select[name="secondInterest"]').val(),
            role: 'tutee'
        };

        $.ajax({
            type: "POST",
            url: "/members/signup-tutees.post",
            contentType: 'application/json',
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function (data, status) {
                var email = btoa(data);
                sessionStorage.setItem("email", email);
                window.location.href = '/members/signup/email';
            },
            error: function (data, textStatus) {
                alert("Error!")
            },
            complete: function (data, textStatus) {
            },
        });
    });
});