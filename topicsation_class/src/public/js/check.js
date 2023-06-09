import {getId} from './checkTokenExpiration.js';

$(document).ready(function () {
    var apiUrl = "https://www.topicsation.site/lesson/{lesson_id}";
    var classId = window.location.pathname.split("/").pop(); // /class/456
    apiUrl = apiUrl.replace("{lesson_id}", classId);

    const token = window.token
    let userId = getId(token)

    // 사용자 체크하기
    $.ajax({
        type:"GET",
        async:false,
        url: apiUrl+"/getMembers",
        success: function (data, textStatus) {

            if(userId != data.tutorId && userId != data.tuteeId){
                alert("예약한 수업이 아닙니다\n" +
                    "Not your reserved class")
                window.location = "https://www.topicsation.site/main"
            }
        },
        error: function (data, textStatus) {
            alert("멤버를 불러오는데 실패하였습니다.");
        },
        complete: function (data, textStatus) {},
    })
})