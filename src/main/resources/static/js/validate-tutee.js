// 정규식 선언
let regName = RegExp(/^[A-Za-z가-힣 ]+$/);
let regEmail = RegExp(/^([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})$/);
let regPwd = RegExp(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,12}$/);

// 변수 선언
let emailCheck = true;
let passwordCheck = true;
let passwordConfirmCheck = true;
let nameCheck = true;

let selectOption;
let selectedOption;

let fileValue;
let fileName;

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
    let password = $("#password").val();
    if (!regPwd.test(password)) {
        $(".password")
            .text("비밀번호는 영문, 숫자포함 6-12자여야합니다.")
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
    let confirmedPassword = $("#password-confirm").val();

    if (password !== confirmedPassword) {
        $(".password_confirm")
            .text("비밀번호가 일치하지 않습니다.")
            .css("color", "red");
        $("#password-confirm").attr("class", "form-control is-invalid");
        passwordConfirmCheck = false;
    } else {
        $(".password_confirm").text("");
        $("#password-confirm").focus();
        $("#password-confirm").attr("class", "form-control is-valid");
        passwordConfirmCheck = true;
    }
});

// 파일 이름 바꾸기
$("#customFile").change(function () {
    fileValue = $("#customFile").val().split("\\");
    fileName = fileValue[fileValue.length - 1]; // 파일명
    $("#showFiles").text(fileName);
});

// 유효성 검사 실패시 제출 안되게 하기
$("#signUpForm").submit(function () {
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
    return true;
});

// 두번째 관심사 제거
$("#first-interest").change(function () {
    selectOption = $("#first-interest option:selected").val();
    selectedOption = $("#second-interest option:selected").val();

    if(selectedOption == selectOption){

        // 남은 옵션 전체 삭제
        $("#second-interest option").remove();

        // 전체 옵션 다시 추가
        $("#second-interest").html(
            "<option value='business'>비즈니스</option><option value='tech'>테크</option><option value='science'>과학</option><option value='entertainment'>엔터테인먼트</option><option value='health'>건강</option>"
        );

        // 첫번째 관심사에서 뽑은 옵션 제거
        $("#second-interest")
            .find("option")
            .each(function () {
                if (this.value == selectOption) {
                    $(this).remove();
                }
            });
    }
});

$("#second-interest").change(function () {
    selectOption = $("#second-interest option:selected").val();
    selectedOption = $("#first-interest option:selected").val();

    if(selectedOption == selectOption) {
        // 남은 옵션 전체 삭제
        $("#first-interest option").remove();

        // 전체 옵션 다시 추가
        $("#first-interest").html(
            "<option value='business'>비즈니스</option><option value='tech'>테크</option><option value='science'>과학</option><option value='entertainment'>엔터테인먼트</option><option value='health'>건강</option>"
        );

        // 첫번째 관심사에서 뽑은 옵션 제거
        $("#first-interest")
            .find("option")
            .each(function () {
                if (this.value == selectOption) {
                    $(this).remove();
                }
            });
    }
});

// "예약을 취소하겠습니다" 유효성 검사
$("#cancelReservation").click(function () {
    if ($("#cancelReservationMessage").val() != "예약을 취소하겠습니다") {
        $("#cancelReservationMessage").focus();
        return false;
    }
    return true;
});
