var _testNews1 =
    '{"content":"SEOUL, April 15 (Yonhap) -- South Korea, the United States and Japan agreed to hold missile defense and anti-submarine drills regularly to counter growing North Korean threats during their senior-level defense talks in Washington earlier this week, Seouls defense ministry said Saturday.They reached the agreement at a session of the Defense Trilateral Talks (DTT) on Friday (Washington time), amid tensions caused by Pyongyangs recent weapons tests, including that of what it claims to be a solid-fuel Hwasong-18 intercontinental ballistic missile (ICBM) on Thursday"}';

$("#todayNews").click(function () {
    $.ajax({
        type: "POST",
        dataType: "text",
        async: false,
        url: "http://localhost:8081/ex/classroom.action",
        data: {testNews1: _testNews1}, // 데이터 넘기는 과정

        success: function (data, textStatus) { // 데이터 받는 과정
            $('#newsView').text(data);
        },
        error: function (data, textStatus) {
            alert("Error!")
        },
        complete: function (data, textStatus) {
        },
    });
});


    $("#logo-main").click(function () {
        var _width = '400';
        var _height = '300';

        // 팝업을 가운데 위치시키기 위해 아래와 같이 값 구하기
        var _left = Math.ceil((window.screen.width - _width) / 2);
        var _top = Math.ceil((window.screen.height - _height) / 2);

        window.open("/lesson/123/evaluate", "Evaluate", 'width=' + _width + ', height=' + _height + ', left=' + _left + ', top=' + _top);

    });

function remaindTime() {

    const curr = new Date();
    const utc = curr.getTime() + (curr.getTimezoneOffset() * 60 * 1000);

    const KR_TIME_DIFF = 9 * 60 * 60 * 1000;
    const kr_curr = new Date(utc + (KR_TIME_DIFF));

    var now = kr_curr
    var now_hour = now.getHours();
    var now_min = now.getMinutes();
    var now_sec = now.getSeconds();
    var time_min = 0;

    if (now_min < 30) {
        time_hour = now_hour;
        time_min = 30;
    } else {
        time_hour = now_hour + 1;
        time_min = 0;
    }

    var end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), time_hour, time_min, 00);
    var open = new Date(now.getFullYear(), now.getMonth(), now.getDate(), now_hour, now_min, 00);

    var nt = now.getTime();
    var ot = open.getTime();
    var et = end.getTime();

    //  $("p.time-title").html("금일 마감까지 남은 시간");
    sec = parseInt(et - nt) / 1000;
    day = parseInt(sec / 60 / 60 / 24);
    sec = (sec - (day * 60 * 60 * 24));
    hour = parseInt(sec / 60 / 60);
    sec = (sec - (hour * 60 * 60));
    min = parseInt(sec / 60);
    sec = parseInt(sec - (min * 60));

    if (hour < 10) {
        hour = "0" + hour;
    }
    if (min < 10) {
        min = "0" + min;
    }
    if (sec < 10) {
        sec = "0" + sec;
    }
    if (min < 5) {
        $('#minutes').css("color", "red");
        $('#seconds').css("color", "red");
    }
    $('#minutes').text(min + "분");
    $('#seconds').text(sec + "초");

    if (min == 0 && sec == 0) {
        setTimeout(function () {
            var _width = '400';
            var _height = '300';

            // 팝업을 가운데 위치시키기 위해 아래와 같이 값 구하기
            var _left = Math.ceil((window.screen.width - _width) / 2);
            var _top = Math.ceil((window.screen.height - _height) / 2);
            location.href = "main.html";
            window.open("evaluate-popup.html", "Evaluate", 'width=' + _width + ', height=' + _height + ', left=' + _left + ', top=' + _top);
            return false;
        }, 1000);
    }
}

setInterval(remaindTime, 1000);