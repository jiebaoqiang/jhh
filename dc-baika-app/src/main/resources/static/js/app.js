$(function () {
    //返回App立即申请页面
    $(".goBackApp").on('click', function () {
        if (navigator.userAgent.match(/(iPhone|iPod|iPad);?/i)) {
            //苹果
            window.webkit.messageHandlers.goBackApp.postMessage(null);
        } else if (navigator.userAgent.match(/android/i)) {
            //安卓
            window.dc.goBackApp();
        }
    });
    //跳App上一个页面
    $(".previouStep").on('click', function () {
        var back = document.referrer;
        if (back != '') {
            history.back();
        } else {
            if (navigator.userAgent.match(/(iPhone|iPod|iPad);?/i)) {
                //苹果
                window.webkit.messageHandlers.previouStep.postMessage(null);
            } else if (navigator.userAgent.match(/android/i)) {
                //安卓
                window.dc.previouStep();
            }
        }

    });

    //返回我的app原生界面
    $(".myCenter").on('click', function () {
        if (navigator.userAgent.match(/(iPhone|iPod|iPad);?/i)) {
            //苹果
            window.webkit.messageHandlers.previouStep.postMessage(null);
        } else if (navigator.userAgent.match(/android/i)) {
            //安卓
            window.dc.previouStep();

        }

    });
    /***
     * 签约完成跳原生待审核页面
     */
    $('.complete').on('click', function () {
        if (navigator.userAgent.match(/(iPhone|iPod|iPad);?/i)) {
            //苹果
            window.webkit.messageHandlers.openAudit.postMessage(null);
        } else if (navigator.userAgent.match(/android/i)) {
            //安卓
            window.dc.openAudit();
        }
    });

});


//拨打客服电话
function callServicePhone(phone) {
    if (navigator.userAgent.match(/(iPhone|iPod|iPad);?/i)) {
        //苹果
        window.webkit.messageHandlers.callServicePhone.postMessage(phone);
    } else if (navigator.userAgent.match(/android/i)) {
        //安卓
        window.dc.callServicePhone(phone);
    }
}

