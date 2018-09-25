$(function () {
    getcookie_all();
    //getpath();

});
//测试ODATA
//var odataurl  = "http://crmtest.jhh.com:9998";

//UATODATA
var odataurl = "http://crmtest.jhh.com:9999";

//正式ODATA
//var odataurl = "http://youmi.jhh.com:8070";


var signkey = "jhhymsj";
var usernum = "";
var userid = "";
var username = "";
var isEmail = "";
var cookiearray_all = [];

var treeindex_p = 0;
var treearray_p = ["ym-z", "ym-u", "ym-y", "ym-v", "ym-g", "ym-d", "ym-c","ym-x"];

var treearray_for_p = ["ym-za", "ym-zb", "ym-zc", "ym-zd","ym-ze","ym-zf", "ym-ua", "ym-ya", "ym-yb", "ym-yc","ym-yd",
    "ym-va","ym-vb","ym-vc","ym-vd","ym-ve","ym-vf","ym-vg","ym-ga", "ym-zma", "ym-zmr",
    "ym-da", "ym-db", "ym-dc", "ym-dd", "ym-de", "ym-df", "ym-dg", "ym-dh","ym-di", "ym-ga", "ym-gb", "ym-gc","ym-gd", "ym-ca","ym-xa","ym-xb"];

var collectors = [];
var collectorsAll = [];
var source = [];
var pruducts = [];
var productsDescription = [];
var authInfo = [];
var orderTypeSource = [];
var orderTypes = [];
var repayTypeSource = [];
var repayTypes = [];
var payChannelsSource = [];
var riskNode=[];
var riskNodeStatus=[];

var loadRiskNode = function () {
    $.ajax({
        url: "/loan-manage/user/getRiskNodeAndStatus.action",
        type: 'GET',
        dataType: "json",
        success: function (result) {
            if (result!=null) {
                var node = result.riskNode;
                var status=result.riskNodeStatus;
                for (var i = 0; i < node.length; i++) {
                    var m = node[i];
                    var obj = {value: m.id, format: m.name};
                    riskNode.push(obj);
                }
                for (var i = 0; i < status.length; i++) {
                    var m = status[i];
                    var obj = {value: m.status, format: m.statusName};
                    riskNodeStatus.push(obj);
                }
            }
        },
        error: function () {
            console.info("数据加载错误");
        },
        timeout: 50000
    });
};

var loadPayChannels = function () {
    $.ajax({
        url: "/loan-manage/loanManagement/queryPayChannels.action",
        data: {skip: 0, take: 10000},
        type: 'POST',
        dataType: "json",
        success: function (result) {
            if (result.code == 1) {
                var payChannels = result.object;
                for (var i = 0; i < payChannels.length; i++) {
                    var obj = {'value': payChannels[i].codeCode, 'format': payChannels[i].meaning};
                    payChannelsSource.push(obj);
                }
            }
        },
        error: function () {
            console.info("数据加载错误");
        },
        timeout: 50000
    });
};

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

var loadCollectors = function () {
    $.ajax({
        url: "/loan-manage/loanManagement/queryReceiptUsersByUser.action?type=1&userNo=" + usernum,
        data: {skip: 0, take: 10000},
        type: 'POST',
        dataType: "json",
        success: function (result) {
            if (result.code == 1) {
                var data = result.object.list;
                for (var i = 0; i < data.length; i++) {
                    var m = data[i];
                    if ("SYS" == m.userName) {

                    } else {
                        var obj = {'value': m.userSysno, 'format': m.userName};
                        collectors.push(obj);
                    }
                }
            }
        },
        error: function () {
            console.info("数据加载错误");
        },
        timeout: 50000
    });
};

var loadCollectorsAll = function () {
    $.ajax({
        url: "/loan-manage/loanManagement/queryAllReceiptUsers.action?type=1&userNo=" + usernum,
        data: {skip: 0, take: 10000},
        type: 'POST',
        dataType: "json",
        success: function (result) {
            if (result.code == 1) {
                var data = result.object.list;
                for (var i = 0; i < data.length; i++) {
                    var m = data[i];
                    if ("SYS" == m.userName) {

                    } else {
                        var obj = {'value': m.userSysno, 'format': m.userName};
                        collectorsAll.push(obj);
                    }
                }
            }
        },
        error: function () {
            console.info("数据加载错误");
        },
        timeout: 50000
    });
};

var loanProducts = function () {
    $.ajax({
        url: "/loan-manage/loanManagement/loanProducts.action",
        type: 'POST',
        dataType: "json",
        success: function (result) {
            if (result.code == 1) {
                var data = result.object;
                for (var i = 0; i < data.length; i++) {
                    var m = data[i];
                    var obj = {value: m.id, format: m.productName };

                    pruducts.push(obj);
                }
                for (var i = 0; i < data.length; i++) {
                    var m = data[i];
                    var obj = {value: m.id, format: m.productDescription };

                    productsDescription.push(obj);
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
        url: "/loan-manage/user/source.action?code=register_source",
        type: 'GET',
        dataType: "json",
        success: function (result) {
            if (result.code == 1) {
                var data = result.object;
                for (var i = 0; i < data.length; i++) {
                    var m = data[i];
                    var obj = {value: m.CODE, format: m.VALUE};
                    source.push(obj);
                }
            }
        },
        error: function () {
            console.info("数据加载错误");
        },
        timeout: 50000
    });
};

var loadUserAuthInfo = function (auth) {
    $.ajax({
        url: "/loan-manage/user/userAuthInfo.action",
        type: 'POST',
        data: {userAuth: auth},
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.code == 0) {
                setMessage(data.message);
            } else {
                authInfo = data.object.auth;
            }
        },
        error: function () {
            console.info("数据加载错误");
        },
        timeout: 50000
    });
};

function disableButton(pageId, buttonIndex) {
    for (var i = 0; i < authInfo.length; i++) {
        var directory = authInfo[i];
        var pages = directory.pages;
        if (pages != undefined) {
            for (var j = 0; j < pages.length; j++) {
                var page = pages[j];
                if (page.id == pageId) {
                    var modules = page.modules;
                    if (modules != undefined) {
                        for (var k = 0; k < modules.length; k++) {
                            var module = modules[k];
                            if (module.type == "Button" && module.index == buttonIndex) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
    }
    return true;
}

var checkPageEnabled = function (pageName) {
    var enabled = false;
    for (var j = 0; j < cookiearray_all.length; j++) {
        if (pageName == cookiearray_all[j]) {
            enabled = true;
            break;
        }
    }
     if (!enabled) {
         window.location.href = "/loan-manage/index.html?e=1#/dashboard.html";
     }
};

// 设置cookie
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}
// 清除cookie
function clearCookie(name) {
    setCookie(name, "", -1);
}

// ///////////////////////////////
function collectAuthInfo(auth) {
    var ids = [];
    loadUserAuthInfo(auth);
    for (var i = 0; i < authInfo.length; i++) {
        var directory = authInfo[i];
        ids.push(directory.id);
        var pages = directory.pages;
        if (pages != undefined) {
            for (var j = 0; j < pages.length; j++) {
                var page = pages[j];
                ids.push(page.id);
            }
        }
    }
    return ids;
}

var getcookie_all = function () {
    try {
        var auth = $.cookie("auth");
        usernum = $.cookie("userid");
        userid = $.cookie("userid");
        username = $.cookie("username");
        if (auth != undefined && usernum != undefined) {
            cookiearray_all = collectAuthInfo(auth);
            loadCollectors();
            loadCollectorsAll();
            loadSource();
            loanProducts();
            loadOrderTypes();
            loadRepayTypes();
            loadPayChannels();
            loadRiskNode();
        } else {
            window.location = "/loan-manage/login.html";
        }
    } catch (e) {
        alert(e);
        window.location = "/loan-manage/login.html";
    }
    //getHeaderInfo();
    getTree();
};
// get the header infos
var getHeaderInfo = function () {
    $("#loginname").html(username);
    $("#out").click(function () {
        var askyou = confirm("确定退出当前登录吗？");
        if (askyou) {
            clearCookie("userid");
            clearCookie("username");
            clearCookie("result");
            window.location = "/loan-manage/login.html"
        }
    });
    $("#userNo").val(usernum);
    $("#modifyPassword").click(function () {
        $("#modifyPasswordModal").modal({show: true, backdrop: 'static', keyboard: false});
    });
    $("#btnModifyPassword").click(function () {
        var oldPwd = $("#oldPwd").val();
        var newPwd = $("#newPwd").val();
        var newPwdConfirm = $("#newPwdConfirm").val();
        var userNo = $("#userNo").val();
        if (oldPwd == null || oldPwd == "") {
            alert("原始密码为空");
            return;
        }
        if (newPwd == null || newPwd == "") {
            alert("新密码为空");
            return;
        }
        if (newPwdConfirm == null || newPwdConfirm == "") {
            alert("确认密码为空");
            return;
        }
        if (newPwd != newPwdConfirm) {
            alert("输入的新密码不匹配");
            return;
        }
        $("#btnModifyPassword").attr("disabled", true);
        $.ajax({
            url: "/loan-manage/auth/modifyPassword.action",
            type: 'POST',
            dataType: "json",
            data: {userNo: userNo, oldPwd: oldPwd, newPwd: newPwd, newPwdConfirm: newPwdConfirm},
            success: function (result) {
                $("#btnModifyPassword").removeAttr("disabled");
                alert(result.message);
                if (result.code == 1) {
                    $("#modifyPasswordModal").modal('hide');
                    clearCookie("userid");
                    clearCookie("username");
                    clearCookie("result");
                    window.location = "/loan-manage/login.html"
                }
            },
            error: function () {
                $("#btnEditUserInfo").removeAttr("disabled");
                alert("网络连接失败");
            },
            timeout: 50000
        });
    });
};
// get the tree menu
var getTree = function () {
    for (var i = 0; i < treearray_p.length; i++) {
        var flag = false;
        for (var j = 0; j < cookiearray_all.length; j++) {
            if (treearray_p[i] == cookiearray_all[j]) {
                flag = true;
                treeindex_p = treeindex_p + 1;
                break;
            }
        }
        if (flag) {
            $("#" + treearray_p[i] + "").css('display', 'block');
            flag = false;
        }

    }
    for (var i = 0; i < treearray_for_p.length; i++) {
        for (var j = 0; j < cookiearray_all.length; j++) {
            if (treearray_for_p[i] == cookiearray_all[j]) {
                $("#" + treearray_for_p[i] + "").css('display', 'block');
                break;
            }
        }
    }
};

String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, '');
};
