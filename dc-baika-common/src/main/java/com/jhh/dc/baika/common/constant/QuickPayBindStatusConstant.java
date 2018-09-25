package com.jhh.dc.baika.common.constant;

/**
 * @auther: wenfucheng
 * @date: 2018/6/6 17:11
 * @description: 快捷支付绑定状态常量
 */
public enum QuickPayBindStatusConstant {

    QUICK_PAY_HLB(1 << 0, "合利宝"), QUICK_PAY_LKL(1 << 1, "拉卡拉"), QUICK_PAY_HUIJU(1 << 2, "汇聚");

    private int flag;
    private String displayName;

    QuickPayBindStatusConstant(int flag, String displayName) {
        this.flag = flag;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getFlag() {
        return flag;
    }
}
