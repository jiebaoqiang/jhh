package com.jhh.dc.baika.service.loan;

import com.jhh.dc.baika.constant.Constant;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import com.jhh.dc.baika.mapper.manager.CollectorsListMapper;
import com.jhh.dc.baika.mapper.manager.RepaymentPlanMapper;
import com.jhh.dc.baika.api.loan.CollectorsListService;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.loan.SystemUser;
import com.jhh.dc.baika.entity.loan.CollectorsList;
import com.jhh.dc.baika.common.util.Assertion;
import com.jhh.dc.baika.common.util.Detect;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 合同相关
 */
@Service
@Setter
public class CollectorsListServiceImpl implements CollectorsListService {
    @Autowired
    private CollectorsListMapper collectorsListMapper;
    @Autowired
    private BorrowListMapper borrowListMapper;
    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;


    @Override
    public int batchUpdate(List borrIds, SystemUser collectors) {
        Assertion.notEmpty(borrIds, "合同ID不能为空");
        if (collectors != null) {
            // 更新催收表
            CollectorsList collectorsList = new CollectorsList();
            collectorsList.setBedueUserSysno(collectors.getUserSysno());
            collectorsList.setBedueName(collectors.getUserName());
            collectorsList.setAcquMode("B");
            collectorsList.setUpdateDate(Calendar.getInstance().getTime());

            Example example = new Example(CollectorsList.class);
            example.createCriteria().andIn("contractSysno", borrIds).andEqualTo("isDelete", 2);
            int successCount = collectorsListMapper.updateByExampleSelective(collectorsList, example);

            //更新合同冗余字段
            BorrowList borrowList = new BorrowList();
            borrowList.setCollectionUser(collectors.getUserSysno());
            Example blExample = new Example(CollectorsList.class);
            blExample.createCriteria().andIn("id", borrIds);
            borrowListMapper.updateByExampleSelective(borrowList, blExample);

            return successCount;
        }
        return 0;
    }

    @Override
    public int saveCompletionStatus(List borrIds) {

        if (Detect.notEmpty(borrIds)) {
            //判断是否结清
            List ids = borrowListMapper.selectIdsBySettleStatus(borrIds);

            if (Detect.notEmpty(ids)) {
                CollectorsList collectorsList = new CollectorsList();
                collectorsList.setStatus("B");
                collectorsList.setUpdateDate(Calendar.getInstance().getTime());

                Example example = new Example(CollectorsList.class);
                example.createCriteria().andIn("contractSysno", ids);

                return collectorsListMapper.updateByExampleSelective(collectorsList, example);
            }
        }
        return 0;
    }

    @Override
    public int saveCompletionStatus(int borrId) {
        if (Detect.isPositive(borrId)) {
            List list = new ArrayList();
            list.add(borrId);
            return saveCompletionStatus(list);
        }
        return 0;
    }

    @Override
    public int updateStagesCollectionStatus(int borrId, int[] repaymentIds, String borrowStatus) {
        if (Detect.notEmpty(borrId + "") && Detect.notEmpty(repaymentIds + "")) {
            //查询borrow_list的collection_user催款人
            String collectionUser = borrowListMapper.selectCollectionUser(borrId);

            //更新repayment_play表，设置对应得collection_user为collUser
            Map paramMap = new HashMap();
            paramMap.put("repaymentIds", repaymentIds);
            paramMap.put("collectionUser", collectionUser);
            repaymentPlanMapper.updateCollectionUser(paramMap);

            if (Constant.STATUS_TO_REPAY.equals(borrowStatus)) {
                //更新collectors_list表，将is_delete字段由2改为1
                collectorsListMapper.deleteCollection(borrId);
                //把催款人置空
                borrowListMapper.resetCollectionUser(borrId);
            }
        }
        return 0;
    }
}
