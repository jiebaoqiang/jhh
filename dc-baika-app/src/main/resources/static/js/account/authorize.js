$("#loanAuth").click(function () {
    $.ajax({
        url: "/dc-baika/account/authAccount",
        type: "post",
        data: {
            perId:perId,
            authType:1,
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
            }else{
                console.log(data.info);
            }
        }
    });
});

$("#repayAuth").click(function () {
    $.ajax({
        url: "/dc-baika/account/authAccount",
        type: "post",
        data: {
            perId:perId,
            authType:2,
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
            }else{
                console.log(data.info);
            }
        }
    });
});

let t2 = window.setInterval(queryLoanStatus,3000);

function queryLoanStatus(){
    $.ajax({
        url: "/dc-baika/account/getAuthStatus",
        type: "post",
        data: {
            perId:perId,
            authType:2,
        },
        dataType: "json",
        success: function (data) {
            if (200 == data.code) {
                let loanAuth = $("#loanAuth");
                if(data.data=='s'){
                    loanAuth.val("s");
                    loanAuth.text("已开通");
                    loanAuth.attr("disabled",true);
                    if($("#repayAuth").val()=='s' && loanAuth.val()=='s'){
                        $("#nextBtn").css({
                            background: null,
                        }).attr("disabled",false);
                    }
                    window.clearInterval(t2);
                }else if(data.data=="p"){
                    loanAuth.text("开通中");
                }
            }
        }
    });
}

let t1 = window.setInterval(queryRepayStatus,3000);

function queryRepayStatus(){
    $.ajax({
        url: "/dc-baika/account/getAuthStatus",
        type: "post",
        data: {
            perId:perId,
            authType:1,
        },
        dataType: "json",
        success: function (data) {
            let repayAuth = $("#repayAuth");
            if (200 == data.code) {
                if(data.data=='s'){
                    repayAuth.val("s");
                    repayAuth.text("已开通");
                    repayAuth.attr("disabled",true);
                    if(repayAuth.val()=='s' && $("#loanAuth").val()=='s'){
                        $("#nextBtn").css({
                            background: '',
                        }).attr("disabled",false);
                    }
                    window.clearInterval(t1);
                }else if(data.data=="p"){
                    repayAuth.text("开通中");
                }
            }
        }
    });
}

$("#nextBtn").click(function(){
    location.href="/dc-baika/form/addBankCard/"+perId;
});