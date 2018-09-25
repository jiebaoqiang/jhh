package com.jhh.dc.baika.entity.share;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//好友邀请佣金规则表
@Entity @Table(name = "commission_rule") @Getter @Setter
public class CommissionRule implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  //编号

    private String name; //规则名称

    private Integer applyPeople;//适用人群：全部用户、安卓、苹果、特殊用户

    private String channelPhone;//渠道号码

    private int inviterLevel;//邀请人级数：一级、二级

    private int trackingStatus;//邀请人状态 1.已注册、2.已放款、3.已还第一期、4.已还第二期、5.已还第三期、6.已还清

    private String commission;//对应佣金

    private Date updateDate;//修改时间

    private String operationUser;//操作人
}