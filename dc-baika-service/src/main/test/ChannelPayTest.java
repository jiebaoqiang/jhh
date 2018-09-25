import com.jhh.dc.baika.api.channel.AgentChannelService;
import com.jhh.dc.baika.api.channel.WithdrawalService;
import com.jhh.dc.baika.api.entity.BindCardVo;
import com.jhh.dc.baika.api.entity.capital.AgentDeductRequest;
import com.jhh.dc.baika.api.entity.capital.AgentPayRequest;
import com.jhh.dc.baika.api.entity.cash.WithdrawalVo;
import com.jhh.dc.baika.api.loan.BankService;
import com.jhh.dc.baika.entity.app.NoteResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 2018/3/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/spring/applicationContext.xml"})
public class ChannelPayTest {

   // @Autowired
    private AgentChannelService agentChannelService;

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private BankService bankService;

    @Test
    public void ChannelDeduct() throws Exception {
        AgentDeductRequest request = new AgentDeductRequest();
        request.setBorrId("2376");
        request.setOptAmount("0.1");
        request.setBankId("4");
        request.setBankNum("6227001050240065448");
        request.setDescription("as");
        request.setCreateUser("SH2009");
        request.setCollectionUser("9999");
        request.setType("2");
        request.setTriggerStyle("0");
        request.setGuid("f31d80d5-9284-4014-a6b3-066873f6da78");
        request.setName("接宝强");
        request.setIdCardNo("230402199407270132");
        request.setPhone("18745745592");
      //  agentDeductService.deduct(request);
    }

    @Test
    public void state() throws Exception {

     //  agentDeductService.state("dc1718032117570822856155");
    }

    @Test
    public void pay(){
        AgentPayRequest agentPayRequest = new AgentPayRequest();
       // agentPayRequest.setBorrId();
        agentChannelService.pay(agentPayRequest);
    }

    @Test
    public void commissiondraw(){
        WithdrawalVo vo = new WithdrawalVo();
        vo.setPerId(41904);
        vo.setAmount(1f);
        vo.setBankNum("6217231001002784356");
        vo.setPayChannel("pay-rong");
        withdrawalService.getCommissionWithdrawal(vo);
    }

    @Test
    public void stateCommission(){
        withdrawalService.commissionState("dc1918041714355464448364");
    }

    @Test
    public void bindCard(){

    }

}
