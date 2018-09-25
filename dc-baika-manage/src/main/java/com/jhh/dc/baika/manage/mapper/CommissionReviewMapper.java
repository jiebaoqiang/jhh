package com.jhh.dc.baika.manage.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.jhh.dc.baika.entity.manager_vo.CommissionReceiveVo;
import com.jhh.dc.baika.entity.share.CommissionReview;

public interface CommissionReviewMapper extends Mapper<CommissionReview> {
    /**
     *
     * @param personId
     */
    List<CommissionReceiveVo> getConmmissionReceiveHistoryByPersonId(String personId);
}