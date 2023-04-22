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
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });
});