$(document).ready(function () {

    let email = atob(sessionStorage.getItem("email"));
    let authKey;
    let dataBody = $("#email-auth");
    let form = $("<form>", {id: "emailTokenForm"});
    let div1 = $("<div>", {class: "form-group"});
    let label1 = $("<label>", {text: "인증코드 입력"});
    let div2 = $("<div>", {class: "form-group"});
    let div3 = $("<div>", {class: "input-group mb-4"});
    let div4 = $("<div>", {class: "input-group-prepend"});
    let span1 = $("<span>", {class: "input-group-text"});
    let span2 = $("<span>", {class: "fas fa-unlock-alt"});
    let input1 = $("<input>", {
        class: "form-control",
        id: "email-code",
        placeholder: "Email Token",
        type: "email_Token",
        "aria-label": "email_Token",
        required: true
    })
    let button1 = $("<button>", {
        type: "button",
        id: "auth-btn",
        class: "btn btn-block btn-primary",
        text: "Authenricate"
    })


    $("#send-btn").click(function () {

        // dataBody 재구성
        dataBody.empty();
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
                authKey = data;
            },
            error: function (data, textStatus) {
                alert("인증코드 전송에 실패하였습니다.");
                location.href = "/members/signup/email";
            },
            complete: function (data, textStatus) {
            },
        });
    });

    $(document).on('click', '#auth-btn', function (event) {
        event.preventDefault();
        let emailCode = $("#email-code").val();
        if (emailCode === authKey) {
            sessionStorage.removeItem("email");
            $.ajax({
                type: "POST",
                url: "/members/signup/success.post",
                contentType: 'application/json',
                data: JSON.stringify({
                    email: email
                }),
                success: function (data, status) {
                    location.href = "/members/signup/success";
                },error: function (data, status) {
                    alert("이메일인증에 실패했습니다")
                    location.href = "/members/signup/email";
                }
            });
        }
        else {
            alert("인증코드가 일치하지 않습니다");
            // event.preventDefault();
        }
    });
});