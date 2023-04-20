$("#loginForm").submit(function (event){
    event.preventDefault();
    var email = $("#email").val()
    var password = $("#password").val()

    $.ajax({
        type: "POST",
        url: "/members/signin.post",
        contentType: 'application/json',
        data:JSON.stringify({
            $email : email,
            $password : password,
            test :"test",
        }),
        success: function(data, status) {
            console.log(data)
            if(data ==="loginSuccess"){window.location.href="/main"} // 성공 시 main 페이지로 이동
            if(data ==="loginFail"){
                $("#loginFail").text("로그인 정보가 틀렸습니다")
                $("#email").val("")
                $("#password").val("")
            }


        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });
})