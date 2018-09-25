var refundRecord = function() {
    checkPageEnabled("ym-xb");
    loadtable_di();
};

var loadtable_di = function() {

    $.ajax({
        type : "post",
        url : "refundRecord/refund.action",
        success : function(msg) {
            loadtable2_di(msg);
        }
    });
};

var loadtable2_di = function(jsonobj) {
    $("#refundTable").dxDataGrid({
        dataSource : {
            store : {
                type : "array",
                data : jsonobj
            }
        },
        /*searchPanel : {
            visible : true,
            width : 240,
            placeholder : "搜索..."
        },*/
        /*"export" : {
            enabled : true,
            fileName : "Employees",
            allowExportSelectedData : true
        },*/
        /*headerFilter : {
            visible : true
        },*/
        filterRow : {
            visible : true,
            applyFilter : "auto"
        },
        rowAlternationEnabled : true,
        showRowLines : true,
//		selection : {
//			mode : "multiple"
//		},
        allowColumnReordering : true,
        allowColumnResizing : true,
        columnAutoWidth : true,
        /*columnChooser : {
            title : "列选择器",
            enabled : true,
            emptyPanelText : '把你想隐藏的列拖拽到这里...'
        },*/
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
            dataField : "serialNo",
            caption : "退款流水号",
            alignment : "center",
        },{
            dataField : "userName",
            caption : "姓名",
            alignment : "center",
        },
        {
            dataField : "idCard",
            caption : "身份证号",
            alignment : "center",
        },
        {
            dataField : "phone",
            caption : "手机号码",
            alignment : "center",
        },
        {
            dataField : "bankName",
            caption : "开户行",
            alignment : "center"
        },
        {
            dataField : "amount",
            caption : "退款金额",
            alignment : "center"
        },
        {
            dataField : "bankNum",
            caption : "银行卡号",
            alignment : "center"
        },
        {
        dataField : "creationDate",
        caption : "确认时间",
        alignment : "center",
        dataType: "date" ,
        format: function (date) {
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
        },
        },{
            dataField : "updateDate",
            caption : "退款时间",
            alignment : "center",
            dataType: "date" ,
            format: function (date) {
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
            },
        }, {
            dataField : "channel",
            caption : "退款渠道",
            alignment : "center"
        },
        {
            dataField : "remark",
            caption : "备注",
            alignment : "center"
        }]
    });
};
