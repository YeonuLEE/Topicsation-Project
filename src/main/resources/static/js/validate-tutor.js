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

// 유효성 검사 실패시 제출 안되게 하기
$("#signInForm").submit(function () {
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
    $("#showFiles").text(fileName);
});