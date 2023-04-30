import {getId, setupHeaderAjax} from "./checkTokenExpiration";

$("#information").click(function (e) {
    e.preventDefault();

    const token = sessionStorage.getItem('accessToken');
    alert(token)

    let userId = getId(token)
    setupHeaderAjax(token)

    $.ajax({
        type: "GET",
        url: "/mypage/"+userId,
        success: function(data, textStatus, xhr) {
            console.log("controller return html: "+data)
            location.href=data
        },
        error: function(xhr, textStatus, errorThrown) {
            console.error("Error fetching mypage:", errorThrown);
        }
    });
})

$("#schedule").click(function (e) {
    e.preventDefault();

    const token = sessionStorage.getItem('accessToken');
    alert(token)

    let userId = getId(token)
    setupHeaderAjax(token)

    $.ajax({
        type: "GET",
        url: "/mypage/"+userId+"/schedule",
        success: function(data, textStatus, xhr) {
            console.log("controller return html: "+data)
            location.href=data
        },
        error: function(xhr, textStatus, errorThrown) {
            console.error("Error fetching mypage:", errorThrown);
        }
    });
})

$("#history").click(function (e) {
    e.preventDefault();

    const token = sessionStorage.getItem('accessToken');
    alert(token)

    let userId = getId(token)
    setupHeaderAjax(token)

    $.ajax({
        type: "GET",
        url: "/mypage/"+userId+"/history",
        success: function(data, textStatus, xhr) {
            console.log("controller return html: "+data)
            location.href=data
        },
        error: function(xhr, textStatus, errorThrown) {
            console.error("Error fetching mypage:", errorThrown);
        }
    });
})