import {getId, setupHeaderAjax} from './checkTokenExpiration.js';
//myPage
$(document).ready(function () {
    $("#mypage-btn").click(function (e) {
        e.preventDefault();

        const token = sessionStorage.getItem('accessToken');
        alert(token)

        let userId = getId(token)
        setupHeaderAjax(token)

        // const xhr = new XMLHttpRequest();
        // xhr.open("GET", "/mypage/" + userId);
        // xhr.setRequestHeader('Authorization', 'Bearer ' + token);
        // xhr.onload = function() {
        //     if (xhr.status === 200) { // 응답 상태 코드가 200이면
        //         window.location.href = "/mypage/" + userId;
        //     }
        // };
        // xhr.send();


        $.ajax({
            type: "GET",
            url: "/mypage/" + userId,
            success: function (data, textStatus, xhr) {
                console.log("controller return html: " + data)
                location.href = data
            },
            error: function (xhr, textStatus, errorThrown) {
                console.error("Error fetching mypage:", errorThrown);
            }
        });
    })

//sign - main은 적용 X해야함 이해안되면 명진에게 물어보세영
// $('#sign-btn').click(function() {
//     const token = sessionStorage.getItem('token');
//     if (token != null) {
//         sessionStorage.removeItem('token');
//         $.ajax({
//             url: '/members/signout',
//             type: 'POST',
//             success: function (data) {
//                 console.log('Signed out successfully');
//                 location.href = "/main"
//             },
//             error: function (jqXHR, textStatus, errorThrown) {
//                 console.error('Error signing out:', textStatus, errorThrown);
//             }
//         });
//     }else{
//         location.href = "/members/signin"
//     }
// });
// $('#signout-btn').click(function() {
    // if ($("#signin-btn").text() == "SIGN IN") {
    //     location.href = "/members/signin"
    // }
    // else {
    //     location.href = "/members/signout"
    // }
// })


    $("#information").click(function (e) {
        e.preventDefault();

        const token = sessionStorage.getItem('accessToken');
        alert(token)

        let userId = getId(token)
        setupHeaderAjax(token)

        $.ajax({
            type: "GET",
            url: "/mypage/" + userId,
            success: function (data, textStatus, xhr) {
                console.log("controller return html: " + data)
                location.href = data
            },
            error: function (xhr, textStatus, errorThrown) {
                console.error("Error fetching mypage:", errorThrown);
            }
        });
    })

    $("#schedule").click(function (e) {
        e.preventDefault();

        const token = sessionStorage.getItem('accessToken');
        alert(token)

        let userId = getId(token)
        setupHeaderAjax(token)

        $.ajax({
            type: "GET",
            url: "/mypage/" + userId + "/schedule",
            success: function (data, textStatus, xhr) {
                console.log("controller return html: " + data)
                location.href = data
            },
            error: function (xhr, textStatus, errorThrown) {
                console.error("Error fetching mypage:", errorThrown);
            }
        });
    })

    $("#history").click(function (e) {
        e.preventDefault();

        const token = sessionStorage.getItem('accessToken');
        alert(token)

        let userId = getId(token)
        setupHeaderAjax(token)

        $.ajax({
            type: "GET",
            url: "/mypage/" + userId + "/history",
            success: function (data, textStatus, xhr) {
                console.log("controller return html: " + data)
                location.href = data
            },
            error: function (xhr, textStatus, errorThrown) {
                console.error("Error fetching mypage:", errorThrown);
            }
        });
    })
});


//TOPICSATION LOGO

