import { setupHeaderAjax, getId, getHeaderAjax } from './checkTokenExpiration.js';

var name;
var first;
var second;
var nationality;
var gender;
var memo;
var password;
var passwordCheck = false;
let userId

// get으로 데이터 받아오기
$(document).ready(function () {

    const token = sessionStorage.getItem('accessToken');

    // access token 만료 기간 검증 및 req header에 삽입
    if(token != null){
        setupHeaderAjax(token)
        userId = getId(token);
    }

    var apiUrl1 = "/mypage/{user_id}/get";
    var apiUrl2 = "/mypage/{user_id}";
    var apiUrl3 = "/mypage/{user_id}/schedule";

    apiUrl1 = apiUrl1.replace("{user_id}", userId);
    apiUrl2 = apiUrl2.replace("{user_id}", userId);
    apiUrl3 = apiUrl3.replace("{user_id}", userId);

    $.ajax({
        type: "GET",
        url: apiUrl1,
        async:false,
        success: function (data, status, xhr) {
            getHeaderAjax(xhr)

            var jsonObject = JSON.parse(data);

            $("#information").attr("href", apiUrl2);
            $("#schedule").attr("href", apiUrl3);

            $('#tutor-name').text(jsonObject.name);
            $("#profile-img").attr("src","/assets/img/profile/" + jsonObject.profileImg);
            $('#name').val(jsonObject.name);
            $('#email').val(jsonObject.email);
            $('#nationality').val(jsonObject.nationality).prop("selected", true);
            $('#first-interest').val(jsonObject.interest1).prop("selected", true);
            $('#second-interest').val(jsonObject.interest2).prop("selected", true);
            $('#memo').val(jsonObject.memo);
            $('#gender-'+jsonObject.genderRadios).prop('checked',true);

            name=$("#name").val(jsonObject.name);
            nationality=$('#nationality').val(jsonObject.nationality).prop("selected", true);
            first = $("#first-interest").val(jsonObject.interest1).prop("selected",true);
            second = $("#second-interest").val(jsonObject.interest2).prop("selected",true);
            gender = $('input[type=radio][name=genderRadios]:checked').val();
            memo =$('#memo').val(jsonObject.memo);
            password = jsonObject.password.toString();

        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });

    var postlink2 = "/mypage/{user_id}/passCheck"
    postlink2 = postlink2.replace("{user_id}", userId);

    //비밀번호 확인
    $("#enter-password").change(function () {
        var pwd1 = $("#enter-password").val().toString();

        $.ajax({
            type: "POST",
            url: postlink2,
            contentType: 'application/json',
            data: JSON.stringify({
                password: pwd1
            }),
            success: function(data, status) {

                if(data === false) {
                    $(".form-control")
                        .text("비밀번호가 다름니다");
                    $("#enter-password").attr("class", "form-control is-invalid");
                    passwordCheck = false;
                } else {
                    $(".form-control").text("인증에 성공했습니다.");
                    $("#enter-password").attr("class", "form-control is-valid");
                    $("#authenticate").text("Save All");
                    passwordCheck = true;
                }
            },
            error: function (data, textStatus) {
                alert("Error!")
            },
            complete: function (data, textStatus) {
            },
        });
    });

    $("#authenticate").click(function () {

        if(passwordCheck){
            name = $("#name").val();
            nationality=$('#nationality').val();
            first = $("#first-interest").val();
            second = $("#second-interest").val();
            gender = $('input[type=radio][name=genderRadios]:checked').val();
            memo =$("#memo").val();

            var postlink = "/mypage/{user_id}/post";
            postlink = postlink.replace("{user_id}", userId);

            $.ajax({
                type: "POST",
                url :  postlink,
                contentType: 'application/json',
                data: JSON.stringify({
                    $name : name,
                    $nationality : nationality,
                    $interest1 : first,
                    $interest2 : second,
                    $gander : gender,
                    $memo : memo
                }),
                success: function (data, status) {
                    $("#modal-default").modal('hide'); // 모달 창 닫기
                    $("#cancel-reservation-message").val("");
                    location.reload();
                },
                error: function (data, textStatus) {
                    alert("Error!")
                },
                complete: function (data, textStatus) {
                },
            });
        }
    });

    $("#reset").click(function (){
        $("#cancel-reservation-message").val("");
    });

    //회원 삭제
    $('#delete').click(function (){
        var userid = userId;
        var postlink = "/mypage/{user_id}/delete";
        postlink = postlink.replace("{user_id}", userid);

        $.ajax({
            type: "post",
            url: postlink,
            contentType: "application/json",
            data: JSON.stringify({
                $user_id: userid,
            }),
            success: function (data, status) {
                $("#modal-default").modal('hide'); // 모달 창 닫기
                window.location.href = "/main"; // 페이지 이동
            },
            error: function (data, textStatus) {
                alert("Error!")
            },
            complete: function (data, textStatus) {
            },
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

    $("#file").on('change', function (){
        var apiUrl = "/mypage/{user_id}/profileUpdate";
        apiUrl = apiUrl.replace("{user_id}", userId);

        var formData = new FormData();
        formData.append('file', $('#file')[0].files[0]);
        $.ajax({
            url: apiUrl,
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response){
                $("#profile-img").load(window.location.href + " #profile-img");
            },
            error: function (error){
                alert("Error : " + error.responseText);
            }
        });
    });
});
