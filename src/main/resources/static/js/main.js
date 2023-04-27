$(document).ready(function () {

    var token = sessionStorage.getItem('token');
    console.log(token);
    if (token != null) {
        $.ajaxSetup({
            beforeSend: function(xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + token);
            }
        });
        $('#sign-btn').text('SIGN OUT');
    }

    var pathURI = window.location.pathname
    var ajaxURI;

    console.log(pathURI);

    if (pathURI == '/main') {
        ajaxURI = pathURI + "/get";
        console.log(ajaxURI);
    } else if (pathURI == '/main/search-all') {
        ajaxURI = pathURI + ".get";
        console.log(ajaxURI);
    }

    $.ajax({
        url: ajaxURI,
        type: "GET",
        success: function (data, status) {
            var jsonData;
            var dataBody;
            var length;

            if (ajaxURI == '/main/get') {
                jsonData = JSON.parse(data);
                console.log(jsonData);
                dataBody = $("#tutor-card");
            } else {
                jsonData = JSON.parse(data);
                console.log(jsonData);
                console.log(jsonData.length)
                dataBody = $("#tutor-card");
            }

            for (var i = 0; i < jsonData.tutor_list.length; i++) {
                var person = jsonData.tutor_list[i];
                var link = "/main/tutors/";
                var imgSrc = "/assets/img/profile/";
                var imgId = "tutor-img";

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

                var div5 = $("<div>", {class: "col-12 col-lg-6 col-xl-7"});

                var div6 = $("<div>", {class: "card-body"});
                var div7 = $("<div>", {class: "mb-3"});

                var span1 = $("<span>", {
                        class: "h5 mb-3 mr-6",
                        text: person.name
                    }
                )

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
                a2.append('<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#dc143c" class="bi bi-heart-fill mr-2" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"/></svg>');
                a2.append(span2);
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

$("#search-form").submit(function (event) {
    event.preventDefault();
    var name = $('#search-name').val();
    var interest = $('#search-interest').val();
    var date = $('#reservate-date').val();

    console.log(name + " " + interest + " " + date);

    var apiUrl = "/main/search-all/search?name=" + name + "&interest=" + interest + "&date=" + date;

    $.ajax({
        url: apiUrl,
        type: "GET",
        success: function (data) {
            var jsonData = JSON.parse(data);
            console.log(jsonData);
            var dataBody = $("#tutor-card");

            dataBody.empty();
            for (var i = 0; i < jsonData.tutor_list.length; i++) {

                var person = jsonData.tutor_list[i];
                var link = "/main/tutors/";
                var imgSrc = "/assets/img/profile/";
                var imgId = "tutor-img";

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

                var div5 = $("<div>", {class: "col-12 col-lg-6 col-xl-7"});

                var div6 = $("<div>", {class: "card-body"});
                var div7 = $("<div>", {class: "mb-3"});

                var span1 = $("<span>", {
                        class: "h5 mb-3 mr-6",
                        text: person.name
                    }
                )

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
                a2.append('<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#dc143c" class="bi bi-heart-fill mr-2" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"/></svg>');
                a2.append(span2);
                div6.append(div8);
                div8.append(span3);
                div8.append(span4);
                div6.append(div9);
                div9.append(span5);
                div9.append(span6);
            }

        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 400) {
                console.log("잘못된 요청입니다.");
            } else {
                console.log("서버에서 오류가 발생했습니다.");
            }
        }
    });
})

$('#sign-btn').click(function() {
    const token = sessionStorage.getItem('token');
    if (token != null) {
        sessionStorage.removeItem('token');
        $.ajax({
            url: '/members/signout',
            type: 'POST',
            success: function (data) {
                console.log('Signed out successfully');
                location.reload();

            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error signing out:', textStatus, errorThrown);
            }
        });
    }else{
        location.href = "/members/signin"
    }
});

//myPage
$("#mypage-btn").click(function () {
    window.location.replace("/mypage/admin")
})

// $(window).on('load', function() {
//     var token = sessionStorage.getItem('token');
//     if (token != null) {
//         $.ajaxSetup({
//             beforeSend: function(xhr) {
//                 xhr.setRequestHeader('Authorization', 'Bearer ' + token);
//             }
//         });
//     }
// });