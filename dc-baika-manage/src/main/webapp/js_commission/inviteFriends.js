var commissionInviteFriendTablesInit = function(){
    checkPageEnabled("ym-vf");
    inviteFriends();
};


var inviteFriends = function(){
    tableUtils.initMuliTableToolBar(
        "inviteFriends",
        "friends/inviteFriends.action",
        null,
        [
            {
                dataField: "phone", caption: "手机号码", alignment: "center", allowFiltering: true
            },
            {
                dataField: "inviterPhone", caption: "邀请我的人", alignment: "center", allowFiltering: true
            },
            {
                dataField: "commissionTotal", caption: "佣金总额", alignment: "center", allowFiltering: false
            },
            {
                dataField: "commissionBalance", caption: "未领佣金", alignment: "center", allowFiltering: false
            },
            {
                dataField: "inviterLevel1Count", caption: "一级邀请人数", alignment: "center", allowFiltering: false
            },
            {
                dataField: "inviterLevel2Count", caption: "二级邀请人数", alignment: "center", allowFiltering: false
            },

        ],
        "邀请好友",
        function(e){
            var dataGrid = e.component;
            var toolbarOptions = e.toolbarOptions.items;
            toolbarOptions.push(
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint : "查看",
                        text: "查看",
                        visible : !disableButton("ym-vf",0),
                        onClick: function() {
                            var selectData = dataGrid.getSelectedRowsData();
                            if (selectData.length == 0) {
                                alert("请选择需要查看信息");
                                return;
                            }
                            if (selectData.length > 1) {
                                alert("一次只能操作一条数据");
                                return;
                            }
                            var customerId = selectData[0].perId;
                            // var contractId = selectData[0].contractKey;
                            // var contract = selectData[0].contractID;
                            load_share_friends_detail(customerId);

                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint : "导出",
                        text: "导出",
                        visible : !disableButton("ym-vf",1),
                        onClick: function() {
                            var filter = tableUtils.loadTableFilter("inviteFriends");
                            filter = JSON.stringify(filter) == undefined ? '' : JSON.stringify(filter);
                            var url = "friends/exportInviteFriendsList.action?filter=" + encodeURI(filter)+"&userNo=" + usernum +"&count="+dataGrid.totalCount();
                            exportData(url,null);
                        }
                    }
                },
                {
                    location: "before",
                    widget: "dxButton",
                    options: {
                        hint: "刷新",
                        text: "刷新",
                        visible : !disableButton("ym-vf",2),
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

var load_share_friends_detail = function (perId) {
    var brroid = 0;
    var borrNum = 0;
    var iWidth = 1350;
    if (window.screen.availWidth < 1350) {
        iWidth = window.screen.availWidth;
    }
    var iHeight = window.screen.availHeight - 34;
    var iTop = 0;
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
    var var_text = "hi=" + perId + "_"+brroid + "_" + borrNum;
    $.ajax({
        type : "post",
        url : "/loan-manage/url/url_lock.action",
        data : {
            var_text : var_text
        },
        success : function(msg) {
            url_locked = msg;
            if(window_type == "pho"){
                window.open("/loan-manage/page_commission/shareFriendsDetail.html?"
                    + url_locked + "", "佣金审核详情", "height=" + iHeight
                    + ", width=" + iWidth + ", top=" + iTop + ", left=" + iLeft
                    + ",location=no,resizable=no");
            }else{
                window.location.href = "/loan-manage/page_commission/shareFriendsDetail.html?"
                    + url_locked + "";
            }

        }
    });
};