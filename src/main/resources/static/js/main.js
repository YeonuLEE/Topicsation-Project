import {getHeaderAjax, setupHeaderAjax, getId} from './checkTokenExpiration.js';



$(document).ready(function () {

    const token = sessionStorage.getItem('accessToken');
    let userId = "default";
    let pathURI = window.location.pathname

    // nullPointerException 예방
    if(token != null){
        // access token 만료 기간 검증 및 req header에 삽입
        setupHeaderAjax(token);
        userId = getId(token);
    }

    $.ajax({
        url: pathURI + "/get",
        type: "GET",
        data : {userId : userId},
        async:false,
        success: function (data, status, xhr) {

            getHeaderAjax(xhr)

            let jsonData = JSON.parse(data);

            for (let i = 0; i < jsonData.tutor_list.length; i++) {
                let person = jsonData.tutor_list[i];
                let link = "/main/tutors/";
                let imgSrc = person.tutor_image;
                let imgId = "tutor-img";


                link = link + person.user_id;
                imgId = imgId + person.user_id;

                let div1 = $("<div>", {class: "col-12 col-md-6 mb-3"});
                let div2 = $("<div>", {class: "card border-light mb-4 animate-up-5"});
                let div3 = $("<div>", {class: "row no-gutters align-items-center"});
                let div4 = $("<div>", {class: "col-12 col-lg-6 col-xl-4"});
                let a1 = $("<a>", {href: link})
                let img = $("<img>", {
                    src: imgSrc,
                    alt: "loft space",
                    className: "card-img p-2 rounded-xl",
                    id: imgId,
                    style: "object-fit: cover; width: 100%; height: 230px;"
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

                let span2 = $("<span>", {
                        class: "h6 mb-3",
                        text: person.like
                    }
                )

                let div8 = $("<div>", {class: "mb-5", style: "color:#424767;"});

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

                let dataBody = $("#tutor-card");

                dataBody.append(div1);
                div1.append(a1);
                a1.append(div2);
                div2.append(div3);
                div3.append(div4);
                div4.append(img);
                div3.append(div5);
                div5.append(div6);
                div6.append(div7);
                div7.append(span1);
                div7.append(heartSpan);
                heartSpan.append('<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#dc143c" class="bi bi-heart-fill mr-2" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"/></svg>');
                heartSpan.append(span2);
                div6.append(div8);
                div8.append(span3);
                div8.append(span4);
                div6.append(div9);
                div9.append(span5);
                div9.append(span6);
            }
        }
    });
});