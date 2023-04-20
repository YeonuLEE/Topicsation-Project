$(document).ready(function () {
    $('#like-btn').click(function () {
        var pathURI = window.location.pathname
        const regex = /\/(\d+)\//;
        const match = pathURI.match(regex);
        const number = match[1];
        var ajaxURL = pathURI + ".do";

        $.ajax({
            url: ajaxURL,
            type: "PUT",
            contentType: 'application/json',
            data: JSON.stringify({
                $evaluate: 'like',
                $lesson_id: number,
                test: "test",
            }),
            success: function (result) {
                // alert(result);
                window.close();
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    });
    $('#dislike-btn').click(function () {
        var pathURI = window.location.pathname
        const regex = /\/(\d+)\//;
        const match = pathURI.match(regex);
        const number = match[1];
        var ajaxURL = pathURI + ".do";

        $.ajax({
            url: ajaxURL,
            type: "PUT",
            contentType: 'application/json',
            data: JSON.stringify({
                $evaluate: 'dislike',
                $lesson_id: number,
                test: "test",
            }),
            success: function (result) {
                // alert(result);
                window.close();
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    });
});