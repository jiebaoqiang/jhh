package com.jhh.dc.baika.constant;


/**风控节点编码 及本系统节点编码
 * @author xuepengfei
 */
public class NodeConstant {

    //----------------------------------------------风控系统-------------------------------
    //身份证正面
    public static final String CARD_FRONT_NODE= "1";
    //身份证反面
    public static final String CARD_BACK_NODE= "2";
    //通讯录认证
    public static final String CONTACT_NODE= "3";
    //个人资料认证
    public static final String PERSON_RISK_NODE= "4";
    //人脸识别认证
    public static final String VERIFY_NODE= "5";
    //运营商认证 聚信立
    public static final String JXL_NODE= "6";
    //芝麻信用
    public static final String ZHIMA_NODE= "7";

//----------------------------------------------本系统-------------------------------
    //身份证正面
    public static final int CARD_FRONT_NODE_ID= 1;
    //身份证反面
    public static final int CARD_BACK_NODE_ID= 2;
    //芝麻信用
    public static final int ZHIMA_NODE_ID= 3;
    //通讯录认证
    public static final int CONTACT_NODE_ID= 4;
    //个人资料认证
    public static final int PERSON_RISK_NODE_ID= 5;
    //人脸识别认证
    public static final int VERIFY_NODE_ID= 6;
    //运营商认证 聚信立
    public static final int JXL_NODE_ID= 7;
    //银行卡认证
    public static final int BANK_NODE_ID= 8;

    //白名单标记
    public static final int WHITETAG = 1;
}
