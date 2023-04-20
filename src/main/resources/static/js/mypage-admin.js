$(document).ready(function() {
    $.ajax({
        type: "GET",
        url: "/mypage/admin/get",
        dataType:"json",
        success: function(data, status) {
            console.log(data);
            console.log(status);
            var tbody = $("#spare-tutor");
            for(var i=0;i<data.length;i++) {
                var spare = data[i];
                var tr = $("<tr>");
                var tno = $("<th scope=\"row\">").text(i+1);
                var tutorName = $("<td>",{text:spare.tutorName});
                var approlDate = $("<td>",{text:spare.approlDate});
                var file = $("<td>",{text:spare.file});
                var success =$("<td><button type=\"submit\" class=\"btn btn-primary\" margin=\"15px\" id=\"success\">승인</button>");
                var fail =$("<td><button type=\"submit\" class=\"btn btn-danger\" id=\"fail\">거부</button>");

                tr.append(tno);
                tr.append(tutorName);
                tr.append(approlDate);
                tr.append(file);
                tr.append(success);
                tr.append(fail);
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