package com.jhh.dc.baika.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.manage.service.Commission.InviteFriendsService;
import com.jhh.dc.baika.entity.manager_vo.CommissionReceiveVo;
import com.jhh.dc.baika.entity.share.CommissionSummary;
import com.jhh.dc.baika.manage.mapper.CommissionReviewMapper;
import com.jhh.dc.baika.manage.mapper.CommissionSummaryMapper;
import com.jhh.dc.baika.common.util.DateUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.Example;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author xingmin
 */
@Service
public class InviteFriendsServiceImpl implements InviteFriendsService {
    private static Logger LOG = Logger.getLogger(InviteFriendsServiceImpl.class);

    @Autowired
    private CommissionSummaryMapper commissionSummaryMapper;

    @Autowired
    private CommissionReviewMapper commissionReviewMapper;

    /**
     * 获取邀请好友列表
     */
    @Override
    public PageInfo<CommissionSummary> queryCommissionSummaryList(Map<String, Object> queryMap, int offset, int size, boolean isReview) {
        Example queryExample = new Example(CommissionSummary.class);
        Example.Criteria criteria = queryExample.createCriteria();
        handleCondition(queryMap, criteria);
        if (queryMap.containsKey("selector") && queryMap.containsKey("desc")) {
            Example.OrderBy orderBy = queryExample.orderBy(queryMap.get("selector").toString());
            if ("desc".equals(queryMap.get("desc"))) {
                orderBy.desc();
            }
        }
        PageHelper.offsetPage(offset, size);
        List<CommissionSummary> commissionSummaries = commissionSummaryMapper.selectByExample(queryExample);
        Iterator<CommissionSummary> iterator = commissionSummaries.iterator();
        while (iterator.hasNext()){
            CommissionSummary commissionSummary = iterator.next();
            if(commissionSummary.getInviterLevel1Count() == 0 || commissionSummary.getInviterLevel1Count() == null){
                iterator.remove();
            }
        }
        return new PageInfo<>(commissionSummaries);
    }

    /**
     * 导出获取邀请好友列表
     * @param queryMap
     * @return
     */
    @Override
    public PageInfo<CommissionSummary> exportCommissionSummaryList(Map<String, Object> queryMap) {
        Example queryExample = new Example(CommissionSummary.class);
        Example.Criteria criteria = queryExample.createCriteria();
        handleCondition(queryMap, criteria);
        if (queryMap.containsKey("selector") && queryMap.containsKey("desc")) {
            Example.OrderBy orderBy = queryExample.orderBy(queryMap.get("selector").toString());
            if ("desc".equals(queryMap.get("desc"))) {
                orderBy.desc();
            }
        }
        List<CommissionSummary> commissionSummaries = commissionSummaryMapper.selectByExample(queryExample);
        Iterator<CommissionSummary> iterator = commissionSummaries.iterator();
        while (iterator.hasNext()){
            CommissionSummary commissionSummary = iterator.next();
            if(commissionSummary.getInviterLevel1Count() == 0 || commissionSummary.getInviterLevel1Count() == null){
                iterator.remove();
            }
        }
        return new PageInfo<>(commissionSummaries);
    }

    /**
     * 获取用户佣金领取记录
     * @param personId
     * @return
     */
    @Override
    public List<CommissionReceiveVo> exportCommissionGetHistory(String personId) {
        List<CommissionReceiveVo> list = commissionReviewMapper.getConmmissionReceiveHistoryByPersonId(personId);
        return list;
    }

    @Override
    public PageInfo<CommissionReceiveVo> queryInviterDetail(Map<String, Object> queryMap, String perId, int offset, int size) {

        PageHelper.offsetPage(offset, size);

        List<CommissionReceiveVo> list = commissionReviewMapper.getConmmissionReceiveHistoryByPersonId(perId);

        return new PageInfo<>(list);
    }

    private static void handleCondition(Map<String, Object> conditionMap, Example.Criteria criteria) {
        for (String entry : conditionMap.keySet()) {
            try {
                if (entry.contains("_end") || entry.contains("_start")) {
                    continue;
                }
                if (null == conditionMap.get(entry + "_start")) {
                    criteria.andEqualTo(entry, conditionMap.get(entry));
                } else {
                    criteria.andGreaterThan(entry, DateUtil.stampToDate((String) conditionMap.get(entry + "_start")));
                    criteria.andLessThan(entry, DateUtil.stampToDate((String) conditionMap.get(entry + "_end")));
                }
            } catch (MapperException e) {
                LOG.error(e);
            }
        }
    }

}