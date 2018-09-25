
var managerbymanager = function() {
	setToolbar_zc();
	loadtable_zc();

    window_add_zc();
};

var setToolbar_zc = function() {
    checkPageEnabled("ym-zc");

	$("#tool_ok").dxButton({
		hint : "启用",
		text:"启用",
		icon : "todo",
		disabled : true,
		onClick : function() {
			var dataGrid = $('#userTable').dxDataGrid('instance');
			var selectobj_param = dataGrid.getSelectedRowsData();
			var brroIds ="";
			for(var i = 0;i<selectobj_param.length;i++ ){
                brroIds += selectobj_param[i].id + ",";
			}
			box_ok_value_zc(brroIds);
		}
	});

	$("#tool_no").dxButton({
		hint : "禁用",
		text:"禁用",
		icon : "close",
		disabled : true,
		onClick : function() {
			var dataGrid = $('#userTable').dxDataGrid('instance');
			var selectobj_param = dataGrid.getSelectedRowsData();
			var brroIds ="";
			for(var i = 0;i<selectobj_param.length;i++ ){
                brroIds += selectobj_param[i].id + ",";
			}
			box_no_value_zc(brroIds);
		}
	});

    $("#tool_add").dxButton({
        hint : "添加",
        text : "添加",
        icon : "add",
        disabled : disableButton("ym-zc",2),
        onClick : function() {
            $("#manager_add").dxPopup({
                visible : true,
            });
            box_add_value_zc();
        }
    });


    $("#tool_del").dxButton({
        hint : "删除",
        text:"删除",
        icon : "close",
        disabled : true,
        // disabled : disableButton("ym-zc",3),
        onClick : function() {
            var dataGrid = $('#userTable').dxDataGrid('instance');
            var selectobj_param = dataGrid.getSelectedRowsData();
            if(selectobj_param.length > 1){
            	alert("一次只允许对一条数据进行删除");
            	return;
			}
            var employNum = selectobj_param[0].employNum;
            var employeeName = selectobj_param[0].employeeName;
            var resultStr = confirm("你确定要删除 "+employeeName+" 的信息么？");
            if(resultStr){
                box_del_value_zc(employNum);
			}

        }
    });


	

};
var loadtable_zc = function(){
	$.ajax({
		type : "GET",
		url :  "risk/reviewers.action",
		success : function(msg) {
			loadtable2_zc(msg);
		}
	});
};

var loadtable2_zc = function(jsonobj) {
	$("#userTable").dxDataGrid({
		dataSource : {
			store : {
				type : "array",
				 data: jsonobj,
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
			var flag2= false;
			for(var i = 0; i<data.selectedRowsData.length; i++){
				if(data.selectedRowsData[i].status != "y"){
					flag = true;
					break;
				}
				
			}
			for(var i = 0; i<data.selectedRowsData.length; i++){
				if(data.selectedRowsData[i].status == "y"){
					flag2 = true;
					break;
				}
			}
			$("#tool_ok").dxButton({
				disabled : !data.selectedRowsData.length || flag2 || disableButton("ym-zc",0) ,
			});
			$("#tool_no").dxButton({
				disabled : !data.selectedRowsData.length || flag ||  disableButton("ym-zc",1) ,
			});
			$("#tool_del").dxButton({
				disabled : !data.selectedRowsData.length || flag2 ||  disableButton("ym-zc",3) ,
			});
		},
		columns : [ {
			dataField : "employNum",
			caption : "员工编号",
			alignment : "center",
		}, {
			dataField : "employeeName",
			caption : "员工姓名",
			alignment : "center",
		}, {
			dataField : "status",
			caption : "启用状态",
			alignment : "center",
			calculateCellValue: function(rowData){
				if(rowData.status=='y'){
					return "启用";
				}else{
					return "禁用";
				}
			}
		}, {
			dataField : "creationDate",
			caption : "创建时间",
			alignment : "center",
			dataType: "date" ,
		}, {
			dataField : "updateDate",
			caption : "更新时间",
			alignment : "center",
			dataType: "date" ,
		}]
	});
};





var box_ok_value_zc = function(brroIds) {
	var status = "y";
	var conmitdata = {
        brroIds:brroIds,
		status:status,
	};
	$.ajax({
		type : "POST",
		url : "risk/reviewers/status.action",
		data : conmitdata,
		success : function(msg) {
			if (msg.code >=0) {
				showMessage("操作成功！");
				setToolbar_zc();
				loadtable_zc();
				
			} else {
				showMessage("操作失败！");
			}
	
		}
	});
};

var box_no_value_zc = function(brroIds) {
	var status = "n";
	var conmitdata = {
		brroIds:brroIds,
		status:status,
	};
	$.ajax({
		type : "POST",
		url : "risk/reviewers/status.action",
		data : conmitdata,
		success : function(msg) {
			if (msg.code >=0) {
				showMessage("操作成功！");
				setToolbar_zc();
				loadtable_zc();
				
			} else {
				showMessage("操作失败！");
			}
		}
	});
};

var box_del_value_zc = function(employNum) {
	var conmitdata = {
        employNum:employNum,
	};
	$.ajax({
		type : "POST",
		url : "risk/delReviewers.action",
		data : conmitdata,
		success : function(msg) {
			if (msg.code == 200) {
				showMessage("操作成功！");
				setToolbar_zc();
				loadtable_zc();

			} else {
				showMessage("操作失败！");
			}
		}
	});
};

//加载新增界面
var window_add_zc = function() {
    $("#manager_add").dxPopup({
        showTitle : true,
        maxWidth : 500,
        maxHeight : 300,
        title : '新增审核员',
        visible : false,
        WindowScroll : true,
        resizeEnabled : true,
        onHiding : function() {
            loadtable_zc();
        },
    });
};

//添加函数
var box_add_value_zc = function() {

    var employeeInfo = "";
    $.ajax({
        url: "risk/reviewers/queryManager.action",
        data: {skip: 0, take: 10000},
        type: 'POST',
        async: false,
        dataType: "json",
        success: function (result) {
            var data = result;
            var html = "";
            for (var i = 0; i < data.length; i++) {
                var selected = data[i].userSysno.indexOf(data.userName) != -1;
                html += "<option " + (selected ? "selected='selected'" : "") + " value='" + data[i].userSysno +","+ data[i].userName + "'>" + data[i].userName + "</option>";
            }
            $("#employNum").empty();
            $("#employNum").append(html);
        },
        error: function () {
            console.info("数据加载错误");
        },

        timeout: 50000
    });

    // 增加保存按钮
    $("#submitadd_zc").dxButton({
        text : "保存",
        hint : "保存",
        icon : "todo",
        // height: 35,
        // width: 70,
        disabled : false,
        onClick : function() {
            employeeInfo = $("#employNum").val();
            var conmitdata = {
                employeeInfo:employeeInfo,
            };
            $.ajax({
                type : "POST",
                url : "risk/addReviewers.action",
                data : conmitdata,
                success : function(msg) {
                    if (msg.code < 500) {
                        showMessage("添加成功");
                        $("#manager_add").dxPopup({
                            visible : false,
                        });
                        setToolbar_zc();
                        loadtable_zc();
                    } else {
                        showMessage(msg.msg);
                        $("#manager_add").dxPopup({
                            visible : false,
                        });
                        setToolbar_zc();
                        loadtable_zc();
                    }
                }
            });
        }
    });
};

