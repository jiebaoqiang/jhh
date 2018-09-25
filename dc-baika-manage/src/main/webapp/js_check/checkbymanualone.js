var checkbymanualone = function() {
	setToolbar_zma();
	loadtable2_zma();

	window_find_zma();
	window_zma();
    window_transfer_zma();

};

var setToolbar_zma = function() {
    checkPageEnabled("ym-zma");

	$("#tool_find").dxButton({
		hint : "查看详情",
		text : "详情",
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
	$("#tool_ok").dxButton({
		hint : "通过",
		text : "通过",
		icon : "todo",
		disabled : true,
		onClick : function() {
			var dataGrid = $('#userTable').dxDataGrid('instance');
			var selectobj_param = dataGrid.getSelectedRowsData();
			$("#window").dxPopup({
				visible : true,
			});
			box_ok_value_zma(selectobj_param[0]);
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
				box_no_value_zma(selectobj_param[0]);
			}
		}
	});

    $("#tool_black").dxButton({
        hint : "拉黑",
        text : "拉黑",
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
                box_black_value_zma(selectobj_param[0]);
            }
        }
    });

    $("#tool_transfer").dxButton({
        hint : "转件",
        text : "转件",
        icon : "revert",
        disabled : true,
        onClick : function() {
            var dataGrid = $('#userTable').dxDataGrid('instance');
            var selectobj_param = dataGrid.getSelectedRowsData();
            var brroid_list = "";
            for (var i = 0; i < selectobj_param.length; i++) {
                brroid_list += selectobj_param[i].id + ",";
            }
            box_transfer_value_zma(brroid_list);

        }
    });

};

var loadtable2_zma = function() {
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
                args.borrStatus = "BS001";

	            $.ajax({
	                url: 'risk/manuallyReview.action' ,
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
							for (var i = 0; i < data.selectedRowsData.length; i++) {
								if (data.selectedRowsData[i].borrStatus != "BS001") {
									flag = true;
									break;
								}

							}

							$("#tool_find")
									.dxButton(
											{
                                                disabled : data.selectedRowsData.length != 1
                                                || flag
                                                || disableButton("ym-zma",0),
											});
							$("#tool_ok")
									.dxButton(
											{
												disabled : data.selectedRowsData.length != 1
														|| flag
                                                		|| disableButton("ym-zma",1),
											});
							$("#tool_no")
									.dxButton(
											{
												disabled : data.selectedRowsData.length != 1
														|| flag
                                                		|| disableButton("ym-zma",2),
											});
                            $("#tool_black")
                                .dxButton(
                                    {
                                        disabled : data.selectedRowsData.length != 1
                                        || flag
                                        || disableButton("ym-zma",3),
                                    });
                            $("#tool_transfer")
                                .dxButton(
                                    {
                                        disabled : (!data.selectedRowsData.length > 0)
                                        			|| disableButton("ym-zma",4)
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
                            dataField: "contactNum",
                            caption: "通讯录个数",
                            alignment: "center",
                            allowFiltering:true,
                            filterOperations:["="],
                            allowSorting:true
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
                            }, {
                                dataField: "borrAmount",
                                caption: "贷款金额",
                                alignment: "center",
                                dataType: "number",
                                allowFiltering:true,
                                filterOperations:["="],
                                allowSorting:false
                            }, {
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
                                        {value: 'BS012', format: '放款失败'},
                                        {value: 'BS014', format: '提前结清'}
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
							dataField : "employeeName",
							caption : "审核人",
							alignment : "center",
                            allowSorting:false
						},
						{
							dataField : "createDate",
							caption : "注册时间",
							alignment : "center",
							dataType:"date",
                            filterOperations:["=","between"]
						} ]
					});
};

// 查看详情
var window_find_zma = function() {
	$("#window_find").dxPopup({
		showTitle : true,
		title : '详情',
		width : "95%",
		height : "88%",
		visible : false,
		WindowScroll : true,
		resizeEnabled : true,
		onHiding : function() {
			loadtable2_zma();
            setToolbar_zma();
		},
	});
};
var window_zma = function() {
	$("#window").dxPopup({
		showTitle : true,
        maxWidth : 500,
        maxHeight : 300,
		title : '确认',
		visible : false,
		WindowScroll : true,
		resizeEnabled : true,
		onHiding : function() {
			setToolbar_zma();
			loadtable2_zma();
		},
	});
};

var window_transfer_zma = function() {
    $("#window_transfer").dxPopup({
        showTitle : true,
        maxWidth : 500,
        maxHeight : 300,
        title : '转件',
        visible : false,
        WindowScroll : true,
        resizeEnabled : true,
        onHiding : function() {
            setToolbar_zma();
            loadtable2_zma();
        },
    });
};

var box_ok_value_zma = function(selectobj) {

    $("#reasonText").html("请说明通过理由");
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
	$("#submit_affirm").dxButton({
        text : "确定",
        hint : "确认发送",
        icon : "todo",
        disabled : false,
        onClick : function() {
            var resultCode = auditBorrList(selectobj.id, reason,"review/pass.action");
            if(resultCode == 200){
                $("#window").dxPopup({
                    visible : false,
                });
            }
        }
    });
};

var box_no_value_zma = function(selectobj) {
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
                    var resultCode = auditBorrList(selectobj.id, reason,"review/reject.action");
                    if(resultCode == 200){
                        $("#window").dxPopup({
                            visible : false,
                        });
                    }
                }
            });
};


var box_black_value_zma = function(selectobj) {
	var reason = "";
	$("#reasonText").html("请说明拉黑理由");
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
                    auditBorrList(selectobj.id, reason,"review/black.action");
                }
            });
};
var box_transfer_value_zma = function(borrIds) {
    $("#window_transfer").dxPopup({
        visible : true,
    });
    var transfer;

    $.ajax({
        type : "GET",
        url : "risk/reviewers.action",
        data : {
            status : "y",
        },
        success : function(msg) {
            $('#transfer').dxSelectBox({
                dataSource : msg,
                placeholder : "必填",
                valueExpr : 'employNum',
                displayExpr : 'employeeName',
                showClearButton : true,
                onValueChanged : function(e) {
                    transfer = e.value;
                }
            });
        }
    });

    // 增加保存按钮
    $("#submit_transfer").dxButton({
        text : "确定",
        hint : "确认发送",
        icon : "todo",
        disabled : false,
        onClick : function() {
            riskTransfer(borrIds, transfer);
        }
    });
};
