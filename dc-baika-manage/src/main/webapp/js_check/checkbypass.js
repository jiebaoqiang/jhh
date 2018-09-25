var checkbypass = function() {
    setToolbar_zd();
    loadtable2_zd();

    window_find_zd();
    window_pass_zd();
    window_zd();
};

var setToolbar_zd = function() {
    checkPageEnabled("ym-zd");

    $("#tool_find").dxButton({
        hint : "查看详细",
        text : "查看详细",
        icon : "find",
        disabled : true,
        onClick : function() {
            var dataGrid = $('#userTable').dxDataGrid('instance');
            var selectobj = dataGrid.getSelectedRowsData();
            var himid = selectobj[0].perId;
            var brroid = selectobj[0].id;
            loadwindow_userinfo(himid, brroid);
        }
    });

    $("#tool_pass").dxButton({
        hint: "通过",
        text: "通过",
        icon: "todo",
        disabled: true,
        visible: !disableButton("ym-zb", 7),
        onClick: function () {
            var dataGrid = $('#userTable').dxDataGrid('instance');
            var selectobj_param = dataGrid.getSelectedRowsData();
            if (selectobj_param.length > 1) {
                showMessage("只能选择一条数据！");
            } else {
                box_pass_value_zd(selectobj_param[0]);
            }
        }
    });

    $("#tool_no").dxButton({
        hint : "拒绝",
        text : "拒绝",
        icon : "close",
        disabled : true,
        onClick : function() {
            var dataGrid = $('#userTable').dxDataGrid('instance');
            var selectobj_param = dataGrid.getSelectedRowsData();
            if (selectobj_param.length > 1) {
                showMessage("只能选择一条数据！");
            } else {
                $("#window").dxPopup({
                    visible : true,
                });
                box_no_value_zd(selectobj_param[0]);
            }
        }
    });

    $("#tool_black_no").dxButton({
        hint : "拉黑并拒绝",
        text : "拉黑并拒绝",
        icon : "close",
        disabled : true,
        onClick : function() {
            var dataGrid = $('#userTable').dxDataGrid('instance');
            var selectobj_param = dataGrid.getSelectedRowsData();
            if (selectobj_param.length > 1) {
                showMessage("只能选择一条数据！");
            } else {
                $("#window").dxPopup({
                    visible : true,
                });
                box_black_value_zd(selectobj_param[0]);
            }
        }
    });

    $("#tool_cancel").dxButton({
        hint : "取消",
        text : "取消",
        icon : "close",
        disabled : true,
        onClick : function() {
            var dataGrid = $('#userTable').dxDataGrid('instance');
            var selectobj_param = dataGrid.getSelectedRowsData();
            if (selectobj_param.length > 1) {
                showMessage("只能选择一条数据！");
            } else {
                $("#window").dxPopup({
                    visible : true,
                });
                box_cancel_value_zd(selectobj_param[0]);
            }
        }
    });
};

var loadtable2_zd = function() {
    DevExpress.config({
        forceIsoDateParsing: true,

    });
    var orders = new DevExpress.data.CustomStore({
        load: function (loadOptions) {
            var deferred = $.Deferred(),
                args = {};
            args.filter = loadOptions.filter ? JSON.stringify(loadOptions.filter) : "";   // Getting filter settings
            args.sort = loadOptions.sort ? JSON.stringify(loadOptions.sort) : "";  // Getting sort settings
            args.requireTotalCount = loadOptions.requireTotalCount; // You can check this parameter on the server side
            if (loadOptions.sort) {
                args.orderby = loadOptions.sort[0].selector;
                if (loadOptions.sort[0].desc)
                    args.orderby += " desc";
            }

            args.skip = loadOptions.skip || 0;
            args.take = loadOptions.take || 15;
            args.employNum = usernum;
            args.borrStatus = "BS003";

            $.ajax({
                url: 'risk/auditsforUser.action' ,
                type: 'GET',
                data: args,
                success: function(result) {
                    result = JSON.parse(result);
                    deferred.resolve(result.list, { totalCount: result.total });
                },
                error: function() {
                    deferred.reject("Data Loading Error");
                },
                timeout: 50000
            });

            return deferred.promise();
        }
    });

    $("#userTable")
        .dxDataGrid(
            {
                dataSource : {
                    store: orders
                },
                dateSerializationFormat  : "yyyy-MM-dd HH:mm:ss",
                remoteOperations: {
                    sorting: true,
                    paging: true,
                    filtering:true
                },
                filterRow : {
                    visible : true,
                    applyFilter : "auto"
                },
                rowAlternationEnabled : true,
                showRowLines : true,
                selection : {
                    mode : "multiple"
                },
                allowColumnReordering : true,
                allowColumnResizing : true,
                columnAutoWidth : true,
                columnChooser : {
                    title : "列选择器",
                    enabled : true,
                    emptyPanelText : '把你想隐藏的列拖拽到这里...'
                },
                columnFixing : {
                    enabled : true
                },
                paging : {
                    pageSize : 15,
                },
                pager : {
                    showPageSizeSelector : true,
                    allowedPageSizes : [ 15, 30, 45, 60 ],
                    showInfo : true,
                    infoText : '第{0}页 . 共{1}页'
                },
                onSelectionChanged : function(data) {
                    var flag = false;


                    $("#tool_find")
                        .dxButton(
                            {
                                disabled : data.selectedRowsData.length != 1
                                || flag
                                || disableButton("ym-zd",0),
                            });
                    $("#tool_pass")
                        .dxButton(
                            {
                                disabled : data.selectedRowsData.length != 1
                                || flag
                                || disableButton("ym-zd",1),
                            });
                    $("#tool_no")
                        .dxButton(
                            {
                                disabled : data.selectedRowsData.length != 1
                                || flag
                                || disableButton("ym-zd",2),
                            });
                    $("#tool_black_no")
                        .dxButton(
                            {
                                disabled : data.selectedRowsData.length != 1
                                || flag
                                || disableButton("ym-zd",3),
                            });
                    $("#tool_cancel")
                        .dxButton(
                            {
                                disabled : (!data.selectedRowsData.length > 0)
                                || disableButton("ym-zd",4)
                            });
                },
                columns : [
                    {dataField: "borrNum",
                        caption: "合同编号",
                        alignment: "center",
                        allowFiltering:true,
                        filterOperations:["="],
                        allowSorting:false
                    }, {
                        dataField: "name",
                        caption: "姓名",
                        alignment: "center",
                        allowFiltering:true,
                        filterOperations:["="],
                        allowSorting:false
                    }, {
                        dataField: "cardNum",
                        caption: "身份证号",
                        alignment: "center",
                        allowFiltering:true,
                        filterOperations:["="],
                        allowSorting:false
                    }, {
                        dataField: "phone",
                        caption: "手机号码",
                        alignment: "center",
                        allowFiltering:true,
                        filterOperations:["="],
                        allowSorting:false
                    },{
                        dataField: "productName",
                        caption: "产品名称",
                        alignment: "center",
                        allowFiltering:true,
                        filterOperations:["="],
                        allowSorting:false,
                        lookup:{
                            dataSource:pruducts,
                            displayExpr: 'format'
                        },width:150
                    },{
                        dataField: "contactNum",
                        caption: "通讯录个数",
                        alignment: "center",
                        allowFiltering:true,
                        filterOperations:["="],
                        allowSorting:true
                    },{
                        dataField: "bankName",
                        caption: "银行名称",
                        alignment: "center",
                        allowFiltering:true,
                        filterOperations:["="],
                        allowSorting:false
                    }, {
                        dataField: "bankCard",
                        caption: "银行卡号",
                        alignment: "center",
                        allowFiltering:true,
                        filterOperations:["="],
                        allowSorting:false
                    }, {
                        dataField : "borrStatusValue",
                        caption : "合同状态",
                        alignment : "center",
                        allowFiltering:false
                    },{
                        dataField: "borrUpStatusValue",
                        caption: "上单状态",
                        alignment: "center",
                        lookup: {
                            dataSource: [
                                {value: 'BS006', format: '正常结清'},
                                {value: 'BS007', format: '已取消'},
                                {value: 'BS008', format: '审核未通过'},
                                {value: 'BS009', format: '电审未通过'},
                                {value: 'BS010', format: '逾期结清'},
                                {value: 'BS012', format: '发放失败/放款失败'},
                                {value: 'BS014', format: '提前结清'}
                            ], displayExpr: 'format'
                        }
                    },
                    {
                        dataField: "baikeluStatus",
                        caption: "自动电呼",
                        alignment: "center",
                        lookup: {
                            dataSource: [
                                {value: '0', format: '未拨打'},
                                {value: '1', format: '拨打中'},
                                {value: '2', format: '未完成'},
                                {value: '3', format: '通过'},
                                {value: '4', format: '拒绝'},
                                {value: '5', format: '未接通'},
                                {value: '6', format: '非本人'},
                            ], displayExpr: 'format', valueExpr: 'value'
                        }
                    },
                    {
                        dataField: "isManualValue",
                        caption: "是否人工审核",
                        alignment: "center",
                        lookup: {
                            dataSource: [
                                {value: '1', format: '是'},
                                {value: '2', format: '否'},
                            ], displayExpr: 'format'
                        }
                    },
                    {
                        dataField : "description",
                        caption : "认证说明",
                        alignment : "center",
                        allowFiltering:false,
                        allowSorting:false
                    },
                    {
                        dataField : "makeborrDate",
                        caption : "签约时间",
                        alignment : "center",
                        dataType:"date",
                        filterOperations:["=","between"]
                    } ]
            });
};

var window_pass_zd = function () {
    $("#window_pass").dxPopup({
        showTitle: true,
        title: '通过',
        maxWidth: 400,
        maxHeight: 220,
        visible: false,
        WindowScroll: true,
        resizeEnabled: true,
        onHiding: function () {
            setToolbar_zd();
            loadtable2_zd();
        }
    });
};

// 查看详情
var window_find_zd = function() {
    $("#window_find").dxPopup({
        showTitle : true,
        title : '详情',
        width : "95%",
        height : "88%",
        visible : false,
        WindowScroll : true,
        resizeEnabled : true,
        onHiding : function() {
            loadtable2_zd();
            setToolbar_zd();
        },
    });
};
var window_zd = function() {
    $("#window").dxPopup({
        showTitle : true,
        maxWidth : 500,
        maxHeight : 300,
        title : '确认',
        visible : false,
        WindowScroll : true,
        resizeEnabled : true,
        onHiding : function() {
            setToolbar_zd();
            loadtable2_zd();
        },
    });
};


var box_pass_value_zd = function(selectobj) {
    $("#window_pass").dxPopup({
        visible: true,
    });


    // 增加保存按钮
    $("#submit_pass").dxButton({
        text: "确定",
        hint: "确认发送",
        icon: "todo",
        disabled: false,
        onClick: function () {
            $.ajax({
                url: "review/contract/pass.action",
                data: {borrId:selectobj.id ,userNum : usernum},
                type: 'POST',
                timeout: 50000,
                dataType:"json",
                success: function (result) {
                    if (result.code == 200) {
                        $("#window_pass").dxPopup({
                            visible: false,
                        });
                        showMessage(result.msg);
                        var dataGrid = $('#userTable').dxDataGrid('instance');
                        dataGrid.refresh();
                    } else {
                        showMessage(result.msg);
                    }
                },
                error: function () {
                    // deferred.reject("Data Loading Error");
                }
            });
        }
    });
};

var box_no_value_zd = function(selectobj) {
    $("#reasonText").html("请说明拒绝理由");
    var reason = "";

    $("#reason").dxTextArea({
        placeholder : "必填",
        height : 100,
        value : reason,
        showClearButton : false,
        onValueChanged : function(data) {
            reason = data.value;
        }
    });
    // 增加保存按钮
    $("#submit_affirm")
        .dxButton(
            {
                text : "确定",
                hint : "确认发送",
                icon : "todo",
                // height: 35,
                // width: 70,
                disabled : false,
                onClick : function() {
                    var resultCode = auditBorrList(selectobj.id, reason,"review/contract/reject.action");
                    if(resultCode == 200){
                        $("#window").dxPopup({
                            visible : false,
                        });
                    }
                }
            });
};

var box_black_value_zd = function(selectobj) {
    $("#reasonText").html("请说明拒绝理由");
    var reason = "";

    $("#reason").dxTextArea({
        placeholder : "必填",
        height : 100,
        value : reason,
        showClearButton : false,
        onValueChanged : function(data) {
            reason = data.value;
        }
    });
    // 增加保存按钮
    $("#submit_affirm")
        .dxButton(
            {
                text : "确定",
                hint : "确认发送",
                icon : "todo",
                // height: 35,
                // width: 70,
                disabled : false,
                onClick : function() {
                    var resultCode = auditBorrList(selectobj.id, reason,"review/contract/blackReject.action");
                    if(resultCode == 200){
                        $("#window").dxPopup({
                            visible : false,
                        });
                    }
                }
            });
};

var box_cancel_value_zd = function(selectobj) {
    var reason = "";
    $("#reasonText").html("请说明取消理由");
    $("#reason").dxTextArea({
        placeholder : "必填",
        height : 100,
        value : reason,
        showClearButton : true,
        onValueChanged : function(data) {
            reason = data.value;
        }
    });
    // 增加保存按钮
    $("#submit_affirm")
        .dxButton(
            {
                text : "确定",
                hint : "确认发送",
                icon : "todo",
                disabled : false,
                onClick : function() {
                    auditBorrList(selectobj.id, reason,"review/contract/cancel.action");
                }
            });
};
