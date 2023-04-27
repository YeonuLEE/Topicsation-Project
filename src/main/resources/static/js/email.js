$("#emailTokenForm").submit(function () {
    const token = $("#token-btn").val();

    // 세션 스토리지에서 값 받아오기
    var emailForm = sessionStorage.getItem("emailForm");
    var nameForm = sessionStorage.getItem("nameForm");
    var passwordForm = sessionStorage.getItem("passwordForm");
    var firstInterestForm = sessionStorage.getItem("firstInterestForm");
    var secondInterestForm = sessionStorage.getItem("secondInterestForm");

    $.ajax({
        type: "POST",
        url: "/members/email.auth",
        contentType: 'application/json',
        data: JSON.stringify({
            $token: token,
            $emailForm: emailForm,
            $nameForm: nameForm,
            $passwordForm: passwordForm,
            $firstInterestForm: firstInterestForm,
            $secondInterestForm: secondInterestForm,
            test: "test",
        }),
        success: function (data, status) {

            window.location.href = "/members/signup/success" // 성공 시 success 페이지로 이동
            // 로그인 하는 사람 체크 할 거 : 서버에서 token 비교하고 일치하지 않을 시에 넘어가면 안되는데 이걸
            //아래의 erorr: function으로 처리할지 success에서 if문을 사용해서 처리할지 백엔드를 구현해야 정확하게 알 거같음
            // 일단 일치 하지 않으면 alert("인증 번호가 일치하지 않습니다!!") 띄우면 될 듯함

        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });
})