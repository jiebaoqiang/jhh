package com.jhh.dc.baika.entity.share;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by chenchao on 2018/3/14.
 */
/**
  邀请人信息, 包含一级邀请人、二级邀请人、特殊用户信息, 若为空，则无对应邀请人
 */
@Setter @Getter
public class InviterInfo {
    Integer perId;
    String phone;
    String deviceType;
    Integer firstLevelInviter;
    String firstLevelInviterPhone;
    Integer secondLevelInviter;
    String secondLevelInviterPhone;
    Integer thirdLevelInviter;
    String thirdLevelInviterPhone;
}
