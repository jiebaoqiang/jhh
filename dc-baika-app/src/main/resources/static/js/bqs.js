/**
 * Created by jiebaoqiang on 2018/9/19.
 * 白骑士设备标识js
 */
var tokenKey = guid();
(function () {
    _saber = {
        partnerId: 'jinhuhang',
        tokenKey:tokenKey
    };
    var aa = document.createElement('script');
    aa.async = true;
    aa.src = ('https:' == document.location.protocol ? 'https://' :
            'http://') + 'df.baiqishi.com/static/webdf/saber.js?t=' + (new
            Date().getTime()/3600000).toFixed(0);
    var bb = document.getElementsByTagName('script')[0];
    bb.parentNode.insertBefore(aa, bb);
})();

function guid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}
