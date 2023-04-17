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
