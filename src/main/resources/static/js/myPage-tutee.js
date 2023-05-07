import { setupHeaderAjax, getId, getHeaderAjax } from './checkTokenExpiration.js';

$(document).ready(function() {

    let name;
    let first;
    let second;
    let userId
    const token = sessionStorage.getItem('accessToken');

    // access token 만료 기간 검증 및 req header에 삽입
    if(token != null){
        setupHeaderAjax(token)
        userId = getId(token);
    }

    let apiUrl = "/mypage/{user_id}";
    apiUrl = apiUrl.replace("{user_id}", userId);

    $.ajax({
        type: "GET",
        url: apiUrl + "/get",
        async:false,
        success: function(data, status,xhr) {
            getHeaderAjax(xhr)

            let jsonObject = JSON.parse(data);

            $("#information").attr("href", apiUrl);
            $("#schedule").attr("href", apiUrl + "/schedule");
            $("#history").attr("href", apiUrl + "/history");

            $("#tutee-name").text(jsonObject.name);
            $("#name").val(jsonObject.name);
            $("#email").val(jsonObject.email);
            $("#first-interest").val(jsonObject.interest1).prop("selected",true);
            $("#second-interest").val(jsonObject.interest2).prop("selected",true);

            name = $("#name").val(jsonObject.name);
            first = $("#first-interest").val(jsonObject.interest1).prop("selected",true);
            second = $("#second-interest").val(jsonObject.interest2).prop("selected",true);
        },
        error: function (data, textStatus) {
            alert("Error!")
        }
    });

    $("#authenticate").click(function (event) {
        event.preventDefault();

        let pwd1 = $("#enter-password").val().toString();

        $.ajax({
            type: "POST",
            url: apiUrl + "/passCheck",
            contentType: 'application/json',
            data: JSON.stringify({
                password: pwd1
            }),
            success: function(data, status) {

                if(data === false) {
                    $("#enter-password").attr("class", "form-control is-invalid");
                    alert("비밀번호가 다릅니다. 확인해주세요.")
                } else {
                    name = $("#name").val();
                    first = $("#first-interest").val();
                    second = $("#second-interest").val();

                    $.ajax({
                        type: "POST",
                        url : apiUrl + "/post",
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
                        }
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
        $.ajax({
            type: "post",
            url: apiUrl + "/delete",
            contentType: "application/json",
            data: JSON.stringify({
                $user_id: userId,
            }),
            success: function (data, status) {
                $("#modal-default").modal('hide'); // 모달 창 닫기
                $("#cancel-reservation-message").val("");

                // 토큰 제거
                sessionStorage.removeItem('accessToken');
                document.cookie = "refreshToken=;  expires=Thu, 01 Jan 1970 00:00:00 UTC ; path=/";

                window.location.href = "/main"; // 페이지 이동
            },
            error: function (data, textStatus) {
                alert("Error!")
            }
        });
    });

    $("#reset").click(function (){
        $("#cancel-reservation-message").val("");
    });
});

