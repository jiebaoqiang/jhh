$(function () {
    // 点击签约
    $('.btn').click(function () {
        if ($('.clickBtn').hasClass('active')) {
            $('.clickBtn').parent().siblings('p').addClass('hide')
        } else {
            $('.clickBtn').parent().siblings('p').removeClass('hide')
            return;
        }
        var that = $(this);
        var form = $('form')[0];
        var bgColor = $(this).css('background');
        if (!form.loanUse.value) {
            $('.tipModal').removeClass('hide');
            $('.tipModal p').text('请选择借款用途');
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
    })

    // modal消失的代码
    // $('.successModal').hide()
    $('.complete').click(function () {
        $('.successModal').hide()
    })

    // 费率详情
    $('.icon-gantanhao').click(function () {
        $('.desModal').removeClass('hide');
    })

    $('.icon-close').click(function () {
        $('.desModal').addClass('hide');
    })

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
})