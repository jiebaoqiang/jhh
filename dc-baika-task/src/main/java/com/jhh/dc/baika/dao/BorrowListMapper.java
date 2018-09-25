package com.jhh.dc.baika.dao;

import java.util.List;
import java.util.Map;

import com.jhh.dc.baika.model.*;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app_vo.BorrowVO;
import com.jhh.dc.baika.model.*;

import tk.mybatis.mapper.common.Mapper;

public interface BorrowListMapper extends Mapper<BorrowList> {

    /**
     * 查询明天还款提醒数据
     * @param date1
     * @param date2
     * @return
     */
    List<BorrPerInfo> getMingtianhuankuanId(String date1, String date2);

    /**
     * 查询资金规划端报表
     * @param map
     * @return
     */
    List<MoneyManagement> sendMoneyManagement(Map map);

    /**
     * 人工审核拒绝
     * @return
     */
    int rejectAudit();

    /**
     * 查询逾期为分单合同
     * @return
     */
    List getCollectorsByOverdue();

    /**
     * 查询逾期天数合同
     * @param day
     * @return
     */
    List getOverdueData(int day);

    /**
     * 查询逾期三天合同id
     */
    List getOverdueThree();
    /**
     * 查询逾期三天合同id
     */
    List getOverdueTwo();
    List getBatchWithhold();

    /**
     * 把逾期三天的合同的催款人改为杨艳更新表collectors_list
     * @param 合同id
     */
    void updateOverdueThreeCollectors(List list);

    /**
     * 更新逾期三天的催款人信息为杨艳，更新合同表冗余字段borrow_list
     */
    void updateOverdueThreeBorrow(List list);
    /**
     * 插入逾期三天的转件历史记录
     */
    void insertCollectorsRecode(List list);

    /**
     *查询逾期一天的催款电话需要的提示信息
     * @return
     */
    List<PromptData> getPromptData();

    /**
     *查询给财务发送的当天还款数据
     * @return
     */
    List<FinanceData> getFinanceData(String date);
    /**
     * 查询逾期三天借款信息，借款人，催款ID，操作人（四期产品）
     */
    List<CollRecordData> getCollectorsOverdueThree();

    /**
     * 查询逾期两天借款信息，借款人，催款ID，操作人（一期产品）
     */
    List<CollRecordData>  getCollectorsOverdueTow();

    /**
     * 查询昨天没打审核电话的首单用户
     * @return
     */
    List<BorrowList> selectUnBaikelu();

    /**
     * 查询该用户是否已经放过款
     * @param perId
     * @return
     */
    List<BorrowList> queryBorrListByPerIdAndStauts(Integer perId);


    /**
     * 选择逾期超过11天的borrow_list 数据
     */
    List<BorrowVO> selectOverDays();

}
