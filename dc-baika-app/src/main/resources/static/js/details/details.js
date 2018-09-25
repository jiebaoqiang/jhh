$(function(){
    
    $('.cancelBtn').on('click', function () {
        $('.successModal').hide()
    })
});
//取消合同
function cancelBorrow(perId,borrId) {
    var bgColor = $("#btn").css('background');
    //取消合同
    $.ajax({
            url: projectName+'/loan/cancelBorrow/' + perId + "/" + borrId,
            type: 'post',
            jsonType: 'json',
            success: function (data) {
                if (200 == data.code) {
                    $("#btn").css("display", "none");
                    $("#goBtn").css("display", "none");
                    $("#borrStatusName").html("已取消");
                } else {
                    alert(data.info);
                }
                $('.successModal').hide();
            },
            complete: function () {
            $("#btn").css({
        background: bgColor,
    }).removeAttr('disabled');
}
});
}
function btnClick(perId,borrId,borrStatus) {
    var bgColor = $("#btn").css('background');
    $("#btn").css({
        background: '#dcdcdc',
    }).attr('disabled', 'disabled');
    //如果状态为缴费系列 调用app
    if (payCost(borrStatus)) {
            if (navigator.userAgent.match(/(iPhone|iPod|iPad);?/i)) {
                //苹果
                window.webkit.messageHandlers.payCost.postMessage(null);
            }else if (navigator.userAgent.match(/android/i)) {
                //安卓
                window.dc.payCost();
            }
        $("#btn").css({
            background: bgColor,
        }).removeAttr('disabled');
    } else if (cancel(borrStatus)) {
        $('.successModal').show();
            $("#btn").css({
                background: bgColor,
            }).removeAttr('disabled');
    } else if (pay(borrStatus)) {
        //付款
        window.location.href = projectName+"/form/getRepayDetails/" + phone + "/" + borrId;
            $("#btn").css({
                background: bgColor,
            }).removeAttr('disabled');
    }
}


//判断按钮是否为缴费状态
function payCost(borrStatus) {
    var payCost = ['BS015','BS017'];
    return payCost.indexOf(borrStatus)!=-1;
}

//判断按钮是否显示为取消
function cancel(borrStatus) {
    var cancel = ['BS001','BS002'];
    return cancel.indexOf(borrStatus)!=-1;
}



//隐藏合同的状态
function hiddenContract(borrStatus) {
    var cancel = ['BS004','BS005','BS006','BS010','BS014'];
    return cancel.indexOf(borrStatus)==-1;
}
//判断按钮是否为付款
function pay(borrStatus) {
    var pay = ['BS004','BS005'];
    return pay.indexOf(borrStatus)!=-1;
}


$(function () {
    //是否显示查看按钮
    //判断合同状态是否为申请中或待签约
    if (hiddenContract(borrStatus)) {
        $("#search").html("--");
        $("#search").removeAttr('href');
    } else {
        $("#search").html("查看");
    }
    //点击按钮显示
    if (cancel(borrStatus)) {
        $("#btn").html("取消");
    } else if (payCost(borrStatus)){
        $("#btn").html("去缴费");
    }else if (pay(borrStatus)){
        $("#btn").html(borrStatusName);
    }else {
        $("#btn").css("display","none");
    }
    //继续申请按钮
    if (cancel(borrStatus)) {
        if (borrStatus == 'BS001'){
            $('#goBtn').html('继续申请');
        }else {
            $('#goBtn').html('去签约');
        }
    }else {
        $("#goBtn").css("display","none");
            $('#btn').removeClass('fl').css({
                width: '15.2rem',
                background: 'linear-gradient(90deg,rgba(113,154,251,1),rgba(81,129,236,1))'
            })
    }

    //点击提交
    $("#btn").on('click',function () {
        btnClick(perId,borrId,borrStatus);
    });
    //点击确认取消
    $(".trueBtn").on('click',function () {
        cancelBorrow(perId,borrId);
    });
    $('#goBtn').on('click',function () {
        window.location.href = projectName + "/form/applyBorrow/"+phone+"/"+prodId;
    })
});

