
var sms_temp = function() {
	setToolbar_yd();
	loadtable_yd();
	
	window_add_yd();
	window_edit_yd();

};
var userpower=""; 
var setToolbar_yd = function() {
    checkPageEnabled("ym-yd");

	$("#tool_add").dxButton({
		hint : "新增",
		text:"新增",
		icon : "add",
		disabled : disableButton("ym-yd",0),
		onClick : function() {
			$("#window_add").dxPopup({
				visible : true,
			});
			box_add_value_yd();
		}
	});
	
	$("#tool_edit").dxButton({
		hint : "编辑",
		text:"编辑",
		icon : "edit",
		disabled : true,
		onClick : function() {
			var dataGrid = $('#userTable').dxDataGrid('instance');
			var selectobj_param = dataGrid.getSelectedRowsData();
			$("#window_edit").dxPopup({
				visible : true,
			});
			box_edit_value_yd(selectobj_param[0]);
		}
	});

	

};

var loadtable_yd = function() {

	$.ajax({
		type : "post",
		url : "info/getAllSmsTemplateList.action",
		success : function(msg) {
			//var jsonTrans = eval("(" + msg + ")").result;
			loadtable2_yd(msg);
		}
	});
};
var loadtable2_yd = function(jsonobj) {
	$("#userTable").dxDataGrid({
		dataSource : {
			store : {
				type : "array",
				data : jsonobj
			}
		},
		searchPanel : {
			visible : true,
			width : 240,
			placeholder : "搜索..."
		},
		"export" : {
			enabled : true,
			fileName : "Employees",
			allowExportSelectedData : true
		},
		headerFilter : {
			visible : true
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
			pageSize : 10,
		},
		pager : {
			showPageSizeSelector : true,
			allowedPageSizes : [10, 15, 30, 45, 60 ],
			showInfo : true,
			infoText : '第{0}页 . 共{1}页'
		},
		onSelectionChanged : function(data) {
			$("#tool_edit").dxButton({
				disabled :(data.selectedRowsData.length != 1) || disableButton("ym-yd",1),
			});
		},
		columns : [ {
			dataField : "templateSeq",
			caption : "ID",
			alignment : "center",
		}, {
			dataField : "templateType",
			caption : "模板类型",
			alignment : "center",
		}, {
			dataField : "content",
			caption : "内容",
			alignment : "left",
		}, {
			dataField : "status",
			caption : "启用状态",
			alignment : "center",
			calculateCellValue: function(rowData){
				if(rowData.status=="1"){
					return "启用";
				}else{
					return "禁用";
				}
				
			}
		}, {
            dataField : "createUser",
            caption : "添加人",
            alignment : "center",

        }, {
			dataField : "createDate",
			caption : "添加时间",
			alignment : "center",
			
		}, {
            dataField : "updateUser",
            caption : "更新人",
            alignment : "center",
        }, {
			dataField : "updateDate",
			caption : "更新时间",
			alignment : "center",
			dataType: "date" ,
		}]
	});
};

//查看详情
var window_add_yd = function() {
	$("#window_add").dxPopup({
		showTitle : true,
		title : '新增短信模板',
		maxWidth : 650,
		maxHeight : 554,
		visible : false,
		WindowScroll : true,
		resizeEnabled : true,
		onHiding : function() {
			setToolbar_yd();
			loadtable_yd();
		},
	});
};

var window_edit_yd = function() {
	$("#window_edit").dxPopup({
		showTitle : true,
		maxWidth : 650,
		maxHeight : 554,
		title : '编辑短信模板',
		visible : false,
		WindowScroll : true,
		resizeEnabled : true,
		onHiding : function() {
			setToolbar_yd();
			loadtable_yd();
		},
	});
};


var box_add_value_yd = function() {
	var templateType = "";
	var content = "";
	var status = "1";
	$("#status").dxCheckBox({
	    text: '启用',
	    value: true,
	    onValueChanged: function(data) {
	    	if(data.value){
	    		status="1";
	    	}else{
	    		status="0";
	    	}
		}
	});
	$("#templateType").dxTextArea({
		placeholder : "必填",
		height : 55,
		value:templateType,
		showClearButton : false,
		onValueChanged : function(data) {
            templateType = data.value;
		}
	});
	$("#content").dxTextArea({
		placeholder : "必填",
		height : 250,
		//width:400,
		value:content,
		showClearButton : false,
		onValueChanged : function(data) {
			content = data.value;
		}
	});
	// 增加保存按钮
	$("#submit_add").dxButton({
		text : "确定",
		hint : "确认发送",
		icon : "todo",
		// height: 35,
		// width: 70,
		disabled : false,
		onClick : function() {
			if (templateType == '') {
				showMessage("必须填写模板类型！");
				return;
			}else if (content==""){
				showMessage("必须填写内容！");
				return;
			}
			var conmitdata = {
                	createUser:usernum,
                	templateType :templateType,
					 content:content,
					 status :status,
			};
			$.ajax({
				type : "POST",
				url : "info/addSmsTemplate.action",
				data : conmitdata,
				success : function(msg) {
					if (msg.code >=0) {
						$("#window_add").dxPopup({
							visible : false,
						});
						
					} else {
						showMessage("操作失败！");
					}
				}
			});
		}
	});
};

var box_edit_value_yd = function(selectobj) {
	var templateSeq = selectobj.templateSeq;
	var templateType = selectobj.templateType;
	var content = selectobj.content;
	var status = selectobj.status;
	$("#status2").dxCheckBox({
	    text: '启用',
	    value: status=="1",
	    onValueChanged: function(data) {
	    	if(data.value){
	    		status="1";
	    	}else{
	    		status="0";
	    	}
		}
	});
	$("#templateType2").dxTextArea({
		placeholder : "必填",
		height : 55,
		value:templateType,
		showClearButton : false,
		onValueChanged : function(data) {
            templateType = data.value;
		}
	});
	$("#content2").dxTextArea({
		placeholder : "必填",
		height : 250,
		//width:400,
		value:content,
		showClearButton : false,
		onValueChanged : function(data) {
			content = data.value;
		}
	});
	// 增加保存按钮
	$("#submit_edit").dxButton({
		text : "确定",
		hint : "确认发送",
		icon : "todo",
		// height: 35,
		// width: 70,
		disabled : false,
		onClick : function() {
			if (templateType == '') {
				showMessage("必须填写模板类型！");
				return;
			}else if (content==""){
				showMessage("必须填写内容！");
				return;
			}
			var conmitdata = {
                	templateSeq:templateSeq,
                	updateUser:usernum,
                	templateType :templateType,
					 content:content,
					 status :status,
			};
			$.ajax({
				type : "POST",
				url : "info/UpdateSmsTemplate.action",
				data : conmitdata,
				success : function(msg) {
					if (msg.code >=0) {
						$("#window_edit").dxPopup({
							visible : false,
						});
						
					} else {
						showMessage("操作失败！");
					}
				}
			});
		}
	});
};


