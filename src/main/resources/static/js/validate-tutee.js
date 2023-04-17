// 정규식 선언
let regName = RegExp(/^[A-Za-z가-힣 ]+$/);
let regEmail = RegExp(/^([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})$/);
let regPwd = RegExp(/^[a-zA-Z0-9]{6,12}$/);

// 변수 선언
var emailCheck = true;
var passwordCheck = true;
var passwordConfirmCheck = true;
var nameCheck = true;

let selectedOption;

//이메일 유효성 검사
$("#email").change(function () {
    if (!regEmail.test($("#email").val())) {
        $(".email").text("유효하지 않은 이메일 양식입니다.").css("color", "red");
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
        $(".name").text("유효하지 않은 이름 양식입니다.").css("color", "red");
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
            .text("비밀번호는 영문, 숫자포함 6-12자여야합니다.")
            .css("color", "red");
        $("#password").attr("class", "form-control is-invalid");
        passwordCheck = false;
    } else {
        $(".password").text("");
        $("#password_confirm").focus();
        $("#password").attr("class", "form-control is-valid");
        passwordCheck = true;
    }
});

// 비밀번호 일치 여부 검사
$("#password_confirm").change(function () {
    var pwd1 = $("#password").val();
    var pwd2 = $("#password_confirm").val();

    if (pwd1 != pwd2) {
        $(".password_confirm")
            .text("비밀번호가 일치하지 않습니다.")
            .css("color", "red");
        $("#password_confirm").attr("class", "form-control is-invalid");
        passwordConfirmCheck = false;
    } else {
        $(".password_confirm").text("");
        $("#password_confirm").focus();
        $("#password_confirm").attr("class", "form-control is-valid");
        passwordConfirmCheck = true;
    }
});

// 파일 이름 바꾸기
$("#customFile").change(function () {
    var fileValue = $("#customFile").val().split("\\");
    var fileName = fileValue[fileValue.length - 1]; // 파일명
    $("#showFiles").text(fileName);
});

// 유효성 검사 실패시 제출 안되게 하기
$("#signInForm").submit(function () {
    if (!emailCheck) {
        $("#email").focus();
        return false;
    } else if (!passwordCheck) {
        $("#password").focus();
        return false;
    } else if (!passwordConfirmCheck) {
        $("#password_confirm").focus();
        return false;
    } else if (!nameCheck) {
        $("#name").focus();
        return false;
    }
    return true;
});

// 두번째 관심사 제거
$("#firstInterestSelect").change(function () {
    // 남은 옵션 전체 삭제
    $("#secondInterestSelect option").remove();

    // 전체 옵션 다시 추가
    $("#secondInterestSelect").html(
        "<option value='politics'>정치</option><option value='economics'>경제</option><option value='IT'>IT</option><option value='fitness'>건강</option><option value='food'>음식</option>"
    );

    // 첫번째 관심사에서 뽑은 옵션 제거
    selectedOption = $("#firstInterestSelect option:selected").val();
    $("#secondInterestSelect")
        .find("option")
        .each(function () {
            if (this.value == selectedOption) {
                $(this).remove();
            }
        });
});

// "예약을 취소하겠습니다" 유효성 검사
$("#cancelReservation").click(function () {
    if ($("#cancelReservationMessage").val() != "예약을 취소하겠습니다") {
        $("#cancelReservationMessage").focus();
        return false;
    }
    return true;
});
