var commissionRuleTablesInit = function(){
    checkPageEnabled("ym-vc");
    initButtonClick();
    initCompanyTabls();
    queryCommRuleGroup();
    initSelectDataLevel();
    initSelectDataStatus();

    $("#editTrackingStatus").change(function () {
        if($("#editTrackingStatus").val() == 1){
            $("#editApplyPeople").val(1);
        }
    });
};

var queryCommRuleGroup=function(){
    var selDom = $("#editApplyPeople");
    $.ajax({
        type:'get',
        url:'commissionRule/queryCommRuleGroup.action',
        async:false,
        success:function(result){
            selDom.html("");
            for(var i=0;i<result.length;i++){
                selDom.append("<option value='"+result[i].codeCode+"'>"+result[i].meaning+"</option>");
            }
        }
    });
};

var initSelectDataLevel=function(){
    var selDom = $("#editInviterLevel");
    $.ajax({
        type:'get',
        url:'commissionRule/queryCommRuleInviterLevel.action',
        async:false,
        success:function(result){
            selDom.html("");
            for(var i=0;i<result.length;i++){
                selDom.append("<option value='"+result[i].codeCode+"'>"+result[i].meaning+"</option>");
            }
        }
    });
};

var initSelectDataStatus=function(){
    var selDom = $("#editTrackingStatus");
    $.ajax({
        type:'get',
        url:'commissionRule/queryCommRuleTrackStatus.action',
        async:false,
        success:function(result){
            selDom.html("");
            for(var i=0;i<result.length;i++){
                selDom.append("<option value='"+result[i].codeCode+"'>"+result[i].meaning+"</option>");
            }
        }
    });
};
var initButtonClick = function () {
  $("#btnEditCommissionRule").click(function(){
      var editName = $("#editName").val();
      if(editName == null || editName == ""){
            alert("规则名称不能为空");
            return;
      }
      if(editName.length > 255){
          alert("规则名称长度太长");
          return;
      }
      var editCommission = $("#editCommission").val();
      if(editCommission == null || editCommission == ""){
          alert("佣金不能为空");
          return;
      }
      if(!(editCommission === "0")){
          if(!Number(editCommission) ){
              alert("佣金只能输入数字");
              return ;
          }
      }
      if(editCommission.toString().indexOf(".") != -1){
          if(editCommission.toString().split(".")[1].length > 2){//小数点后大于两位
              alert("小数点仅能保留两位");
              return ;
          }
      }
      if(parseFloat(editCommission) < 0){
          alert("佣金不能为负数");
          return ;
      }

      if(editCommission.toString().split(".")[0].length > 8){
          alert("佣金长度太长");
          return ;
      }

      var myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      if ($("#editChannelPhone").val()!=null&&$("#editChannelPhone").val().trim()!="") {
          if(!myreg.test($("#editChannelPhone").val()))
          {
              alert('请输入正确的手机号码！');
              return;
          }
      }
      var formData = $("#editcompany-form").serialize();
      $("#btnEditCompanyInfo").attr("disabled",true);
      $.ajax({
          url: "commissionRule/updateCommissionRule.action",
          type: 'POST',
          dataType: "json",
          data:formData,
          success: function (result) {
              $("#editCommissionRule").modal("hide");
              $("#btnEditCommissionRule").removeAttr("disabled");
              if (result.code == 1) {
                  tableUtils.refresh("commissionRule");
              }else{
                  alert("用户操作失败");
              }
          },
          error: function () {
              $("#btnEditCommissionRule").removeAttr("disabled");
              alert("数据提交失败");
          },
          timeout: 50000
      });
  });
};
var initCompanyTabls = function(){
    tableUtils.initMuliTableToolBar(
        "commissionRule",
        "commissionRule/queryCommissionRuleList.action",
        null,
        [
            {
                dataField: "id", caption: "编号", alignment: "center", allowFiltering: true, filterOperations:["="]
            },
            {
                dataField: "name", caption: "规则名称", alignment: "center", allowFiltering: true, filterOperations:["="]
            },
            {
                dataField: "applyPeople",caption: "适用人群",alignment: "center",allowFiltering: true,
                lookup: {
                    dataSource: [
                         { value: '1', format: '全部用户' },
                         { value: '2', format: '安卓' },
                         { value: '3', format: '苹果' },
                         { value: '4', format: '特殊用户' }

                    ],
                    valueExpr: 'value',
                    displayExpr: 'format'
                }
            },
            {
                dataField: "channelPhone", caption: "渠道号码", alignment: "center", allowFiltering: true, filterOperations:["="]
            },
            {
                dataField: "inviterLevel",caption: "邀请人级数",alignment: "center",allowFiltering: true,
                lookup: {
                    dataSource: [
                        { value: '1', format: '一级' },
                        { value: '2', format: '二级' }
                    ],
                    valueExpr: 'value',
                    displayExpr: 'format'
                }
            },
            {
                dataField: "trackingStatus",caption: "邀请人状态",alignment: "center",allowFiltering: true,
                lookup: {
                    dataSource: [
                        { value: '1', format: '已注册' },
                        { value: '2', format: '已放款' },
                        { value: '3', format: '已还第一期' },
                        { value: '4', format: '已还第二期' },
                        { value: '5', format: '已还第三期' },
                        { value: '6', format: '已还清' }
                    ],
                    valueExpr: 'value',
                    displayExpr: 'format'
                }
            },
            {
                dataField: "commission",caption: "对应佣金",alignment: "center",allowFiltering: false,dataType: "date"
            },
            {
                dataField: "updateDate",caption: "修改时间",alignment: "center",allowFiltering: true,dataType: "date", filterOperations:["=","between"]
            },
            {
                dataField: "operationUser",caption: "操作人",alignment: "center",allowFiltering: true, filterOperations:["="]
            }
        ],
        "规则列表",
        function(e){
            var dataGrid = e.component;
            var toolbarOptions = e.toolbarOptions.items;
            toolbarOptions.push(
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "新增",
                        text: "新增",
                        visible : !disableButton("ym-vc",0),
                        icon: "add",
                        onClick: function () {
                            $("#operationUser").val($.cookie('userid'));
                            $("#editId").val("");
                            $("#editName").val("");
                            $("#editChannelPhone").val("");
                            $("#editCommission").val("");
                            $("#editApplyPeople").val(1);
                            $("#editChannelPhone").val("");
                            $("#editInviterLevel").val(1);
                            $("#editTrackingStatus").val(1);
                            $("#editCommissionRuleTitle").html("添加规则");
                            $("#editCommissionRule").modal({show: true, backdrop: 'static', keyboard: false});
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "编辑",
                        text: "编辑",
                        visible : !disableButton("ym-vc",1),
                        icon: "edit",
                        onClick: function () {
                            var selectRows = dataGrid.getSelectedRowsData();
                            if(selectRows.length != 1){
                                alert("每次仅能操作一条数据");
                                return;
                            }
                            $("#operationUser").val($.cookie('userid'));
                            $("#editId").val(selectRows[0].id);
                            $("#editName").val(selectRows[0].name);
                            $("#editApplyPeople").val(selectRows[0].applyPeople);
                            $("#editChannelPhone").val(selectRows[0].channelPhone);
                            $("#editInviterLevel").val(selectRows[0].inviterLevel);
                            $("#editTrackingStatus").val(selectRows[0].trackingStatus);
                            $("#editCommission").val(selectRows[0].commission);
                            $("#editCommissionRuleTitle").html("修改规则");
                            $("#editCommissionRule").modal({show: true, backdrop: 'static', keyboard: false});
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "删除",
                        text: "删除",
                        visible :!disableButton("ym-vc",2),
                        icon: "close",
                        onClick: function () {
                            var selectRows = dataGrid.getSelectedRowsData();
                            if(selectRows.length < 1){
                                alert("至少选择一条数据");
                                return;
                            }
                            var idfordel="";
　　　　　　　　　　　　　　for(var i=0;i<selectRows.length;i++){
                                 idfordel+=selectRows[i].id+",";
                            }
                            $.ajax({
                                type : "POST",
                                url :"commissionRule/deleteCommissionRule.action",
                                data : {
                                    'idfordel' : idfordel
                                },
                                success : function(msg) {
                                    if (msg.code >  0) {
                                        showMessage("删除成功！");
                                        tableUtils.refresh("commissionRule");
                                    } else {
                                        showMessage("删除失败！");
                                    }
                                }
                            });

                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "刷新",
                        text: "刷新",
                        visible : !disableButton("ym-vc",3),
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


