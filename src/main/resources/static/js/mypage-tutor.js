var name;
var profileImg;
var first;
var second;
var nationality;
var gender;
var password;
var passwordCheck = false;

// get으로 데이터 받아오기
$(document).ready(function () {
    var pathURI = window.location.pathname
    const regex = /\/mypage\/(\d+)/;
    const match = pathURI.match(regex);
    const userId= match[1];
    console.log(userId)
    var apiUrl1 = "/mypage/{user_id}/get";
    var apiUrl2 = "/mypage/{user_id}";
    var apiUrl3 = "/mypage/{user_id}/schedule";

    apiUrl1 = apiUrl1.replace("{user_id}", userId);
    apiUrl2 = apiUrl2.replace("{user_id}", userId);
    apiUrl3 = apiUrl3.replace("{user_id}", userId);
    console.log(apiUrl1);
    $.ajax({
        type: "GET",
        url: apiUrl1,
        success: function (data, status) {
            var jsonObject = JSON.parse(data);

            $("#information").attr("href", apiUrl2);
            $("#schedule").attr("href", apiUrl3);

            $('#tutor-name').text(jsonObject.name);
            $("#profile-img").attr("src", jsonObject.profileImg);
            $('#name').val(jsonObject.name);
            $('#email').val(jsonObject.email);
            $('#nationality').val(jsonObject.nationality).prop("selected", true);
            $('#first-interest').val(jsonObject.interest1).prop("selected", true);
            $('#second-interest').val(jsonObject.interest2).prop("selected", true);
            //$("radio[name='genderRadios'][value='" + jsonObject.genderRadios + "']").attr('checked', true);
            $('input[type=radio][name=genderRadios][value="' + jsonObject.genderRadios + '"]').prop('checked', true);

            name=$("#name").val(jsonObject.name);
            profileImg = $("#profile-img").attr("src", jsonObject.profileImg);
            nationality=$('#nationality').val(jsonObject.nationality).prop("selected", true);
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
            // profileImg = $("#profile-img").attr("src", );
            nationality=$('#nationality').val();
            first = $("#first-interest").val();
            second = $("#second-interest").val();

            console.log(name);
            console.log(profileImg);
            console.log(nationality);
            console.log(first);
            console.log(second);

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
                    $profileImg : profileImg,
                    $nationality : nationality,
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

    //비밀번호 확인
    $("#enter-password").change(function () {
        var pwd1 = $("#enter-password").val().toString();
        console.log(pwd1);
        console.log(password);

        if (password != pwd1) {
            $(".form-control")
                .text("password is different");
            $("#enter-password").attr("class", "form-control is-invalid");
            passwordCheck = false;
        } else {
            $(".form-control").text("");
            $("#enter-password").attr("class", "form-control is-valid");
            passwordCheck = true;
        }
    });

    $("#reset").click(function (){
        $("#cancel-reservation-message").val("");
    });
});