package com.jhh.dc.baika.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositWebService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.loan.AccountService;
import com.jhh.dc.baika.common.enums.QianQiDepositEnum;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.bankdeposit.BankDepositCommon;
import com.jhh.dc.baika.entity.bankdeposit.CustAuthRequest;
import com.jhh.dc.baika.mapper.app.PersonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther: wenfucheng
 * @date: 2018/9/10 10:04
 * @description:
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private QianQiDepositWebService qianQiDepositWebService;

    /**
     * 开户
     * @param perId
     * @return
     */
    @Override
    public Map<String, String> getOpenAccountHtmlParam(Integer perId) {
        Person person = personMapper.selectByPrimaryKey(perId);
        Map<String,String> map = new HashMap<>();
        map.put("perId",String.valueOf(perId));
        map.put("phone",person.getPhone());
        return map;
    }

    /**
     * 授权
     * @param perId
     * @param authType
     * @param webResponseUrl
     * @return
     */
    @Override
    public ResponseDo<BankDepositCommon> authAccount(String perId, String authType, String webResponseUrl) {
        Person person = personMapper.selectByPrimaryKey(Integer.parseInt(perId));
        CustAuthRequest custAuthRequest = new CustAuthRequest();
        custAuthRequest.setCustNo(person.getCustNo());
        custAuthRequest.setAuthType("1".equals(authType)? QianQiDepositEnum.AUTH_TYPE_LOAN.getCode():QianQiDepositEnum.AUTH_TYPE_REPAY.getCode());
        custAuthRequest.setRegisterPhone(person.getPhone());
        custAuthRequest.setResponsePath(webResponseUrl);
        ResponseDo<BankDepositCommon> responseDo = qianQiDepositWebService.custAuth(custAuthRequest);
        if(200!=responseDo.getCode()){
            log.warn("调用授权接口失败:"+responseDo.getInfo());
        }
        return responseDo;
    }

    @Override
    public ResponseDo<String> getAuthStatus(Integer perId, Integer authType) {
        Person person = personMapper.selectByPrimaryKey(perId);
        ResponseDo<String> responseDo = ResponseDo.newSuccessDo(1==authType?person.getIsRepayAuth():person.getIsLoanAuth());
        return responseDo;
    }

    @Override
    public ResponseDo<Person> getOpenAccountStatus(String perId) {
        Person person = personMapper.selectByPrimaryKey(Integer.parseInt(perId));
        return ResponseDo.newSuccessDo(person);
    }
}
