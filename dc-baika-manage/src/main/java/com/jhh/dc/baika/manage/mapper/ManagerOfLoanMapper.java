package com.jhh.dc.baika.manage.mapper;

import java.util.List;

import com.jhh.dc.baika.entity.manager_vo.CardPicInfoVo;
import com.jhh.dc.baika.entity.manager_vo.LoanInfoVo;
import com.jhh.dc.baika.entity.manager_vo.PrivateVo;

public interface ManagerOfLoanMapper {

    PrivateVo selectUserPrivateVo(int perid);

    CardPicInfoVo getCardPicById(int himid);

    List<LoanInfoVo> selectLoanInfoPrivateVo(int himid);

    List<LoanInfoVo> selectLoanInfoPrivateVoForOperator(int himid);
}
