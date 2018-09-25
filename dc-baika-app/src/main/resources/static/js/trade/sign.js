
$(function(){

    // 点击支付按钮变灰禁用
    $('.btn').on('click', function () {
        var that = $(this);
        var form = $('form')[0];
        var bgColor = $(this).css('background');
        if ('pay_money' == prodType) {
            if (!form.loanUse.value) {
                $('.tipModal').removeClass('hide');
                $('.tipModal p').text('请选择借款用途');
                return;
            }
        }
        if($('.clickBtn').hasClass('active')){
            $('.clickBtn').parent().siblings('p').addClass('hide')
        }else{
            $('.clickBtn').parent().siblings('p').removeClass('hide');
            return;
        }
            $(this).css({
                background: '#dcdcdc',
            }).attr('disabled', 'disabled');
            $.ajax({
                    url: projectName+'/loan/signingBorrow/'+form.perId.value+'/'+form.borrId.value,
                    type: 'post',
                    data:{loanUse:form.loanUse.value},
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 200) {
                            $('.successModal').show();
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


    // 选择
    $('.list li').on('click', function () {
        $('.list').slideUp(200, function () {
        });
        $('.modal').fadeOut(200);
        if ($(this).text() === '取消' || $(this).text() === '请选择您的借款用途') {

        } else {
            $('.method').text($(this).text());
            $('#loanUse').val($(this).text());
        }
    });


});