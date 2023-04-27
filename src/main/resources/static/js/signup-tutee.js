// POST로 tutee 회원가입 정보 보내기
$(document).ready(function () {
    $("#signUpForm").on('submit',function(event) {
        event.preventDefault();

        // var email = $('#email').val();
        // var password = $('#password').val();
        // var passwordConfirm = $('#password-confirm').val();
        // var name = $('#name').val();
        // var firstInterest = $('#first-interest').val();
        // var secondInterest = $('#second-interest').val();

        // var formData = $("#signUpForm").serialize();

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
                console.log(data)
                if (data === "signupSuccess") {
                    window.location.href = "/members/signup/email"
                }
                if (data === "signupFail") {
                    alert("회원가입에 실패하셨습니다.");
                }
            },
            error: function (data, textStatus) {
                alert("Error!")
            },
            complete: function (data, textStatus) {
            },
        });
    });
});