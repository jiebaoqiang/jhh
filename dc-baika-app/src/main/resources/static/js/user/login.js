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
    // 点击发送
    $('.sendBtn').on('click', function () {
        var that = $(this);
        var res = $(this).parent().siblings('li').find('input').val();
        if (!res) {

            $(this).parent().siblings('li').find('.tips').removeClass('hide');
            $(this).parent().siblings('li').find('.tips').text('图片验证码不能为空！');
            return;
        } else {
            $(this).parent().siblings('li').find('.tips').addClass('hide');
        }

        // 发送短信验证码
        $.ajax({
            url: path+'/user/sendSms',
            type: 'post',
            data: {
                phone: $("input[name='phone']").val(),
                timestamp: timestamp,
                graphiCode: $("input[name='graphiCode']").val(),
            },
            dataType: "json",
            success: function (data) {
                code = data.code;
                if (200 == code) {
                    if (!that.hasClass('disabled')) {
                        that.addClass('disabled');
                        var num = 60;
                        that.text(num + 's');
                        that.css('color', '#ccc');

                        var timer = setInterval(function() {
                                num--;
                                    that.text(num + 's');

                                if (num < 1
                    )
                        {
                            clearInterval(timer);
                            that.text('重新获取');
                            that.css('color', '#ff9e13');
                            that.removeClass('disabled')
                        }
                    },
                        1000
                    )
                    }
                } else if (code == 1002 || code == 1001) {
                    that.parent().siblings('li').find('.tips').removeClass('hide');
                    that.parent().siblings('li').find('.tips').text(data.info);
                } else {
                    $('.tipModal').removeClass('hide');
                    $('.tipModal p').text(data.info);
                }
            }
        })
    });


    // 光标离开提示
    $('input').on('blur', function () {
        var val = $(this).val();
        var reg = /^\d{6}$/;
        if (val) {
            if (!reg.test($('.num').val()) && $(this).hasClass('num')) {
                $('.num').siblings('i').removeClass('hide');
                $('.num').siblings('i').text('动态码格式不正确');
            } else {
                $(this).siblings('i').addClass('hide');
            }
        } else {
            $(this).siblings('i').removeClass('hide');
            if($(this).hasClass('num')){
                $(this).siblings('i').text('动态验证码不能为空！');
            }else{
                $(this).siblings('i').text('图片验证码不能为空！');
            }
        }
    });
    // 点击登录
    $('.loginBtn').on('click', function () {
        var that = $(this);
        var bgColor = that.css('background');
        $('input').trigger('blur');
        if (!$('.clickBtn').hasClass('active')) {
            $('.clickBtn').parent().siblings('span').removeClass('hide');
            return;
        } else {
            $('.clickBtn').parent().siblings('span').addClass('hide');
        }
        if($('.tips').hasClass('hide')){
            that.css({
                background: '#dcdcdc',
            }).attr('disabled', 'disabled');
            $.ajax({
                url:path+'/user/login',
                type:'post',
                data:$("#form").serialize(),
                dataType:'json',
                success:function(data){

                    if(data.code == 1002){ //是图片验证码
                        $('.imgCode').siblings('i').text(data.info).removeClass('hide');
                    }else if(data.code == 1003){
                        $('.num').siblings('i').text(data.info).removeClass('hide');
                    }else if (200 == data.code){
                        window.location.href=path+"form/userInfo/"+data.data.phone;
                    }else {
                        $('.tipModal').removeClass('hide');
                        $('.tipModal p').text(data.info);
                    }

                },
                complete:function(){
                    that.css({
                        background: bgColor,
                    }).removeAttr('disabled');
                }
            })
        }
    })
});