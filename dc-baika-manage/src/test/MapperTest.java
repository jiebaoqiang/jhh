/**
 * Created by chenchao on 2018/8/6.
 */

import com.jhh.dc.baika.entity.manager.CollectorsRecord;
import com.jhh.dc.baika.manage.mapper.CollectorsRecordMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 2018/3/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/applicationContext-dao.xml"})
public class MapperTest {

    @Autowired
    private CollectorsRecordMapper mapper;

    @Test
    public void testBatchInsert() throws Exception {
        for (int j = 0; j < 10; j++) {
            List<CollectorsRecord> records = new ArrayList<CollectorsRecord>();
            for (int i = 0; i < 3; i++) {
                CollectorsRecord record1 = new CollectorsRecord();
                record1.setContractId("abc");
                records.add(record1);
            }
            mapper.batchInsertCollectorsRecord(records);
        }
    }
}
