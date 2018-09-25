package com.jhh.dc.baika.common.constant;

/**
 * 支付渠道常量类
 *
 * @author wenfucheng
 */
public interface PayCenterChannelConstant {

    /**
     * 银生宝
     */
    String PAY_CHANNEL_YSB = "pay-rong";

    /**
     * 合利宝(普通支付)
     */
    String PAY_CHANNEL_HLB = "helipay-pay-ronghe";

    /**
     * 合利宝(快捷支付)
     */
    String PAY_CHANNEL_HLB_QUICK = "helipay-fast-ronghe";


    /**
     * 汇聚(快捷支付)
     */
    String PAY_CHANNEL_HUIJU_QUICK = "huiju-fast-ronghe";

    /**
     * 拉卡拉
     */
    String PAY_CHANNEL_LKL = "pay-lkl";

    /**
     * 拉卡拉(快捷支付)
     */
    String PAY_CHANNEL_LKL_QUICK = "lkl-fast-ronghe";

    /**
     * 支付宝
     */
    String PAY_CHANNEL_ZFB = "pay-zfb";

    /**
     * 业务默认渠道
     */
    String PAY_CHANNEL_DEFAULT = "";
}
