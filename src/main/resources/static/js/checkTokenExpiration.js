export function setupHeaderAjax(token) {
    $.ajaxSetup({
        beforeSend: function (xhr) {
            if (!checkTokenExp(token)) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + token)
            } else {
                xhr.setRequestHeader('Authorization', null)
            }
        }
    });
}

export function getHeaderAjax(xhr){
    let accessToken = null
    //accesstoken 뽑아내기
    const authorization = xhr.getResponseHeader("Authorization");
    if(authorization != null){
        accessToken = authorization.substring(7);
        //accesstoken 저장
        sessionStorage.setItem("accessToken", accessToken);
    }
}

export function setupHeader(token) {
    let xhr = new XMLHttpRequest();
    if (!checkTokenExp(token)) {
        xhr.setRequestHeader('Authorization', 'Bearer ' + token)
    } else {
        xhr.setRequestHeader('Authorization', null)
    }
}

function checkTokenExp(accessToken) {
    let base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
    let payload = atob(base64Payload, 'base64');
    let result = JSON.parse(payload.toString());

    const expirationTime = result.exp * 1000 // exp는 초 단위이므로 밀리초 단위로 변환
    return expirationTime < Date.now();
}

export function getId(accessToken) {
    let base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
    let payload = atob(base64Payload, 'base64');
    let result = JSON.parse(payload.toString())

    return result.sub;
}

export function getRoles(accessToken) {
    let base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
    let payload = atob(base64Payload, 'base64');
    let result = JSON.parse(payload.toString())

    return result.roles;
}





