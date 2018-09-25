package com.jhh.dc.baika.api.app;

import com.jhh.dc.baika.api.entity.*;
import com.jhh.dc.baika.api.entity.*;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.Product;
import com.jhh.dc.baika.entity.app_vo.SignInfo;

import java.util.Map;

/**
 *  借款模块接口
 * @author xuepengfei
 *2016年9月28日上午9:31:58
 */
public interface LoanService {

    /**
     * 合同签约，状态改为已签约，添加签约时间
     * @param borrId 合同id
     * @param loanUse 用途
     * @return
     */
    ResponseDo<?> signingBorrow(String borrId, String loanUse);

    /**
     * 取消借款申请。判断合同状态，在申请中的合同才能取消借款申请。
     * @param per_id 用户ID
     * @param borr_id 合同id
     * @return
     */
    ResponseDo<?> cancelAskBorrow(String per_id, String borr_id);

    /**
     * 获取签约界面信息
     * @return
     */
    SignInfo getSignInfo(String phone);
    /**
     * 根据合同编号查询催收人
     * @param borrId
     */
    String getCollectionUser(int borrId);

    /**
     * 用户借款状态节点
     * @return
     */
    ResponseDo<Map<String,Object>> applyBorrow(String phone, String productId, String borrNum, String reviewer);

    /**
     *  修改合同状态
     * @param perId 用户Id
     * @param rlStatus 合同状态
     */
    ResponseDo<BorrowList> updateBorrowStatus(String perId,String rlStatus);


    /**
     *  跳转还款页面
     * @param perId
     * @param borrId
     */
    ResponseDo<RepayInfoDo> jumpRepay(String perId, String borrId);

    /**
     *  跳转提现页面
     * @param perId
     * @param borrId
     */
    ResponseDo<DepositDo> jumpWithdrawal(String perId, String borrId);

    /**
     *  清结算调用同步合同信息
     * @param borrowList
     * @return
     */
    ResponseDo<?> synchronousBorrow(BorrowList borrowList);

    /**
     * 获取用户支付页面信息
     */
    ResponseDo<PaymentInfoDo> paymentInfo(PaymentInfoVo vo);

    /**
     * 获取合同详情信息
     * @param borrId
     * @return
     */
    Map<String,Object> getContractInfoByBorrId(Integer borrId);

    /**
     * 向A同步合同状态
     */
    void synchBorrowStatus();

    void synchBorrowStatusOverdue();

    String updateBorrowStatusByBorrowNum(String borrowId, String status);

    Map<String, String> getContractImageByBorrId(Integer borrId);
}
