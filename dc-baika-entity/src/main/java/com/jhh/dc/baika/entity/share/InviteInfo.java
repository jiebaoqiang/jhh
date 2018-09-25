package com.jhh.dc.baika.entity.share;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuepengfei on 2018/3/8.
 */
@Data
public class InviteInfo implements Serializable {

    public InviteInfo(){}

    public InviteInfo(int inviteId, String inviteUrl, int canWithdraw, String commissionTotal, String commissionBalance) {
        this.inviteId = inviteId;
        this.inviteUrl = inviteUrl;
        this.canWithdraw = canWithdraw;
        this.commissionTotal = commissionTotal;
        this.commissionBalance = commissionBalance;
    }
    /**
     * 我的邀请码
     */
    private int inviteId;
    /**
     * 我的分享页面
     */
    private String inviteUrl;
    /**
     * 是否可以领取佣金 1:是  0:否
     */
    private int canWithdraw;
    /**
     * 所有佣金
     */
    private String commissionTotal;
    /**
     * 可领佣金
     */
    private String commissionBalance;
    /**
     * 一级好友佣金
     */
    private List<CommissionOrder> level1Orders;
    /**
     * 二级好友佣金
     */
    private List<CommissionOrder> level2Orders;

}
