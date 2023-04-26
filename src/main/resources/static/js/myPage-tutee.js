var name;
var first;
var second;
var password;
var passwordCheck = false;
$(document).ready(function() {
    var pathURI = window.location.pathname
    const regex = /\/mypage\/(\d+)/;
    const match = pathURI.match(regex);
    const userId= match[1];
    console.log(userId)
    var apiUrl1 = "/mypage/{user_id}/get";
    var apiUrl2 = "/mypage/{user_id}";
    var apiUrl3 = "/mypage/{user_id}/schedule";
    var apiUrl4 = "/mypage/{user_id}/history";

    apiUrl1 = apiUrl1.replace("{user_id}", userId);
    apiUrl2 = apiUrl2.replace("{user_id}", userId);
    apiUrl3 = apiUrl3.replace("{user_id}", userId);
    apiUrl4 = apiUrl4.replace("{user_id}", userId);

    $.ajax({
        type: "GET",
        url: apiUrl1,
        success: function(data, status) {
            var jsonObject = JSON.parse(data);

            $("#information").attr("href", apiUrl2);
            $("#schedule").attr("href", apiUrl3);
            $("#history").attr("href", apiUrl4);

            $("#tutee-name").text(jsonObject.name);
            $("#name").val(jsonObject.name);
            $("#email").val(jsonObject.email);
            $("#first-interest").val(jsonObject.interest1).prop("selected",true);
            $("#second-interest").val(jsonObject.interest2).prop("selected",true);

            name=$("#name").val(jsonObject.name);
            first = $("#first-interest").val(jsonObject.interest1).prop("selected",true);
            second = $("#second-interest").val(jsonObject.interest2).prop("selected",true);
            password = jsonObject.password.toString();
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });

    $("#authenticate").click(function () {
        $("#signInForm").button(function () {
            if (!passwordCheck) {
                $("#password").focus();
                return false;
            }
            return true;
        });

        if(passwordCheck){
            name = $("#name").val();
            first = $("#first-interest").val();
            second = $("#second-interest").val();

            var user_id = userId;
            var postlink = "/mypage/{user_id}/post";
            postlink = postlink.replace("{user_id}", user_id);
            console.log("실행");
            alert("실행");
            $.ajax({
                type: "POST",
                url :  postlink,
                contentType: 'application/json',
                data: JSON.stringify({
                    $name : name,
                    $interest1 : first,
                    $interest2 : second
                }),
                success: function (data, status) {
                    $("#modal-default").modal('hide'); // 모달 창 닫기
                    console.log(data);
                    $("#cancel-reservation-message").val("");
                },
                error: function (data, textStatus) {
                    alert("Error!")
                },
                complete: function (data, textStatus) {
                },
            });
        }
    });

    $("#enter-password").change(function () {
        var pwd1 = $("#enter-password").val().toString();
        console.log(pwd1);
        console.log(password);

        if (password != pwd1) {
            console.log("들어옴")
            $(".form-control")
                .text("비밀번호가 다름니다");
            $("#enter-password").attr("class", "form-control is-invalid");
            passwordCheck = false;
        } else {
            $(".form-control").text("");
            $("#enter-password").attr("class", "form-control is-valid");
            passwordCheck = true;
        }
    });
});