export function moveToErrorPage (xhr){
    if(xhr.status == 401){
        window.location.href = "error/401"
    }else if(xhr.status == 404){
        window.location.href = "error/404"
    }else if(xhr.status == 500){
        window.location.href = "error/500"
    }
}