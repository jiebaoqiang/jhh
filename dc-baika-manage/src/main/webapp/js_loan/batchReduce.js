var setToolBar_batchReduce = function(){
    $(".modal").on('hide.bs.modal', function () {
        tableUtils.clearSelection("batchReduceTable");
    });
    $(".modal").on('hidden.bs.modal', function () {
        tableUtils.clearSelection("batchReduceTable");
    });

    $("#btnReduceBatch1").click(function(){
        var reduceData = $("#reduceData1").val();
        var reduceMoney = $("#reduceMoney1").val();
        var deductionsType ;
        var requestUrl = $("#requestUrl1").val();
        if(!requestUrl){
            alert("错误");
            return;
        }
        //操作金额判断
        if(!optAument(reduceMoney, "", "扣款", "batchDeductions")){
            return;
        }

        if(reduceMoney == 0){
            deductionsType = 1;
        }else {
            deductionsType = 2;
        }
        $("#btnReduceBatch1").attr("disabled",true);
        $.ajax({
            type:'POST',
            url: requestUrl,
            data: {reduceData:reduceData,reduceMoney:reduceMoney, deductionsType:deductionsType},
            success: function(result) {
                $("#btnReduceBatch1").removeAttr("disabled");
                alert(result.message);
                if(result.code == 1){
                    $("#collectionReduceBatch1").modal("hide");
                    tableUtils.refresh("batchReduceTable");
                }else{

                }
            },
            error: function(data) {
                $("#collectionReduceBatch1").removeAttr("disabled");
                console.info(data);

            },
            timeout: 50000
        });
    });
};

var initBatchReduceTable = function () {

    tableUtils.initMuliTableToolBar(
        "batchReduceTable",
        'loanManagement/queryBatchReduce.action?userNo='+usernum,
        null,
        [
            {dataField : "bedueDays",caption : "逾期天数",alignment : "center",allowFiltering:true,filterOperations:["=","between"],allowSorting:true},
            {dataField : "customerName",caption : "姓名",alignment : "center",allowFiltering:true,filterOperations:["="],allowSorting:false},
            {
                dataField : "productId",
                caption : "产品名称",
                alignment : "center",
                width:150,
                lookup:{
                    dataSource:pruducts,displayExpr: 'format',valueExpr: 'value'
                }
            },
            {dataField : "mstRepayAmount",caption : "剩余还款总额",alignment : "center",allowFiltering:false,allowSorting:true},
            {dataField : "endDateString",caption : "到期日期",alignment : "center",allowFiltering:true,allowSorting:true
                ,filterOperations:["=","between"],dataType:"date",calculateCellValue: function (data) {
                    if(data.endDateString){
                        return data.endDateString.toString();
                    }else{
                        return "";
                    }
                }
            },
            {dataField : "auditer",caption : "催收人",alignment : "center",allowFiltering:true,allowSorting:false,
                lookup: {
                    dataSource: collectorsAll,
                    valueExpr: 'value',
                    displayExpr: 'format'
                }
            },
            {dataField : "status",caption : "上单状态",alignment : "center",allowFiltering:true,allowSorting:false,
                lookup:{
                    dataSource:[
                        { value: '1', format: '成功' },
                        { value: '2', format: '失败可扣' },
                        { value: '3', format: '失败' },
                        { value: '4', format: '空白' }
                    ],displayExpr: 'format',valueExpr: 'value'
                }
            },
            {dataField : "reason",caption : "失败原因",alignment : "center",allowFiltering:true,allowSorting:false},
            {dataField : "contractKey",caption : "合同编号ID",visible: false}
        ],
        "贷后管理导出"+new Date(),
        function(e){
            var dataGrid = e.component;
            var toolbarOptions = e.toolbarOptions.items;
            toolbarOptions.push(
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint : "查看",
                        text: "查看",
                        visible : !disableButton("ym-dh",0),
                        onClick: function() {
                            var selectData = dataGrid.getSelectedRowsData();
                            if (selectData.length == 0) {
                                alert("请选择需要查看信息");
                                return;
                            }
                            if (selectData.length > 1) {
                                alert("一次只能操作一条数据");
                                return;
                            }
                            var customerId = selectData[0].customerId;
                            var contractId = selectData[0].contractKey;
                            var contract = selectData[0].contractID;
                            console.info(customerId+"="+contractId+"="+contract);
                            layer_alert(customerId, contractId,contract);
                            dataGrid.clearSelection();
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint : "批量扣款(拉卡拉)",
                        text : "批量扣款(拉卡拉)",
                        visible : !disableButton("ym-dh",1),
                        onClick : function() {
                            $("#reduce_batch1").resetForm();
                            var selectData = tableUtils.loadTableSelectRows("batchReduceTable");
                            console.log(selectData);
                            if (selectData.length == 0) {
                                alert("请选择需要需要扣款的信息,至少一条。");
                                return;
                            }
                            var customerNames = "";
                            for (var i = 0; i < selectData.length; i++) {
                                var customerName = selectData[i].customerName;
                                if (selectData[i].borrStatus == "BS005") {
                                    if (i == selectData.length - 1) {
                                        customerNames += customerName;
                                    } else {
                                        customerNames += customerName + ",";
                                    }
                                }
                            }
                            if(customerNames == ""){
                                alert("只能代扣状态逾期未还的单子");
                                return
                            }
                            var data = JSON.stringify(selectData);
                            $("#requestUrl1").val("loanManagement/batchCollection.action?createUser="+usernum);
                            $("#reduceData1").val(data);
                            $("#reduceNames1").val(customerNames);
                            $("#collectionReduceBatch1").modal({show: true, backdrop: 'static', keyboard: false});
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "刷新",
                        text: "刷新",
                        visible : !disableButton("ym-dh",2),
                        icon: "refresh",
                        onClick: function () {
                            tableUtils.refresh("batchReduceTable");
                        }
                    }
                });
        }
    );
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

var loanBatchReduce = function () {
    $('.modal-backdrop').hide();
    setToolBar_batchReduce();
    checkPageEnabled("ym-dh");
    initBatchReduceTable();
};
function optAument(inMoney, maxMoney, desc, type) {
    //批量扣款不能可以输入0
    if(type != "batchDeductions"){
        if(parseFloat(inMoney) <= 0){
            alert(desc + "金额必须大于0");
            return false;
        }

        if(!Number(inMoney) ){
            alert("金额只能输入数字");
            return false;
        }

        if(inMoney.toString().indexOf(".") != -1){
            if(inMoney.toString().split(".")[1].length > 2){//小数点后大于两位
                alert("小数点仅能保留两位");
                return false;
            }
        }

        if(parseFloat(inMoney) > parseFloat(maxMoney)){
            alert(desc + "金额不能大于最大" + desc + "金额");
            return false;
        }
    }else {
        if(parseFloat(inMoney) < 0){
            alert(desc + "金额不能为负数");
            return false;
        }
        if(!(inMoney === "0")){
            if(!Number(inMoney) ){
                alert("金额只能输入数字");
                return false;
            }
        }

        if(inMoney.toString().indexOf(".") != -1){
            if(inMoney.toString().split(".")[1].length > 2){//小数点后大于两位
                alert("小数点仅能保留两位");
                return false;
            }
        }

    }
    return true;
}