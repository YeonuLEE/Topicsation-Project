import {getHeaderAjax, setupHeaderAjax} from './checkTokenExpiration.js';

var name = $("#search-name").val();
var interest = $("#search-interest").val();
var date = $("#reserve-date").val();

var apiUrl = "/main/search-all/get?page={page}&size={size}"

function searchGet(page){
    apiUrl = "/main/search-all/get?page={page}&size={size}"
    apiUrl = apiUrl.replace("{page}",page);
    apiUrl = apiUrl.replace("{size}",6);

    if(interest != null)
        apiUrl += "&interest=" + interest;

    if(name != "")
        apiUrl += "&name=" + name;

    if(date != "")
        apiUrl += "&date=" + date;


    $.ajax({
        url: apiUrl,
        type: "GET",
        async: false,
        success: function (data, status, xhr) {
            getHeaderAjax(xhr)
            
            var all_list = data.all_list;
            var page = data.page;
            var total = data.total;

            var dataBody = $("#tutor-card");
            dataBody.empty();
            for (var i = 0; i < all_list.length; i++) {
                var person = all_list[i];
                console.log(person);
                var link = "/main/tutors/";
                var imgSrc = "/assets/img/profile/";
                var imgId = "tutor-img";

                console.log(person.user_id);
                link = link + person.user_id;
                imgSrc = imgSrc + person.tutor_image;
                imgId = imgId + person.user_id;

                var div1 = $("<div>", {class: "col-12 col-md-6 mb-3"});
                var div2 = $("<div>", {class: "card border-light mb-4 animate-up-5"});
                var div3 = $("<div>", {class: "row no-gutters align-items-center"});
                var div4 = $("<div>", {class: "col-12 col-lg-6 col-xl-4"});
                var a1 = $("<a>", {href: link})
                var a2 = $("<a>", {href: link})
                var img = $("<img>", {
                    src: imgSrc,
                    alt: "loft space",
                    className: "card-img p-2 rounded-xl",
                    id: imgId
                });

                var div5 = $("<div>", {class: "col-12 col-lg-6 col-xl-8"});

                var div6 = $("<div>", {class: "card-body"});
                var div7 = $("<div>", {class: "mb-3"});

                var span1 = $("<span>", {
                        class: "h5 mb-3",
                        text: person.name
                    }
                )
                var heartSpan = $("<span>", {style: "float: right"})
                var svg = $("<svg>", {
                    xmlns: "http://www.w3.org/2000/svg",
                    width: "200",
                    height: "200",
                    fill: "#dc143c",
                    class: "bi bi-heart-fill",
                    viewBox: "0 0 16 16",
                });


                var path = " <path fill-rule=\"evenodd\"\n" +
                    "d=\"M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z\" />";

                var span2 = $("<span>", {
                        class: "h6 mb-3",
                        text: person.like
                    }
                )

                var div8 = $("<div>", {class: "mb-5"});

                var span3 = $("<span>", {class: "fas fa-map-marker-alt mr-2"});
                var span4 = $("<span>", {text: person.nationality});

                var div9 = $("<div>", {class: "d-flex my-3"})

                var span5 = $("<span>", {
                    class: "badge badge-pill badge-primary ml-2",
                    text: "#" + person.interest1
                })
                var span6 = $("<span>", {
                    class: "badge badge-pill badge-primary ml-2",
                    text: "#" + person.interest2
                })


                dataBody.append(div1);
                div1.append(div2);
                div2.append(div3);
                div3.append(div4);
                div4.append(a1);
                a1.append(img);
                div3.append(div5);
                div5.append(div6);
                div6.append(div7);
                div7.append(a2);
                a2.append(span1);
                a2.append(heartSpan);
                heartSpan.append('<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#dc143c" class="bi bi-heart-fill mr-2" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"/></svg>');
                heartSpan.append(span2);
                div6.append(div8);
                div8.append(span3);
                div8.append(span4);
                div6.append(div9);
                div9.append(span5);
                div9.append(span6);


            }
            pagination(page,total);
        },
        error: function (xhr, textStatus, errorThrown) {
            if (xhr.status === 400) {
                console.log("잘못된 요청입니다.");
            } else {
                console.log("서버에서 오류가 발생했습니다.");
            }
        }
    });
}

$(document).ready(function (){
    const token = sessionStorage.getItem('accessToken');
    // nullPointerException 예방
    if(token != null){
        // access token 만료 기간 검증 및 req header에 삽입
        setupHeaderAjax(token)
    }
    searchGet(1);
});

$("#search-form").submit(function (e){
    name = $("#search-name").val();
    interest = $("#search-interest").val();
    date = $("#reserve-date").val();
    e.preventDefault();
    searchGet(1);
});

function pagination(currentPage, total) {
    var totalPages = Math.ceil(total / 6);
    var pageGroupSize = 5;
    var startPage = Math.ceil(currentPage / pageGroupSize) * pageGroupSize - pageGroupSize + 1;
    var endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

    if(total == 0) {
        $("#search-info").text("검색결과가 없습니다");
    } else {
        $("#search-info").text("전체 튜터를 확인해보세요");
    }
    var pagination = $(".pagination");
    pagination.empty();

    // 이전 페이지 링크 추가
    var prevDisabled = startPage === 1 ? "disabled" : "";
    if(total > 0) {
        pagination.append(`<li class="page-item ${prevDisabled}">
                          <a class="page-link" href="#" aria-label="Previous" data-page="${startPage - 1}">Previous</a>
                        </li>`);
    }

    // 페이지 번호 링크 추가
    for (var i = startPage; i <= endPage; i++) {
        var activeClass = i === currentPage ? "active" : "";
        var listItem = `<li class="page-item ${activeClass}">
                    <a class="page-link" href="#" data-page="${i}">${i}</a>
                  </li>`;
        pagination.append(listItem);
    }

    // 다음 페이지 링크 추가
    var nextDisabled = endPage === totalPages ? "disabled" : "";
    if(total >0) {
        pagination.append(`<li class="page-item ${nextDisabled}">
                          <a class="page-link" href="#" aria-label="Next" data-page="${endPage + 1}">Next</a>
                        </li>`);
    }
    // 페이지 링크에 대한 클릭 이벤트 리스너 추가
    $(".pagination .page-link").on("click", function (event) {
        event.preventDefault();
        var pageNumber = parseInt($(this).data("page"));
        if (!isNaN(pageNumber)) {
            searchGet(pageNumber);
        }
    });
}










