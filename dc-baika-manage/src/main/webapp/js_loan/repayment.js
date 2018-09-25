
var initTable_rm = function(){
    tableUtils.initMuliTableToolBar(
    	"userTable",
        "repayment/order.action?userNo="+usernum,
        null,
        [
        {dataField : "blackList",caption : "黑名单",alignment : "center",allowSorting:false,allowFiltering:false,
            calculateCellValue: function (data) {
                if(data.blackList) {
                    if (data.blackList == "Y") {
                        return "是"
                    } else if (data.blackList == "N") {
                        return "否"
                    }
                }
            }
        },
        {
            dataField : "borrNum",
            caption : "合同编号",
            alignment : "center",
            filterOperations:["="],
            allowSorting:false
        },
        {
            dataField : "userName",
            caption : "姓名",
            alignment : "center",
            filterOperations:["="],
            allowSorting:false,
            width:110
        },
        {
            dataField : "idCard",
            caption : "身份证号",
            alignment : "center",
            filterOperations:["="],
            allowSorting:false,
            width:190
        },
        {
            dataField : "customerMobile",
            caption : "手机号码",
            alignment : "center",
            filterOperations:["="],
            allowSorting:false,
            width:140
        },
        {
            dataField : "bankName",
            caption : "开户行",
            alignment : "center",
            filterOperations:["="],
            allowSorting:false
        },
        {
            dataField : "bankNum",
            caption : "银行卡号",
            alignment : "center",
            filterOperations:["="],
            allowSorting:false
        },
        {
            dataField : "prodId",
            caption : "产品名称",
            alignment : "center",
            width:150,
            lookup:{
                dataSource:pruducts,displayExpr: 'format',valueExpr: 'value'
            }
        },
        {
            dataField : "typeWithChannel",
            caption : "还款类型",
            alignment : "center",
            width:160,
            lookup: { dataSource: repayTypeSource,  displayExpr: 'format' } ,
            calculateCellValue: function (data) {
                if(data.type>14 && data.type<=18){
                    return repayTypes[data.typeWithChannel];
                }else{
                    return repayTypes[data.type];
                }
            }
        },
        {
            dataField : "rlState",
            caption : "还款状态",
            alignment : "center",
            allowSorting:false,
            lookup: { dataSource: [
                { value: 'p', format: '处理中' },
                { value: 's', format: '成功' },
                { value: 'f', format: '失败' }
            ],  displayExpr:'format'} ,
            cellTemplate: function (container, options) {
                var stateValue = options.value;
                if(stateValue == 'p'){
                    $("<div>").append(rlStateFormat(stateValue)+"&nbsp;&nbsp;<button type='button' onclick='queryState(\"" + options.data.serialNo + "\")' class='btn btn-primary btn-xs' style='font-size: 12px;line-height: 15px;'>查询</button>").appendTo(container)
                }else{
                    $("<div>").append(rlStateFormat(stateValue)).appendTo(container);
                }
            }
        },
        {
            dataField : "amount",
            caption : "还款金额",
            alignment : "center",
            allowFiltering:false,
            allowSorting:false
        },
        {
            dataField : "insDate",
            caption : "还款时间",
            alignment : "center",
            dataType: 'date',
            filterOperations:["=",">=","<","between"],
            format:formatDate
        },
        {
            dataField : "updateDate",
            caption : "结清时间",
            alignment : "center",
            dataType: 'date',
            filterOperations:["=",">=","<","between"],
            format:formatDate
        },
        {dataField : "createUser",caption : "操作人",alignment : "center",allowFiltering:true,allowSorting:false,width:100,
            lookup: {
                dataSource: collectorsAll,
                displayExpr: 'format'
            },
            calculateCellValue: function (data) {
                return data.createUserName;
            }
        },
        {dataField : "collectionUser",caption : "催收人",alignment : "center",allowFiltering:true,allowSorting:false,width:100,
            lookup: {
                dataSource: collectorsAll,
                displayExpr: 'format'
            },
            calculateCellValue: function (data) {
                return data.collectionUserName;
            }
        },
        {
            dataField : "remark",
            caption : "备注信息",
            alignment : "center",
            allowFiltering:false,
            allowSorting:false
        },
        {
            dataField : "serialNo",
            caption : "还款流水号",
            alignment : "center",
            filterOperations:["="],
            allowSorting:false
        },
        {
            dataField : "sidNo",
            caption : "支付流水号",
            alignment : "center",
            filterOperations:["="],
            allowSorting:false,
            calculateCellValue: function (data) {
                if (data.type >= 15 && data.type <= 18) {
                    return data.sidNo;
                } else {
                    return data.serialNo
                }
            }
        },
        {
            dataField : "overdueDays",
            caption : "逾期天数",
            alignment : "center",
            filterOperations:["="]
        }, {dataField : "channel",caption : "支付渠道",visible: false}],
        "还款流水"+new Date(),
        function(e){
            var dataGrid = e.component;
            var toolbarOptions = e.toolbarOptions.items;
            toolbarOptions.push(
            {
                location: "before",
                widget: "dxButton",
                options: {
                    hint: "查看",
                    text: "查看",
                    visible : !disableButton("ym-da",0),
                    icon: "find",
                    onClick: function () {
                        var selectData = dataGrid.getSelectedRowsData();
                        if (selectData.length == 0) {
                            alert("请选择需要查看的还款流水");
                            return;
                        }
                        if (selectData.length > 1) {
                            alert("一次只能操作一条数据");
                            return;
                        }
                        var customerId = selectData[0].perId;
                        var contractKey = selectData[0].borrId;
                        var contractId = selectData[0].borrNum;
                        layer_alert(customerId, contractKey, contractId);
                    }
                }
            }
            ,
            {
                location: "before",
                widget: "dxButton",
                options: {
                    hint: "导出",
                    text: "导出",
                    visible : !disableButton("ym-da",1),
                    icon: "download",
                    onClick: function () {
                        var filter = dataGrid.getCombinedFilter();
                        filter = JSON.stringify(filter) == undefined ? '' : JSON.stringify(filter);
                        var url = "repayment/order/export.action?filter=" + encodeURI(filter) + "&userNo=" +usernum+"&count="+dataGrid.totalCount();
                        exportData(url,null);
                    }
                }
            }
            ,
            {
                location: "before",
                widget: "dxButton",
                options: {
                    hint: "拉黑",
                    text: "拉黑",
                    visible : !disableButton("ym-da",2),
                    onClick: function () {
                        var selectobj = dataGrid.getSelectedRowsData();
                        if (selectobj.length > 0 && selectobj.length == 1) {
                            if(selectobj[0].blackList == 'Y'){
                                alert("该用户已经被拉黑！");
                                dataGrid.clearSelection();
                                return;
                            }
                            if (confirm("确定要将该用户拉入黑名单吗?")) {
                                $("#form-whiteBlackList").resetForm();
                                $("#whiteBlackListUserId").val(usernum);
                                $("#whiteBlackListContractId").val(selectobj[0].perId);
                                $("#whiteBlackListType").val(0);
                                $("#whiteBlackListTitle").html("拉入黑名单原因");
                                $("#whiteBlackList").modal({show: true, backdrop: 'static', keyboard: false});
                            }
                        } else if(selectobj.length > 1){
                            alert("一次只能操作拉黑一个用户");
                            dataGrid.clearSelection();
                        } else {
                            alert("请选择需要拉入黑名单的记录");
                            dataGrid.clearSelection();
                        }
                    }
                }
            }
            ,
            {
                location: "before",
                widget: "dxButton",
                options: {
                    hint: "刷新",
                    text: "刷新",
                    visible : !disableButton("ym-da",3),
                    icon: "refresh",
                    onClick: function () {
                        tableUtils.refresh("userTable");
                    }
                }
            })
        });
};
//导出函数
var exportData = function(url,params){
    var form=$("<form>");//定义一个form表单
    form.attr("style","display:none");
    form.attr("target","");
    form.attr("method","post");
    form.attr("action",url);
    $("body").append(form);//将表单放置在web中
    form.submit();//表单提交
};

var rlStateFormat = function (value) {
    if(value != null){
        if (value=="p"){
            return "处理中";
        }else if(value=="s"){
            return "成功";
        }else if(value=="f"){
            return "失败";
        }else{
            return "";
        }
    }
};

var queryState = function(serialNo){
    if(serialNo != null){
        $("#showMessage").html("正在从第三方查询流水号为:&nbsp;&nbsp;"+serialNo+"&nbsp;&nbsp;的扣款情况");
        $("#queryDialog").modal({show: true, backdrop: 'static', keyboard: false});
        $.ajax({
            type: 'POST',
            url: "orderStatus.action?serialNo="+serialNo,
            success: function (result) {
                setTimeout("callBack("+result.code+",'"+result.msg+"')",1000);
            },
            error: function (data) {
                $("#queryDialog").modal('hide');

            },
            timeout: 50000
        });
    }else{
        alert("该单流水号为空");
    }
};
var callBack = function(code,message){
    alert(message);
    $("#queryDialog").modal('hide');
    tableUtils.refresh("userTable");
};
var inintRepaymentBtn = function() {
    $("#btnWhiteBlackList").click(function(){
        var reason = $("#reason").val();
        if(reason == null || reason === ""){
            alert("请输入原因");
        }else{
            $("#btnWhiteBlackList").attr("disabled",true);
            $.ajax({
                type: "POST",
                url: "user/black.action",
                data: {
                    himid_list: $("#whiteBlackListContractId").val(),
                    operator: username,
                    reason: reason,
                    usernum: usernum,
                    type: $("#whiteBlackListType").val(),
                },
                success: function (result) {
                    $("#btnWhiteBlackList").removeAttr("disabled");
                    if (result.code == 200) {
                        alert("操作成功");
                        $("#whiteBlackList").modal("hide");
                        tableUtils.refresh("userTable");
                    } else {
                        alert(result.msg);

                    }
                },
                error: function (data) {
                    console.info(data);

                },
                timeout: 50000
            });
        }
    });
};
var repayment = function() {
    $('.modal-backdrop').hide();
    checkPageEnabled("ym-da");
    initTable_rm();
    inintRepaymentBtn();
};