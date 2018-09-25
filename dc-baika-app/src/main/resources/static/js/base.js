var projectName= '/dc-baika';
var oHtml = document.documentElement;

var token = getCookie('token');
if (token == null || token == ''){
    //verifyUserSession();
}

getSize();
function getSize(){
    // 获取屏幕的宽度
    var ascreen=oHtml.clientWidth;
    if(ascreen>=750){
        oHtml.style.fontSize = '20px';
    }else{
        oHtml.style.fontSize=ascreen/20+"px";
    };
}
// 当窗口发生改变的时候调用
window.onresize = function(){
    getSize();
}

function timer(e){
    if (!e.hasClass('disabled')) {
        e.addClass('disabled')
        var num = 60;
        e.text(num + 's')
        e.css('color', '#ccc')

        var timer = setInterval(function(){
            num--
            e.text(num + 's')

            if (num < 1) {
                clearInterval(timer);
                e.text('重新获取')
                e.css('color', '#4f9cf3')
                e.removeClass('disabled')
            }
        }, 1000)
    }
}

$(function () {

    // 姓名的处理
    var str = $('.username').text(),
        sum, length;
    if (str.length > 2) {
        sum = str.slice(-2);
        length = str.length - 2;
    } else {
        sum = str.slice(-1);
        length = str.length - 1;
    }
    for (var i = 0; i < length; i++) {
        sum = '*' + sum;
    }
    $('.username').text(sum)

})

//验证会话过期跳登陆页面
function verifyUserSession() {
    if (navigator.userAgent.match(/(iPhone|iPod|iPad);?/i)) {
        //苹果
        window.webkit.messageHandlers.loginApp.postMessage(null);
    }else if (navigator.userAgent.match(/android/i)) {
        //安卓
        window.dc.loginApp();
    }
}

$(function(){
    // 点击勾选
    $('.clickBtn').on('click',function(){
        $(this).toggleClass('active');
    })

    // 借款用途下拉框
    $('.method').on('click',function(){
        $('.modal').fadeIn(200)
        $('.list').slideDown(200);
    })

    //  点击关闭模态框
    $('.true').on('click', function () {
        $(this).parents('.tipModal').addClass('hide');
    })

});

//拦截所有ajax 请求 判断会话是否过期,对会话过期进行统一处理
$(function () {
    hookAjax({
        //拦截回调
        onreadystatechange:function(xhr){
            if (xhr.readyState==4 && xhr.status==200){
                if (JSON.parse(xhr.responseText).code == 300){
                   // verifyUserSession();
                }
            }
        }
    })
});

function getCookie(sName)
{
    var aCookie = document.cookie.split("; ");
    for (var i=0; i < aCookie.length; i++)
    {
        var aCrumb = aCookie[i].split("=");
        if (sName == aCrumb[0])
            return unescape(aCrumb[1]);
    }
    return null;
}
