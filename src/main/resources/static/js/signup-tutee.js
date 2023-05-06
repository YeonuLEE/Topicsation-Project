// POST로 tutee 회원가입 정보 보내기
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
        let confirmedPass = $("#password-confirm").val();

        if (password != confirmedPass) {
            $(".password-confirm")
                .text("비밀번호가 일치하지 않습니다.")
                .css("color", "red");
            $("#password-confirm").attr("class", "form-control is-invalid");
            passwordConfirmCheck = false;
        } else {
            $(".password-confirm").text("");
            $("#password-confirm").focus();
            $("#password-confirm").attr("class", "form-control is-valid");
            passwordConfirmCheck = true;
        }
    });

// 두번째 관심사 제거
//     $("#first-interest").change(function () {
//         console.log($("#first-interest option:selected").val())
//         console.log($("#second-interest option:selected").val())
//         // 남은 옵션 전체 삭제
//         $("#second-interest option").remove();
//
//         // 전체 옵션 다시 추가
//         $("#second-interest").html(
//             "<option value='business'>비즈니스</option><option value='tech'>테크</option><option value='science'>과학</option><option value='entertainment'>엔터테인먼트</option><option value='health'>건강</option>"
//         );
//
//         // 첫번째 관심사에서 뽑은 옵션 제거
//         selectedOption = $("#first-interest option:selected").val();
//         $("#second-interest")
//             .find("option")
//             .each(function () {
//                 if (this.value == selectedOption) {
//                     $(this).remove();
//                 }
//             });
//     });
    let firstSelected;
    let secondSelected;

    $("#first-interest").change(function () {
        firstSelected = $("#first-interest option:selected").val();

        if ($("#second-interest option:selected").length === 0) {

            // 남은 옵션 전체 삭제
            $("#second-interest option").remove();

            // 전체 옵션 다시 추가
            $("#second-interest").html(
                "<option value='business'>비즈니스</option><option value='tech'>테크</option><option value='science'>과학</option><option value='entertainment'>엔터테인먼트</option><option value='health'>건강</option>"
            );

            $("#second-interest").find("option").each(function () {

                if (this.value == firstSelected) {
                    $(this).remove();
                }
            });
            $("#second-interest").val(secondSelected);

        }
        if (secondSelected) {
            $("#second-interest").val(secondSelected);
        }
        secondSelected = $("#second-interest option:selected").val();

    });

    $("#second-interest").change(function () {
        secondSelected = $("#second-interest option:selected").val();

        if ($("#first-interest option:selected").length === 0) {
            // 남은 옵션 전체 삭제
            $("#first-interest option").remove();

            // 전체 옵션 다시 추가
            $("#first-interest").html(
                "<option value='business'>비즈니스</option><option value='tech'>테크</option><option value='science'>과학</option><option value='entertainment'>엔터테인먼트</option><option value='health'>건강</option>"
            );

            $("#first-interest").find("option").each(function () {
                if (this.value == secondSelected) {
                    $(this).remove();
                }
            });
            $("#first-interest").val(firstSelected);
        }
        if (firstSelected) {
            $("#first-interest").val(firstSelected);
        }
        firstSelected = $("#first-interest option:selected").val();
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

        let data = {
            email: $('input[name="email"]').val(),
            name: $('input[name="name"]').val(),
            password: $('input[name="password"]').val(),
            passwordConfirm: $('input[name="passwordConfirm"]').val(),
            firstInterest: $('select[name="firstInterest"]').val(),
            secondInterest: $('select[name="secondInterest"]').val(),
            role: 'tutee'
        };

        $.ajax({
            type: "POST",
            url: "/members/signup-tutees.post",
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (data, status) {
                let email = btoa(data);
                sessionStorage.setItem("email", email);
                window.location.href = '/members/signup/email';
            },
            error: function (data, textStatus) {
                alert("이미 존재하는 회원입니다.")
            },
            complete: function (data, textStatus) {
            },
        });
    });
});