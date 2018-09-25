package com.jhh.dc.baika.service;

public interface BorrListService {

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
   * 逾期三天分给杨艳（四期产品）
   */
  void updateOverdueThree();

  /**
   * 逾期两天分给杨艳（一期产品）
   */
  void updateOverdueTwo();

  /**
   * 批量代扣
   */
  void batchWithhold();
  /**
   * 百可录打首单审核电话
   */
  void rcCallPhone();

  /**
   * 查询逾期未还的合同的一天内的最近一笔订单状态和订单失败的原因,早上6点运行定时任务。并标记下来：
   *  •对成功的订单，标记为‘成功’
   *  •农业银行、建设银行的银行卡都不可扣款,用户的主卡为农业银行卡、建行卡标记为‘失败’
   *  •其他失败原因均标记为‘失败可扣’
   */
  void batchQueryResult();
}
