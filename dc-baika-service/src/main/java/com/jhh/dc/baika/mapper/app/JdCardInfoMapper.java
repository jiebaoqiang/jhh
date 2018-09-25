package com.jhh.dc.baika.mapper.app;

import com.jhh.dc.baika.entity.app.JdCardInfo;
import com.jhh.dc.baika.entity.app_vo.JdCardDetailVO;
import com.jhh.dc.baika.entity.app_vo.JdCardInfoVO;
import com.jhh.dc.baika.entity.app_vo.JdCardKeyInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JdCardInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JdCardInfo record);

    int insertSelective(JdCardInfo record);

    JdCardInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JdCardInfo record);

    int updateByPrimaryKey(JdCardInfo record);

    List<JdCardInfoVO> selectCardInfoByPhone(String phone);

    JdCardDetailVO selectCardDetailByCardId(@Param("perId") Integer perId,@Param("borrNum") String borrNum);

    void updateFetchTimeById(Integer jdCardId);

    JdCardInfo getUnusedCard();

    JdCardKeyInfo getCardKeyInfoByCardId(Integer id);
}