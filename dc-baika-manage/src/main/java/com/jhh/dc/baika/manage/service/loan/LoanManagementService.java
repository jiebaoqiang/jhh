package com.jhh.dc.baika.manage.service.loan;

import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.entity.app.BankVo;
import com.jhh.dc.baika.entity.app.Product;
import com.jhh.dc.baika.entity.loan.SystemUser;
import com.jhh.dc.baika.entity.loan.ReceiptUsers;
import com.jhh.dc.baika.entity.manager.CodeValue;
import com.jhh.dc.baika.entity.manager.CollectorsListVo;
import com.jhh.dc.baika.entity.manager.CollectorsRemark;
import com.jhh.dc.baika.entity.manager.Download;
import com.jhh.dc.baika.entity.manager_vo.LoanInfoVo;
import com.jhh.dc.baika.entity.manager_vo.LoanManagementVo;
import com.jhh.dc.baika.manage.entity.AskCollection;
import com.jhh.dc.baika.manage.entity.LoansRemarkOutVo;
import com.jhh.dc.baika.manage.entity.LoansRemarkVo;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.entity.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface LoanManagementService {
    /**
     * 分页加载贷后管理
     *
     * @param contractKey
     * @return
     */
    PageInfo<LoanManagementVo> selectLoanManagementInfo(HttpServletRequest request, String contractKey);

    /**
     * 分页加载贷后管理
     *
     * @param contractKey
     * @return
     */
    PageInfo<LoanManagementVo> selectLoanManagementInfo(HttpServletRequest request, String contractKey, String from);

    /**
     * 分页加载催收人员
     *
     * @param queryMap
     * @return
     */
    PageInfo<ReceiptUsers> selectReceiptUsers(Map<String, Object> queryMap, int offset, int size);

    /**
     * 请求扣款
     *
     * @param askCollection
     * @return
     */
    Result askCollection(AskCollection askCollection);

    /**
     * 查询导出条数
     *
     * @param queryMap
     * @return
     */
    int queryExportCount(Map<String, Object> queryMap);

    /**
     * 查询导出信息
     *
     * @param queryMap
     * @return
     */
    List<LoanManagementVo> selectExportData(Map<String, Object> queryMap, Integer count, String userNo);

    /**
     * 检查是否可以下载及检查最大下载条数
     *
     * @return
     */
    Download checkCanDownload();

    /**
     * 查询所有产品
     *
     * @return
     */
    List<Product> selectProducts();


    List<LoanInfoVo> selectLoanInfoPrivateVo(int perId);

    /**
     * 拉取催收人员
     *
     * @param userNo
     * @return
     */
    PageInfo<SystemUser> selectReceiptUsers(Map<String, Object> queryParams, String userNo, Integer type, int offset, int size);
    PageInfo<SystemUser> selectReceiptUsers(String type, int offset, int size);

    /**
     * 查询催收信息
     * @param queryMap
     * @return
     */
    PageInfo<CollectorsListVo> selectCollectorsInfo(Map<String, Object> queryMap, int offset, int size, String userNo);

    /**
     * 添加催收备注
     * @param remark
     * @return
     */
    int addCollectionRemark(CollectorsRemark remark);

    /**
     * 分页查询催收队列
     * @param queryMap 查询参数
     * @return
     */
    PageInfo<CollectorsListVo> selectCollectorsListVo(Map<String, Object> queryMap);

    /**
     * 转件
     * @param contractIds 合同号
     * @param userId 转至人ID
     * @param opUserId 操作人ID
     * @return
     */
    Response transferLoan(String contractIds, String userId,String opUserId);

    /**
     * 查询用户主卡
     * @param userId
     * @return
     */
    BankVo selectMainBankByUserId(Integer userId);

    /**
     * 申请减免
     * @param contractId 合同号
     * @param reduce 减免金额
     * @return
     */
    Result reduceLoan(String contractId, String reduce, String remark,String type, String userName, String bedueDays);

    /**
     * 查询批量代扣专用数据
     * @param parameterMap
     * @return
     */
    List queryBatchReduce(Map<String, String[]> parameterMap);
    /**
     * 查询批量代扣流水数据
     * @param parameterMap
     * @return
     */
    List getBatchReduceList(Map<String, String[]> parameterMap);

    /**
     * 批量扣款
     * @param askCollections 批量扣款对象数组
     * @param reduceMoney 批量扣款金额
     * @param deductionsType 批量扣款类型
     * @param payChannel 批量扣款渠道
     * @return
     */
    Result batchCollection(List<LoanManagementVo> askCollections, String reduceMoney, String createUser, String deductionsType, String payChannel);

    /**
     * 查询扣款渠道
     * @return
     */
    List<CodeValue> selectPayChannels();

    /**
     * 导出催收备注(外包)
     * @param queryMap
     * @param userNo
     * @return
     */
    List<LoansRemarkOutVo> selectExportLoansRemarkForOutWorkers(Map<String, Object> queryMap, String userNo);

    /**
     * 导出催收备注
     * @param queryMap
     * @param userNo
     * @return
     */
    List<LoansRemarkVo> selectExportLoansRemarkVo(Map<String, Object> queryMap, String userNo);

    /**
     * 获取最大减免金额
     * @param loanManagementVo
     * @return
     */
    Map getMaxReduceAmount(LoanManagementVo loanManagementVo);
}
