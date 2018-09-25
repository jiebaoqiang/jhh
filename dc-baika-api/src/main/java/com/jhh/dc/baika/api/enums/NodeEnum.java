package com.jhh.dc.baika.api.enums;

/**
 * 2018/7/13.
 * 用户签约前申请流程节点enum
 */
public enum NodeEnum {
    /**
     * 用户未登录
     */
    UN_LOGIN_NODE("NS001"),

    /**
     * 用户个人信息未填写
     */
    UN_SAVE_USER_INFO("NS002"),

    /**
     * 用户未开户
     */
    UN_OPEN_ACCOUNT("NS003"),

    /**
     * 用户未授权
     */
    UN_AUTH("NS004"),

    /**
     * 用户未开通快捷支付
     */
    UN_QUICK_PAY("NS005"),

    /**
     *未签约或签约未通过
     */
    UN_SIGN("NS006"),

    /**
     *已签约
     */
    SIGNED("NS007"),

    /**
     * 未知节点
     */
    ERROR_NODES("NS999"),
    ;

    private String node;

    NodeEnum(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }


    public static boolean contains(String rulerName) {
        NodeEnum[] properties = values();
        for (NodeEnum property : properties) {
            if (property.name().equals(rulerName)) {
                return true;
            }
        }
        return false;
    }
}
