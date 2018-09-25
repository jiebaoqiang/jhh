package com.jhh.dc.baika.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.jhh.dc.baika.util.MapUtils;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.loan.JdCardService;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.app_vo.JdCardDetailVO;
import com.jhh.dc.baika.mapper.app.JdCardInfoMapper;
import com.jhh.dc.baika.mapper.app.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 2018/7/30.
 */
@Service
public class JdCardServiceImpl implements JdCardService {

    @Autowired
    private JdCardInfoMapper jdCardInfoMapper;
    @Autowired
    private PersonMapper personMapper;

    /**
     * 根据京东卡id获取详细信息
     * @param borrNum 合同编号
     * @return
     */
    @Override
    public ResponseDo<JdCardDetailVO> getCardDetailByCardId(String phone,String borrNum) {
        Person p = personMapper.getPersonByPhone(phone);
        if (p == null){
            return ResponseDo.newFailedDo("该用户不存在");
        }
        JdCardDetailVO data = jdCardInfoMapper.selectCardDetailByCardId(p.getId(),borrNum);
        if (data == null){
            return ResponseDo.newFailedDo("该卡片非本人卡片，请验证后重试");
        }
        data.setBorrStatusName(MapUtils.setBorrName(data.getBorrStatus(),data.getProdType()));
        return ResponseDo.newSuccessDo(data);
    }

}
