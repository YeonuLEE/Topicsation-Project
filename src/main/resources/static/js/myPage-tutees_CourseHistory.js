<!-- Ajax get data -->
$(document).ready(function () {
    var pathURI = window.location.pathname
    const regex = /\/mypage\/(\d+)\/history/;
    const match = pathURI.match(regex);
    const userId= match[1];
    // console.log(userId)
    var apiUrl1 = "/mypage/{user_id}/history/get";
    var apiUrl2 = "/mypage/{user_id}";
    var apiUrl3 = "/mypage/{user_id}/schedule";
    var apiUrl4 = "/mypage/{user_id}/history";

    apiUrl1 = apiUrl1.replace("{user_id}", userId);
    apiUrl2 = apiUrl2.replace("{user_id}", userId);
    apiUrl3 = apiUrl3.replace("{user_id}", userId);
    apiUrl4 = apiUrl4.replace("{user_id}", userId);

    $.ajax({
        type: "GET",
        url: apiUrl1,
        dataType: "json",
        success: function (data, status) {

            $("#information").attr("href", apiUrl2);
            $("#schedule").attr("href", apiUrl3);
            $("#history").attr("href", apiUrl4);

            var jsonData = JSON.parse(JSON.stringify(data));

            $("#name").text(jsonData.name);
            console.log(jsonData.name);

            //테이블에 튜티의 정보 뿌려주기
            var tbody = $("#tutee-history");
            for (var i = 0; i < jsonData.history.length; i++) {
                var tutee = jsonData.history[i];
                var tr = $("<tr>");
                var tno = $("<th scope=\"row\">").text(i + 1);
                var classdate = $("<td>", {text: tutee.class_date});
                var tutorname = $("<td>", {text: tutee.tutor_name});
                var memo = $("<td>", {text: tutee.memo});

                tr.append(tno);
                tr.append(classdate);
                tr.append(tutorname);
                tr.append(memo);
                tbody.append(tr);
            }
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });
});