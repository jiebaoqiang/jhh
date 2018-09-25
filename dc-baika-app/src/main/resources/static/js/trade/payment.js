/**
 * Created by jiebaoqiang on 2018/7/24.
 */
var patternBankCard = /^([1-9])(\d{14,18})$/;
var u = navigator.userAgent, app = navigator.appVersion;
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //g
var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
if (isAndroid) {
    $("#device").val('android');
}
if (isIOS) {
    $("#device").val('ios');
}
$(function () {
    // 隐藏信息
    var reg = /^(\d{4})\d+(\d{4}|\d{3}(X|x))$/;
    $('.card').text($('.card').text().replace(reg, "$1*********$2"));
    $('.userCard').text($('.userCard').text().replace(reg, "$1*********$2"));
    $('.phone').text($('.phone').text().replace(/^(\d{3})\d+(\d{4})$/, "$1****$2"));

    // 点击支付按钮变灰禁用
    $('.btn').on('click', function () {
        var that = $(this);
        var bgColor = $(this).css('background');
        if (!$('.vaild').val()) {
            $('.tipModal').removeClass('hide');
            $('.tipModal p').text("请输入短信验证码");
            return;
        }
        $(this).css({
            background: '#dcdcdc',
        }).attr('disabled', 'disabled');
        $.ajax({
                url: projectName+'/trade/agentDeduct',
                type: 'post',
                data: $('form').serialize(),
                dataType: 'json',
                success: function (data) {
                    if (200 == data.code) {
                        window.location.href = projectName + "/form/paying/"+registerPhone;
                    } else {
                        $('.tipModal').removeClass('hide');
                        $('.tipModal p').text(data.info);
                    }
                },
                complete: function () {
                that.css({
            background: bgColor,
        }).removeAttr('disabled');
    }
    })
    });

    // 点击发送
    $('.sendBtn').on('click', function () {
        if (!$(this).hasClass('disabled')) {
            if (!sendSms()) {
                return;
            }
        }
        //  倒计时
        timer($(this));

    });

    //弹框消失
    $('.true').on('click', function () {
        $(this).parents('.tipModal').addClass('hide');
    });

    /**
     *  跳转意见反馈
     */
    $('.feedback').on('click',function () {
        window.location.href = projectName+"/form/feedback/"+perId;
    })
});

function sendSms() {
    var bank_num = $("input[name='bankNum']").val();
    var phone = $("input[name='phone']").val();
    var event = 'register';
    if (!patternBankCard.test(bank_num)) {
        $('.tipModal').removeClass('hide');
        $('.tipModal p').text("银行卡号格式不正确");
        return;
    }
    if (phone == '') {
        $('.tipModal').removeClass('hide');
        $('.tipModal p').text("请输入手机号");
        return;
    }

    $.ajax({
        url: projectName+"/sms/getPayCode",
        type: "post",
        data: {
            phone:phone,
            event: event,
            bankNum:bank_num,
            perId:perId,
            tokenKey:tokenKey,
            smsType:'repay',
            device:$("#device").val()
        },
        dataType: "json",
        success: function (data) {
            if (200 == data.code) {

            }else {
                $('.tipModal').removeClass('hide');
                $('.tipModal p').text(data.info);
            }
        }
    });
    return true;
}

