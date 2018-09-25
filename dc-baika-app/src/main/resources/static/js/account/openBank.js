$.ajax({
    url: "/dc-baika/account/getOpenAccountStatus",
    type: "post",
    data: {
        perId:perId
    },
    dataType: "json",
    success: function (data) {
        if (200 == data.code) {
            let openBtn = $("#openBtn");
            if(data.data.isOpenAccount=='p'){
                //openBtn.attr("disabled",true);
                openBtn.css({
                    background: 'rgba(199,214,251,1)',
                }).attr("disabled",true);
                openBtn.text("开户中");
            }else if(data.data.isOpenAccount=='s'){
                location.href="/dc-baika/account/authAccountHtml/"+perId;
            }
        }else {
            $('.tipModal').removeClass('hide');
            $('.tipModal p').text(data.info);
        }
    }
});


$("button").click(function () {
    $.ajax({
        url: "/dc-baika/account/openAccount",
        type: "post",
        data: {
            perId:perId,
            phone:phone,
        },
        dataType: "json",
        success: function (data) {
            if (200 == data.code) {
                // 构建一个form表单
                var form = $("#form");
                form.attr("action",data.data.url);
                $("#merchantNo").val(data.data.merchantNo);
                $("#merOrderNo").val(data.data.merOrderNo);
                $("#jsonEnc").val(data.data.jsonEnc);
                $("#keyEnc").val(data.data.keyEnc);
                $("#sign").val(data.data.sign);
                form.submit();//表单提交
            }else {
                $('.tipModal').removeClass('hide');
                $('.tipModal p').text(data.info);
            }
        }
    });
});