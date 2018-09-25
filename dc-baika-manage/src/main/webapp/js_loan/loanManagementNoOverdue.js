var setToolBar_lmg = function () {
    $(".modal").on('hide.bs.modal', function () {
        tableUtils.clearSelection("loanManagementTable");
    });
    $(".modal").on('hidden.bs.modal', function () {
        tableUtils.clearSelection("loanManagementTable");
    });
}

var initLoanManagementNoOverdueTable = function () {

    tableUtils.initMuliTableToolBar(
        "loanManagementNoOverdueTable",
        'loanManagement/loan.action?userNo=' + usernum,
        null,
        [
            {
                dataField: "blackList", caption: "黑名单", alignment: "center", allowFiltering: false, allowSorting: false,
                calculateCellValue: function (data) {
                    if (data.blackList) {
                        if (data.blackList == "Y") {
                            return "是";
                        } else if (data.blackList == "N") {
                            return "否";
                        }
                    }
                },
                lookup: {
                    dataSource: [
                        {value: 'Y', format: '是'},
                        {value: 'N', format: '否'},
                    ], displayExpr: 'format'
                }, width: 80, fixed: true
            },
            {
                dataField: "bedueDays",
                fixed: true,
                caption: "逾期天数",
                alignment: "center",
                allowFiltering: true,
                filterOperations: ["=", ">", "between"],
                allowSorting: true
            },
            {dataField: "customerId", fixed: true, caption: "用户ID", alignment: "center", visible: false},
            {
                dataField: "customerName",
                fixed: true,
                caption: "姓名",
                alignment: "center",
                width: 110,
                allowFiltering: true,
                filterOperations: ["="],
                allowSorting: false
            },
            {
                dataField: "customerIdValue",
                fixed: true,
                caption: "身份证",
                alignment: "center",
                width: 190,
                allowFiltering: true,
                filterOperations: ["="],
                allowSorting: false
            },
            {
                dataField: "customerMobile",
                fixed: true,
                caption: "手机号码",
                alignment: "center",
                width: 140,
                allowFiltering: true,
                filterOperations: ["="],
                allowSorting: false
            },
            {
                dataField: "repayRetryFlag",
                caption: "还款失败补偿",
                alignment: "center",
                allowFiltering: true,
                allowSorting: false,
                calculateCellValue: function (data) {
                    if (data.repayRetryFlag) {
                        if (data.repayRetryFlag == "1") {
                            return "待补偿";
                        } else if (data.repayRetryFlag == "2") {
                            return "补偿中";
                        }else if (data.repayRetryFlag == "3") {
                            return "已补偿";
                        }else {
                            return "未知";
                        }
                    }
                },
                lookup: {
                    dataSource: [
                        {value: '1', format: '待补偿'},
                        {value: '2', format: '补偿中'},
                        {value: '3', format: '已补偿'}
                    ], displayExpr: 'format'
                },
                width: 100
            },
            {
                dataField: "productId",
                caption: "产品类型",
                alignment: "center",
                allowFiltering: true,
                allowSorting: false,
                width: 150,
                lookup: {
                    dataSource: pruducts, displayExpr: 'format', valueExpr: 'value'
                }
            },
            {dataField: "amount", caption: "贷款金额", alignment: "center", allowFiltering: false, allowSorting: false},
            {dataField: "interestSum", caption: "应还利息", alignment: "center", allowFiltering: false, allowSorting: false},
            {dataField: "totalAmount", caption: "应还合计", alignment: "center", allowFiltering: false, allowSorting: false},
            {
                dataField: "surplusTotalAmount",
                caption: "剩余还款",
                alignment: "center",
                allowFiltering: false,
                filterOperations: ["="],
                allowSorting: true
            },
            {
                dataField: "endDateString",
                caption: "到期日",
                alignment: "center",
                allowFiltering: true,
                filterOperations: ["=", "between"],
                allowSorting: true,
                dataType: "date",
                calculateCellValue: function (data) {
                    if (data.endDateString) {
                        return data.endDateString.toString().substring(0, 10);
                    } else {
                        return "";
                    }
                },
                width: 120
            },
            {
                dataField: "settleDateString",
                caption: "结清日",
                alignment: "center",
                allowFiltering: true,
                filterOperations: ["=", "between"],
                dataType: "date",
                allowSorting: true,
                calculateCellValue: function (data) {
                    if (data.settleDateString) {
                        return data.settleDateString.toString().substring(0, 10);
                    } else {
                        return "";
                    }
                },
                width: 120
            },
            {
                dataField: "borrStatus",
                caption: "合同状态",
                alignment: "center",
                allowFiltering: true,
                allowSorting: false,
                lookup: {
                    dataSource: [
                        {value: 'BS004', format: '待还款'}
                    ], displayExpr: 'format', valueExpr: 'value'
                },
                width: 100
            },
            {
                dataField: "auditer",
                caption: "催收人",
                alignment: "center",
                allowFiltering: true,
                allowSorting: false,
                width: 100,
                lookup: {
                    dataSource: collectorsAll,
                    valueExpr: 'value',
                    displayExpr: 'format'
                }
            },
            {
                dataField: "lastCallDateString", caption: "最新催收时间", alignment: "center",
                allowFiltering: true, filterOperations: ["=", "between"], dataType: "date", allowSorting: true,
                calculateCellValue: function (data) {
                    if (data.lastCallDateString) {
                        return data.lastCallDateString.toString().substring(0, 19);
                    } else {
                        return "";
                    }
                }
            },
            {
                dataField: "orderString",
                caption: "最新扣款时间",
                alignment: "center",
                allowFiltering: true,
                filterOperations: ["=", "between"],
                dataType: "date",
                allowSorting: true,
                calculateCellValue: function (data) {
                    if (data.orderString) {
                        return data.orderString.toString().substring(0, 19);
                    }
                }
            },
            {
                dataField: "contractID",
                caption: "合同编号",
                alignment: "center",
                allowFiltering: true,
                allowSorting: false,
                filterOperations: ["="]
            },
            {dataField: "contractKey", caption: "合同编号ID", visible: false},
            {dataField: "loanAmount", caption: "放款金额", alignment: "center", allowFiltering: false, allowSorting: false},

        ],
        "贷后管理导出" + new Date(),
        function (e) {
            var dataGrid = e.component;
            var toolbarOptions = e.toolbarOptions.items;
            toolbarOptions.push(
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "查看",
                        text: "查看",
                        visible: !disableButton("ym-di", 0),
                        onClick: function () {
                            var selectData = dataGrid.getSelectedRowsData();
                            if (selectData.length == 0) {
                                alert("请选择需要查看信息");
                                return;
                            }
                            if (selectData.length > 1) {
                                alert("一次只能操作一条数据");
                                return;
                            }
                            var customerId = selectData[0].customerId;
                            var contractId = selectData[0].contractKey;
                            var contract = selectData[0].contractID;
                            console.info(customerId + "=" + contractId + "=" + contract);
                            layer_alert(customerId, contractId, contract);
                            dataGrid.clearSelection();
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "导出",
                        text: "导出",
                        visible: !disableButton("ym-di", 8),
                        onClick: function () {
                            var filter = dataGrid.getCombinedFilter();
                            filter = JSON.stringify(filter) == undefined ? '' : JSON.stringify(filter);
                            var url = "loanManagement/loan/export.action?count=" + dataGrid.totalCount() + "&filter=" + encodeURI(filter) + "&userNo=" + usernum;
                            exportData(url, null);
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "刷新",
                        text: "刷新",
                        visible: !disableButton("ym-di", 10),
                        icon: "refresh",
                        onClick: function () {
                            tableUtils.refresh("loanManagementTable");
                        }
                    }
                },{
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "还款",
                        text: "还款",
                        visible: !disableButton("ym-di", 11),
                        onClick: function () {
                            var selectData = dataGrid.getSelectedRowsData();
                            if (selectData.length == 0) {
                                alert("请选择需要还款信息");
                                return;
                            }
                            if (selectData.length == 0) {
                                alert("请选择需要还款信息");
                                return;
                            }
                            if (selectData[0].repayRetryFlag != 1) {
                                alert("只能操作待补偿状态合同");
                                return;
                            }
                            var perId = selectData[0].customerId;
                            var phone = selectData[0].customerMobile;
                            var amount = selectData[0].surplusTotalAmount;
                            var borrNum = selectData[0].contractID;
                            var borrId = selectData[0].contractKey;
                            $.ajax({
                                type: 'POST',
                                url: "loanManagement/trade.action",
                                data: {
                                    perId : perId,
                                    phone : phone,
                                    amount : amount,
                                    borrNum : borrNum,
                                    borrId : borrId,
                                    triggerStyle : 0
                                },
                                success: function (result) {
                                    result = JSON.parse(result);
                                    alert(result.info);
                                },
                                error: function (result) {
                                    console.log(result);
                                    alert("系统繁忙，请稍候再试");

                                },
                                timeout: 50000
                            });
                            dataGrid.clearSelection();
                        }
                    }
                });
        }
    );
};

//导出函数
var exportData = function (url, params) {
    var form = $("<form>");//定义一个form表单
    form.attr("style", "display:none");
    form.attr("target", "");
    form.attr("method", "post");
    form.attr("action", url);
    $("body").append(form);//将表单放置在web中
    form.submit();//表单提交
};

var loanManagementNoOverdue = function () {
    $('.modal-backdrop').hide();
    setToolBar_lmg();
    checkPageEnabled("ym-di");
    initLoanManagementNoOverdueTable();
};