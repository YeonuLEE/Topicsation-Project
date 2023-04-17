var cell_id = "";
var tagId = "";

$(document).ready(function () {
    $(".datepicker").datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        startDate: "0d",
    });
    $(".datepicker").datepicker("setDate", "today");

    $(".datepicker").click(function () {
        $(".cell").removeClass("select");
    });

    $(".cell").click(function () {
        if (!$(this).hasClass("select")) {
            tagId = $(this).attr("id");

            // 모달 띄우기
            $("#modal-default").modal();
        }
    });

    $("#booking").click(function () {
        $("#" + tagId).addClass("select");
    });
});