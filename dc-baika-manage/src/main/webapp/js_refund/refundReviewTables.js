var refundReviewTablesInit = function(){
    checkPageEnabled("ym-xa");
    initRefundTables();


    //确认
    $("#btnReview_ok").click(function(){
        var refundId = $("#refundId").val();
        var perId=$("#perId").val();
        $("#btnReview_ok").attr("disabled",true);
        $.ajax({
            type:'POST',
            url: "refundReview/affirm/"+perId+".action",
            data: {operator: $.cookie('userid')},
            success: function(result) {
                alert(result.msg);
                $("#btnReview_ok").removeAttr("disabled");
                tableUtils.refresh("refundReview");
                $("#refundOkModal").modal("hide");
            },
            error: function(data) {
                console.info(data);
                $("#btnReview_ok").removeAttr("disabled");

            },
            timeout: 50000
        });
    });
    //退款
    $("#btnReview").click(function(){
        var refundId = $("#refundId").val();
        var perId=$("#perId").val();
        var btnReview = $("#message-text").val();
        $("#btnReview").attr("disabled",true);
        $.ajax({
            type:'POST',
            url: "refundReview/refund/"+refundId+".action",
            data: {operator: $.cookie('userid'),remark:btnReview},
            success: function(result) {
                alert(result.msg);
                $("#btnReview").removeAttr("disabled");
                tableUtils.refresh("refundReview");
                $("#refundModal").modal("hide");
            },
            error: function(data) {
                console.info(data);
                $("#btnReview").removeAttr("disabled");

            },
            timeout: 50000
        });
    });

    //驳回
    $("#btnReview_no").click(function () {
        var refundId = $("#refundId").val();
        var btnReview_no = $("#message-text-no").val();
        $("#btnReview_no").attr("disabled",true);
        $.ajax({
            type:'POST',
            url: "refundReview/reject/"+refundId+".action",
            data: {remark:btnReview_no,operator: $.cookie('userid')},
            success: function(result) {
                alert(result.msg);
                $("#btnReview_no").removeAttr("disabled");
                tableUtils.refresh("refundReview");
                $("#refundNoModal").modal("hide");
            },
            error: function(data) {
                console.info(data);
                $("#btnReview_no").removeAttr("disabled");

            },
            timeout: 50000
        });
    });

};
var initRefundTables = function(){
    var auth=$.cookie("auth");
    tableUtils.initMuliTableToolBar(
        "refundReview",
        "refundReview/queryRefundReviewList.action?auth="+auth,
        null,
        [
            {
                dataField: "id", caption: "编号", alignment: "center",  filterOperations:["="],visible:false
            },
            {
                dataField: "perId", caption: "用户编号", alignment: "center",  filterOperations:["="],visible:false
            },
            {
                dataField: "userName", caption: "姓名", alignment: "center", allowFiltering: true, filterOperations:["="]
            },
            {
                dataField: "cardNum",caption: "身份证号码",alignment: "center",allowFiltering: true,filterOperations:["="]
            },
            {
                dataField: "phone", caption: "手机号码", alignment: "center", allowFiltering: true, filterOperations:["="]
            },
            {
                dataField: "balance", caption: "操作金额", alignment: "center", allowFiltering: true, filterOperations:["=","between","<",">"]
            },
            {
                dataField: "bankName", caption: "银行名称", alignment: "center", allowFiltering: true, filterOperations:["="]
            },
            {
                dataField: "bankNum", caption: "银行卡号", alignment: "center", allowFiltering: true, filterOperations:["="]
            },
            {
                dataField: "status",caption: "状态",alignment: "center",allowFiltering: true,
                lookup: {
                    dataSource: [
                        { value: '-1', format: '待确认' },
                        { value: '1', format: '已确认' },
                        { value: '2', format: '退款中' },
                        { value: '3', format: '退款失败' }
                        // { value: '4', format: '退款成功' },
                        // { value: '5', format: '退款拒绝' }
                        ],
                    valueExpr: 'value',
                    displayExpr: 'format'
                }
            },
            {
                dataField: "createDate",caption: "确认时间",alignment: "center",allowFiltering: true,dataType: "date", filterOperations:["=","between","<",">"],
                format: function (date) {
                    return tableUtils.formatDate(date);
                }
            }
        ],
        "退款列表",
        function(e){
            var dataGrid = e.component;
            var toolbarOptions = e.toolbarOptions.items;
            toolbarOptions.push(
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "详情",
                        text: "详情",
                        visible : !disableButton("ym-xa",0),
                        icon: "add",
                        onClick : function() {
                            var dataGrid = $('#refundReview').dxDataGrid('instance');
                            var selectobj = dataGrid.getSelectedRowsData();
                            var himid = selectobj[0].perId;
                            var brroid = selectobj[0].id;
                            loadwindow_userinfo(himid, brroid);
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "确认",
                        text: "确认",
                        visible : !disableButton("ym-xa",1),
                        icon: "edit",
                        onClick: function () {
                            var selectRows = dataGrid.getSelectedRowsData();
                            if (selectRows.length < 1) {
                                alert("请选择一条记录");
                                return;
                            }
                            if(selectRows.length != 1){
                                alert("每次仅能操作一条数据");
                                return;
                            }
                            if(selectRows[0].status!=-1){
                                alert("订单只有待确认状态，才允许确认操作");
                                return;
                            }
                            $("#refundId").val(selectRows[0].id);
                            $("#perId").val(selectRows[0].perId);
                            $("#okName").text(selectRows[0].userName);
                            $("#okMoney").text(selectRows[0].balance);
                            $("#okBankName").text(selectRows[0].bankName);
                            $("#okBankNo").text(selectRows[0].bankNum);
                            $("#refundOkModal").modal({show: true, backdrop: 'static', keyboard: false});
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "退款",
                        text: "退款",
                        visible :!disableButton("ym-xa",2),
                        icon: "edit",
                        onClick: function () {
                            var selectRows = dataGrid.getSelectedRowsData();
                            if (selectRows.length < 1) {
                                alert("请选择一条记录");
                                return;
                            }
                            if(selectRows.length !=1){
                                alert("每次仅能操作一条数据");
                                return;
                            }
                            if(selectRows[0].status!=1&&selectRows[0].status!=3){
                                alert("只有已确认和退款失败状态才允许退款操作");
                                return ;
                            }
                            $("#message-text").val("");
                            $("#refundId").val(selectRows[0].id);
                            $("#refundName").text(selectRows[0].userName);
                            $("#refundMoney").text(selectRows[0].balance);
                            $("#refundBankName").text(selectRows[0].bankName);
                            $("#refundBankNo").text(selectRows[0].bankNum);
                            $("#refundModal").modal({show: true, backdrop: 'static', keyboard: false});
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "驳回",
                        text: "驳回",
                        visible : !disableButton("ym-xa",3),
                        icon: "close",
                        onClick: function () {
                            var selectRows = dataGrid.getSelectedRowsData();
                            if (selectRows.length < 1) {
                                alert("请选择一条记录");
                                return;
                            }
                            if(selectRows.length !=1){
                                alert("每次仅能操作一条数据");
                                return;
                            }
                            if(selectRows[0].status!=1&&selectRows[0].status!=3){
                                alert("只有已确认和退款失败状态才允许驳回操作");
                                return ;
                            }

                            $("#message-text-no").val("");
                            $("#refundId").val(selectRows[0].id);
                            $("#perId").val(selectRows[0].perId);
                            $("#refundNoModal").modal({show: true, backdrop: 'static', keyboard: false});
                        }
                    }
                }
            );
        }
    )
};


