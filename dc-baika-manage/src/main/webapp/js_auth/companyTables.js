var companyTablesInit = function(){
    checkPageEnabled("ym-gc");
    initButtonClick();
    initCompanyTabls();
};
var initButtonClick = function () {
  $("#btnEditCompanyInfo").click(function(){
      var companyName = $("#companyName").val();
      var status= $("#status").val();
      if(companyName == null || companyName == ""){
            alert("公司名称不能为空");
            return;
      }
      if(status == null || status == ""){
          alert("公司状态不能为空");
          return;
      }
      var formData = $("#editcompany-form").serialize();
      $("#btnEditCompanyInfo").attr("disabled",true);
      $.ajax({
          url: "auth/editCompanyInfo.action",
          type: 'POST',
          dataType: "json",
          data:formData,
          success: function (result) {
              $("#editCompanyInfo").modal("hide");
              $("#btnEditCompanyInfo").removeAttr("disabled");
              if (result.code == 1) {
                  tableUtils.refresh("companyTables");
              }else{
                  alert("用户操作失败");
              }
          },
          error: function () {
              $("#btnEditCompanyInfo").removeAttr("disabled");
              alert("数据提交失败");
          },
          timeout: 50000
      });
  });
};
var initCompanyTabls = function(){
    tableUtils.initMuliTableToolBar(
        "companyTables",
        "auth/loanCompanysByPage.action",
        null,
        [
            {
                dataField: "name", caption: "公司名称", alignment: "center", allowFiltering: true
            },
            {
                dataField: "status",caption: "公司状态",alignment: "center",allowFiltering: true,
                lookup: {
                    dataSource: [
                        { value: '1', format: '启用' },
                        { value: '0', format: '未启用' }
                    ],
                    valueExpr: 'value',
                    displayExpr: 'format'
                }
            }
        ],
        "公司列表",
        function(e){
            // var dataGrid = e.component;
            // var toolbarOptions = e.toolbarOptions.items;
            // toolbarOptions.push(
            //     {
            //         location: "before",
            //         widget: "dxButton",
            //         options: {
            //             hint: "新增",
            //             text: "新增",
            //             visible : !disableButton("ym-gc",0),
            //             icon: "add",
            //             onClick: function () {
            //                 $("#editCompanyInfoTitle").html("添加公司");
            //                 $("#companyId").val("");
            //                 $("#companyName").val("");
            //                 $("#status").val("");
            //                 $("#editCompanyInfo").modal({show: true, backdrop: 'static', keyboard: false});
            //             }
            //         }
            //     },
            //     {
            //         location: "before",
            //         widget: "dxButton",
            //         options: {
            //             hint: "编辑",
            //             text: "编辑",
            //             visible : !disableButton("ym-gc",1),
            //             icon: "edit",
            //             onClick: function () {
            //                 var selectRows = dataGrid.getSelectedRowsData();
            //                 if(selectRows.length != 1){
            //                     alert("每次仅能操作一条数据");
            //                     return;
            //                 }
            //                 $("#companyId").val(selectRows[0].id);
            //                 $("#companyName").val(selectRows[0].name);
            //                 $("#status").val(selectRows[0].status);
            //                 $("#editCompanyInfoTitle").html("编辑公司");
            //                 $("#editCompanyInfo").modal({show: true, backdrop: 'static', keyboard: false});
            //             }
            //         }
            //     },
            //     {
            //         location: "before",
            //         widget: "dxButton",
            //         options: {
            //             hint: "刷新数据",
            //             text: "刷新数据",
            //             visible : !disableButton("ym-gc",2),
            //             icon: "refresh",
            //             onClick: function () {
            //                 dataGrid.refresh();
            //             }
            //         }
            //     }
            // );
        }
    )
};
