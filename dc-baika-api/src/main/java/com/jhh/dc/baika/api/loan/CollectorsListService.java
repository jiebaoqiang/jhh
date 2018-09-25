package com.jhh.dc.baika.api.loan;


import java.util.List;

import com.jhh.dc.baika.entity.loan.SystemUser;

public interface CollectorsListService {

  /**
   * 批量分单
   * @param borrIds
   * @param collectors
   * @return
   */
    int batchUpdate(List borrIds, SystemUser collectors);

  /**
   * 更新完成状态
   * @param borrIds
   * @return
   */
    int saveCompletionStatus(List borrIds);

  /**
   * 更新完成状态
   * @param borrId
   * @return
   */
  int saveCompletionStatus(int borrId);

  /**
   * 更新分期催收状态
   * @param borrId b_borrow_list
   * @param repaymentIds
   * @return
   */
  int updateStagesCollectionStatus(int borrId,int[] repaymentIds, String borrowStatus);
}
