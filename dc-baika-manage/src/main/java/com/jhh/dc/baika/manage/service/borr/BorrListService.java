package com.jhh.dc.baika.manage.service.borr;


import java.util.List;

import com.jhh.dc.baika.manage.entity.Response;

public interface BorrListService {

  /**
   * 获取合同
   * @param perId
   * @return
   */
  Response getBorrByPerId(Integer perId);


  /**
   * 取消合同
   * @param id
   * @return
   */
  Response cancelBorrList(Integer id);

  /**
   * 合同转件
   * @param borrIds 合同号
   * @param userId 转至人ID
   * @param opUserId 操作人ID
   * @return
   */
  Response saveTransferBorrList(List<String> borrIds, String userId,String opUserId);

  /**
   * 自动分单
   * 分单特殊
   */
  Integer submenuTransfer();

  /**
   * 人工审核跑批
   */
  void rejectAudit();
  /**
   * 百可录打电话
   */
  void rcCallPhone();
}
