/**
 * Created by jiebaoqiang on 2018/8/3.
 */
$(function(){

    var flag1,flag2,flag = true;
    // 动画
    $('input').on('keyup',function(){
        if($(this).val() && flag){
            flag = false;
            $(this).next().animate({
                opacity: 1,
                height: '2.5rem',
            },600)
        }else if(!$(this).val()){
            flag = true;
            $(this).next().animate({
                opacity: 0,
                height: 0,
            },600)
        }else {

        }
    });


    // 验证
    $('.pwd').on('blur',function(){
        flag = true;
        var val = $(this).val();
        var reg = /^\d{6}$/;
        if(val.length != 6 || !reg.test(val)){
            flag1 = false;
            $('.toast').text('请设置6位数字密码').fadeIn().delay(2000).fadeOut()
        }else{
            flag1 = true;

        }
    });
    $('.newPwd').on('blur',function(){
        flag = true;
        if(flag1){
            var val = $(this).val();
            var oldVal = $('.pwd').val();
            if(val != oldVal){
                flag2 = false;
                $('.toast').text('两次输入的密码不一样').fadeIn().delay(2000).fadeOut()
            }else{
                flag2 = true;
            }
        }
    });


    $('.btn').on('click',function(){
        var that = $(this);
        var bgColor = that.css('background');
        if(flag1){
            $('.newPwd').trigger('blur');
            if(flag2){
                // 验证成功
                $(this).css({
                    background: '#BEE7DC',
                }).attr('disabled', 'disabled');

                $.ajax({
                    url: projectName+'/user/setPassword',
                    type: 'post',
                    data: $('form').serialize(),
                    dataType: 'json',
                    success: function (data) {
                        if (returnUrl == null || returnUrl == '') {
                            returnUrl = '/form/applyBorrow/'+$('form')[0].phone.value+'/'+prodId+'?prodType='+prodType;
                        }
                        if (200 ==data.code){
                            $('.toast').text("支付密码设置成功,3秒后跳转").fadeIn().delay(3000).fadeOut(0,function(){
                                window.location.href = projectName + returnUrl;
                            });
                        }else {
                            $('.toast').text(data.info).fadeIn().delay(3000).fadeOut(0,function () {
                                that.css({
                                    background: bgColor,
                                }).removeAttr('disabled');
                            });
                        }
                    }
                })
            }
        }else{
            $('.pwd').trigger('blur');
        }
    })

});