var userOption;
$.getJSON("/dc-baika/user/getUserJson", function (data){
    userOption = data;
});
$('.debtInfo').click(function(){
    $("#option").html("");
    $("#option").append("<li>取消</li>")
    $(userOption.debtInfo).each(function(index,domEle){
        $("#option").append("<li>"+domEle+"</li>")
    });

    $('.modal').show().find('.list').slideDown();
    $('.list li').on('click', function () {
        list("debtInfo",this);
    });
})

$('.liveInfo').click(function(){
    $("#option").html("");
    $("#option").append("<li>取消</li>")
    $(userOption.liveInfo).each(function(index,domEle){
        $("#option").append("<li>"+domEle+"</li>")
    });

    $('.modal').show().find('.list').slideDown();
    $('.list li').on('click', function () {
        list("liveInfo",this);
    });
})

$('.transInfo').click(function(){
    $("#option").html("");
    $("#option").append("<li>取消</li>")
    $(userOption.transInfo).each(function(index,domEle){
        $("#option").append("<li>"+domEle+"</li>")
    });

    $('.modal').show().find('.list').slideDown();
    $('.list li').on('click', function () {
        list("transInfo",this);
    });
})

$('.companyInfo').click(function(){
    $("#option").html("");
    $("#option").append("<li>取消</li>")
    $(userOption.companyInfo).each(function(index,domEle){
        $("#option").append("<li>"+domEle+"</li>")
    });

    $('.modal').show().find('.list').slideDown();
    $('.list li').on('click', function () {
        list("companyInfo",this);
    });
})

$('.relationship1').click(function(){
    $("#option").html("");
    $("#option").append("<li>取消</li>")
    $(userOption.relationship).each(function(index,domEle){
        $("#option").append("<li>"+domEle+"</li>")
    });

    $('.modal').show().find('.list').slideDown();
    $('.list li').on('click', function () {
        list("relationship1",this);
    });
})

$('.relationship2').click(function(){
    $("#option").html("");
    $("#option").append("<li>取消</li>")
    $(userOption.relationship).each(function(index,domEle){
        $("#option").append("<li>"+domEle+"</li>")
    });

    $('.modal').show().find('.list').slideDown();
    $('.list li').on('click', function () {
        list("relationship2",this);
    });
})


function list(id,obj){
    $('.list').slideUp(200,function(){});
    $('.modal').fadeOut(200);
    if($(obj).text() === '取消'){
        return;
    }else{
        $('.method').text($(obj).text());
        $('#'+id).val($(obj).text());
    }
}

$("#submitBtn").click(function(){
    let serialize = $("#userForm").serialize()

    $.post("/dc-baika/user/saveUserInfo",serialize,function(data){
        if(data.code==200){
            location.href="/dc-baika/account/openAccountHtml/"+$("#perId").val();
        }
    });

});