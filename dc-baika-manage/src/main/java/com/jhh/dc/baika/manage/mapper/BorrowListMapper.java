package com.jhh.dc.baika.manage.mapper;

import org.apache.ibatis.annotations.Param;

import com.jhh.dc.baika.entity.app.BorrowList;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BorrowListMapper extends Mapper<BorrowList> {

    /**
     * 根据用户ID查询合同
     * @param perId
     * @return
     */
    List getBorrByPerId(Integer perId);

    /**
     * 查询逾期为分单合同
     * @return
     */
    List getCollectorsByOverdue();

    /**
     * 查询ID根据状态
     * @param borrIds
     * @return
     */
    List selectIdsByStatus(List borrIds, List status);

    /**
     * 查询合同号根据状态
     * @param borrIds
     * @return
     */
    List selectBorrNumByStatus(List borrIds, List status);

    /**
     * 人工审核拒绝
     * @return
     */
    int rejectAudit();

    /**
     * 查询该用户是否已经放过款
     * @param perId
     * @return
     */
    List<BorrowList> queryBorrListByPerIdAndStauts(Integer perId);

    /**
     * 查询昨天没打审核电话的首单用户
     * @return
     */
    List<BorrowList> selectUnBaikelu();

    /**
     * 根据合同id更改合同状态
     * @param borrId
     * @param status
     */
    void updateStatusById(@Param("borrId") Integer borrId, @Param("status") String status);

    /**
     * 根据合同id查询合同逾期还款金额
     * @param borrId
     */
    String getMstRepayAmount(Integer borrId);
}
