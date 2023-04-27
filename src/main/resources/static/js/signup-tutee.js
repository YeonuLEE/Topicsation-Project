// POST로 tutee 회원가입 정보 보내기
$("#create-account").click(function () {
    var email = $('#email').val();
    var password = $('#password').val();
    var passwordConfirm = $('#password-confirm').val();
    var name = $('#name').val();
    var firstInterest = $('#first-interest').val();
    var secondInterest = $('#second-interest').val();

    $.ajax({
        type: "POST",
        url: "/members/signup-tutees.post",
        contentType: 'application/json',
        data: JSON.stringify({
            $email: email,
            $password: password,
            $passwordConfirm: passwordConfirm,
            $name: name,
            $firstInterest: firstInterest,
            $secondInterest: secondInterest,
        }),
        success: function (data, status) {
            console.log(data)
            if (data === "signupSuccess") {
                window.location.href = "/members/signup/email"
            } // 성공 시 main 페이지로 이동
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