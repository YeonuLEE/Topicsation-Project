import {getId, setupHeaderAjax} from './checkTokenExpiration.js';
//myPage
$(document).ready(function () {

    // 변수 선언
    const token = sessionStorage.getItem('accessToken');
    let userId

    // nullPointerException 예방
    if(token != null) {
        userId = getId(token)
        setupHeaderAjax(token)
    }

    // 로그인 로그아웃 버튼 바꾸기
    if (token != null) {
        $('#sign-btn').text('SIGN OUT');
    }

    // 비회원 마이페이지 버튼 안보 이게 하기
    if(token == null){
        $('#mypage-btn').hide();
    }

    $("#mypage-btn").click(function (e) {
        e.preventDefault();

        $.ajax({
            type: "GET",
            url: "/mypage/" + userId,
            success: function (data, textStatus, xhr) {
                location.href = data
            },
            error: function (xhr, textStatus, errorThrown) {
                console.error("Error fetching mypage:", errorThrown);
            }
        });
    })

    $('#sign-btn').click(function() {

        if (token != null) {
            sessionStorage.removeItem('accessToken');
            document.cookie = "refreshToken=;  expires=Thu, 01 Jan 1970 00:00:00 UTC ; path=/";

            $.ajax({
                url: '/members/signout',
                type: 'POST',
                success: function (data) {
                    location.href = "/main"
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.error('Error signing out:', textStatus, errorThrown);
                }
            });
        }else{
            location.href = "/members/signin"
        }
    });

    $("#information").click(function (e) {
        e.preventDefault();

        $.ajax({
            type: "GET",
            url: "/mypage/" + userId,
            success: function (data, textStatus, xhr) {
                location.href = data
            },
            error: function (xhr, textStatus, errorThrown) {
                console.error("Error fetching mypage:", errorThrown);
            }
        });
    })

    $("#schedule").click(function (e) {
        e.preventDefault();

        $.ajax({
            type: "GET",
            url: "/mypage/" + userId + "/schedule",
            success: function (data, textStatus, xhr) {
                location.href = data
            },
            error: function (xhr, textStatus, errorThrown) {
                console.error("Error fetching mypage:", errorThrown);
            }
        });
    })

    $("#history").click(function (e) {
        e.preventDefault();

        $.ajax({
            type: "GET",
            url: "/mypage/" + userId + "/history",
            success: function (data, textStatus, xhr) {
                location.href = data
            },
            error: function (xhr, textStatus, errorThrown) {
                console.error("Error fetching mypage:", errorThrown);
            }
        });
    })
});
