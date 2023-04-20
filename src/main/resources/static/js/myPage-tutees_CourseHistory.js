<!-- Ajax get data -->
 $(document).ready(function() {
    $.ajax({
        type: "GET",
        url: "/mypage/{user_id}/history/get",
        dataType:"json",
        success: function(data, status) {
            var jsonData = JSON.parse(JSON.stringify(data));

            $("#name").text(jsonData.name);
            console.log(jsonData.name);

            //테이블에 튜티의 정보 뿌려주기
            var tbody = $("#tutee-history");
            for(var i=0;i<jsonData.history.length;i++) {
                var tutee = jsonData.history[i];
                var tr = $("<tr>");
                var tno = $("<th scope=\"row\">").text(i+1);
                var classdate = $("<td>",{text:tutee.class_date});
                var tutorname = $("<td>",{text:tutee.tutor_name});
                var memo = $("<td>",{text:tutee.memo});

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