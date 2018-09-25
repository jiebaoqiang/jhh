var borrId_root;
$(function() {
    init();

});

var init = function() {
    try {
        var url = location.href;
        var data_url = url.split("?")[1];
        $("#serial").attr("src",decodeURI(data_url))
    } catch (e) {
        // window.location = "/jhhoa/login.html";
    }
};

