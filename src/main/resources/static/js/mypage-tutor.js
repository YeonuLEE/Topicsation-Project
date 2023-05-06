import { setupHeaderAjax, getId, getHeaderAjax } from './checkTokenExpiration.js';

// get으로 데이터 받아오기
$(document).ready(function () {

    let name;
    let first;
    let second;
    let nationality;
    let gender;
    let memo;
    let password;
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
        success: function (data, status, xhr) {
            getHeaderAjax(xhr)

            let jsonObject = JSON.parse(data);

            $("#information").attr("href", apiUrl);
            $("#schedule").attr("href", apiUrl + "/schedule");

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
                    nationality=$('#nationality').val();
                    first = $("#first-interest").val();
                    second = $("#second-interest").val();
                    gender = $('input[type=radio][name=genderRadios]:checked').val();
                    memo =$("#memo").val();

                    $.ajax({
                        type: "POST",
                        url :  apiUrl + "/post",
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
                        }
                    });
                }
            },
            error: function (data, textStatus) {
                alert("Error!")
            }
        });
    });

    $("#reset").click(function (){
        $("#cancel-reservation-message").val("");
    });

    //회원 삭제
    $('#delete').click(function (){
        let postlink = "/mypage/{user_id}/delete";
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
            $(this).css("opacity", 0.3);
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
        let formData = new FormData();
        formData.append('file', $('#file')[0].files[0]);
        $.ajax({
            url: apiUrl + "/profileUpdate",
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
