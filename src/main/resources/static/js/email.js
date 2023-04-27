$(document).ready(function () {
    var email = atob(sessionStorage.getItem("email"));
    var authKey;
    $("#send-btn").click(function () {
        var dataBody = $("#email-auth");
        dataBody.empty();
        var form = $("<form>", {id: "emailTokenForm"});
        var div1 = $("<div>", {class: "form-group"});
        var label1 = $("<label>", {text: "인증코드 입력"});
        var div2 = $("<div>", {class: "form-group"});
        var div3 = $("<div>", {class: "input-group mb-4"});
        var div4 = $("<div>", {class: "input-group-prepend"});
        var span1 = $("<span>", {class: "input-group-text"});
        var span2 = $("<span>", {class: "fas fa-unlock-alt"});
        var input1 = $("<input>", {
            class: "form-control",
            id: "email-token",
            placeholder: "Email Token",
            type: "email_Token",
            "aria-label": "email_Token",
            required: true
        })
        var button1 = $("<button>", {
            type: "button",
            id: "auth-btn",
            class: "btn btn-block btn-primary",
            text: "Authenricate"
        })
        dataBody.append(form);
        form.append(div1);
        div1.append(label1);
        div1.append(div2);
        div2.append(div3);
        div3.append(div4);
        div4.append(span1);
        span1.append(span2);
        div3.append(input1);
        form.append(button1);
        $.ajax({
            type: "POST",
            url: "/members/email.send",
            contentType: 'application/json',
            data: JSON.stringify({
                email: email
            }),
            success: function (data, status) {
                if(data == "sendFail"){
                    alert("인증코드 전송에 실패하였습니다.");
                    location.href="/members/signup/email";
                }
                else{
                    authKey = data;
                    $("#auth-btn").click(function(event) {
                        event.preventDefault();
                        var token = $("#email-token").val();
                        if(token == authKey)
                            //여기에 ajax해주는데, location.href 이거는 그 ajax에 success에 넣어야함.
                            location.href = "/members/signup/success";
                        else{
                            alert("인증코드가 일치하지 않습니다.");
                            location.href = "/members/signup/email";
                        }
                    });
                }

            },
            error: function (data, textStatus) {
                alert("111")
                // location.href="/members/signup/email";
            },
            complete: function (data, textStatus) {
            },
        });
    });

});

