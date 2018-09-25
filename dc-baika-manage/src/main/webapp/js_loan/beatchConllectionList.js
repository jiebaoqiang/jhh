var initTable_bcl = function(){
    tableUtils.initMuliTableToolBar(
        "userTable",
        "loanManagement/queryBatchReduceList.action",
        null,
        [ {
            dataField : "borrNum",
            caption : "合同编号",
            alignment : "center",
            filterOperations:["="]
        },{
            dataField : "serialNo",
            caption : "还款流水号",
            alignment : "center",
            filterOperations:["="]
        }, {
            dataField : "userName",
            caption : "姓名",
            alignment : "center",
            filterOperations:["="],
            allowSorting:false
        }, {
            dataField : "idCard",
            caption : "身份证号",
            alignment : "center",
            filterOperations:["="]
        }, {
            dataField : "customerMobile",
            caption : "手机号码",
            alignment : "center",
            filterOperations:["="]
        },{
            dataField : "productName",
            caption : "产品类型",
            alignment : "center",
            width:150,
            lookup: { dataSource: pruducts,  displayExpr: 'format' }
        },{
            dataField : "bedueDays",
            caption : "逾期天数",
            alignment : "center",
            allowFiltering:true,
            filterOperations:["="]
        },{
            dataField : "deductionsType",
            caption : "类型",
            alignment : "center",
            lookup: { dataSource: [
                { value: '1', format: '全额' },
                { value: '2', format: '定额' },
            ],  displayExpr: 'format' } ,
            calculateCellValue: function (data) {
                if(data.deductionsType){
                    if (data.deductionsType=='1'){
                        return "全额"
                    }else if (data.deductionsType=='2'){
                        return "定额"
                    }else{
                        return data.deductionsType;
                    }
                }
            }
        },{
            dataField : "amount",
            caption : "金额",
            alignment : "center",
            allowFiltering:false,
        }, {
            dataField : "rlState",
            caption : "状态",
            alignment : "center",
            lookup: { dataSource: [
                { value: 'p', format: '处理中' },
                { value: 's', format: '成功' },
                { value: 'f', format: '失败' },
            ],  displayExpr: 'format' } ,
            cellTemplate: function (container, options) {
                var stateValue = options.value;
                if(stateValue == 'p'){
                    $("<div>").append(rlStateFormat(stateValue)+"&nbsp;&nbsp;<button type='button' onclick='queryState(\"" + options.data.serialNo + "\")' class='btn btn-primary btn-xs' style='font-size: 12px;line-height: 15px;'>查询</button>").appendTo(container)
                }else{
                    $("<div>").append(rlStateFormat(stateValue)).appendTo(container);
                }
            }
        },{dataField : "createUser",caption : "操作人",alignment : "center",filterOperations:["="],allowFiltering:true,allowSorting:false,
                lookup: {
                    dataSource: collectorsAll,
                    displayExpr: 'format'
                },
                calculateCellValue: function (data) {
                    return data.createUserName;
                }
        }, {
            dataField : "insDate",
            caption : "还款时间",
            alignment : "center",
            dataType: 'date',
            filterOperations:["=","between"],
            format: formatDate
        }],
        "还款流水"+new Date(),
        function(e){
            var dataGrid = e.component;
            var toolbarOptions = e.toolbarOptions.items;
            toolbarOptions.push({
                location: "before",
                widget: "dxButton",
                options: {
                    hint: "查看",
                    text: "查看",
                    visible : !disableButton("ym-dg",0),
                    icon: "find",
                    onClick: function () {
                        var selectData = dataGrid.getSelectedRowsData();
                        if (selectData.length == 0) {
                            alert("请选择需要查看的还款计划");
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
            },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "刷新",
                        text: "刷新",
                        visible : !disableButton("ym-dg",1),
                        icon: "refresh",
                        onClick: function () {
                            tableUtils.refresh("userTable");
                        }
                    }
                })
        });
};

var beatchConllectionList = function(){
    $('.modal-backdrop').hide();
    checkPageEnabled("ym-dg");
    initTable_bcl();
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
