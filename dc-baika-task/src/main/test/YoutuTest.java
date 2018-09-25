import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositBackService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhangqi on 2018/9/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@Slf4j
public class YoutuTest {


    @Autowired
    QianQiDepositBackService qianQiDepositBackService;

    @Test
    public void query(){
        log.info("hehe");
        qianQiDepositBackService.queryAccountAuthAll();
    }

}
