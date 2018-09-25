package com.jhh.dc.baika.api.app;

import com.jhh.dc.baika.api.entity.DetailsDo;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.details.AccountDetails;
import com.jhh.dc.baika.api.entity.details.RepayDetails;
import com.jhh.dc.baika.entity.app.BorrowList;

import java.util.List;

/**
 *  账单相关接口
 * 2018/8/23.
 */
public interface DetailsService {

    /**
     *  账户中心 用户基础金额信息
     * @param phone 手机号
     * @return 视图
     */
    ResponseDo<AccountDetails> getAccountDetails(String phone);

    /**
     *  获取用户借款列表
     * @param perId 用户Id
     * @return 视图
     */
    ResponseDo<List<BorrowList>> getMyBorrowList(Integer perId);

    /**
     *  借款详情
     * @param phone 手机号
     * @param borrNum 合同号
     * @return 视图
     */
    ResponseDo<DetailsDo> getDetails(String phone, String borrNum);

    /**
     *  还款详情
     * @param phone 手机号
     * @param borrId 合同号
     * @return 详情信息
     */
    ResponseDo<RepayDetails> getRepayDetails(String phone, String borrId);
}
