package com.jhh.dc.baika.api.loan;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.app_vo.JdCardDetailVO;

/**
 * 2018/7/30.
 */
public interface JdCardService {

    ResponseDo<JdCardDetailVO> getCardDetailByCardId(String phone,String borrNum);
}
