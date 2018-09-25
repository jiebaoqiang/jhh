package com.jhh.dc.baika.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.jhh.dc.baika.manage.mapper.*;
import com.jhh.dc.baika.api.loan.CollectorsListService;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.common.Constants;
import com.jhh.dc.baika.entity.loan.SystemUser;
import com.jhh.dc.baika.entity.loan.CollectorsList;
import com.jhh.dc.baika.entity.loan.PerAccountLog;
import com.jhh.dc.baika.entity.manager.CollectorsRecord;
import com.jhh.dc.baika.entity.manager.LoanCompanyBorrow;
import com.jhh.dc.baika.entity.manager.LoanCompanyOrder;
import com.jhh.dc.baika.entity.manager.Order;
import com.jhh.dc.baika.manage.entity.Result;
import com.jhh.dc.baika.manage.mapper.*;
import com.jhh.dc.baika.common.util.BorrNum_util;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.Detect;
import com.jinhuhang.settlement.dto.SettleDto;
import com.jinhuhang.settlement.dto.SettlementResult;
import com.jinhuhang.settlement.service.SettlementAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DubboTranService {
    private Logger logger = LoggerFactory.getLogger(LoanManagementServiceImpl.class);
    @Autowired
    private CollectorsListMapper collectorsListMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SettlementAPI rmiSettlementService;
    @Autowired
    private CollectorsRecordMapper collectorsRecordMapper;
    @Autowired
    private LoanCompanyBorrowMapper companyBorrowMapper;
    @Autowired
    private CompanyOrderMapper companyOrderMapper;
    @Autowired
    private SystemUserMapper collectorsMapper;
    @Autowired
    private BorrowListMapper borrowListMapper;
    @Autowired
    private CollectorsListService collectorsListService;
    @Autowired
    private PerAccountLogMapper perAccountLogMapper;


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Result callDubbo(BorrowList borrowList, String reduce, String remark, String type, String userName, String bedueDays) {
        logger.info("进入dubbo调用,参数是：" + borrowList.getId() + "->" + reduce + "->" + remark + "->" + type + "->" + userName + "->" + bedueDays);
        Map<String,Object> map = new HashMap<>();
        map.put("contractId",borrowList.getId());
        map.put("rlState","p");
        List<Order> ordersByArgs = orderMapper.getOrdersByArgs(map);
        if (ordersByArgs != null && ordersByArgs.size() > 0){
            return new Result(201,"当前有正在处理的订单，请稍候重试");
        }
        Result result = new Result();
        CollectorsList collectorsList = new CollectorsList();
        collectorsList.setContractId(borrowList.getBorrNum());
        collectorsList.setIsDelete(2);
        collectorsList = collectorsListMapper.selectOne(collectorsList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
        String serNo = "dc" + sdf.format(new Date()) + BorrNum_util.getStringRandom(10);
        //生成减免订单
        Order order = new Order();
        order.setPerId(0);
        order.setSerialNo(serNo);
        order.setCompanyId(0);
        order.setContractId(borrowList.getId());
        order.setBorrNum(borrowList.getBorrNum());
        order.setPerId(borrowList.getPerId());
        order.setProdId(borrowList.getProdId());
        order.setOptAmount(reduce);
        order.setActAmount(reduce);
        order.setRlRemark(remark);
        order.setRlState("p");
        order.setType(type);
        order.setStatus("y");
        order.setRlDate(new Date());
        order.setCreationDate(order.getRlDate());
        order.setCreateUser(userName);
        order.setOverdueDays(Integer.parseInt(bedueDays));
        if (collectorsList != null && Detect.notEmpty(collectorsList.getBedueUserSysno())) {
            order.setCollectionUser(collectorsList.getBedueUserSysno());
        }

        int count = orderMapper.insertSelective(order);

        if (count > 0) {

            logger.info("订单生成成功" + order.toString());

            //取得订单号
            int orderId = order.getId();

            try {
                // 管理后台操作“减免、线下还款”后，APP中未能展示相关流水记录，应该展示，且标识分别为“减免”、“线下缴款”
                if("6".equals(type) || "7".equals(type)) {
                    //资金流水
                    PerAccountLog pal = new PerAccountLog();
                    pal.setPerId(order.getPerId());
                    pal.setOrderId(order.getId());
                    pal.setOperationType(order.getType());
                    pal.setAmount(order.getOptAmount());
                    if ("6".equals(type)) {
                        pal.setRemark("减免");
                    } else if ("7".equals(type)) {
                        pal.setRemark("线下缴款");
                    }
                    pal.setAddtime(new Date());
                    perAccountLogMapper.insertSelective(pal);
                }

                //生成公司流水记录
                SystemUser c = new SystemUser();
                c.setUserSysno(userName);
                SystemUser collectors = collectorsMapper.selectOne(c);
                if (collectors != null && Constants.COLLECTORS_OUT.equals(collectors.getLevelType())) {
                    LoanCompanyOrder companyOrder = new LoanCompanyOrder();
                    companyOrder.setOrderId(orderId);
                    companyOrder.setCompanyId(collectors.getUserGroupId());
                    companyOrder.setCreateUser(userName);
                    companyOrder.setCreateDate(new Date());
                    companyOrderMapper.insert(companyOrder);
                }
            } catch (Exception e) {
                logger.info("查询创建人失败。。。。。。。。");
            }

            logger.info("开始调dubbo");

            SettlementResult response = null;
            try {
                SettleDto settleDto= new SettleDto();
                settleDto.setAmount(new BigDecimal(order.getActAmount()));
                settleDto.setBid(order.getId());
                settleDto.setBorrowid(borrowList.getId());
                settleDto.setType((short)0);

//                if(borrowList.getOverdueDays() > 7){
//                    //逾期大于七天提前结清
//                    settleDto.setType((short)1);
//                }
//                // 线下还款，还款金额大于剩余还款金额，则提前结清
//                if ("7".equals(type) && Float.valueOf(reduce).compareTo(borrowList.getSurplusAmount()) > 0){
//                    settleDto.setType((short)1);
//                }

                response = rmiSettlementService.settle(settleDto);
            } catch (Exception e) {
                logger.error("调用清结算发生异常：" + e.getMessage());
            }

            BorrowList temp = new BorrowList();
            temp.setId(borrowList.getId());
            temp.setCurrentRepayTime(order.getCreationDate());
            borrowListMapper.updateByPrimaryKeySelective(temp);

            if (response == null) {
                logger.info("dubbo调用失败");
                result.setCode(Result.FAIL);
                result.setMessage("清结算调用失败");

                order.setRlState("f");
                order.setReason(result.getMessage());
                orderMapper.updateByPrimaryKeySelective(order);
                return result;
            }

            if (response.getCode() != Result.SUCCESS) {
                logger.info("dubbo调用失败");
                result.setCode(Result.FAIL);
                result.setMessage("清结算处理失败");

                order.setRlState("f");
                order.setReason(result.getMessage());
                orderMapper.updateByPrimaryKeySelective(order);
                return result;
            }
            logger.info("dubbo调用成功");
            //成功更新order状态
            order.setRlState("s");
            orderMapper.updateByPrimaryKeySelective(order);

            if (Constants.OrderType.MITIGATE_PUNISHMENT.equals(type)) {
                BorrowList currentBorrowList = borrowListMapper.selectByPrimaryKey(temp);
                temp = new BorrowList();
                temp.setId(borrowList.getId());
                temp.setActReduceAmount(currentBorrowList.getActReduceAmount() + Float.parseFloat(reduce));
                borrowListMapper.updateByPrimaryKeySelective(temp);
            }

            if (response.getModel() != null) {
                logger.info("清结算返回结果:" + JSON.toJSONString(response));
                String borrStatus = JSON.parseObject(response.getModel()).getString("borrStatus");
                if (CodeReturn.STATUS_PAY_BACK.equals(borrStatus) || CodeReturn.STATUS_DELAY_PAYBACK.equals(borrStatus) || CodeReturn.STATUS_EARLY_PAYBACK.equals(borrStatus)) {
                    collectorsListService.saveCompletionStatus(borrowList.getId());
                }
            }

            result.setCode(Result.SUCCESS);
            result.setMessage("操作成功");
            return result;
        } else {
            logger.info("订单生成失败");
            result.setCode(Result.FAIL);
            result.setMessage("订单生成失败");
            return result;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void doTransferLoan(
            List<CollectorsList> updateCollectorsList,
            List<CollectorsList> insertCollectorsList,
            List<CollectorsRecord> collectorsRecords,
            List<LoanCompanyBorrow> updateCompanyBorrow,
            List<LoanCompanyBorrow> insertCompanyBorrow) throws Exception {
        int i = 0, j = 0, k = 0, m = 0, n = 0;
        if (updateCollectorsList != null && updateCollectorsList.size() > 0) {
            i = collectorsListMapper.batchUpdateCollectorsList(updateCollectorsList);

            logger.info("催收记录更新条数:{},实际更新条数:{}", updateCollectorsList.size(), i);
            if (i != updateCollectorsList.size()) {//有数据更新失败,所有数据回滚
                throw new RuntimeException("催收记录更新失败,需要更新条数：" + updateCollectorsList.size() + ",实际更新条数:" + i);
            }
        }

        if (insertCollectorsList != null && insertCollectorsList.size() > 0) {
            j = collectorsListMapper.batchInsertCollectorsList(insertCollectorsList);
            logger.info("催收记录插入条数:{},实际更新条数:{}", insertCollectorsList.size(), j);
            if (j != insertCollectorsList.size()) {//有数据插入失败,所有数据回滚
                throw new RuntimeException("催收记录插入失败,需要插入条数：" + insertCollectorsList.size() + ",实际插入条数:" + j);
            }
        }

        if (collectorsRecords != null && collectorsRecords.size() > 0) {
            k = collectorsRecordMapper.batchInsertCollectorsRecord(collectorsRecords);
            logger.info("转件记录插入条数:{},实际插入条数:{}", collectorsRecords.size(), k);
        }

        if (updateCompanyBorrow != null && updateCompanyBorrow.size() > 0) {
            m = companyBorrowMapper.batchUpdate(updateCompanyBorrow);
            logger.info("公司催收记录更新条数:{},实际更新条数:{}", updateCompanyBorrow.size(), m);
            if (m != updateCompanyBorrow.size()) {
                throw new RuntimeException("公司催收记录更新失败,需要更新条数：" + updateCompanyBorrow.size() + ",实际更新条数:" + j);
            }
        }

        if (insertCompanyBorrow != null && insertCompanyBorrow.size() > 0) {
            n = companyBorrowMapper.batchInsert(insertCompanyBorrow);
            logger.info("公司催收记录插入条数:{},实际插入条数:{}", insertCompanyBorrow.size(), n);
            if (n != insertCompanyBorrow.size()) {
                throw new RuntimeException("公司催收记录插入失败,需要插入条数：" + insertCompanyBorrow.size() + ",实际插入条数:" + j);
            }
        }
    }
}
