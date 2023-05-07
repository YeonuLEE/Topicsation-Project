import {getId, setupHeaderAjax} from './checkTokenExpiration.js';
import { moveToErrorPage } from './error/MoveToErrorPage.js';

//myPage
$(document).ready(function () {

    // AJAX 에러 처리기로 설정
    $.ajaxSetup({
        error: moveToErrorPage
    });

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
            }
        });
    })

    $('#sign-btn').click(function() {

        if (token != null) {
            sessionStorage.removeItem('accessToken');
            document.cookie = "refreshToken=;  expires=Thu, 01 Jan 1970 00:00:00 UTC ; path=/";
            location.href = "/main"
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
            }
        });
    })
});
