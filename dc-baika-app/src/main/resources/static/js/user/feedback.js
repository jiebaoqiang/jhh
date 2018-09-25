/**
 * Created by jiebaoqiang on 2018/7/24.
 */
$(function(){
    //$("input[name='prodTypeCode']").val(prodType);
    // 点击提交验证
    $("#btn").on('click',function(){
        var that = $(this);
        var bgColor = $(this).css('background');
        var val = $('textarea').val();
        if(val){
            if(val.length > 200){
                $('.toast').text('内容不能超过200字').fadeIn().delay(2000).fadeOut();
                return;
            }
        }else{
            $('.toast').text('内容不能为空').fadeIn().delay(2000).fadeOut();
            return;
        }
        $(this).css({
            background: '#dcdcdc',
        }).attr('disabled', 'disabled');
        $.ajax({
                url: projectName+'/user/feedback',
                type: 'post',
                data:  $('form').serialize(),
                dataType: 'json',
                success: function (data) {
                    $('.toast').text(data.info).fadeIn().delay(2000).fadeOut();
                    //$('textarea').val("");
                },
                complete: function() {
                that.css({
            background: bgColor,
        }).removeAttr('disabled');
    }
    })
    });

    $("#textarea").bind('input propertychange',function(){
        var num = $(this).val().length;
        //$("#textNum").val(num);
        document.getElementById("textNum").innerText = num;
        //alert(num);
        if(num > 200){
            $('.toast').text('内容不能超过200字！').fadeIn().delay(2000).fadeOut();
        }
    })

    $('.trueBtn').click(function(){
        $(this).parents('.successModal').addClass('hide')
    })

});
