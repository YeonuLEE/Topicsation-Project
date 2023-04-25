//Tutor Sign-up

//Tutee Sign-up


//myPage
$("#mypage-btn").click(function () {
    location.href = "#"
})

//sign
$('#sign-btn').click(function() {
    const token = sessionStorage.getItem('token');
    if (token != null) {
        sessionStorage.removeItem('token');
        $.ajax({
            url: '/members/signout',
            type: 'POST',
            success: function (data) {
                console.log('Signed out successfully');
                location.reload();
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

