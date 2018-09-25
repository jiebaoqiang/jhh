var checkbymanual_result = function() {
	setToolbar_zmr();
	loadtable2_zmr();

	window_find_zmr();

};
var setToolbar_zmr = function() {
    checkPageEnabled("ym-zmr");

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
    $("#tool_export").dxButton({
        hint : "导出",
        text : "导出",
        icon : "export",
        disabled : true,
        onClick : function(loadOptions) {
            var dataGrid = $('#userTable').dxDataGrid('instance');
            var filter = dataGrid.getCombinedFilter();
            filter = JSON.stringify(filter) == undefined ? '' : JSON.stringify(filter);
            var url = "risk/manuallyReview/export.action?employ_num=" + usernum + "&filter=" + encodeURI(filter);
            exportData(url,null);
        }
    });
};

var loadtable2_zmr = function() {
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
							allowedPageSizes : [10, 15, 30, 45, 60 ],
							showInfo : true,
							infoText : '第{0}页 . 共{1}页'
						},
						onSelectionChanged : function(data) {
							var flag = false;
							for (var i = 0; i < data.selectedRowsData.length; i++) {
								if (data.selectedRowsData[i].borr_status == "BS001") {
									flag = true;
									break;
								}

							}

							$("#tool_find")
									.dxButton(
											{
                                                disabled : data.selectedRowsData.length != 1
                                                || flag
												|| disableButton("ym-zmr",0),
											});

                            $("#tool_export")
                                .dxButton(
                                    {
                                        disabled :
                                        	disableButton("ym-zmr",1),
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
                                lookup: {
                                    dataSource: [
                                        {value: 'BS002', format: '待签约'},
                                        {value: 'BS003', format: '已签约'},
                                        {value: 'BS004', format: '待还款'},
                                        {value: 'BS005', format: '逾期未还'},
                                        {value: 'BS006', format: '正常结清'},
                                        {value: 'BS007', format: '已取消' },
                                        {value: 'BS008', format: '审核未通过' },
                                        {value: 'BS009', format: '电审未通过'},
                                        {value: 'BS010', format: '逾期结清'},
                                        {value: 'BS011', format: '放款中'},
                                        {value: 'BS012', format: '放款失败'},
                                        {value: 'BS014', format: '提前结清'}
                                    ], displayExpr: 'format'
                                }
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
                                dataField : "reason",
                                caption : "人工审核理由",
                                alignment : "center",
                                allowFiltering:false,
                                allowSorting:false
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
                                filterOperations:["="],
                                allowSorting:false
                            },
                            {
                                dataField : "auditTime",
                                caption : "审核时间",
                                alignment : "center",
                                dataType:"date",
                                filterOperations:["=","between"],
                                format: function (date){
                                    return formatDateToTime(date);
								}
                            },{
                                dataField : "makeborrDate",
                                caption : "签约时间",
                                alignment : "center",
                                dataType:"date",
                                filterOperations:["=","between"]
                            }
						]
					});
};

// 查看详情
var window_find_zmr = function() {
	$("#window_find").dxPopup({
		showTitle : true,
		title : '详情',
		width : "95%",
		height : "88%",
		visible : false,
		WindowScroll : true,
		resizeEnabled : true,
		onHiding : function() {
			loadtable2_zmr();
		},
	});
};


