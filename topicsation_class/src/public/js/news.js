var testNews1 =
  '{"content":"SEOUL, April 15 (Yonhap) -- South Korea, the United States and Japan agreed to hold missile defense and anti-submarine drills regularly to counter growing North Korean threats during their senior-level defense talks in Washington earlier this week, Seouls defense ministry said Saturday.They reached the agreement at a session of the Defense Trilateral Talks (DTT) on Friday (Washington time), amid tensions caused by Pyongyangs recent weapons tests, including that of what it claims to be a solid-fuel Hwasong-18 intercontinental ballistic missile (ICBM) on Thursday"}';

$(document).ready(function () {
  var apiUrl = "http://localhost:8081/lesson/{lesson_id}/newsView";
  var classId = window.location.pathname.split("/").pop(); // /class/456
  apiUrl = apiUrl.replace("{lesson_id}", classId);

  $("#todayNews").click(function () {
    $.ajax({
      type: "POST",
      dataType: "text",
      async: false,
      url: apiUrl,
      contentType: "application/json",
      data: JSON.stringify({ $testNews1: testNews1 }),

      success: function (data, textStatus) {
        // 데이터 받는 과정
        $("#newsView").text(data);
      },
      error: function (data, textStatus) {
        alert("Error!");
      },
      complete: function (data, textStatus) {},
    });
  });
});
