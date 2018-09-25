var allRoleInfo = [];
var selfRoleInfo = [];
function productTablesInit() {
    checkPageEnabled("ym-gd");
    initProBtnClick();
    initProductUserTables();
    initTermBut();
}


//初始化提交按钮
function initProBtnClick() {
    $("#btnEditUserInfo").click(function(){
        var product_name = $("#product_name").val();
        var product_description = $("#product_description").val();
        var product_icon = $("#product_icon").val();
        var finance_title = $("#finance_title").val();
        var terms = $("#term input[type=checkbox]");
        var information_manage_fee = $("#information_manage_fee").val();
        var account_manage_fee = $("#account_manage_fee").val();
        var platform_manage_fee = $("#platform_manage_fee").val();
        var terminterest = $("input[name='terminterest']");
        var penalty_level_1 = $("input[name=penalty_level_1]");
        var penalty_level_2 = $("input[name=penalty_level_2]");
        var penalty_level_3 = $("input[name=penalty_level_3]");
        var penalty_level_4 = $("input[name=penalty_level_4]");
        var penalty_level_5 = $("input[name=penalty_level_5]");
        var penalty_interest_leve_1 = $("input[name=penalty_interest_leve_1]");
        var penalty_interest_leve_2 = $("input[name=penalty_interest_leve_2]");
        var penalty_interest_leve_3 = $("input[name=penalty_interest_leve_3]");
        var penalty_interest_leve_4 = $("input[name=penalty_interest_leve_4]");
        var penalty_interest_leve_5 = $("input[name=penalty_interest_leve_5]");
        var times = $("#times").val();
        var minimum_amount = $("#minimum_amount").val();
        var maximum_amount = $("#maximum_amount").val();
        var product_type_id = $("#product_type_id").val();
        var repayment_method = $("#repayment_method").val();
        var regex = /^[0-9]+$/;
        if(product_name == null || product_name == ''){
            alert("产品名称不能为空");
            return;
        }
        if(product_description == null || product_description == ''){
            alert("产品描述不能为空");
            return;
        }
        if(finance_title == null || finance_title == ''){
            alert("融资标题不能为空");
            return;
        }
        var flag = false;
        $.each(terms,function(i,obj){
            if(obj.checked){
                flag = true;
            }
        });
        if(!flag){
            alert("期数不能为空");
            return;
        }
        if(information_manage_fee=='' || !Number(information_manage_fee) || Number(information_manage_fee) < 0){
            alert("信息管理费不能为空且不能小于0");
            return;
        }

        if(account_manage_fee=='' || !Number(account_manage_fee) || Number(account_manage_fee) < 0){
            alert("账户管理费不能为空且不能小于0");
            return;
        }
        if(platform_manage_fee=='' || !Number(platform_manage_fee) || Number(platform_manage_fee) < 0){
            alert("平台服务费不能为空且不能小于0");
            return;
        }
        var flag2 = true;
        $.each(terminterest,function(i,obj){
            if($(obj).val()=='' || !Number($(obj).val()) || Number($(obj).val()) < 0){
                flag2 = false;

            }
        });
        if(!flag2){
            alert("产品利率不能为空且不能小于0");
            return;
        }
        /*$.each(penalty_level_1,function(i,obj){
            if($(obj).val() == null || $(obj).val() == ''){
                alert("逾期1~3天日罚息利率不能为空");
                return;
            }
        })
        $.each(penalty_level_2,function(i,obj){
            if($(obj).val() == null || $(obj).val() == ''){
                alert("逾期4~10天日罚息利率不能为空");
                return;
            }
        })
        $.each(penalty_level_3,function(i,obj){
            if($(obj).val() == null || $(obj).val() == ''){
                alert("逾期11~30天日罚息利率不能为空");
                return;
            }
        })
        $.each(penalty_level_4,function(i,obj){
            if($(obj).val() == null || $(obj).val() == ''){
                alert("逾期31~90天日罚息利率不能为空");
                return;
            }
        })
        $.each(penalty_level_5,function(i,obj){
            if($(obj).val() == null || $(obj).val() == ''){
                alert("逾期91天以上日罚息利率不能为空");
                return;
            }
        })
        $.each(penalty_interest_leve_1,function(i,obj){
            if($(obj).val() == null || $(obj).val() == ''){
                alert("逾期1~3天日违约金利率不能为空");
                return;
            }
        })
        $.each(penalty_interest_leve_2,function(i,obj){
            if($(obj).val() == null || $(obj).val() == ''){
                alert("逾期4~10天日违约金利率不能为空");
                return;
            }
        })
        $.each(penalty_interest_leve_3,function(i,obj){
            if($(obj).val() == null || $(obj).val() == ''){
                alert("逾期11~30天日违约金利率不能为空");
                return;
            }
        })
        $.each(penalty_interest_leve_4,function(i,obj){
            if($(obj).val() == null || $(obj).val() == ''){
                alert("逾期31~90天日违约金利率不能为空");
                return;
            }
        })
        $.each(penalty_interest_leve_5,function(i,obj){
            if($(obj).val() == null || $(obj).val() == ''){
                alert("逾期91天以上日违约金利率不能为空");
                return;
            }
        })
        if(times == null || times == ''){
            alert("倍数不能为空");
            return;
        }*/

        if(minimum_amount=='' || !Number(minimum_amount) || Number(minimum_amount) < 0){
            alert("最低额度不能为空且不能小于0");
            return;
        }
        if(maximum_amount=='' || !Number(maximum_amount) || Number(maximum_amount) < 0){
            alert("最高额度不能为空且不能小于0");
            return;
        }
        if(+maximum_amount < +minimum_amount){
            alert("最高额度不能小于最低额度");
            return;
        }
        if(product_type_id == null || product_type_id == ''){
            alert("申请类型不能为空");
            return;
        }
        if(repayment_method == null || repayment_method == ''){
            alert("还款方式不能为空");
            return;
        }
        var url;
        if($("#editSystemUserInfoTitle").html().trim() == "添加产品"){
            if(product_icon == null || product_icon == ''){
                alert("产品图标不能为空");
                return;
            }
            url = "product/addProduct.action";
        }else if($("#editSystemUserInfoTitle").html().trim() == "修改产品"){
            url = "product/updateProduct.action";
        }
        $("#btnEditUserInfo").attr("disabled",true);
        $("#edituser-form").ajaxSubmit({
            type: "post",
            url: url,
            success: function (result) {
                $("#editSystemUserInfo").modal("hide");
                $("#btnEditUserInfo").removeAttr("disabled");
                if (result.code == 0) {
                    tableUtils.refresh("systemUserTables");
                }else{
                    alert("用户操作失败");
                }
            },
            error: function () {
                $("#btnEditUserInfo").removeAttr("disabled");
                alert("网络连接失败");
            },
            timeout: 50000
        });
    });
}
//初始化查询按钮
function initProductUserTables() {
    tableUtils.initMuliTableToolBar(
        "systemUserTables",
        "product/getProducts.action?userNo="+usernum,
        null,
        [
            {dataField: "id", caption: "产品名称", alignment: "center", allowFiltering: false,visible:false},
            {dataField: "product_name", caption: "产品名称", alignment: "center", allowFiltering: false},
            {dataField: "term", caption: "期数", alignment: "center", allowFiltering: false,calculateCellValue:function(data){
                var terms = "";
                if(data.productTerm){
                    $.each(data.productTerm,function(i,term){
                        if(i == data.productTerm.length-1){
                            terms+=term.term;
                        }else{
                            terms+=term.term+"|";
                        }
                    });
                }
                return terms;
            }},
            {dataField: "information_manage_fee", caption: "信息管理费(%)", alignment: "center", allowFiltering: false,},
            {dataField: "account_manage_fee", caption: "账户管理费(%)", alignment: "center", allowFiltering: false,},
            {dataField: "interest_rate", caption: "利息(%)", alignment: "center", allowFiltering: false,calculateCellValue:function(data){
                var interest = "";
                if(data.productTerm) {
                    $.each(data.productTerm, function (i, it) {
                        if (i == data.productTerm.length - 1) {
                            interest += it.interest_rate;
                        } else {
                            interest += it.interest_rate + "|";
                        }
                    });
                }
                return interest;
            }},
            {dataField: "minimum_amount", caption: "最低额度", alignment: "center", allowFiltering: false,},
            {dataField: "maximum_amount", caption: "最高额度", alignment: "center", allowFiltering: false,},
            {dataField: "repayment_method", caption: "还款方式", alignment: "center", allowFiltering: false,calculateCellValue:function(data){
                    if(data.repayment_method == 'A'){
                        return "等额本息";
                    }else if(data.repayment_method == 'B'){
                        return "先息后本";
                    }else if(data.repayment_method == 'C'){
                        return "到期本息";
                    }
            }},
            {dataField: "creation_user", caption: "添加人", alignment: "center", allowFiltering: false,},
            {dataField: "creation_date", caption: "添加时间", alignment: "center", allowFiltering: false,dataType: "date"},
            {dataField: "update_user", caption: "修改人", alignment: "center", allowFiltering: false,},
            {dataField: "update_date", caption: "修改时间", alignment: "center", allowFiltering: false,dataType: "date"},
            {dataField: "status",caption: "是否启用",alignment: "center",allowFiltering: false,calculateCellValue: function (data) {
                    if (data.status == 'A') {
                        return "启用";
                    } else {
                        return "禁用";
                    }
                }
            },
            /*{dataField: "penalty_level_1", caption: "违约金-逾期1-3天日利率", alignment: "center", allowFiltering: false,visible:false},
            {dataField: "penalty_level_2", caption: "违约金-逾期4-10天日利率", alignment: "center", allowFiltering: false,visible:false},
            {dataField: "penalty_level_3", caption: "违约金-逾期11-30天日利率", alignment: "center", allowFiltering: false,visible:false},
            {dataField: "penalty_level_4", caption: "违约金-逾期31-90天日利率", alignment: "center", allowFiltering: false,visible:false},
            {dataField: "penalty_level_5", caption: "违约金-逾期90天以上日利率", alignment: "center", allowFiltering: false,visible:false},
            {dataField: "penalty_interest_leve_1", caption: "罚息-逾期1-3天日利率", alignment: "center", allowFiltering: false,visible:false},
            {dataField: "penalty_interest_leve_2", caption: "罚息-逾期4-10天日利率", alignment: "center", allowFiltering: false,visible:false},
            {dataField: "penalty_interest_leve_3", caption: "罚息-逾期11-30天日利率", alignment: "center", allowFiltering: false,visible:false},
            {dataField: "penalty_interest_leve_4", caption: "罚息-逾期31-90天日利率", alignment: "center", allowFiltering: false,visible:false},
            {dataField: "penalty_interest_leve_5", caption: "罚息-逾期90天以上日利率", alignment: "center", allowFiltering: false,visible:false},*/
        ],
        "系统用户",
        function (e) {
            var dataGrid = e.component;
            var toolbarOptions = e.toolbarOptions.items;
            toolbarOptions.push(
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "添加",
                        text: "添加",
                        visible : !disableButton("ym-gd",0),
                        icon: "add",
                        onClick: function () {
                            $("#edituser-form").resetForm();
                            $("#btnEditUserInfo").removeAttr("disabled");
                            $("#editSystemUserInfoTitle").html("添加产品");
                            //因为添加，编辑，详情用的都是同一个页面，所有在此处需要删除在其他地方动态添加的元素
                            $("#edituser-form input,#edituser-form select").attr("disabled",false);
                            $("#product_icon").css("display","inline");
                            $("#product_icon ~ img").remove();
                            $("#id").remove();
                            $("#interest_table1").empty();
                            $("#interest_table3").empty();
                            $("#interest_table6").empty();
                            $("#interest_table1").parent().css("display","none");
                            $("#interest_table3").parent().css("display","none");
                            $("#interest_table6").parent().css("display","none");
                            $("#creation_user").attr("disabled",false);
                            $("#update_user").attr("disabled",true);
                            $("#creation_user").val(usernum);
                            $("#btnEditUserInfo").css("display","inline");
                            $("#editSystemUserInfo").modal({show: true, backdrop: 'static', keyboard: false});
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "详情",
                        text: "详情",
                        visible : !disableButton("ym-gd",1),
                        icon: "check",
                        onClick: function () {
                            var selectRows = dataGrid.getSelectedRowsData();
                            if(selectRows.length != 1){
                                alert("每次仅能操作一条数据");
                                return;
                            }
                            $("#edituser-form").resetForm();
                            $("#btnEditUserInfo").removeAttr("disabled");

                            $("#editSystemUserInfoTitle").html("产品详情");
                            var data = selectRows[0];
                            //console.info(data.id);
                            $("#product_name").val(data.product_name);
                            $("#finance_title").val(data.finance_title);
                            //隐藏图片上传选项，显示图片
                            $("#product_icon").css("display","none");
                            $("#product_icon ~ img").remove();
                            $("#product_icon").after("<img src="+data.product_icon+" />");
                            $("#product_description").val(data.product_description);
                            var tm = data.productTerm;
                            var tt = $("#term input");
                            for(var i=0; i<tt.length ;i++){
                                var obj = tt[i];
                                for(var j=0; j<tm.length; j++){
                                    if($(obj).val() == tm[j].term){
                                        obj.checked = true;
                                    }
                                }
                            }
                            $("#information_manage_fee").val(data.information_manage_fee);
                            $("#account_manage_fee").val(data.account_manage_fee);
                            $("#platform_manage_fee").val(data.platform_manage_fee);
                            $("#interest_table1").empty();
                            $("#interest_table3").empty();
                            $("#interest_table6").empty();
                            $("#interest_table1").parent().css("display","none");
                            $("#interest_table3").parent().css("display","none");
                            $("#interest_table6").parent().css("display","none");
                            for(var i=0 ;i<tm.length ;i++){
                                var tr = $("<tr>");
                                var td1 = $("<td style=\"width:150px \">");
                                var td2 = $("<td style=\"width:150px \">");
                                var td3 = $("<td style=\"width:150px \">");
                                if(tm[i].term == 1){
                                    var interest_table1 = $("#interest_table1");
                                    td2.html("1期产品");
                                    td3.html("<input type='text' id='term1interest'name='terminterest' value="+tm[i].interest_rate+"  style='width:75px'>&nbsp;%");
                                    tr.append(td1);
                                    tr.append(td2);
                                    tr.append(td3);
                                    interest_table1.append(tr);
                                    //addPenalty($("#interest_table1"),tm[i]);
                                    interest_table1.parent().css("display","block");
                                }else if(tm[i].term == 3){
                                    var interest_table3 = $("#interest_table3");
                                    td2.html("3期产品");
                                    td3.html("<input type='text' id='term3interest'name='terminterest' value="+tm[i].interest_rate+"  style='width:75px'>&nbsp;%");
                                    tr.append(td1);
                                    tr.append(td2);
                                    tr.append(td3);
                                    interest_table3.append(tr);
                                    //addPenalty($("#interest_table3"),tm[i]);
                                    interest_table3.parent().css("display","block");
                                }else if(tm[i].term == 6){
                                    var interest_table6 = $("#interest_table6");
                                    td2.html("6期产品");
                                    td3.html("<input type='text' id='term6interest'name='terminterest' value="+tm[i].interest_rate+"  style='width:75px'>&nbsp;%");
                                    tr.append(td1);
                                    tr.append(td2);
                                    tr.append(td3);
                                    interest_table6.append(tr);
                                    //addPenalty($("#interest_table6"),tm[i]);
                                    interest_table6.parent().css("display","block");
                                }

                            }
                            //$("#times").val(data.times);
                            $("#minimum_amount").val(data.minimum_amount);
                            $("#maximum_amount").val(data.maximum_amount);
                            $("#product_type_id").val(data.product_type_id);
                            $("#repayment_method").val(data.repayment_method);
                            var ss = $("#status input");
                            for(var i=0 ;i<ss.length ;i++){
                                if(ss[i].value == data.status){
                                    ss[i].checked = true;
                                }else{
                                    ss[i].checked = false;
                                }
                            }
                            $("#btnEditUserInfo").css("display","none");
                            $("#edituser-form input,#edituser-form select").attr("disabled",true);
                            $("#editSystemUserInfo").modal({show: true, backdrop: 'static', keyboard: false});
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "修改",
                        text: "修改",
                        visible : !disableButton("ym-gd",2),
                        icon: "edit",
                        onClick: function () {
                            var selectRows = dataGrid.getSelectedRowsData();
                            if(selectRows.length != 1){
                                alert("每次仅能操作一条数据");
                                return;
                            }
                            $("#edituser-form").resetForm();
                            $("#btnEditUserInfo").removeAttr("disabled");
                            $("#editSystemUserInfoTitle").html("修改产品");
                            var data = selectRows[0];
                            //因为添加，编辑，详情用的都是同一个页面，所有在此处需要删除在其他地方动态添加的元素
                            $("#edituser-form input,#edituser-form select").attr("disabled",false);
                            $("#product_icon").css("display","inline");
                            $("#product_icon ~ img").remove();
                            $("#interest_table tr:has(input)").remove();
                            $("#id").remove();
                            $("#edituser-form").append("<input type='hidden' id='id' name='id' value="+data.id+" >");
                            $("#product_name").val(data.product_name);
                            $("#finance_title").val(data.finance_title);
                            $("#product_description").val(data.product_description);
                            $("#creation_user").attr("disabled",true);
                            $("#update_user").attr("disabled",false);
                            $("#update_user").val(usernum);
                            var tm = data.productTerm;
                            var tt = $("#term input");
                            for(var i=0; i<tt.length ;i++){
                                var obj = tt[i];
                                for(var j=0; j<tm.length; j++){
                                    if($(obj).val() == tm[j].term){
                                        obj.checked = true;
                                    }
                                }
                            }
                            $("#information_manage_fee").val(data.information_manage_fee);
                            $("#account_manage_fee").val(data.account_manage_fee);
                            $("#platform_manage_fee").val(data.platform_manage_fee);
                            $("#interest_table1").empty();
                            $("#interest_table3").empty();
                            $("#interest_table6").empty();
                            $("#interest_table1").parent().css("display","none");
                            $("#interest_table3").parent().css("display","none");
                            $("#interest_table6").parent().css("display","none");
                            for(var i=0 ;i<tm.length ;i++){
                                var tr = $("<tr>");
                                var td1 = $("<td style=\"width:150px \">");
                                var td2 = $("<td style=\"width:150px \">");
                                var td3 = $("<td style=\"width:150px \">");
                                td1.html("<input type='hidden' name='term_id' value="+tm[i].term_id+"  style='width:75px'>");
                                if(tm[i].term == 1){
                                    var interest_table1 = $("#interest_table1");
                                    td2.html("1期产品");
                                    td3.html("<input type='text' id='term1interest'name='terminterest'  value="+tm[i].interest_rate+"  style='width:75px'>&nbsp;%");
                                    tr.append(td1);
                                    tr.append(td2);
                                    tr.append(td3);
                                    interest_table1.append(tr);
                                    //addPenalty($("#interest_table1"),tm[i]);
                                    interest_table1.parent().css("display","block");
                                }else if(tm[i].term == 3){
                                    var interest_table3 = $("#interest_table3");
                                    td2.html("3期产品");
                                    td3.html("<input type='text' id='term3interest'name='terminterest' value="+tm[i].interest_rate+"  style='width:75px'>&nbsp;%");
                                    tr.append(td1);
                                    tr.append(td2);
                                    tr.append(td3);
                                    interest_table3.append(tr);
                                    //addPenalty($("#interest_table3"),tm[i]);
                                    interest_table3.parent().css("display","block");
                                }else if(tm[i].term == 6){
                                    var interest_table6 = $("#interest_table6");
                                    td2.html("6期产品");
                                    td3.html("<input type='text' id='term6interest'name='terminterest'  value="+tm[i].interest_rate+" style='width:75px'>&nbsp;%");
                                    tr.append(td1);
                                    tr.append(td2);
                                    tr.append(td3);
                                    interest_table6.append(tr);
                                    //addPenalty($("#interest_table6"),tm[i]);
                                    interest_table6.parent().css("display","block");
                                }

                            }
                            //$("#times").val(data.times);
                            $("#minimum_amount").val(data.minimum_amount);
                            $("#maximum_amount").val(data.maximum_amount);
                            $("#product_type_id").val(data.product_type_id);
                            $("#repayment_method").val(data.repayment_method);
                            var ss = $("#status input");
                            for(var i=0 ;i<ss.length ;i++){
                                if(ss[i].value == data.status){
                                    ss[i].checked = true;
                                }else{
                                    ss[i].checked = false;
                                }
                            }
                            $("#btnEditUserInfo").css("display","inline");
                            $("#editSystemUserInfo").modal({show: true, backdrop: 'static', keyboard: false});
                        }
                    }
                }
            );
        }
    )
}
//初始化期数复选框，按键
function initTermBut(){
    $("#term input[type='checkbox']").on("click",function(){
        if(this.checked){
            //表示期数对应利息
            var tr = $("<tr>");
            var td1 = $("<td style=\"width:150px \">");
            var td2 = $("<td style=\"width:150px \">");
            var td3 = $("<td style=\"width:150px \">");
            td1.html("<input type='hidden' name='term_id' value='#' style='width:75px'>");
            if($(this).val() == '1'){
                var interest_table1 = $("#interest_table1");
                td2.html("1期产品");
                td3.html("<input type='text' id='term1interest'name='terminterest'  style='width:75px'>&nbsp;%");
                tr.append(td1);
                tr.append(td2);
                tr.append(td3);
                interest_table1.append(tr);
                //期数对应违约金和罚息
                //addPenalty(interest_table1);
                interest_table1.parent().css("display","block");

            }else if($(this).val() == '3'){
                var interest_table3 = $("#interest_table3");
                td2.html("3期产品");
                td3.html("<input type='text' id='term3interest'name='terminterest'  style='width:75px'>&nbsp;%");
                tr.append(td1);
                tr.append(td2);
                tr.append(td3);
                interest_table3.append(tr);
                //期数对应违约金和罚息
                //addPenalty(interest_table3);
                interest_table3.parent().css("display","block");
            }else if($(this).val() == '6'){
                var interest_table6 = $("#interest_table6");
                td2.html("6期产品");
                td3.html("<input type='text' id='term6interest' name='terminterest' style='width:75px'>&nbsp;%");
                tr.append(td1);
                tr.append(td2);
                tr.append(td3);
                interest_table6.append(tr);
                //期数对应违约金和罚息
                //addPenalty(interest_table6);
                interest_table6.parent().css("display","block");
            }

            //排序
            /*var arr = $("table[sort]").sort(function(a,b){
                return $(a).attr("sort")-$(b).attr("sort");
            });
            $("#interest_table tr:has(input)").each(function(){$(this.remove())});
            $.each(arr,function(i,obj){$("#interest_table").append($(this).parent().parent())})*/
        }else{
            if($(this).val() == '1'){
                $("#interest_table1").empty();
                $("#interest_table1").parent().css("display","none");
            }else if($(this).val() == '3'){
                $("#interest_table3").empty();
                $("#interest_table3").parent().css("display","none");
            }else if($(this).val() == '6'){
                $("#interest_table6").empty();
                $("#interest_table6").parent().css("display","none");
            }
        }
    })
}
function addPenalty(interest_table1,obj) {
    //添加罚息
    for (var i = 0; i < 5; i++) {
        var tr2 = $("<tr>");
        var td21 = $("<td style=\"width:150px \">");
        var td22 = $("<td style=\"width:150px \">");
        var td23 = $("<td style=\"width:150px \">");
        switch (i) {
            case 0:
                td21.html("违约金");
                td22.html("逾期1~3天日利率");
                if(obj){
                    td23.html("<input type='text' name='penalty_level_1' value="+obj.penalty_level_1+" style='width:75px'>&nbsp;%");
                }else{
                    td23.html("<input type='text' name='penalty_level_1'  style='width:75px'>&nbsp;%");
                }
                break;
            case 1:
                td22.html("逾期4~10天日利率");
                if(obj){
                    td23.html("<input type='text' name='penalty_level_2' value="+obj.penalty_level_2+" style='width:75px'>&nbsp;%");
                }else{
                    td23.html("<input type='text' name='penalty_level_2'  style='width:75px'>&nbsp;%");
                }
                break;
            case 2:
                td22.html("逾期11~30天日利率");
                if(obj){
                    td23.html("<input type='text' name='penalty_level_3' value="+obj.penalty_level_3+" style='width:75px'>&nbsp;%");
                }else{
                    td23.html("<input type='text' name='penalty_level_3'  style='width:75px'>&nbsp;%");
                }
                break;
            case 3:
                td22.html("逾期31~90天日利率");
                if(obj){
                    td23.html("<input type='text' name='penalty_level_4' value="+obj.penalty_level_4+" style='width:75px'>&nbsp;%");
                }else{
                    td23.html("<input type='text' name='penalty_level_4'  style='width:75px'>&nbsp;%");
                }
                break;
            case 4:
                td22.html("逾期91天以上日利率");
                if(obj){
                    td23.html("<input type='text' name='penalty_level_5' value="+obj.penalty_level_5+" style='width:75px'>&nbsp;%");
                }else{
                    td23.html("<input type='text' name='penalty_level_5'  style='width:75px'>&nbsp;%");
                }
                break;
        }
        tr2.append(td21);
        tr2.append(td22);
        tr2.append(td23);
        interest_table1.append(tr2);
    }
    //添加违约金
    for (var i = 0; i < 5; i++) {
        var tr2 = $("<tr>");
        var td21 = $("<td style=\"width:150px \">");
        var td22 = $("<td style=\"width:150px \">");
        var td23 = $("<td style=\"width:150px \">");
        switch (i) {
            case 0:
                td21.html("罚息");
                td22.html("逾期1~3天日利率");
                if(obj){
                    td23.html("<input type='text' name='penalty_interest_leve_1' value="+obj.penalty_interest_leve_1+" style='width:75px'>&nbsp;%");
                }else{
                    td23.html("<input type='text' name='penalty_interest_leve_1'  style='width:75px'>&nbsp;%");
                }
                break;
            case 1:
                td22.html("逾期1~3天日利率");
                if(obj){
                    td23.html("<input type='text' name='penalty_interest_leve_2' value="+obj.penalty_interest_leve_2+" style='width:75px'>&nbsp;%");
                }else{
                    td23.html("<input type='text' name='penalty_interest_leve_2'  style='width:75px'>&nbsp;%");
                }
                break;
            case 2:
                td22.html("逾期11~30天日利率");
                if(obj){
                    td23.html("<input type='text' name='penalty_interest_leve_3' value="+obj.penalty_interest_leve_3+" style='width:75px'>&nbsp;%");
                }else{
                    td23.html("<input type='text' name='penalty_interest_leve_3'  style='width:75px'>&nbsp;%");
                }
                break;
            case 3:
                td22.html("逾期31~90天日利率");
                if(obj){
                    td23.html("<input type='text' name='penalty_interest_leve_4' value="+obj.penalty_interest_leve_4+" style='width:75px'>&nbsp;%");
                }else{
                    td23.html("<input type='text' name='penalty_interest_leve_4'  style='width:75px'>&nbsp;%");
                }
                break;
            case 4:
                td22.html("逾期91天以上日利率");
                if(obj){
                    td23.html("<input type='text' name='penalty_interest_leve_5' value="+obj.penalty_interest_leve_5+" style='width:75px'>&nbsp;%");
                }else{
                    td23.html("<input type='text' name='penalty_interest_leve_5'  style='width:75px'>&nbsp;%");
                }
                break;
        }
        tr2.append(td21);
        tr2.append(td22);
        tr2.append(td23);
        interest_table1.append(tr2);
    }
}