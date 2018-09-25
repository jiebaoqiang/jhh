package com.jhh.dc.baika.api.loan;

import com.jhh.dc.baika.api.entity.BindCardVo;
import com.jhh.dc.baika.api.entity.capital.AgentDeductRequest;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.app.Bank;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.pay.driver.pojo.BankBaseInfo;

/**
 * 绑定银行卡
 * @author xuepengfei
 *2016年11月4日上午9:35:34
 */
public interface BankService {


    /**绑卡白骑士
     * @param per_id
     * @param name
     * @param card_num
     * @param bank_num
     * @param bank_phone
     * @param tokenKey
     * @return
     */
    boolean bindingBQS(String per_id, String name, String card_num, String bank_num, String bank_phone, String tokenKey, String device);
    

    /**
     * 获取可以代扣及支付的银行卡列表
     * @return
     */
    ResponseDo<BankBaseInfo[]> getBankList();

    ResponseDo<?> payCenterBindCard(BindCardVo vo);

    /**
     * 主动还款期间签约快捷支付
     * @param request
     * @return
     */
    ResponseDo<String> bindQuickPayDuringPay(AgentDeductRequest request);

    /**
     * 绑卡期间签约快捷支付
     * @param vo
     * @return
     */
    ResponseDo<?> bindQuickPayDuringBind(BindCardVo vo);

}
