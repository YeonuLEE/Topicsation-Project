import {getHeaderAjax, setupHeaderAjax} from './checkTokenExpiration.js';

$(document).ready(function (){
    const token = sessionStorage.getItem('accessToken');
    // nullPointerException 예방
    if(token != null){
        // access token 만료 기간 검증 및 req header에 삽입
        setupHeaderAjax(token)
    }
    searchGet(1, "", null, "");

    $("#search-form").submit(function (e){
        e.preventDefault();

        let name = $("#search-name").val();
        let interest = $("#search-interest").val();
        let date = $("#reserve-date").val();

        searchGet(1, name, interest, date);
    });
});

function searchGet(page, name, interest, date){
    let apiUrl = "/main/search-all/get?page={page}&size={size}"
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

            let all_list = data.all_list;
            let page = data.page;
            let total = data.total;

            let dataBody = $("#tutor-card");
            dataBody.empty();
            for (let i = 0; i < all_list.length; i++) {
                let person = all_list[i];
                let link = "/main/tutors/";
                let imgSrc = "/assets/img/profile/";
                let imgId = "tutor-img";

                link = link + person.user_id;
                imgSrc = imgSrc + person.tutor_image;
                imgId = imgId + person.user_id;

                let div1 = $("<div>", {class: "col-12 col-md-6 mb-3"});
                let div2 = $("<div>", {class: "card border-light mb-4 animate-up-5"});
                let div3 = $("<div>", {class: "row no-gutters align-items-center"});
                let div4 = $("<div>", {class: "col-12 col-lg-6 col-xl-4"});
                let a1 = $("<a>", {href: link})
                let a2 = $("<a>", {href: link})
                let img = $("<img>", {
                    src: imgSrc,
                    alt: "loft space",
                    className: "card-img p-2 rounded-xl",
                    id: imgId
                });

                let div5 = $("<div>", {class: "col-12 col-lg-6 col-xl-8"});

                let div6 = $("<div>", {class: "card-body"});
                let div7 = $("<div>", {class: "mb-3"});

                let span1 = $("<span>", {
                        class: "h5 mb-3",
                        text: person.name
                    }
                )
                let heartSpan = $("<span>", {style: "float: right"})
                let svg = $("<svg>", {
                    xmlns: "http://www.w3.org/2000/svg",
                    width: "200",
                    height: "200",
                    fill: "#dc143c",
                    class: "bi bi-heart-fill",
                    viewBox: "0 0 16 16",
                });


                let path = " <path fill-rule=\"evenodd\"\n" +
                    "d=\"M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z\" />";

                let span2 = $("<span>", {
                        class: "h6 mb-3",
                        text: person.like
                    }
                )

                let div8 = $("<div>", {class: "mb-5"});

                let span3 = $("<span>", {class: "fas fa-map-marker-alt mr-2"});
                let span4 = $("<span>", {text: person.nationality});

                let div9 = $("<div>", {class: "d-flex my-3"})

                let span5 = $("<span>", {
                    class: "badge badge-pill badge-primary ml-2",
                    text: "#" + person.interest1
                })
                let span6 = $("<span>", {
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






function pagination(currentPage, total) {
    let totalPages = Math.ceil(total / 6);
    let pageGroupSize = 5;
    let startPage = Math.ceil(currentPage / pageGroupSize) * pageGroupSize - pageGroupSize + 1;
    let endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

    if(total == 0) {
        $("#search-info").text("검색결과가 없습니다");
    } else {
        $("#search-info").text("전체 튜터를 확인해보세요");
    }
    let pagination = $(".pagination");
    pagination.empty();

    // 이전 페이지 링크 추가
    let prevDisabled = startPage === 1 ? "disabled" : "";
    if(total > 0) {
        pagination.append(`<li class="page-item ${prevDisabled}">
                          <a class="page-link" href="#" aria-label="Previous" data-page="${startPage - 1}">Previous</a>
                        </li>`);
    }

    // 페이지 번호 링크 추가
    for (let i = startPage; i <= endPage; i++) {
        let activeClass = i === currentPage ? "active" : "";
        let listItem = `<li class="page-item ${activeClass}">
                    <a class="page-link" href="#" data-page="${i}">${i}</a>
                  </li>`;
        pagination.append(listItem);
    }

    // 다음 페이지 링크 추가
    let nextDisabled = endPage === totalPages ? "disabled" : "";
    if(total >0) {
        pagination.append(`<li class="page-item ${nextDisabled}">
                          <a class="page-link" href="#" aria-label="Next" data-page="${endPage + 1}">Next</a>
                        </li>`);
    }
    // 페이지 링크에 대한 클릭 이벤트 리스너 추가
    $(".pagination .page-link").on("click", function (event) {
        event.preventDefault();
        let pageNumber = parseInt($(this).data("page"));
        if (!isNaN(pageNumber)) {
            searchGet(pageNumber);
        }
    });
}










