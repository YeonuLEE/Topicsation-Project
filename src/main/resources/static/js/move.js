//Tutor Sign-up

//Tutee Sign-up


//myPage
$("#mypage-btn").click(function () {
    window.location.replace("/mypage/admin")
})

//sign - main은 적용 X해야함 이해안되면 명진에게 물어보세영
$('#sign-btn').click(function() {
    const token = sessionStorage.getItem('token');
    if (token != null) {
        sessionStorage.removeItem('token');
        $.ajax({
            url: '/members/signout',
            type: 'POST',
            success: function (data) {
                console.log('Signed out successfully');
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
// $('#signout-btn').click(function() {
    // if ($("#signin-btn").text() == "SIGN IN") {
    //     location.href = "/members/signin"
    // }
    // else {
    //     location.href = "/members/signout"
    // }
// })



//TOPICSATION LOGO

