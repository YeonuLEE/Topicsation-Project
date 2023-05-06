// POST로 tutor 회원가입 정보 보내기
$(document).ready(function () {

    // 정규식 선언
    let regName = RegExp(/^[A-Za-z가-힣 ]+$/);
    let regEmail = RegExp(/^([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})$/);
    let regPwd = RegExp(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,12}$/);

    // 변수 선언
    let emailCheck = true;
    let passwordCheck = true;
    let passwordConfirmCheck = true;
    let nameCheck = true;

    let fileValue;
    let fileName;
    
    let firstSelected;
    let secondSelected;

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
        let password = $("#password").val();

        if (!regPwd.test(password)) {
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
        let password = $("#password").val();
        let confirmedPass = $("#password-confirm").val();

        if (password != confirmedPass) {
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

    // 튜터 인증 파일 이름 바꾸기
    $("#customFile").change(function () {
        fileValue = $("#customFile").val().split("\\");
        fileName = fileValue[fileValue.length - 1]; // 파일명

        $("#show-files").text(fileName);
    });

    // 첫번째 관심사 선택
    $("#first-interest").change(function () {
        firstSelected = $("#first-interest option:selected").val();

        // 남은 옵션 전체 삭제
        $("#second-interest option").remove();

        // 전체 옵션 다시 추가
        $("#second-interest").html(
            " <option value=\"\" disabled selected style=\"display: none\">First Interest</option><option value='business'>Business</option><option value='tech'>Tech</option><option value='science'>Science</option><option value='entertainment'>Entertainment</option><option value='health'>Health</option>"
        );
        // 첫번째 관심사에서 뽑은 옵션 제거
        $("#second-interest").find("option").each(function () {

            if (this.value == firstSelected) {
                $(this).remove();
            }
        });
        $("#second-interest").val(secondSelected);
    });
    // 두번째 관심사 선택
    $("#second-interest").change(function () {
        secondSelected = $("#second-interest option:selected").val();

        // 남은 옵션 전체 삭제
        $("#first-interest option").remove();

        // 전체 옵션 다시 추가
        $("#first-interest").html(
            " <option value=\"\" disabled selected style=\"display: none\">Second Interest</option><option value='business'>Business</option><option value='tech'>Tech</option><option value='science'>Science</option><option value='entertainment'>Entertainment</option><option value='health'>Health</option>"
        );
        // 두번째 관심사에서 뽑은 옵션 제거
        $("#first-interest").find("option").each(function () {
            if (this.value == secondSelected) {
                $(this).remove();
            }
        });
        $("#first-interest").val(firstSelected);
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

        const formData = new FormData(this);
        formData.append("role", "tutor");

        $.ajax({
            type: "POST",
            url: "/members/signup-tutors.post",
            data: formData,
            processData: false, // processData를 false로 설정하여 jQuery가 데이터를 처리하지 않도록 함
            contentType: false, // contentType을 false로 설정하여 jQuery가 contentType을 설정하지 않도록 함
            success: function (data, status) {
                let email = btoa(data);
                sessionStorage.setItem("email", email);
                window.location.href = '/members/signup/email';
            },
            error: function (data, textStatus) {
                alert("Member who already exists.")
            },
            complete: function (data, textStatus) {
            },
        });
    });
});