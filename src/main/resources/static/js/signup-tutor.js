// POST로 tutor 회원가입 정보 보내기
$("#create-account").click(function (event){
    event.preventDefault();
    var email = $('#email').val();
    var password = $('#password').val();
    var name = $('#name').val();
    var gender = $('input[name="genderRadios"]:checked').val();
    var nationality = $('#nationality').val();
    var firstInterest = $('#first-interest').val();
    var secondInterest = $('#second-interest').val();

    //var customFile = $('#customFile').val();
    // var fileInput = document.getElementById('customFile');
    // var customFile = fileInput.files[0];
    // if(customFile.files.length === 0){
    //     alert("파일은 선택해주세요");
    //     return;
    // }
    // var formData = new FormData();
    // formData.append('customFile', customFile);

    $.ajax({
        type: "POST",
        url: "/members/signup-tutors.post",
        contentType: 'application/json',
        data:JSON.stringify({
            $email : email,
            $password : password,
            $name : name,
            $gender :gender,
            $nationality : nationality,
            $firstInterest : firstInterest,
            $secondInterest : secondInterest,
        }),
        success: function(data, status) {
            console.log(data)
            if(data == "success"){
                alert("회원가입에 성공")
                window.location.href="/members/signup/success"} // 성공 시 main 페이지로 이동
            if(data == "fail"){
                alert("회원가입에 실패");
            }
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });


});