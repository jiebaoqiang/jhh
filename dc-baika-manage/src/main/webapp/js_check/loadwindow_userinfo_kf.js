$(function() {
    getpath();
    getTabs();
    loadSource();
});

var perId_root  = 0;

var name = "";
var cardNum = "";
var qqNum = "";
var email = "";
var education = "";
var marry = "";
var getchild = "";

var usuallyaddress = "";
var sourceValue = "";
var phone = "";
var address = "";
var birthday = "";
var blacklist = "";
var createDate = "";

var profession = "";
var monthlypay = "";
var business = "";
var busiCity = "";
var busiAddress = "";
var busiPhone = "";

var relatives = "";
var relativesName = "";
var relaPhone = "";
var society = "";
var societyName = "";
var sociPhone = "";
var isCar = "";
var houseCondition = "";

var getpath = function() {
    try {
        var url = location.href;
        var data_url = url.split("?")[1];
        $.ajax({
            type : "POST",
            url : "/loan-manage/url/url_open.action",
            data : {
                var_text_locked : data_url
            },
            success : function(msg) {
                data_url = msg;
                var info = data_url.split("=")[1];
                var perId = info.split("_")[0];
                var brroId = info.split("_")[1];
                perId_root = perId;
                loadbox_data(perId);

                getCardPicById(perId);

            }
        });
    } catch (e) {
        // window.location = "/jhhoa/login.html";
    }

};

var loadbox_box = function() {
    $("#name").dxTextBox({
        value : name,
        showClearButton : false,
    });
    $("#card_num").dxTextBox({
        value : cardNum,
        showClearButton : false,
    });
    $("#qq_num").dxTextBox({
        value : qqNum,
        showClearButton : false,
    });
    $("#email").dxTextBox({
        value : email,
        showClearButton : false,
    });
    $("#education").dxTextBox({
        value : education,
        showClearButton : false,
    });
    $("#marry").dxTextBox({
        value : marry,
        showClearButton : false,
    });
    $("#getchild").dxTextBox({
        value : getchild,
        showClearButton : false,
    });

    $("#usuallyaddress").dxTextArea({
        value : usuallyaddress,
        showClearButton : false,
    });
    $("#source_value").dxTextBox({
        value : sourceValue,
        showClearButton : false,
    });
    $("#phone").dxTextBox({
        value : phone,
        showClearButton : false,
    });
    $("#address").dxTextArea({
        value : address,
        showClearButton : false,
    });
    $("#birthday").dxDateBox({
        value : birthday,
        showClearButton : false,
    });
    $("#blacklist").dxTextBox({
        value : blacklist,
        showClearButton : false,
    });
    $("#create_date").dxDateBox({
        value : createDate,
        showClearButton : false,
    });

    $("#profession").dxTextBox({
        value : profession,
        showClearButton : false,
    });
    $("#monthlypay").dxTextBox({
        value : monthlypay,
        showClearButton : false,
    });
    $("#business").dxTextBox({
        value : business,
        showClearButton : false,
    });
    $("#busi_city").dxTextBox({
        value : busiCity,
        showClearButton : false,
    });
    $("#busi_address").dxTextBox({
        value : busiAddress,
        showClearButton : false,
    });
    $("#busi_phone").dxTextBox({
        value : busiPhone,
        showClearButton : false,
    });

    $("#relatives").dxTextBox({
        value : relatives,
        showClearButton : false,
    });
    $("#relatives_name").dxTextBox({
        value : relativesName,
        showClearButton : false,
    });
    $("#rela_phone").dxTextBox({
        value : relaPhone,
        showClearButton : false,
    });
    $("#society").dxTextBox({
        value : society,
        showClearButton : false,
    });
    $("#society_name").dxTextBox({
        value : societyName,
        showClearButton : false,
    });
    $("#soci_phone").dxTextBox({
        value : sociPhone,
        showClearButton : false,
    });
};

var getTabs = function() {
    $("#myTabs").dxTabs({
        dataSource : [ {
            text : "客户信息",
            icon : "user",
            content : "1"
        }, {
            text : "贷款信息",
            icon : "money",
            content : "2"
        }, {
            text : "银行卡信息",
            icon : "card",
            content : "3"
        },{
            text : "黑名单",
            icon : "clear",
            content : "5"
        }, {
            text : "资金流水",
            icon : "bookmark",
            content : "6"
        },{
            text:"认证流程",
            icon: "bookmark",
            content: "7"
        }],
        selectedIndex : 0,
        onItemClick : function(e) {
            $("#1").css("display", "none");
            $("#2").css("display", "none");
            $("#3").css("display", "none");
            $("#4").css("display", "none");
            $("#5").css("display", "none");
            $("#6").css("display", "none");
            $("#7").css("display", "none");
            $("#8").css("display", "none");
            $("#9").css("display", "none");
            $("#" + e.itemData.content + "").css("display", "block");
            if(e.itemData.content =="2"){
                getLoan(perId_root);
            }else if (e.itemData.content =="3"){
                getBank(perId_root);
            }else if (e.itemData.content =="4"){
                getRiskReport(phone, cardNum);
            }else if (e.itemData.content =="5"){
                getBlack(perId_root);
            }else if (e.itemData.content =="6"){
                getOrders(perId_root);
            }else if (e.itemData.content =="7"){
                getNode(perId_root);
            }else if (e.itemData.content =="8"){
                getNodeDetail(perId_root);
            }else if (e.itemData.content =="9"){
                getPhones(perId_root);
            }
        }
    });
};

