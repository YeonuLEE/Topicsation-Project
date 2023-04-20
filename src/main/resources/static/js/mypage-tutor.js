// get으로 데이터 받아오기
$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "/mypage/{tutor_id}/get",
        success: function (data, status) {
            var jsonObject = JSON.parse(data);

            $('#tutor-name').text(jsonObject.tutorName);
            $("#profile-img").attr("src", jsonObject.profileImg);
            $('#name').val(jsonObject.name);
            $('#email').val(jsonObject.email);
            $('#nationality').val(jsonObject.nationality);
            $('#first-interest').val(jsonObject.interest1).prop("selected", true);
            $('#second-interest').val(jsonObject.interest2).prop("selected", true);
            $(":radio[name='genderRadios'][value='" + jsonObject.genderRadios + "']").attr('checked', true);

        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });
});