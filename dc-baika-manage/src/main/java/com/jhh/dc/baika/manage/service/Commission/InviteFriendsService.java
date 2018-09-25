package com.jhh.dc.baika.manage.service.Commission;

import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.entity.manager_vo.CommissionReceiveVo;
import com.jhh.dc.baika.entity.share.CommissionSummary;

import java.util.List;
import java.util.Map;

public interface InviteFriendsService {

    /**
     * 获取邀请好友列表
     * @return PageInfo<CommissionReview>
     */
    PageInfo<CommissionSummary> queryCommissionSummaryList(Map<String, Object> queryMap, int offset, int size, boolean isReview);

    /**
     * 导出获取邀请好友列表
     * @param queryMap
     * @return
     */
    PageInfo<CommissionSummary> exportCommissionSummaryList(Map<String, Object> queryMap);

    /**
     * 获取用户佣金领取记录
     * @param personId
     * @return
     */
    List<CommissionReceiveVo> exportCommissionGetHistory(String personId);

    /**
     * 领取记录
     * @param queryMap
     * @param offset
     * @param size
     * @return
     */
    PageInfo<CommissionReceiveVo> queryInviterDetail(Map<String, Object> queryMap,String perId, int offset, int size);
}
