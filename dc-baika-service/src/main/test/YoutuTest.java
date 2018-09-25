import com.jhh.dc.baika.api.bankdeposit.QianQiDepositBackService;
import com.jhh.dc.baika.constant.Constant;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.bankdeposit.QianQiBack;
import com.jhh.dc.baika.entity.bankdeposit.RepaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by zhangqi on 2018/9/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/applicationContext.xml"})
@Slf4j
public class YoutuTest {


    @Autowired
    QianQiDepositBackService mQianQiDepositBackService;

    @Test
    public void query(){

        QianQiBack qianQiBack = new QianQiBack();

        qianQiBack.setAcctNo("123");
        qianQiBack.setCustNo("4556");
        qianQiBack.setStatus("200");
        qianQiBack.setPhone("13636587874");
        qianQiBack.setTradeCode(Constant.CG1044);
        String b = mQianQiDepositBackService.accountBack(qianQiBack);

        log.info("hehe"+b);
    }

    @Test
    public void findAllPPerson(){
        List<Person> allPerson = mQianQiDepositBackService.findAllPerson(3);

        log.info(allPerson.size()+"hehe");
    }

    @Test
    public void queryTest(){

        mQianQiDepositBackService.queryAccountAuthAll();
//        mQianQiDepositBackService.findAllPerson(2);
    }

    @Test
    public void queryTrade(){
        mQianQiDepositBackService.queryTradeAll();
    }

    @Test
    public void repayment(){
        RepaymentRequest repaymentRequest = new RepaymentRequest();
        repaymentRequest.setAmount("30");
        repaymentRequest.setBaikaOrderNo("123213213213");
        repaymentRequest.setCapital("10");
        repaymentRequest.setIncomeAmt("20");
        repaymentRequest.setBorrowId("12");
        repaymentRequest.setPayerAcctNo("110331807100010430");
        repaymentRequest.setPeriod("1");
        mQianQiDepositBackService.repayment(repaymentRequest);
    }
}
