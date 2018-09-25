
var register_source_operation = function() {
    setToolbar_ga_register();
    loadtable_ga_register();
    window_edit_value_register();
};

var setToolbar_ga_register = function() {
    $("#tool_edit").dxButton({
        hint : "修改",
        icon : "edit",
        visible : true,
        disabled:true,
        onClick : function() {
            var dataGrid = $('#registerSource').dxDataGrid('instance');
            var selectobj_param = dataGrid.getSelectedRowsData();
            if(selectobj_param.length > 1){
                showMessage("只能选择一条数据修改！");
            }else{
                $("#window_edit_value").dxPopup({
                    visible : true,
                });
                box_edit_value_operation(selectobj_param[0]);
            }
        }
    });
};


//加载界面
var loadtable_ga_register = function() {
    $.ajax({
        type : "post",
        url : "info/queryChannelCollectors.action",
        data:{},
        success : function(msg) {
            loadtable2_value_register(msg);
        }
    });
};

var loadtable2_value_register = function(jsonobj) {
    $("#registerSource").dxDataGrid({
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
            pageSize : 15,
        },
        pager : {
            showPageSizeSelector : true,
            allowedPageSizes : [10, 15, 30, 45, 60 ],
            showInfo : true,
            infoText : '第{0}页 . 共{1}页'
        },
        onSelectionChanged : function(data) {
            $("#tool_edit").dxButton({
                disabled : data.selectedRowsData.length != 1
            });
        },
        columns : [
            {
                dataField: "id",
				caption: "编号",
				alignment: "center"
			}, {
            dataField : "userName",
            caption : "渠道商名",
            alignment : "center",
        }, {
            dataField : "channelPercent",
            caption : "扣量百分比",
            alignment : "center",
            format:function(desc){
                if(!isNaN(desc)){
                    desc=parseInt(desc*100).toFixed(0);
                   return desc+"%";
                } else{
                   return desc;
                }
            }
        }, {
                dataField : "updateUser",
                caption : "修改人",
                alignment : "center",
            },
            {
                dataField : "updateDate",
                caption : "修改时间",
                alignment : "center",
                dataType: "date",
                format:formatDate,
            }]
    });
};

//加载修改界面
var window_edit_value_register = function() {
    $("#window_edit_value").dxPopup({
        showTitle : true,
        maxWidth : 600,
        maxHeight : 500,
        title : '修改',
        visible : false,
        WindowScroll : true,
        resizeEnabled : true,
        onHiding : function() {

        },
    });
};


//修改的
var box_edit_value_operation = function(selectobj) {
	var id=selectobj.id;
    var u_w_lookupcodemeaning_value = selectobj.userName;
    var u_w_description_value = selectobj.channelPercent;
    if(!isNaN(u_w_description_value)&&u_w_description_value!=''){
        u_w_description_value= u_w_description_value*100;
        u_w_description_value=parseInt(u_w_description_value).toFixed(0);
    }

    $("#u_w_lookupcodemeaning_value").dxTextBox({
        mode : "text",
       // placeholder : "必填",
        showClearButton : false,
        value :u_w_lookupcodemeaning_value,
        readOnly:true,
        onValueChanged : function(data) {
            u_w_lookupcodemeaning_value=data.value;
        }
    });

    $("#u_w_description_value").dxTextBox({
        mode : "text",
        placeholder : "必填",
        showClearButton : false,
        value :u_w_description_value,
        onValueChanged : function(data) {
            u_w_description_value=data.value;
        }
    });

    // 增加保存按钮
    $("#submitedit_ga_value").dxButton({
        text : "保存",
        hint : "保存",
        icon : "todo",
        disabled : false,
        onClick : function() {
            // if (u_w_lookupcodemeaning_value == '') {
            //     showMessage("渠道名不能为空");
            //     return;
            // }
            if (!/^([1-9]\d{0,1}|100|0)$/.test(u_w_description_value)) {
                 showMessage("扣量百分比必须为整数且只允许为0-100区间值");
                 return;
            }
            u_w_description_value=u_w_description_value*0.01.toFixed(2);
                var conmitdata = {
                id:id,
                userName:u_w_lookupcodemeaning_value,
                channelPercent:u_w_description_value,
                updateUser: $.cookie('username')
            };
            $.ajax({
                type : "POST",
                url : "info/updateChannelPercent.action",
                data : conmitdata,
                success : function(msg) {

                    if (msg.code > 0) {
                        showMessage("修改成功！");
                        $("#window_edit_value").dxPopup({
                            visible : false,
                        });
                        loadtable_ga();
                    } else {
                        showMessage("修改失败！");
                    }
                }
            });
        }
    });
};
