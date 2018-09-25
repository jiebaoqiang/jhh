package com.jhh.dc.baika.api.loan;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.bankdeposit.BankDepositCommon;

import java.util.Map;

/**
 * @auther: wenfucheng
 * @date: 2018/9/10 10:03
 * @description:
 */
public interface AccountService {
    Map<String, String> getOpenAccountHtmlParam(Integer perId);

    ResponseDo<BankDepositCommon> authAccount(String perId, String authType, String webResponseUrl);

    ResponseDo<String> getAuthStatus(Integer perId, Integer authType);

    ResponseDo<Person> getOpenAccountStatus(String perId);
}
