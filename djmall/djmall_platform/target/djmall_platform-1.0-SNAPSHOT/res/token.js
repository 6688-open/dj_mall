var token_get = function (url, callback) {
    $.ajax({
        type: "GET",
        url: url,
        beforeSend: function (request) {
            request.setRequestHeader("TOKEN", cookie.get("TOKEN"));
        },
        success: callback
    });
};
var token_post = function (url, param, callback) {
    $.ajax({
        type: "POST",
        url: url,
        data: param,
        beforeSend: function (request) {
            request.setRequestHeader("TOKEN", cookie.get("TOKEN"));
        },
        success: callback
    });
};

var set_login = function (TOKEN, NICK_NAME) {
    cookie.set("TOKEN", TOKEN, 1);
    cookie.set("NICK_NAME", NICK_NAME, 1);
}
// 验证是否登陆
var check_login = function () {
    if (cookie.get("TOKEN") != undefined && cookie.get("NICK_NAME") != undefined) {
        return true;
    }
    return false;
};
var logout = function () {
    // cookie.delete("TOKEN");
    // cookie.delete("NICK_NAME");
    cookie.clear();
    window.location.href = "/";
}

$.ajaxSetup({
    type: "POST",
    error: function (jqXHR, textStatus, errorMsg) {  // 出错时默认的处理函数
        // jqXHR 是经过jQuery封装的XMLHttpRequest对象
        // textStatus 可能为： null、"timeout"、"error"、"abort"或"parsererror"
        // errorMsg 可能为： "Not Found"、"Internal Server Error"等
        switch (jqXHR.status) {
            case(500):
                alert("服务器系统内部错误");
                break;
            case(401):
                alert("未登录");
                logout();
                break;
            case(403):
                alert("当前用户没有权限");
                break;
            case(408):
                alert("请求超时");
                break;
            default:
                alert("未知错误");
        }
    }
});