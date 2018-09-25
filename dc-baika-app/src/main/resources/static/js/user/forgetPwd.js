/**
 * Created by jiebaoqiang on 2018/7/30.
 */
function changeImg() {
    // 刷新图片
    var time = (new Date()).valueOf();
    var imgSrc = $("#validateCode");
    var src = imgSrc.attr("src");
    imgSrc.attr("src", path+"/user/validateCode?time=" + time + "&timestamp=" + timestamp);
}

$(function () {
    changeImg();
    $("#timestamp").val(timestamp);
    var code;
    var flag = true,
        tip = false;
    // 动画
    $('input').on('keyup', function () {
        if ($(this).val() && flag) {
            flag = false;
            $(this).next().animate({
                opacity: 1,
                height: '2.5rem',
            }, 600)
        } else if (!$(this).val()) {
            flag = true;
            $(this).next().animate({
                opacity: 0,
                height: 0,
            }, 600)
        } else {

        }
    });


    // 验证
    $('.sendBtn').on('click', function () {
        var that = $(this);
        var code;
        var res = $(this).parent().prev().find('input').val();
        if (!res) {
            $('.toast').text('图形验证码不能为空').fadeIn().delay(2000).fadeOut();
            return;
        }
        // 发送短信验证码
        $.ajax({
            url: path+'/user/sendSms',
            type: 'post',
            data: {
                phone: $("input[name='phone']").val(),
                timestamp: timestamp,
                prodType:prodType,
                graphiCode: $("input[name='graphiCode']").val(),
            },
            dataType: "json",
            success: function (data) {
                code = data.code;
                if (200 == code) {
                    $('.toast').text('验证码发送成功，请注意查收').fadeIn().delay(2000).fadeOut();
                    //  倒计时
                    timer($(that));
                } else {
                    $('.toast').text(data.info).fadeIn().delay(2000).fadeOut();
                }
            }
        })
    });

    $('input').on('blur', function () {
        flag = true;
        var val = $(this).val();
        var reg = /^\d{6}$/;
        if (val) {
            if (!reg.test($('.num').val()) && $(this).hasClass('num')) {
                $('.toast').text('动态码格式不正确').fadeIn().delay(2000).fadeOut();
                tip=false;
            }else{
                tip= true;
            }
        } else {
            tip=false;
            if ($(this).hasClass('num')) {
                $('.toast').text('动态验证码不能为空！').fadeIn().delay(2000).fadeOut()
            } else {
                $('.toast').text('图片验证码不能为空！').fadeIn().delay(2000).fadeOut()
            }
        }
    });


    $('.btn').on('click', function () {
        var that = $(this);
        var bgColor = that.css('background');
        $('input').trigger('blur');

        if (tip) {
            that.css({
                background: '#BEE7DC',
            }).attr('disabled', 'disabled');
            $.ajax({
                url: path+'/user/forgetPayPwd',
                type: 'post',
                data: $("form").serialize(),
                dataType: 'json',
                success: function (data) {
                     if (200 == data.code){
                        window.location.href = path +"/form/setPassword/"+$("input[name='phone']").val()
                            +"?returnUrl=" +returnUrl;
                    }else {
                        $('.toast').text(data.info).fadeIn().delay(2000).fadeOut();
                    }
                },
                complete: function () {
                    that.css({
                        background: bgColor,
                    }).removeAttr('disabled');
                }
            })
        }
    })

});
