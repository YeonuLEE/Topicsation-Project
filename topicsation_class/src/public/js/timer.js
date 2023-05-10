$(document).ready(function () {
  var pathURI = "https://www.topicsation.site/{lesson_id}";
  var classId = window.location.pathname.split("/").pop(); // /class/456
  var apiUrl = pathURI.replace("{lesson_id}", classId);

  function remaindTime() {
    const curr = new Date();
    const utc = curr.getTime() + curr.getTimezoneOffset() * 60 * 1000;

    const KR_TIME_DIFF = 9 * 60 * 60 * 1000;
    const kr_curr = new Date(utc + KR_TIME_DIFF);

    var now = kr_curr;
    var now_hour = now.getHours();
    var now_min = now.getMinutes();
    var time_min = 0;

    if (now_min < 30) {
      time_hour = now_hour;
      time_min = 30;
    } else {
      time_hour = now_hour + 1;
      time_min = 0;
    }

    var end = new Date(
      now.getFullYear(),
      now.getMonth(),
      now.getDate(),
      time_hour,
      time_min,
      00
    );
    var open = new Date(
      now.getFullYear(),
      now.getMonth(),
      now.getDate(),
      now_hour,
      now_min,
      00
    );

    var nt = now.getTime();
    var ot = open.getTime();
    var et = end.getTime();

    //  $("p.time-title").html("금일 마감까지 남은 시간");
    sec = parseInt(et - nt) / 1000;
    day = parseInt(sec / 60 / 60 / 24);
    sec = sec - day * 60 * 60 * 24;
    hour = parseInt(sec / 60 / 60);
    sec = sec - hour * 60 * 60;
    min = parseInt(sec / 60);
    sec = parseInt(sec - min * 60);

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
      $("#minutes").css("color", "red");
      $("#seconds").css("color", "red");
    }
    $("#minutes").text(min + "분");
    $("#seconds").text(sec + "초");

    if (min == 0 && sec == 0) {
      setTimeout(function () {
        var _width = "400";
        var _height = "300";

        // 팝업을 가운데 위치시키기 위해 아래와 같이 값 구하기
        var _left = Math.ceil((window.screen.width - _width) / 2);
        var _top = Math.ceil((window.screen.height - _height) / 2);

        var popup = window.open(
          apiUrl + "/evaluate",
          "Evaluate",
          "width=" +
            _width +
            ", height=" +
            _height +
            ", left=" +
            _left +
            ", top=" +
            _top
        );

        popup.onload = function () {
          window.opener.location.href = "https://www.topicsation.site/main";
        };

        return false;
      }, 1000);
    }
  }

  setInterval(remaindTime, 1000);
});
