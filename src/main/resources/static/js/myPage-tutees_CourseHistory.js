import { setupHeaderAjax, getId, getHeaderAjax} from './checkTokenExpiration.js';

$(document).ready(function () {

    const token = sessionStorage.getItem('accessToken');
    let userId;

    if(token != null){
        // access token 만료 기간 검증 및 req header에 삽입
        setupHeaderAjax(token)
        userId = getId(token);
    }

    let apiUrl = "/mypage/{user_id}";
    apiUrl = apiUrl.replace("{user_id}", userId);

    $.ajax({
        type: "GET" ,
        url: apiUrl + "/history/get",
        dataType: "json",
        async:false,
        success: function (data, status, xhr) {
            getHeaderAjax(xhr)

            $("#information").attr("href", apiUrl);
            $("#schedule").attr("href", apiUrl + "/schedule");
            $("#history").attr("href", apiUrl + "/history");

            let jsonData = JSON.parse(JSON.stringify(data));

            $("#name").text(jsonData.name);

            //테이블에 튜티의 정보 뿌려주기
            let tbody = $("#tutee-history");
            for (let i = 0; i < jsonData.history.length; i++) {
                let tutee = jsonData.history[i];
                let tr = $("<tr>");
                let tno = $("<th scope=\"row\">").text(i + 1);
                let classdate = $("<td>", {text: tutee.class_date});
                let tutorname = $("<td>", {text: tutee.tutor_name});
                let memo = $("<td>")
                    .append($("<a>", {href: tutee.memo1, text: tutee.memo1, target: "_blank"}))
                    .append($("<br>"))
                    .append($("<a>", {href: tutee.memo2, text: tutee.memo2, target: "_blank"}))
                    .append($("<br>"))
                    .append($("<a>", {href: tutee.memo3, text: tutee.memo3, target: "_blank"}));

                tr.append(tno);
                tr.append(classdate);
                tr.append(tutorname);
                tr.append(memo);
                tbody.append(tr);
            }
        },
        error: function (data, textStatus) {
            alert("Error!")
        }
    });
});