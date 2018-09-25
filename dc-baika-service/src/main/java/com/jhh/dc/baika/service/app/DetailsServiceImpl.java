package com.jhh.dc.baika.service.app;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.api.app.DetailsService;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositWebService;
import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.api.entity.DetailsDo;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.details.AccountDetails;
import com.jhh.dc.baika.api.entity.details.RepayDetails;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import com.jhh.dc.baika.mapper.app.CodeValueMapper;
import com.jhh.dc.baika.mapper.app.PersonMapper;
import com.jhh.dc.baika.service.capital.BasePayServiceImpl;
import com.jinhuhang.settlement.dto.SettlementResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * 2018/8/23.
 */
@Slf4j
@Service
public class DetailsServiceImpl implements DetailsService {

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private AgreementServiceImpl agreementService;

    @Autowired
    private QianQiDepositWebService qianQiDepositWebService;

    @Autowired
    private CodeValueMapper codeValueMapper;

    @Override
    public ResponseDo<AccountDetails> getAccountDetails(String phone) {
        log.info("--------------------调用用户个人中心 phone = "+phone);
        //获取用户信息
        Person p = personMapper.getPersonByPhone(phone);
        if (p == null){
            return ResponseDo.newFailedDo(StateCode.PHONE_NOTREGISTER_MSG);
        }
        //调用第三方查询用户余额 TODO
        String balance = qianQiDepositWebService.queryUserAccountAmount(p.getPhone());
        //获取用户最新合同
        BorrowList newBorr = borrowListMapper.selectNow(p.getId());
        //获取客服电话
        String servicePhone = codeValueMapper.selectCodeByType("service_phone");
        AccountDetails accountDetails = new AccountDetails(p,newBorr,balance);
        accountDetails.setServicePhone(servicePhone);
        return ResponseDo.newSuccessDo(accountDetails);
    }


    @Override
    public ResponseDo<List<BorrowList>> getMyBorrowList(Integer perId) {
        List<BorrowList> list = borrowListMapper.selectByPerId(perId);
        return ResponseDo.newSuccessDo(list);
    }

    @Override
    public ResponseDo<DetailsDo> getDetails(String phone, String borrNum) {
        Person person = personMapper.getPersonByPhone(phone);
        if (person == null) {
            return ResponseDo.newFailedDo("该用户不存在");
        }
        DetailsDo detail = borrowListMapper.getDetails(borrNum);

        if (detail.getPlanRepay() == 0 || detail.getAmountSurplus() == 0) {
            //调用风控试算接口
            ResponseDo<SettlementResult> planInfo = agreementService.getPlanInfo(detail.getBorrId());
            if (200 == planInfo.getCode()) {
                JSONObject obj = JSONObject.parseObject(planInfo.getData().getModel());
                detail.setPlanRepay(obj.getFloat("plan_repay"));
                detail.setAmountSurplus(obj.getFloat("amount_surplus"));
            }
        }
        return ResponseDo.newSuccessDo(detail);
    }

    @Override
    public ResponseDo<RepayDetails> getRepayDetails(String phone, String borrId) {

        RepayDetails details = borrowListMapper.getRepayDetails(borrId);
        return ResponseDo.newSuccessDo(details);
    }

}
