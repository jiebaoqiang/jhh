var commissionReviewTablesInit = function(){
    checkPageEnabled("ym-vd");
    queryCommissionReviewResultList();
};


var queryCommissionReviewResultList = function(){
    tableUtils.initMuliTableToolBar(
        "commissionReview",
        "commissionReview/queryCommissionReviewResultList.action",
        null,
        [
            {
                dataField: "id", caption: "编号", alignment: "center", allowFiltering: true, filterOperations:["="]
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
                dataField: "status",caption: "审核状态",alignment: "center",allowFiltering: true,
                lookup: {
                    dataSource: [
                        { value: '2', format: '审核拒绝' },
                        { value: '4', format: '放款成功' },
                    ],
                    valueExpr: 'value',
                    displayExpr: 'format'
                }
            },
            {
                dataField: "reason", caption: "备注", alignment: "center", allowFiltering: true
            },
            {
                dataField: "applyDate",caption: "申请时间",alignment: "center",  filterOperations:["=","between"],dataType: "date",
                format: function (date) {
                    return tableUtils.formatDate(date);
                }
            },
            {
                dataField: "reviewDate",caption: "审核时间",alignment: "center",  filterOperations:["=","between"],dataType: "date",
                format: function (date) {
                    return tableUtils.formatDate(date);
                }
            },
            {
                dataField: "employNum",caption: "审核人",alignment: "center",allowFiltering: true
            },
        ],
        "佣金审核结果列表",
        function(e){
            var dataGrid = e.component;
            var toolbarOptions = e.toolbarOptions.items;
            toolbarOptions.push(

                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "刷新",
                        text: "刷新",
                        visible : !disableButton("ym-vd",0),
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
