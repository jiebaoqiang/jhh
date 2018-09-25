package com.jhh.dc.baika.api.bankdeposit;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.bankdeposit.QianQiBack;
import com.jhh.dc.baika.entity.bankdeposit.RepaymentRequest;
import com.jhh.dc.baika.entity.bankdeposit.YoutuQuery;

import java.util.List;

public interface QianQiDepositBackService {


    ResponseDo<String> rate();

    String accountBack(QianQiBack qianQiBack);

    String authBack(QianQiBack qianQiBack);

    List<Person> findAllPerson(int code);

    QianQiBack query(String type,String phone,String authType);

    void queryAccountAuthAll();

    void queryTradeAll();

    ResponseDo<YoutuQuery> queryTrade(String type, String bankOrderNo);

    ResponseDo<YoutuQuery> repayment(RepaymentRequest repaymentRequest);
}
