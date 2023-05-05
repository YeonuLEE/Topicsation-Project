$(document).ready(function () {
  var apiUrl = "http://localhost:8081/lesson/{lesson_id}";
  var classId = window.location.pathname.split("/").pop(); // /class/456
  apiUrl = apiUrl.replace("{lesson_id}", classId);

  // 뉴스 저장
  var todayNews;
  var todayNewsContent;
  var recommendedNews = [];
  var firstNewsContent;
  var secondNewsContent;

  // 뉴스 내용 불러오기
  $.ajax({
    type:"GET",
    async:false,
    url: apiUrl+"/getNews",
    success: function (data, status) {
      // 데이터 받는 과정
      todayNews = data.today
      // today 삭제
      delete data.today

      // 나머지 뉴스 값 할당
      for (const key in data) {
        if (data.hasOwnProperty(key)) {
          const value = data[key];
          recommendedNews.push(value)
        }
      }

      // 뉴스 삽입
      $("#today_title").text(todayNews.title)
      todayNewsContent = todayNews.content
      $("#first_title").text(recommendedNews[0].title)
      firstNewsContent = recommendedNews[0].content
      $("#second_title").text(recommendedNews[1].title)
      secondNewsContent = recommendedNews[1].content

    },
    error: function (data, textStatus) {
      alert("뉴스를 불러오는데 실패하였습니다.");
    },
    complete: function (data, textStatus) {},
  })

  // 오늘의 뉴스 내용 띄우기
  $("#todayNewsBtn").click(function () {
    $("#newsView").text(todayNewsContent)
  });

  // 첫번째 추천 뉴스 내용 띄우기
  $("#firstNewsBtn").click(function () {
    $("#newsView").text(firstNewsContent)
  });

  // 두번째 추천 뉴스 내용 띄우기
  $("#secondNewsBtn").click(function () {
    $("#newsView").text(secondNewsContent)
  });

});
