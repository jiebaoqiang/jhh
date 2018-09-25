package com.jhh.dc.baika.api.loan;

import com.jhh.dc.baika.entity.manager_vo.PrivateVo;

public interface CompanyAService {
    //根据手机号获取用户详细信息
    PrivateVo queryUserPrivateByPhone(String phone);
}
