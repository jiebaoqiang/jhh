$(function(){
    var amountSurplus =  $("input[name='amountSurplus']");
    var alsoAmount = amountSurplus.val();
    $("#actualAmount ").html((alsoAmount * 1 + fee * 1).toFixed(2));
    $("input[name='actualAmount']").val((alsoAmount * 1 + fee * 1).toFixed(2));
    amountSurplus.bind("input propertychange change", function (event) {
        var alsoAmount = amountSurplus.val();
        $("#actualAmount ").html((alsoAmount * 1 + fee * 1).toFixed(2));
        $("input[name='actualAmount']").val((alsoAmount * 1 + fee * 1).toFixed(2));
        var pattern = /^([1-9]\d*|0)(\.\d{1,2}|\.)?$/;
        if (!pattern.test(alsoAmount)) {
            amountSurplus.val("");
            $("#actualAmount ").html(fee.toFixed(2));
            $("input[name='actualAmount']").val(fee.toFixed(2));
        }
    });

    // 点击支付按钮变灰禁用
    $('.btn').on('click', function () {
        var forObj = $('#paymentInfo')[0];
        var bgColor = $(this).css('background');
        $('.tipModal').removeClass('hide');
        if (!forObj.amountSurplus.value || forObj.amountSurplus.value == 0) {
            $('.tipModal p').text('请输入付款金额');
        } else if (forObj.amountSurplus.value > amount) {
            $('.tipModal p').text('付款金额不能大于最大应付金额');
        }  else {
            $('.tipModal').addClass('hide');
            $(this).css({
                background: '#dcdcdc',
            }).attr('disabled', 'disabled');
            forObj.submit();
                $(this).css({
                    background: bgColor,
                }).removeAttr('disabled');
        }
    });

    $('.jumpDetails').on('click',function () {
        window.location.href = projectName +'/form/jumpDetails/'+phone+'/'+borrNum;
    })
});

$(function () {

    $('.bankCard').on('click', function () {
        $('.bankCard img').attr('src', projectName+'/images/noActive.png');
        $(this).find('img').attr('src', projectName+'/images/active.png');
        $('.place').val($(this).data().id);
    })

});

function addBankCard() {
    $("#addBank").submit();
}