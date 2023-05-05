import { setupHeaderAjax, getId, getHeaderAjax } from './checkTokenExpiration.js';

var name;
var first;
var second;
var passwordCheck = false;
let userId
$(document).ready(function() {

    const token = sessionStorage.getItem('accessToken');

    // access token 만료 기간 검증 및 req header에 삽입
    if(token != null){
        setupHeaderAjax(token)
        userId = getId(token);
    }


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
        async:false,
        success: function(data, status,xhr) {
            getHeaderAjax(xhr)

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
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });

    var postlink2 = "/mypage/{user_id}/passCheck"
    postlink2 = postlink2.replace("{user_id}", userId);

    $("#authenticate").click(function (event) {
        event.preventDefault();

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
                    $("#enter-password").attr("class", "form-control is-invalid");
                    alert("비밀번호가 다릅니다. 확인해주세요.")
                    passwordCheck = false;
                } else {
                    name = $("#name").val();
                    first = $("#first-interest").val();
                    second = $("#second-interest").val();

                    var postlink = "/mypage/{user_id}/post";
                    postlink = postlink.replace("{user_id}", userId);

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
            },
            error: function (data, textStatus) {
                alert("Error!")
            },
            complete: function (data, textStatus) {
            },
        });
    });

    //회원 삭제
    $('#delete').click(function (){
        var postlink = "/mypage/{user_id}/delete";
        postlink = postlink.replace("{user_id}", userId);

        $.ajax({
            type: "post",
            url: postlink,
            contentType: "application/json",
            data: JSON.stringify({
                $user_id: userId,
            }),
            success: function (data, status) {
                $("#modal-default").modal('hide'); // 모달 창 닫기
                $("#cancel-reservation-message").val("");

                sessionStorage.removeItem('accessToken');
                document.cookie = "refreshToken=;  expires=Thu, 01 Jan 1970 00:00:00 UTC ; path=/";

                window.location.href = "/main"; // 페이지 이동
            },
            error: function (data, textStatus) {
                alert("Error!")
            },
            complete: function (data, textStatus) {
            },
        });
    });

    $("#reset").click(function (){
        $("#cancel-reservation-message").val("");
    });
});

