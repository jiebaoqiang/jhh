var perId = 0;
var initCommissition = function(){
    getpath();
};
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
                perId = himid;
                var $e = $('#commissionReviewDetail');
                initTable($e,null,null,true,true,true,"");
            }
        });
    } catch (e) {

    }

};

var queryParam = function(param){
    var options = {
        phone:param.search,
        limit:param.limit,
        offset:param.offset
    };
    return options;
};

var initTable = function ($e,index,row,$detail,detailView,showSearch,url) {
    url = '/loan-manage/commissionReview/detail/'+perId+'.action?level=1';
    if(row !== null && row !== undefined && row !== ""){
        $e = $detail.html('<table id="subTable"></table>').find('table');
        var parentId = row.perId;
        url = '/loan-manage/commissionReview/detail/'+perId+'.action?level=2&perId2='+parentId;
    }
    $e.bootstrapTable({
        url: url,         //请求后台的URL（*）
        method: 'get',                      //请求方式（*）
        toolbar: '#toolbar',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: false,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        queryParams: queryParam,         //传递参数（*）
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [15, 25, 50, 60],        //可供选择的每页的行数（*）
        search: showSearch,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: showSearch,
        showColumns: false,                  //是否显示所有的列
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 1,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: detailView,                   //是否显示父子表
        columns: [{
            field: 'phone',
            title: '手机号'
        }, {
            field: 'type',
            title: '类型'
        }, {
            field: 'trackingStatus',
            title: '邀请阶段'
        }, {
            field: 'commission',
            title: '佣金'
        }],
        responseHandler: function (res) {
            return {
                "total": res.total,
                "rows": res.record
            }
        },//注册加载子表的事件。注意下这里的三个参数！
        onExpandRow: function (index, row, $detail) {
            initTable(null, index, row, $detail, false, false, "");
        }
    })
};

$(function () {
    $('#exportBtn').on('click',function () {
        var url = "/loan-manage/commissionReview/exportCommissionDetail.action?personId=" +perId;
        exportData(url,null);
    })
});

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