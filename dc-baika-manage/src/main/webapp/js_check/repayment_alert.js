var borrId_root;
$(function() {
    getpath();
    loadtable2_rep();

});

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
                borrId_root = info.split("_")[1];

            }
        });
    } catch (e) {
        // window.location = "/jhhoa/login.html";
    }
};

var loadtable2_rep = function() {


        DevExpress.config({
            forceIsoDateParsing: true,
        });

        var repayPlan = new DevExpress.data.CustomStore({
            load: function (loadOptions) {
                var deferred = $.Deferred(), args = {};
                args.borrId = borrId_root;
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


        $("#userTable").dxDataGrid({
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
                caption : "还款日期",
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
                dataField : "overdueDays",
                caption : "逾期天数",
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





