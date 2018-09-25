$(function() {
    $('.modal-backdrop').hide();
    loadRepayTypes();
	getpath();
	getTabs();
	loadUserId();
    loadSource();
	
	$("#serchOrders").click(function(){
		getOrders(brroid_root);
	});
    $("#polyXinliCredit").click(function(){
        loadtable_credit(card_num, name, himid_root);
    });

	$("#modileContact").click(function(){
		getModileContact(himid_root);
	});
	$("#modileContactExport").click(function () {
        getModileContactExport(himid_root);
    });

    $("#polyXinliCreditExport").click(function () {
        getPolyXinliCreditExport(phone, name, himid_root);
    })

});

var riskSource = [];
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

var loadUserId = function () {
    loginId = $.cookie("userid");
};

var himid_root  = 0;
var brroid_root = 0;
var loginId = "";
var borrNum_root = "";
var getpath = function() {
	try {
		var url = location.href;
		var data_url = url.split("?")[1];
		$.ajax({
			type : "post",
			url : "/loan-manage/url/url_open.action",
			data : {
				var_text_locked : data_url
			},
			success : function(msg) {
				data_url = msg;
				var info = data_url.split("=")[1];
				var himid = info.split("_")[0];
				brroid_root = info.split("_")[1];
				borrNum_root = info.split("_")[2];
				himid_root = himid;
				loadbox_data(himid);
			}
		});
	} catch (e) {
		// window.location = "/jhhoa/login.html";
	}

};

var getTabs = function() {

    $('#myTab a').click(function (e) {
	  	e.preventDefault();
	  	$(this).tab('show');
	  	
	  	if($(this).attr('id') == "tab_loan"){
	  		loadtable_loan(brroid_root);
	  	}
	  	else if($(this).attr('id') == "tab_repayPlan"){
            loadtable_repayPlan(brroid_root);
        }else if($(this).attr('id') == "tab_bank"){
	  		loadtable_bank(himid_root);
	  	}else if($(this).attr('id') == "tab_memo"){
	  		loadtable_memo(brroid_root);
	  		initToolbar_memo();
	  	}else if($(this).attr('id') == "tab_credit") {
	  		loadtable_credit(card_num, name, himid_root);
	  	}else if($(this).attr('id') == "tab_black"){
            loadtable_black(himid_root)
		}
	});
    
	$('.newAdd_btn').click(function(){
		$('#myModal_add').modal('show');
	}); 
};

var name = "";
var card_num = "";
var qq_num = "";
var email = "";
var education = "";
var marry = "";
var getchild = "";

var usuallyaddress = "";
var source_value = "";
var phone = "";
var address = "";
var birthday = "";
var blacklist = "";
var create_date = "";

var profession = "";
var monthlypay = "";
var business = "";
var busi_city = "";
var busi_address = "";
var busi_phone = "";
var busi_province = "";

var relatives = "";
var relatives_name = "";
var rela_phone = "";
var society = "";
var society_name = "";
var soci_phone = "";

var perCouponId = "";
var prodId = "";
var phones = "";

var repayTypeSource = [];
var repayTypes = [];

var loadRepayTypes = function () {
    $.ajax({
        url: "/loan-manage/loanManagement/queryRepayType.action",
        data: {skip: 0, take: 10000},
        type: 'POST',
        dataType: "json",
        success: function (result) {
            if (result.code == 1) {
                repayTypes = result.object;
                for(var key in repayTypes){
                    var obj = {'value': key, 'format': repayTypes[key]};
                    repayTypeSource.push(obj);
                }
            }
        },
        error: function () {
            console.info("数据加载错误");
        },
        timeout: 50000
    });
};

var loadbox_data = function(himid) {
	$.ajax({
		type : "GET",
		url :  "/loan-manage/userInfo.action",
		data : {
			'perId' : himid
		},
		success : function(msg) {
			var himinfo = msg;
			name = himinfo.name;
			card_num = himinfo.cardNum;
			qq_num = himinfo.qqNum;
			email = himinfo.email;
			education = himinfo.education;
			marry = himinfo.marry;
			getchild = himinfo.getchild;

			usuallyaddress = himinfo.usuallyaddress;
			source_value = himinfo.sourceValue;
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
			create_date = himinfo.createDate;
			
			
			profession = himinfo.profession;
			monthlypay = himinfo.monthlypay;
			business = himinfo.business;
			busi_city = himinfo.busiCity;
			busi_address = himinfo.busiAddress;
			busi_phone = himinfo.busiPhone;
			busi_province = himinfo.busiProvince;

			relatives = himinfo.relativesValue;
			relatives_name = himinfo.relativesName;
			rela_phone = himinfo.relaPhone;
			society = himinfo.societyValue;
			society_name = himinfo.societyName;
			soci_phone = himinfo.sociPhone;

			loadbox_box();

			imageZ = himinfo.imageZ;
            imageF = himinfo.imageF;
            if (imageZ != null && imageZ != "") {
                $("#imageZ").html(
                    "<img width='500' height='300' src='" + imageZ + "'/>");
            }
            if (imageF != null && imageF != "") {
                $("#imageF").html(
                    "<img width='500' height='300' src='" + imageF + "'/>");
            }

			//征信报告按钮加载
			creditInvestigation(phone, card_num);
		}
	});

};

var loadbox_box = function() {
	//基本信息
	$("#name").val(name);
	$("#cardNum").val(card_num);
	$("#phone").val(phone);
	
	//个人信息
	$("#infoName").val(name);
	$("#infoCardNum").val(card_num);
	$("#infoQQNum").val(qq_num);
	$("#infoEmail").val(email);
	$("#infoEducation").val(education);
	$("#infoMarry").val(marry);
	$("#infoGetchild").val(getchild);
	
	//职业信息
	$("#workProfession").val(profession);
	$("#workMonthlypay").val(monthlypay);
	$("#workBusiness").val(business);
	$("#workBusiCity").val(busi_city);
	$("#workBusiAddress").val(busi_address);
	$("#workBusiPhone").val(busi_phone);
	$("#workBusiProvince").val(busi_province);
	
	//社会关系
	$("#slRelatives").val(relatives);
	$("#slRelativesName").val(relatives_name);
	$("#slRelativesPhone").val(rela_phone);
	$("#slSociety").val(society);
	$("#slSocietyPhone").val(soci_phone);
	$("#slSocietyName").val(society_name);
};

// load the table of loan
var loadtable_loan = function(borrId) {
	$.ajax({
		type : "GET",
		url : "/loan-manage/loanManagement/loan/detail.action",
		data : {
			"contractKey" : borrId,
			"from" : "detail"
		},
		success : function(msg) {
            result = JSON.parse(msg);
			var jsonobj = result.list[0];
 			$("#productType").val(jsonobj.productName);
            $("#borrAmount").val(jsonobj.amount);
            $("#termCount").val(jsonobj.termCount);
            $("#loanAmount").val(jsonobj.loanAmount);

			var borrStatus = jsonobj.stateString;
			if(borrStatus == "BS004"){
                borrStatus = "待还款";
            }else if(borrStatus == "BS005"){
                borrStatus = "逾期未还";
            }else if(borrStatus == "BS006"){
                borrStatus = "正常结清";
            }else if(borrStatus == "BS010"){
                borrStatus = "逾期结清";
            }
			$("#borrStatus").val(borrStatus);
            $("#payAmount").val(jsonobj.shouldPayAmount);
			$("#repayDate").val(jsonobj.endDateString?jsonobj.endDateString.substr(0,10):"");

			var isManual=jsonobj.isManual;
            if(borrStatus == "4") {
                isManual = "是";
            }else{
                isManual = "否";
			}

			$("#isManage").val(isManual);
			$("#description").val(jsonobj.description);
            // $("#bedueDays").val(jsonobj.bedueDays);
            // $("#penalty").val(jsonobj.penalty);
            // $("#totalAmount").val(jsonobj.surplusTotalAmount);
            // $("#rental").val(jsonobj.rental);
            // $("#mstRepayAmount").val(jsonobj.mstRepayAmount);
            // $("#payTotalAmount").val(jsonobj.repayAmount);
            // $("#serviceAmount").val(jsonobj.serviceAmount);

			getOrders(brroid_root);
		}
	});
};

var getOrders = function(borrId){
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
            args.borrId = borrId;
            $.ajax({
                url: "/loan-manage/loan/order.action",
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
	
	
	$("#orderTable").dxDataGrid({
				dataSource : {
					 store: orders
				},
				dateSerializationFormat:"yyyy-MM-dd HH:mm:ss",
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
					dataField : "borrNum",
					caption : "合同编号",
					alignment : "center",
                    allowFiltering:false
				},{
                    dataField : "typeWithChannel",
                    caption : "类型",
                    alignment : "center",
                    lookup: { dataSource: repayTypeSource,  displayExpr: 'format' } ,
                    calculateCellValue: function (data) {
                        if(data.type>14 && data.type<=18){
                            return repayTypes[data.typeWithChannel];
                        }else{
                            return repayTypes[data.type];
                        }
                    }
                }, {
                    dataField : "rlState",
                    caption : "状态",
                    alignment : "center",
                    lookup: { dataSource: [
                        { value: 'p', format: '处理中' },
                        { value: 's', format: '成功' },
                        { value: 'f', format: '失败' },
                    ],  displayExpr: 'format' } ,
                    cellTemplate: function (container, options) {
                        var stateValue = options.value;
                        if(stateValue){
                            var stateValues = stateValue.split(",");
                            $("<div>").append(rlStateFormat(stateValues[0])).appendTo(container);
                        }
                    }
                },{
					dataField : "actAmount",
					caption : "金额",
					alignment : "center",
                    allowFiltering:false,
				}, {
					dataField : "creationDate",
					caption : "创建时间",
					alignment : "center",
					dataType: "date" ,
                    allowFiltering:false,
                    format: function (date) {
                        return tableUtils.formatDate(date);
                    }
				}, {
                    dataField : "updateDate",
                    caption : "更新时间",
                    alignment : "center",
                    dataType: "date" ,
                    allowFiltering:false,
                    format: function (date) {
                        return tableUtils.formatDate(date);
                    }
                }, {
					dataField : "reason",
					caption : "备注信息",
					alignment : "center",
					allowSorting:false,
                    allowFiltering:false,
				}, {
					dataField : "serialNo",
					caption : "流水号",
					alignment : "center",
				}, {
                    dataField : "overdueDays",
                    caption : "逾期天数",
                    alignment : "center",
                    filterOperations:["="]
				}]
			});
};

var rlStateFormat = function (value) {
    if(value != null){
        if (value=="p"){
            return "处理中";
        }else if(value=="s"){
            return "成功";
        }else if(value=="f"){
            return "失败";
        }else{
            return "";
        }
    }
};

var loadtable_bank = function(perId){
	DevExpress.config({
	    forceIsoDateParsing: true,
	});
	
	var banks = new DevExpress.data.CustomStore({
        load: function (loadOptions) {
            	var deferred = $.Deferred();
                $.ajax({
                url: "../user/bank/" + perId + ".action",
                success: function(result) {
                    deferred.resolve(result.data, { totalCount: 1 });
                },
                error: function() {
                    deferred.reject("Data Loading Error");
                },
                timeout: 50000
            });
    
            return deferred.promise();
        }
    });
	
	
	$("#bankTable").dxDataGrid({
				dataSource : {
					 store: banks
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
                     dataField : "quickBinding",
                     caption : "快捷绑卡",
                     alignment : "center",
				}, {
					dataField : "status",
					caption : "状态",
					alignment : "center",
				} ]
			});
};

var loadtable_repayPlan = function(borrId){
    DevExpress.config({
        forceIsoDateParsing: true,
    });

    var repayPlan = new DevExpress.data.CustomStore({
        load: function (loadOptions) {
            var deferred = $.Deferred(), args = {};
            args.borrId = borrId;
            $.ajax({
                url: "/loan-manage/repayTermPlan.action",
                data: args,
                success: function(result) {
                    var obj = result.object;
                    var list = obj.list;
                    deferred.resolve(list, { totalCount: 1 });
                },
                error: function() {
                    deferred.reject("Data Loading Error");
                },
                timeout: 50000
            });

            return deferred.promise();
        }
    });


    $("#repayPlanTable").dxDataGrid({
        dataSource : {
            store: repayPlan
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
        columns : [ {
            dataField : "term",
            caption : "	期数",
            alignment : "center",
        }, {
            dataField : "repayDate",
            caption : "到期日",
            alignment : "center",
            dataType: "date" ,
        }, {
            dataField : "totalAmount",
            caption : "应付总额",
            alignment : "center",
        },{
            dataField : "rentalAmount",
            caption : "应付租金",
            alignment : "center",
        }, {
            dataField : "penalty",
            caption : "应付违约金",
            alignment : "center",
        }, {
            dataField : "ransomAmount",
            caption : "应付回购手机费",
            alignment : "center",
        }, {
            dataField : "depositAmount",
            caption : "押金",
            alignment : "center",
        }, {
            dataField : "surplusTotalAmount",
            caption : "剩余应付",
            alignment : "center",
        }, {
            dataField : "surplusRentalAmount",
            caption : "剩余应付租金",
            alignment : "center",
        }, {
            dataField : "surplusPenalty",
            caption : "剩余应付违约金",
            alignment : "center",
        }, {
            dataField : "surplusRansomAmount",
            caption : "剩余应付回购手机费",
            alignment : "center",
        }, {
            dataField : "overdueDays",
            caption : "逾期天数",
            alignment : "center",
        }, {
            dataField : "collectionUser",
            caption : "催款人",
            alignment : "center",
        }, {
            dataField : "status",
            caption : "状态",
            alignment : "center",
            calculateCellValue: function (data) {
            	if (data.status === '0'){
            		return "待还款"
				}else if (data.status === '1'){
					return "逾期未还";
				}else if(data.status === '2'){
					return "正常结清";
				}else if(data.status === '3'){
					return "逾期结清";
				}else{
					return "";
				}
            }
        } ]
    });
};

var initToolbar_memo = function() {
		$("#memo_tool_add").dxButton({
			hint : "新增备注",
			text : "新增",
			icon : "add",
			onClick : function() {
				$("#contractId").val(borrNum_root);
				$("#contractKey").val(brroid_root);
				$("#createUser").val(loginId);
				$("#collectionMemo").modal({show:true,backdrop: 'static', keyboard: false});
			}
		});
	
		$("#btnRemark_layer").off("click").click(function () {
			var remark = $("#remark").val();
				if(remark == null || remark == "" || remark.length < 1){
					alert("请输入备注内容");
					return;
				}
			var formData = $("#remarkForm_layer").serialize();
			$("#btnRemark_layer").attr("disabled",true);
			$.ajax({
				url: "/loan-manage/loanManagement/collectionRemark.action",
				data: formData,
				success: function(result) {
					if(result.code == 1){
						alert("操作成功");
						$("#remark").val("");
						$("#collectionMemo").modal("hide");
						tableUtils.refresh("memoTable");
						$("#btnRemark_layer").removeAttr("disabled");
					}else{

					}
				},
				error: function(data) {
					console.info(data);

				},
				timeout: 50000
			});
		});
};

var loadtable_memo = function(borrId){
	DevExpress.config({
	    forceIsoDateParsing: true,
	});
	
	var memos = new DevExpress.data.CustomStore({
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
            args.borrId = borrId;
            $.ajax({
                url: "/loan-manage/memo.action",
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
	
	
	$("#memoTable").dxDataGrid({
				dataSource : {
					 store: memos
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
				columnAutoWidth : false,
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
				columns : [{
					dataField : "",
					caption : "",
					alignment : "",
					width: "100",
				}, {
					dataField : "type",
					caption : "	标题",
					alignment : "center",
                    allowSorting:false,
                    calculateCellValue: function (data) {
                        if(data.type){
                            if (data.type=='A'){
                                return "催收备注"
                            }else if (data.type=='B'){
                                return "提醒备注"
                            }else{
                                return data.type;
                            }
                        }
                    }
				}, {
					dataField : "remark",
					caption : "	内容",
					alignment : "center",
                    allowSorting:false,
				}, {
					dataField : "createUser",
					caption : "	催收人",
					alignment : "center",
                    allowSorting:false,
				}, {
					dataField : "createDate",
					caption : "	时间",
					alignment : "center",
                    dataType: 'date',
                    format:formatDate
				}]
			});
};


var loadtable_credit = function(idCard, name, perId){
	DevExpress.config({
	    forceIsoDateParsing: true,
	});
	
	var credits = new DevExpress.data.CustomStore({
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
            args.perId = perId;
            $.ajax({
                url: "/loan-manage/polyXinli/credit.action",
                data: args,
                success: function(result) {
                	result = JSON.parse(result);
                    if(result.list != null){
                        deferred.resolve(result.list[0], { totalCount: result.total });
                        phones = "";
                        for(var i = 0; i < result.list[0].length; i++){
                            phones += result.list[0][i].phone_num + ",";
                        }
                    }else{
                        deferred.resolve(result.list, { totalCount: result.total });
                    }

                },
                error: function() {
                    deferred.reject("Data Loading Error");
                },
                timeout: 50000
            });
    
            return deferred.promise();
        }
    });
	
	
	$("#creditTable").dxDataGrid({
				dataSource : {
					 store: credits
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
					dataField : "phone_num",
					caption : "手机号码",
					alignment : "center",
				},{
					dataField : "contact_name",
					caption : "互联网标识",
					alignment : "center",
				}, {
					dataField : "phone_num_loc",
					caption : "归属地",
					alignment : "center",
				}, {
					dataField : "call_cnt",
					caption : "联系次数",
					alignment : "center",
				}, {
					dataField : "call_len",
					caption : "联系时间",
					alignment : "center",
					calculateCellValue: function (data) {
						if(data.call_len){
                            if(data.call_len.indexOf('\.') > -1){
                                return data.call_len.substring(0,data.call_len.indexOf('\.')+3);
							}else {
                            	return data.call_len;
							}
						}else {
							return "";
						}
                    }
				}, {
					dataField : "call_out_cnt",
					caption : "主叫次数",
					alignment : "center",
				}, {
					dataField : "call_in_cnt",
					caption : "被叫次数",
					alignment : "center",
				}, {
					dataField : "yMName",
					caption : "通讯录姓名",
					alignment : "center",
				}]
			});
};

function creditInvestigation(phone, idcard){
	$("#creditInvestigation").click(function(){
        var args = {};
        args.idcard = idcard;
        args.phone = phone;
        $.ajax({
            url: "/loan-manage/polyXinli/creditInvestigation.action",
            data: args,
            success: function(result) {
                window.open(result);
            },
            timeout: 50000
        });
	});
}

var getModileContact = function(perId){
	DevExpress.config({
	    forceIsoDateParsing: true,
	});

	var contacts = new DevExpress.data.CustomStore({
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
            args.perId = perId;
            $.ajax({
                url: "/loan-manage/polyXinli/contact.action",
                data: args,
                success: function(result) {
                	result = JSON.parse(result);
                	if(result.list != null){
                        deferred.resolve(result.list[0], { totalCount: result.total });
					}else{
                        deferred.resolve(result.list, { totalCount: result.total });
					}


                },
                error: function() {
                    deferred.reject("Data Loading Error");
                },
                timeout: 50000
            });

            return deferred.promise();
        }
    });


	$("#modileContactTable").dxDataGrid({
				dataSource : {
					 store: contacts
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
					dataField : "phone",
					caption : "手机号码",
					alignment : "center",
				},{
					dataField : "name",
					caption : "	通讯录姓名",
					alignment : "center",
				}]
			});
};

function getModileContactExport(perId) {
		var url = "/loan-manage/export/phonebook.action?perId="+perId;
	    exportData(url,null);
}

function getPolyXinliCreditExport(phone, name, perId) {
    var url = "/loan-manage/export/polyXinliCredit.action?perId="+perId+"&phone="+phone+"&name="+name;
    exportData(url,null);
}

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

//load the table of banck
var loadtable_black = function(perId) {
    $.ajax({
        type : "GET",
        url :  "../user/black/" + perId + ".action",
        success : function(msg) {
            loadtable2_black(msg.data);
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
			width : 800
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

Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};


function getContract(){
    if(borrNum_root != null&&borrNum_root!=undefined&&borrNum_root!="undefined"){
        $.ajax({
            type : "GET",
            url : "/loan-manage/contract/down/" + borrNum_root + ".action",
            success : function(msg) {
                if(msg.code == 200){
                    window.open(msg.data);
                }else {
            		if(msg.info!=undefined){
                        alert(msg.info)
					}else{
                        alert("查看电子合同异常！")
					}
                }
            }
        });
    }else{
        alert("该订单号为空");
    }
}
var formatDate = function (date) {
    var month = date.getMonth() + 1,
        day = date.getDate(),
        year = date.getFullYear(),
        hours = date.getHours(),
        minutes = date.getMinutes(),
        seconds = date.getSeconds();
    if(month < 10){
        month = "0" + month;
    }
    if(day < 10){
        day = "0" + day;
    }
    if(hours < 10){
        hours = "0" + hours;
    }
    if(minutes < 10){
        minutes = "0" + minutes;
    }
    if(seconds < 10){
        seconds = "0" + seconds;
    }

    return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
};