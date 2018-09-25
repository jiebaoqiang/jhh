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
    var str = $('.name').text();
    var sum = str.slice(-2);
    for(var i = 0; i < str.length-2 ; i++){
        sum = '*'+ sum;
    }
    $('.name').text(sum);

    //获取银行列表
    $.ajax({
        url: projectName+"/bindCard/getBankList",
        async: false,
        success: function (data) {
            if (200 == data.code) {
                $.each(data.data, function (a, b) {
                    $(".list ul").append("<li data-id='" + b.bankCode + "'>" + b.bankName + "</li>");
                });
            }
        }
    });
    // 点击支付按钮变灰禁用
    $('.btn').on('click', function () {
        $(".tokenKey").val(tokenKey);
        var that = $(this);
        var form = $('form').serialize();
        var bgColor = $(this).css('background');
        $('.tipModal').removeClass('hide');
        if (!$('#bankName').val()) {
            $('.tipModal p').text('选择银行不能为空！');
        } else if (!patternBankCard.test($('.card').val())) {
            $('.tipModal p').text('银行卡号格式不正确！');
        } else if (!$('.vaild').val()) {
            $('.tipModal p').text('验证码不能为空！');
        } else {
            $('.tipModal').addClass('hide');
            $(this).css({
                background: '#dcdcdc',
            }).attr('disabled', 'disabled');
            $.ajax({
                    url: projectName+'/bindCard/insertBankInfo',
                    type: 'post',
                    data: form,
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 200) {
                            var phone = $("input[name='phone']").val();
                            if (returnUrl != '' && returnUrl != null){
                                window.location.href= returnUrl;
                            }else {
                                window.location.href= projectName+"/form/jumpSign/"+phone;
                            }
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
        }
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
    // 选择
    $('.list li').on('click', function () {
        $('.list').slideUp(200, function () {
        });
        $('.modal').fadeOut(200);
        if ($(this).text() === '取消' || $(this).text() === '请选择您的借款用途') {

        } else {
            $('.method').text($(this).text());
            $('#bankName').val($(this).text());
            $('#bankCode').val($(this).attr("data-id"));
        }
    })

});

function sendSms() {
    var bank_num = $("input[name='bank_num']").val();
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
            tokenKey:tokenKey,
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

