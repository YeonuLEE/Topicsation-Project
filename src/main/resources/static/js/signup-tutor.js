// POST로 tutor 회원가입 정보 보내기
$(document).ready(function () {

    // 정규식 선언
    let regName = RegExp(/^[A-Za-z가-힣 ]+$/);
    let regEmail = RegExp(/^([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})$/);
    let regPwd = RegExp(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,12}$/);

// 변수 선언
    var emailCheck = true;
    var passwordCheck = true;
    var passwordConfirmCheck = true;
    var nameCheck = true;

    let selectedOption;

//이메일 유효성 검사
    $("#email").change(function () {
        if (!regEmail.test($("#email").val())) {
            $(".email").text("Invalid email format").css("color", "red");
            $("#email").attr("class", "form-control is-invalid");
            emailCheck = false;
        } else {
            $(".email").text("");
            $("#email").attr("class", "form-control is-valid");
            emailCheck = true;
        }
    });

//이름 유효성 검사
    $("#name").change(function () {
        if (!regName.test($("#name").val())) {
            $(".name").text("That is not a valid name.").css("color", "red");
            $("#name").attr("class", "form-control is-invalid");
            nameCheck = false;
        } else {
            $(".name").text("");
            $("#name").attr("class", "form-control is-valid");
            nameCheck = true;
        }
    });


// 비밀번호 유효성 검사
    $("#password").change(function () {
        var pwd1 = $("#password").val();
        if (!regPwd.test(pwd1)) {
            $(".password")
                .text("6-12 characters and numbers & characters.")
                .css("color", "red");
            $("#password").attr("class", "form-control is-invalid");
            passwordCheck = false;
        } else {
            $(".password").text("");
            $("#password-confirm").focus();
            $("#password").attr("class", "form-control is-valid");
            passwordCheck = true;
        }
    });

// 비밀번호 일치 여부 검사
    $("#password-confirm").change(function () {
        var pwd1 = $("#password").val();
        var pwd2 = $("#password-confirm").val();

        if (pwd1 != pwd2) {
            $(".password-confirm").text("Password does not match.").css("color", "red");
            $("#password-confirm").attr("class", "form-control is-invalid");
            passwordConfirmCheck = false;
        } else {
            $(".password-confirm").text("");
            $("#password-confirm").focus();
            $("#password-confirm").attr("class", "form-control is-valid");
            passwordConfirmCheck = true;
        }
    });

// 파일 이름 바꾸기
    $("#customFile").change(function () {
        var fileValue = $("#customFile").val().split("\\");
        var fileName = fileValue[fileValue.length - 1]; // 파일명
        $("#showFiles").text(fileName);
    });

    // 두번째 관심사 제거
    $("#first-interest").change(function () {
        // 남은 옵션 전체 삭제
        $("#second-interest option").remove();

        // 전체 옵션 다시 추가
        $("#second-interest").html(
            "<option value='politics'>Politics</option><option value='economics'>Economics</option><option value='IT'>IT</option><option value='fitness'>Fitness</option><option value='food'>Food</option>"
        );

        // 첫번째 관심사에서 뽑은 옵션 제거
        selectedOption = $("#first-interest option:selected").val();
        $("#second-interest")
            .find("option")
            .each(function () {
                if (this.value == selectedOption) {
                    $(this).remove();
                }
            });
    });

// 프로필 사진 수정
    $("#profile-img").hover(
        function () {
            // $(this).attr("src","./image/p002.jpg");
            $(this).css("opacity", 0.3);
            // $(".profile-text").css("position","absolute")
        },
        function () {
            $(this).css("opacity", 1);
        }
    );

// 사진 업로드 기능
    $("#profileImgButton").click(function () {
        $("#file").click();
        $("#profileImgButton").blur();
    });

    function uploadFile(e) {
        console.log("File Name : ", e.value);
    }

// 파일 이름 바꾸기
    $("#customFile").change(function () {
        var fileValue = $("#customFile").val().split("\\");
        var fileName = fileValue[fileValue.length - 1]; // 파일명
        $("#show-files").text(fileName);
    });

    $("#signUpForm").on('submit', function (event) {
// 유효성 검사 실패시 제출 안되게 하기
        if (!emailCheck) {
            $("#email").focus();
            return false;
        } else if (!passwordCheck) {
            $("#password").focus();
            return false;
        } else if (!passwordConfirmCheck) {
            $("#password-confirm").focus();
            return false;
        } else if (!nameCheck) {
            $("#name").focus();
            return false;
        }

        event.preventDefault();
        // var data = {
        //     email: $('input[name="email"]').val(),
        //     name: $('input[name="name"]').val(),
        //     password: $('input[name="password"]').val(),
        //     passwordConfirm: $('input[name="passwordConfirm"]').val(),
        //     // gender: $('input[name="gender"]').val(),
        //     gender: $('input[type="radio"][name="genderRadios"]:checked').val(),
        //     nationality: $('select[name="nationality"]').val(),
        //     firstInterest: $('select[name="firstInterest"]').val(),
        //     secondInterest: $('select[name="secondInterest"]').val(),
        //     role: 'tutor'
        // };

        // FormData 객체 생성
        const formData = new FormData();
        const form = $("#signUpForm")[0];

        // form의 모든 입력 값을 formData에 추가
        for (let i = 0; i < form.length - 1; i++) {
            if (form[i].type === "file") {
                formData.append(form[i].name, form[i].files[0]);
            } else {
                formData.append(form[i].name, form[i].value);
            }
        }
        alert(form)

        $.ajax({
            type: "POST",
            url: "/members/signup-tutors.post",
            enctype: "multipart/form-data",
            data: formData,
            processData: false, // processData를 false로 설정하여 jQuery가 데이터를 처리하지 않도록 함
            contentType: false, // contentType을 false로 설정하여 jQuery가 contentType을 설정하지 않도록 함
            success: function (data, status) {
                if (data === "signupFail") {
                    alert("Member who already exists.")
                } else {
                    var email = btoa(data);
                    sessionStorage.setItem("email", email);
                    window.location.href = '/members/signup/email';
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