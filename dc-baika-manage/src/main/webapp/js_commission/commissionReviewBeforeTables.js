var commissionReviewBeforeTablesInit = function(){
    checkPageEnabled("ym-ve");
    queryCommissionReviewList();

    $("#btnReview_submit").click(function () {

        var reviewId = $("#reviewId").val();
        var reviewResult = $("#reviewResult").val();
        var reviewReason = $("#message-text").val();

        $("#btnReview_submit").attr("disabled",true);
        $.ajax({
            type:'POST',
            url: "commissionReview/updateCommissionReviewResult.action",
            data: {reviewId:reviewId, reviewResult:reviewResult, reviewReason:reviewReason,userId: $.cookie('userid')},
            success: function(result) {
                alert(result.msg);
                $("#btnReview_submit").removeAttr("disabled");
                tableUtils.refresh("commissionReviewBefore");
                $("#reviewModal").modal("hide");
            },
            error: function(data) {
                console.info(data);
                $("#btnReview_submit").removeAttr("disabled");

            },
            timeout: 50000
        });

        $("#btnReduce").removeAttr("disabled");
    });
};


var queryCommissionReviewList = function(){
    tableUtils.initMuliTableToolBar(
        "commissionReviewBefore",
        "commissionReview/queryCommissionReviewList.action",
        null,
        [
            {
                dataField: "id", caption: "申请编号", alignment: "center", allowFiltering: true
            },
            {
                dataField: "phone", caption: "用户手机号", alignment: "center", allowFiltering: true
            },
            {
                dataField: "applyAmount", caption: "申请佣金", alignment: "center", allowFiltering: true
            },
            {
                dataField: "isChannel",caption: "是否为渠道",alignment: "center",allowFiltering: true,
                lookup: {
                    dataSource: [
                        { value: '1', format: '是' },
                        { value: '2', format: '否' },
                    ],
                    valueExpr: 'value',
                    displayExpr: 'format'
                }
            },
            {
                dataField: "applyDate",caption: "申请时间",alignment: "center",  filterOperations:["=","between"], dataType: "date",
                format: function (date) {
                    return tableUtils.formatDate(date);
                }
            },
            {
                dataField: "status",caption: "审核状态",alignment: "center",allowFiltering: true,
                lookup: {
                    dataSource: [
                        { value: '0', format: '未放款' },
                        { value: '1', format: '放款中' },
                        { value: '3', format: '放款失败' },
                    ],
                    valueExpr: 'value',
                    displayExpr: 'format'
                }
            },
        ],
        "佣金审核",
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
                        visible : !disableButton("ym-ve",0),
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
                            var customerId = selectData[0].perId;
                            load_commission_detail(customerId);
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint : "通过",
                        text: "通过",
                        visible : !disableButton("ym-ve",1),
                        onClick: function() {
                            $("#reject-form").resetForm();
                            var selectData = dataGrid.getSelectedRowsData();
                            if (selectData.length == 0) {
                                alert("请选择一条记录");
                                return;
                            }
                            if (selectData.length > 1) {
                                alert("一次只能审核一条数据");
                                return;
                            }
                            if(selectData[0].status==1){
                                alert("该佣金已是放款中");
                                return;
                            }
                            $("#reviewId").val(selectData[0].id);
                            $("#reviewResult").val("1");
                            $("#reviewModal").modal({show: true, backdrop: 'static', keyboard: false});
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint : "拒绝",
                        text: "拒绝",
                        visible : !disableButton("ym-ve",2),
                        onClick: function() {
                            $("#reject-form").resetForm();
                            var selectData = dataGrid.getSelectedRowsData();
                            if (selectData.length < 1) {
                                alert("请选择一条记录");
                                return;
                            }
                            if (selectData.length > 1) {
                                alert("一次只能审核一条数据");
                                return;
                            }
                            $("#reviewId").val(selectData[0].id);
                            $("#reviewResult").val("2");
                            $("#reviewModal").modal({show: true, backdrop: 'static', keyboard: false});
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "刷新",
                        text: "刷新",
                        visible : !disableButton("ym-ve",3),
                        icon: "refresh",
                        onClick: function () {
                            dataGrid.refresh();
                        }
                    }
                }
            );
        }
    )
};

var load_commission_detail = function (perId) {
    var brroid = 0;
    var borrNum = 0;
    var iWidth = 1350;
    if (window.screen.availWidth < 1350) {
        iWidth = window.screen.availWidth;
    }
    var iHeight = window.screen.availHeight - 34;
    var iTop = 0;
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
    var var_text = "hi=" + perId + "_"+brroid + "_" + borrNum;
    $.ajax({
        type : "post",
        url : "/loan-manage/url/url_lock.action",
        data : {
            var_text : var_text
        },
        success : function(msg) {
            url_locked = msg;
            if(window_type == "pho"){
                window.open("/loan-manage/page_commission/commissionReviewBeforeTablesDetail.html?"
                    + url_locked + "", "佣金审核详情", "height=" + iHeight
                    + ", width=" + iWidth + ", top=" + iTop + ", left=" + iLeft
                    + ",location=no,resizable=no");
            }else{
                window.location.href = "/loan-manage/page_commission/commissionReviewBeforeTablesDetail.html?"
                    + url_locked + "";
            }

        }
    });
};