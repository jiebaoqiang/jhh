var riskSource = [];
var orderTypeSource = [];
var orderTypes = [];

var loadOrderTypes = function () {
    $.ajax({
        url: "/loan-manage/loanManagement/queryOrderType.action",
        data: {skip: 0, take: 10000},
        type: 'POST',
        dataType: "json",
        success: function (result) {
            if (result.code == 1) {
                orderTypes = result.object;
                for(var key in orderTypes){
                    var obj = {'value': key, 'format': orderTypes[key]};
                    orderTypeSource.push(obj);
                }
            }
        },
        error: function () {
            console.info("数据加载错误");
        },
        timeout: 50000
    });
};

var loadSource = function () {
    $.ajax({
        url: "/loan-manage/user/source.action?code=risk_source",
        type: 'GET',
        dataType: "json",
        success: function (result) {
            if (result.code == 1) {
                var data = result.object;
                for (var i = 0; i < data.length; i++) {
                    var m = data[i];
                    var obj = {value: m.CODE, format: m.VALUE};
                    riskSource.push(obj);
                }
            }
        },
        error: function () {
            console.info("数据加载错误");
        },
        timeout: 50000
    });
};

var loadbox_data = function(perId) {
	$.ajax({
		type : "GET",
		url : "../user/person/" + perId + ".action",
		success : function(msg) {
			var himinfo = msg.data;
			name = himinfo.name;
			cardNum = himinfo.cardNum;
			qqNum = himinfo.qqNum;
			email = himinfo.email;
			education = himinfo.education;
			marry = himinfo.marry;
			getchild = himinfo.getchild;

			usuallyaddress = himinfo.usuallyaddress;
			sourceValue = himinfo.sourceValue;
			phone = himinfo.phone;
			address = himinfo.address;
			birthday = himinfo.birthday;
			if(himinfo.blacklist == 'N'){
				blacklist = "否";
			}else if(himinfo.blacklist == 'Y'){
				blacklist = "是";
			}else {
				blacklist = '';
			}
			createDate = himinfo.createDate;
			
			
			profession = himinfo.profession;
			monthlypay = himinfo.monthlypay;
			business = himinfo.business;
			busiCity = himinfo.busiCity;
			busiAddress = himinfo.busiAddress;
			busiPhone = himinfo.busiPhone;
			relatives = himinfo.relativesValue;
			relativesName = himinfo.relativesName;
			relaPhone = himinfo.relaPhone;
			society = himinfo.societyValue;
			societyName = himinfo.societyName;
			sociPhone = himinfo.sociPhone;
            isCar = himinfo.isCar;
            houseCondition = himinfo.houseCondition;
            loadOrderTypes();
            loadbox_box();

            var imageZ = himinfo.imageZ;
			var imageF = himinfo.imageF;
			if (imageZ != null && imageZ != "") {
				$("#imageZ").html(
					"<img width='500' height='300' src='" + imageZ + "'/>");
			}
			if (imageF != null && imageF != "") {
				$("#imageF").html(
					"<img width='500' height='300' src='" + imageF + "'/>");
			}


		}
	});
};

//
// var getCardPicById = function(perId) {
// 	$.ajax({
// 		type : "GET",
// 		url : "../user/identityCard/" + perId + ".action",
// 		success : function(msg) {
// 			var cardinfo = msg.data;
//             if(cardinfo != null) {
//                 var imageZ = cardinfo.imageZ;
//                 var imageF = cardinfo.imageF;
//                 if (imageZ != null && imageZ != "") {
//                     $("#imageZ").html(
//                         "<img width='500' height='300' src='" + imageZ + "'/>");
//                 }
//                 if (imageF != null && imageF != "") {
//                     $("#imageF").html(
//                         "<img width='500' height='300' src='" + imageF + "'/>");
//                 }
//             }
// 		}
// 	});
// }

// load the table of loan
var getLoan = function(perId) {
	$.ajax({
		type : "GET",
		url : "../borrList/" + perId + ".action",
		success : function(msg) {
			if(msg.code == 200 ){
                loadtable2_loan(msg.data);
			}
		}
	});
};

// load the table
var loadtable2_loan = function(jsonobj) {
	$("#loan_table").dxDataGrid({
		dataSource : {
			store : {
				type : "array",
				// key : "id",
				data : jsonobj
			}
		},
		rowAlternationEnabled : true,
		showRowLines : true,
		allowColumnReordering : true,
		allowColumnResizing : true,
		columnAutoWidth : true,
		columnFixing : {
			enabled : true
		},
		paging : {
			pageSize : 20,
		},
		pager : {
			showPageSizeSelector : true,
			allowedPageSizes : [10, 15, 30, 45, 60 ],
			showInfo : true,
			infoText : '第{0}页 . 共{1}页'
		},
		onSelectionChanged : function(data) {
			$("#tool_edit").dxButton({
				disabled : data.selectedRowsData.length != 1,
			});
			$("#tool_del").dxButton({
				disabled : !data.selectedRowsData.length,
			});
			$("#tool_info").dxButton({
				disabled : data.selectedRowsData.length != 1,
			});
			// deleteButton.option("disabled", !data.selectedRowsData.length)
		},
		columns : [ {
			dataField : "borrNum",
			caption : "借款编号",
			alignment : "center",

		},{
            dataField : "borrAmount",
            caption : "贷款金额",
            alignment : "center",
        },{
            dataField : "productName",
            caption : "产品名称",
            alignment : "center"
		},{
			dataField : "loanDay",
			caption : "借款期数",
			alignment : "center"
		},{
            dataField : "makeborrDate",
            caption : "签约日期",
            alignment : "center",
            dataType : "date",
        },{
            dataField : "payDate",
            caption : "放款日期",
            alignment : "center",
            dataType : "date"
        },{
			dataField : "planrepayDate",
			caption : "到期日",
			alignment : "center",
			dataType : "date"
		},{
            dataField : "actRepayDate",
            caption : "结清日",
            alignment : "center",
            dataType : "date"
        },{
            dataField : "planRepay",
            caption : "应还款总额",
            alignment : "center",
        },{
            dataField : "actRepayAmount",
            caption : "实际还款总额",
            alignment : "center",
        },{
			dataField : "borrStatus",
			caption : "合同状态",
			alignment : "center",
		},{
            dataField : "isManual",
            caption : "是否人工审核",
            alignment : "center",
            calculateCellValue: function (data) {
                if(data.isManual != 4){
                    return "是";
                }else{
                    return "否";
                }
            },
            lookup: { dataSource: [
                { value: '1', format: '是' },
                { value: '2', format: '否' },
            ],  displayExpr: 'format' }
        }, {
            dataField : "description",
            caption : "认证说明",
            alignment : "center"
        },
         {
                dataField : "jdNumber",
                caption : "购物卡卡号",
                alignment : "center"
         }
        // {
        //     dataField : "bedueDays",
        //     caption : "逾期天数",
        //     alignment : "center"
        // },
        // {
        //     dataField : "loanRemark",
        //     caption : "催收备注",
        //     alignment : "center",
        //     cellTemplate: function (container, options) {
        //         var borrId = options.data.id;
        //         $("<div>").append("&nbsp;&nbsp;<button type='button' onclick='newWindowLoanRemark(\""+borrId+"\")' class='btn btn-primary btn-xs' style='font-size: 12px;line-height: 15px;'>查询</button>").appendTo(container);
        //     }
        // }
        // ,{
        //         dataField : "serial_number",
        //         caption : "手机序列号",
        //         alignment : "center",
        //         cellTemplate: function (container, options) {
        //             var stateValues = options.data;
        //             if(stateValues.serial_number == undefined || stateValues.serial_number == null || stateValues.serial_number == ""){
        //                 $("<div>").append("&nbsp;&nbsp;<button type='button' onclick='newWindowSerialNumImage (\""+stateValues.serial_number_url+"\")' class='btn btn-primary btn-xs' style='font-size: 12px;line-height: 15px;'>查询</button>").appendTo(container);
        //             }
        //             else{
        //                 container.text(stateValues.serial_number);
        //             }
        //         }
        //     }
        //     ,{
        //         dataField : "repayment",
        //         caption : "还款计划",
        //         alignment : "center",
        //         cellTemplate: function (container, options) {
        //             var stateValues = options.data;
        //             $("<div>").append("&nbsp;&nbsp;<button type='button' onclick='newWindowRepayment (\""+stateValues.id+"\")' class='btn btn-primary btn-xs' style='font-size: 12px;line-height: 15px;'>查询</button>").appendTo(container);
        //         }
			// }
            // ,{
            //     dataField : "baikelu",
            //     caption : "百可录",
            //     alignment : "center",
            //     cellTemplate: function (container, options) {
            //         var stateValues = options.values;
            //         $("<div>").append("&nbsp;&nbsp;<button type='button' onclick='newWindowBaikelu(\""+stateValues[0]+"\")' class='btn btn-primary btn-xs' style='font-size: 12px;line-height: 15px;'>查询</button>").appendTo(container);
            //     }
            // }
        // {
        //     dataField : "downLoadContract",
        //     caption : "电子合同",
        //     alignment : "center",
        //     cellTemplate: function (container, options) {
        //         var stateValues = options.values;
        //         $("<div>").append("&nbsp;&nbsp;<button type='button' onclick='downLoadContract(\""+stateValues[0]+"\")' class='btn btn-primary btn-xs' style='font-size: 12px;line-height: 15px;'>下载</button>").appendTo(container);
        //     }
        // }
        ]
	});
};

var newWindowLoanRemark = function(borrId){
    if(!borrId || typeof(borrId)=="undefined" || borrId=="undefined"){
        alert("该订单号为空");
    }else{
        loanRemark_alert(borrId);
    }
};

var newWindowBaikelu = function(serialNo){
    if(!serialNo || typeof(serialNo)=="undefined" || serialNo=="undefined"){
        alert("该订单号为空");
    }else{
        baikelu_alert(serialNo);
    }
};
var newWindowRepayment = function(borrId){
	if(!borrId || typeof(borrId)=="undefined" || borrId=="undefined"){
        alert("该订单号为空");
	}else{
        repayment_alert(borrId);
	}
};
var newWindowSerialNumImage = function(serial){
	if(!serial || typeof(serial)=="undefined" || serial=="undefined") {
        alert("序列号地址为空");
	}else{
        serialImage_alert(serial);
	}
};

var getRiskReport = function(phone, cardNum) {
    $.ajax({
        type : "GET",
        url : "../report/polyXinli.action",
        success : function(msg) {
            if(msg){
            	if(msg.code == 200){
                    $("#riskReport").attr("src",msg.data + "?phone="+ phone +"&idcard=" + cardNum);
                    $("#riskReport").attr("height",document.documentElement.clientHeight - $("#myTabs").height() - 10);
				}else{
                    alert(msg.msg);
				}
            }
        }
    });
};

var downLoadContract = function(serialNo){
    if(serialNo != null){
        $.ajax({
            type : "GET",
            url : "/loan-manage/contract/down/" + serialNo + ".action",
            success : function(msg) {
            	if(msg.code == 200){
                    window.open(msg.data);
				}else {
            		alert(msg.data)
				}
            }
        });
    }else{
        alert("该订单号为空");
    }
};



// load the table of banck
var getBank = function(perId) {
	$.ajax({
		type : "GET",
		url : "../user/bank/" + perId + ".action",
		success : function(msg) {
			loadtable2_banck(msg.data);
		}
	});
};

// load the table
var loadtable2_banck = function(jsonobj) {
	$("#bank_table").dxDataGrid({
		dataSource : {
			store : {
				type : "array",
				// key : "id",
				data : jsonobj
			}
		},
		rowAlternationEnabled : true,
		showRowLines : true,
		allowColumnReordering : true,
		allowColumnResizing : true,
		columnAutoWidth : true,
		columnFixing : {
			enabled : true
		},
		paging : {
			pageSize : 20,
		},
		pager : {
			showPageSizeSelector : true,
			allowedPageSizes : [10, 15, 30, 45, 60 ],
			showInfo : true,
			infoText : '第{0}页 . 共{1}页'
		},
		onSelectionChanged : function(data) {
			$("#tool_edit").dxButton({
				disabled : data.selectedRowsData.length != 1,
			});
			$("#tool_del").dxButton({
				disabled : !data.selectedRowsData.length,
			});
			$("#tool_info").dxButton({
				disabled : data.selectedRowsData.length != 1,
			});
			// deleteButton.option("disabled", !data.selectedRowsData.length)
		},
		columns : [ {
			dataField : "bankName",
			caption : "银行名称",
			alignment : "center",

		}, {
			dataField : "bankNum",
			caption : "银行卡号",
			alignment : "center",
		}, {
			dataField : "bankPhone",
			caption : "预留手机号",
			alignment : "center",
		}, {
			dataField : "createDate",
			caption : "添加时间",
			alignment : "center",
			dataType : "date",
		}, {
			dataField : "status",
			caption : "状态",
			alignment : "center",
		} ]
	});
};
//load the table of banck
var getBlack = function(perId) {
	$.ajax({
		type : "GET",
		url :  "../user/black/" + perId + ".action",
		success : function(msg) {
			if(msg.code == 200){
                loadtable2_black(msg.data);
			}
		}
	});
};

// load the table
var loadtable2_black = function(jsonobj) {
	$("#black_table").dxDataGrid({
		dataSource : {
			store : {
				type : "array",
				// key : "id",
				data : jsonobj
			}
		},
		rowAlternationEnabled : true,
		showRowLines : true,
		allowColumnReordering : true,
		allowColumnResizing : true,
		columnAutoWidth : true,
		columnFixing : {
			enabled : true
		},
		paging : {
			pageSize : 20,
		},
		pager : {
			showPageSizeSelector : true,
			allowedPageSizes : [10, 15, 30, 45, 60 ],
			showInfo : true,
			infoText : '第{0}页 . 共{1}页'
		},
		onSelectionChanged : function(data) {
			$("#tool_edit").dxButton({
				disabled : data.selectedRowsData.length != 1,
			});
			$("#tool_del").dxButton({
				disabled : !data.selectedRowsData.length,
			});
			$("#tool_info").dxButton({
				disabled : data.selectedRowsData.length != 1,
			});
			// deleteButton.option("disabled", !data.selectedRowsData.length)
		},
		columns : [ {
			dataField : "type",
			caption : "操作",
			alignment : "center",
			calculateCellValue: function (data) {
                if(data.type == 0){
                    return "拉黑";
                }else if(data.type == 1){
                    return "洗白";
                }
            }
		}, {
			dataField : "reason",
			caption : "原因",
			alignment : "center",
			width:800
		},{
            dataField : "sys",
            caption : "来源",
            alignment : "center",
            lookup: {
                dataSource: riskSource,
                valueExpr: 'value',
                displayExpr: 'format'
            }
        },{
			dataField : "handlerName",
			caption : "操作人",
			alignment : "center",
		}, {
			dataField : "createTime",
			caption : "操作时间",
			alignment : "center",
			dataType : "date",
		}]
	});
};

var getOrders = function(perId) {
	$.ajax({
		type : "GET",
		url :  "../user/order/" + perId + ".action",
		success : function(msg) {
			loadtable2_loan_kf(msg.data);
		}
	});
};

var loadtable2_loan_kf = function(jsonobj) {
	
	$("#order_table").dxDataGrid({
		dataSource : {
			store : {
				type : "array",
				data : jsonobj
			}
		},
		rowAlternationEnabled : true,
		showRowLines : true,
		allowColumnReordering : true,
		allowColumnResizing : true,
		columnAutoWidth : true,
		columnFixing : {
			enabled : true
		},
		paging : {
			pageSize : 20,
		},
		pager : {
			showPageSizeSelector : true,
			allowedPageSizes : [10, 15, 30, 45, 60 ],
			showInfo : true,
			infoText : '第{0}页 . 共{1}页'
		},
		onSelectionChanged : function(data) {
			$("#tool_edit").dxButton({
				disabled : data.selectedRowsData.length != 1,
			});
			$("#tool_del").dxButton({
				disabled : !data.selectedRowsData.length,
			});
			$("#tool_info").dxButton({
				disabled : data.selectedRowsData.length != 1,
			});
			// deleteButton.option("disabled", !data.selectedRowsData.length)
		},
		columns : [ {
            dataField : "serialNo",
            caption : "订单编号",
            alignment : "center",
		},{
            dataField : "sid",
            caption : "支付流水号",
            alignment : "center",
            calculateCellValue: function (data) {
                if (data.type >= 15 && data.type <= 18) {
                    return data.sid;
                } else {
                    return data.serialNo
                }
            }
        }, {
			dataField : "typeWithChannel",
			caption : "类型",
			alignment : "center",
            calculateCellValue: function (data) {
                if(data.type>14 && data.type<=18){
                    return orderTypes[data.typeWithChannel];
                }else{
                    return orderTypes[data.type];
                }
            }

		},{
            dataField : "borrNum",
            caption : "合同编号",
            alignment : "center",
        }, {
			dataField : "optAmount",
			caption : "金额",
			alignment : "center"
		}, {
			dataField : "rlState",
			caption : "状态",
			alignment : "center",
			 calculateCellValue: function (data) {
				 if(data.rlState){
					 //交易状态：p处理中，s成功，f失败,q清结算处理中,c清结算失败
					 if(data.rlState=='p'){
						 return "处理中";
					 }else if (data.rlState=='s'){
						 return "成功"
					 }else if (data.rlState=='f'){
						 return "失败"
					 }else if (data.rlState=='q'){
						 return "清结算处理中"
					 }else if (data.rlState=='c'){
						 return "清结算失败"
					 }else if(data.rlState=='m'){
					 	return "匹配中";
					 }else if(data.rlState=='ms'){
					 	return "匹配成功";
					 }else{
						 return "";
					 }
				 }
	            },
		}, {
			dataField : "reason",
			caption : "原因",
			alignment : "center",
		} , {
			dataField : "creationDate",
			caption : "创建时间",
			alignment : "center",
            dataType :'date',
            format :function(date){
                return tableUtils.formatDate(date);
			}
			/*calculateCellValue: function (data) {
				var d = new Date(parseInt(data.updateDate)).toLocaleString().replace(/:\d{1,2}$/,' '); 
				return d;
	            }*/
		} , {
            dataField : "updateDate",
            caption : "更新时间",
            alignment : "center",
            dataType :'date',
            format :function(date){
                return tableUtils.formatDate(date);
            }
		}, {
            dataField : "overdueDays",
            caption : "逾期天数",
            alignment : "center",
            filterOperations:["="]
        }

		]
	});
};


var getNode = function(perId) {
    $.ajax({
        url: "../user/node/" + perId + ".action",
        type : "GET",
        success: function(msg) {
            if(msg.code == 200){
                loadNodeTable(msg.data);
            }
        }
    });
};


function loadNodeTable(node){
    $("#node_table").dxDataGrid({
        dataSource : {
            type : "array",
            store: node
        },
        dateSerializationFormat:"yyyy-MM-dd HH:mm:ss",
        remoteOperations: {
            sorting: true,
            paging: true,
            filtering:true
        },
        rowAlternationEnabled : true,
        showRowLines : true,
        allowColumnReordering : true,
        allowColumnResizing : true,
        columnAutoWidth : true,
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
        columns : [ {
            dataField : "nodeName",
            caption : "节点",
            alignment : "center",
            allowFiltering:false,
            allowSorting:false
        },{
            dataField : "nodeStatusName",
            caption : "状态",
            alignment : "center",
            allowFiltering:false,
            allowSorting:false
        },{
            dataField : "description",
            caption : "说明",
            alignment : "center",
			allowFiltering:false,
            allowSorting:false
        },{
            dataField : "nodeDate",
            caption : "认证日期",
            alignment : "center",
            dataType: "date",
            allowFiltering:false,
            allowSorting:false
        }]
    });
}
var getNodeDetail = function(perId) {
    $.ajax({
        url: "../user/node/detail/" + perId + ".action",
        type : "GET",
        success: function(msg) {
            if(msg.code == 200){
            	if(msg.data.notExpires.length > 0){
                    $("#nodeDetial_source").html("当前提额:" + msg.data.notExpires[0].score);
				}
                loadNodeDetailNotExpiresTable(msg.data.notExpires);
                loadNodeDetailIsExpiresTable(msg.data.isExpires);
            }
        }
    });
};


function loadNodeDetailNotExpiresTable(node){
    $("#nodeDetial_table_notExpires").dxDataGrid({
        dataSource : {
            type : "array",
            store: node
        },
        dateSerializationFormat:"yyyy-MM-dd HH:mm:ss",
        remoteOperations: {
            sorting: true,
            paging: true,
            filtering:true
        },
        rowAlternationEnabled : true,
        showRowLines : true,
        selection : {
            mode : "multiple"
        },
        allowColumnReordering : true,
        allowColumnResizing : true,
        columnAutoWidth : true,
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
        columns : [ {
            dataField : "orgCode",
            caption : "认证机构",
            alignment : "center",
            allowFiltering:false,
            allowSorting:false,
            calculateCellValue: function (data) {
                if (data.orgCode == 'jxl') {
                    return "聚信立";
                } else if(data.orgCode == 'CC'){
                    return "信用卡";
                }else if(data.orgCode ==  'JD'){
                    return "京东";
                }else if(data.orgCode ==  'taobao'){
                    return "淘宝";
                }else if(data.orgCode ==  'chsi'){
                    return "学信网";
                }else if(data.orgCode ==  'zhima'){
                    return "芝麻";
                }else if(data.orgCode ==  'gjj'){
                    return "公积金";
                }
            },
        },{
            dataField : "addValue",
            caption : "提升额度",
            alignment : "center",
            allowFiltering:false,
            allowSorting:false
        },{
            dataField : "remark",
            caption : "规则",
            alignment : "center",
            allowFiltering:false,
            allowSorting:false
        },{
            dataField : "nodeDate",
            caption : "认证日期",
            alignment : "center",
            dataType: "date",
            allowFiltering:false,
            allowSorting:false
        },{
            dataField : "expireTime",
            caption : "过期日期",
            alignment : "center",
            dataType: "date",
            allowFiltering:false,
            allowSorting:false
        }]
    });
}
function loadNodeDetailIsExpiresTable(node){
    $("#nodeDetial_table_isExpires").dxDataGrid({
        dataSource : {
            type : "array",
            store: node
        },
        dateSerializationFormat:"yyyy-MM-dd HH:mm:ss",
        remoteOperations: {
            sorting: true,
            paging: true,
            filtering:true
        },
        rowAlternationEnabled : true,
        showRowLines : true,
        selection : {
            mode : "multiple"
        },
        allowColumnReordering : true,
        allowColumnResizing : true,
        columnAutoWidth : true,
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
        columns : [ {
            dataField : "orgCode",
            caption : "认证机构",
            alignment : "center",
            allowFiltering:false,
            allowSorting:false,
            calculateCellValue: function (data) {
                if (data.orgCode == 'jxl') {
                    return "聚信立";
                } else if(data.orgCode == 'CC'){
                    return "信用卡";
                }else if(data.orgCode ==  'JD'){
                    return "京东";
                }else if(data.orgCode ==  'taobao'){
                    return "淘宝";
                }else if(data.orgCode ==  'chsi'){
                    return "学信网";
                }else if(data.orgCode ==  'zhima'){
                    return "芝麻";
                }else if(data.orgCode ==  'gjj'){
                    return "公积金";
                }
            },
        },{
            dataField : "addValue",
            caption : "提升额度",
            alignment : "center",
            allowFiltering:false,
            allowSorting:false
        },{
            dataField : "remark",
            caption : "规则",
            alignment : "center",
            allowFiltering:false,
            allowSorting:false
        },{
            dataField : "nodeDate",
            caption : "认证日期",
            alignment : "center",
            dataType: "date",
            allowFiltering:false,
            allowSorting:false
        },{
            dataField : "expireTime",
            caption : "过期日期",
            alignment : "center",
            dataType: "date",
            allowFiltering:false,
            allowSorting:false
        }]
    });
}
var getPhones = function(perId) {
	var args = {};
	args.skip = 0;
	args.take = 5000;
	args.perId = perId;
    args.phones = "";
	$.ajax({
		url: "../contact.action",
		data: args,
        type : "GET",
		success: function(msg) {
            if(msg.code == 200){
                loadPhoneTable(msg.data);
            }
		}
	});
};

function loadPhoneTable(contacts){
    $("#phone_table").dxDataGrid({
        dataSource : {
            type : "array",
            store: contacts
        },
        export: {enabled: true, allowExportSelectedData: true, excelFilterEnabled: true, fileName: "phoneList"},
        dateSerializationFormat:"yyyy-MM-dd HH:mm:ss",
        remoteOperations: {
            sorting: true,
            paging: true,
            filtering:true
        },
        rowAlternationEnabled : true,
        showRowLines : true,
        selection : {
            mode : "multiple"
        },
        allowColumnReordering : true,
        allowColumnResizing : true,
        columnAutoWidth : true,
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
        columns : [ {
            dataField : "phone",
            caption : "手机号码",
            alignment : "center",
        },{
            dataField : "name",
            caption : "通讯录姓名",
            alignment : "center",
        }]
    });
}

