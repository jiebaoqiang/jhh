package com.jhh.dc.baika.common.enums;

/**
 * 百可录打电话模板整理
 * zhushuaifei
 */
public enum BaikeluRemindEnum {
    NO_DOWNAPP_REMIND("01","未下载app用户","1"),
    NO_LOGIN_REMIND("02","未登陆app用户","2"),
    YES_IDCARD_AUTH("03","身份证认证","已认证"),
    YES_PHONE_BOOK_AUTH("04","通讯录认证","已认证"),
    YES_PERSION_AUTH("05","个人认证","已认证"),
    YES_FACE_AUTH("06","人脸识别","已认证"),
    YES_MOBILE_AUTH("07","手机认证","已认证"),
    NO_SIGN_CONTRACT_REMIND("08","待签约","待签约"),
    IS_OPEN_BAIKELU("true","打开百可录",""),
    IS_CLOSE("off","关闭",""),
    IS_OPEN("on","打开",""),

    FAIL_ONE("1","Missing required fields","所需数据不全"),
    FAIL_TWO("2","Invalid JSON format","JSON数据块 \"mdata\" 格式错误"),
    FAIL_THREE("3","Request should be multi-part","HTTP 请求应为 multi-part格式"),

    ;

    private String code;
    private String desc;
    private String status;
    BaikeluRemindEnum(String code, String desc, String status){
        this.code = code;
        this.desc = desc;
        this.status=status;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public static String getDescByCode(String code){
        for (BaikeluRemindEnum baikeluRemindEnum: BaikeluRemindEnum.values()){
            if(baikeluRemindEnum.code.equals(code)){
                return baikeluRemindEnum.desc;
            }
        }
        return null;
    }
    public static String getStatusByCode(String code){
        for (BaikeluRemindEnum baikeluRemindEnum: BaikeluRemindEnum.values()){
            if(baikeluRemindEnum.code.equals(code)){
                return baikeluRemindEnum.status;
            }
        }
        return null;
    }
}
