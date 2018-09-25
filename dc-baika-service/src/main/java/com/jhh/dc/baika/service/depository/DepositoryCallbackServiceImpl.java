package com.jhh.dc.baika.service.depository;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositBackService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.depository.DepositoryCallbackService;
import com.jhh.dc.baika.api.depository.DepositoryPayService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.trade.DepositoryTrade;
import com.jhh.dc.baika.common.enums.PayTriggerStyleEnum;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.constant.Constant;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.app_vo.PushPersonVo;
import com.jhh.dc.baika.entity.app_vo.RabbitMessage;
import com.jhh.dc.baika.entity.bankdeposit.QianQiBack;
import com.jhh.dc.baika.entity.bankdeposit.YoutuQuery;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import com.jhh.dc.baika.mapper.app.PersonMapper;
import com.jhh.dc.baika.mapper.gen.LoanOrderDOMapper;
import com.jhh.dc.baika.mapper.gen.domain.LoanOrderDO;
import com.jhh.dc.baika.service.capital.SettlementUtil;
import com.jhh.dc.baika.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2018/9/10.
 */
@Service
@Slf4j
public class DepositoryCallbackServiceImpl implements DepositoryCallbackService {

    @Autowired
    private QianQiDepositBackService qianQiDepositBackService;

    @Autowired
    private DepositoryPayService depositoryPayService;

    @Autowired
    private LoanOrderDOMapper loanOrderDOMapper;

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private SettlementUtil settlementUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseDo<?> callbackRecharge(QianQiBack qianQiBack) {
        log.info("充值回调接收参数 qianQiBack = {}",qianQiBack);
        LoanOrderDO loanOrderDO = loanOrderDOMapper.selectBySerNo(qianQiBack.getBaikaOrderNo());
        if (loanOrderDO != null) {
            if (CodeReturn.SUCCESS_CODE.equals(qianQiBack.getStatus())) {
                //充值成功 修改订单
                updateOrder(loanOrderDO.getId(), "s", qianQiBack.getMessage());
                //调用存管还款
                DepositoryTrade trade = setDepositoryTrade(loanOrderDO);
                depositoryPayService.depositoryRepay(trade);
            } else {
                //充值失败
                updateOrder(loanOrderDO.getId(), "f", qianQiBack.getMessage());
            }
        }else {
            log.error("--------------------------该笔订单号不存在");
            ResponseDo.newFailedDo("订单号不存在");
        }
        return ResponseDo.newSuccessDo();
    }

    @Override
    public ResponseDo<?> callbackWithdrawal(QianQiBack qianQiBack) {
        log.info("提现回调接收参数 qianQiBack = {}",qianQiBack);
        LoanOrderDO loanOrderDO = loanOrderDOMapper.selectBySerNo(qianQiBack.getBaikaOrderNo());
        if (loanOrderDO != null) {
            if (CodeReturn.SUCCESS_CODE.equals(qianQiBack.getStatus())) {
                loanOrderDO.setSid(qianQiBack.getU2jinfuOrderNo());
                loanOrderDOMapper.updateByPrimaryKey(loanOrderDO);
            } else {
                //提现
                updateOrder(loanOrderDO.getId(), "f", qianQiBack.getMessage());
                loanOrderDOMapper.updateStatusByPId(loanOrderDO.getId(),"f",qianQiBack.getMessage());
            }
        }else {
            log.error("--------------------------该笔订单号不存在");
            ResponseDo.newFailedDo("订单号不存在");
        }
        return ResponseDo.newSuccessDo();
    }

    @Override
    public ResponseDo<?> searchRechargeOrder(String serialNo) {
        log.info("充值主动查询参数 serialNo" +serialNo);
        if (StringUtils.isNotEmpty(serialNo)){
            LoanOrderDO loanOrderDO = loanOrderDOMapper.selectBySerNo(serialNo);
            ResponseDo<YoutuQuery> queryTrade = qianQiDepositBackService.queryTrade(Constant.CG1045, serialNo);
            log.info("充值主动查询返回参数 queryTrade" +queryTrade);
            if (Constants.DeductQueryResponseConstants.SUCCESS_CODE == queryTrade.getCode()){
                //充值成功 修改订单
                updateOrder(loanOrderDO.getId(),"s","");
                //调用还款
                DepositoryTrade trade = setDepositoryTrade(loanOrderDO);
                depositoryPayService.depositoryRepay(trade);
            }else if (Constants.DeductQueryResponseConstants.SUCCESS_ORDER_SETTLE_FAIL == queryTrade.getCode()){
                //充值失败
                updateOrder(loanOrderDO.getId(),"f",queryTrade.getInfo());
            }
        }
        return ResponseDo.newSuccessDo();
    }

    @Override
    public ResponseDo<?> searchRepayOrder(String serialNo) {
        log.info("还款主动查询订单编号为serialNo = "+serialNo);
        if (StringUtils.isNotEmpty(serialNo)){
            LoanOrderDO loanOrderDO = loanOrderDOMapper.selectBySerNo(serialNo);
            ResponseDo<YoutuQuery> queryTrade = qianQiDepositBackService.queryTrade(Constant.CG1010, serialNo);
            log.info("还款主动查询返回参数 queryTrade" +queryTrade);
            BorrowList borrow =borrowListMapper.selectByPrimaryKey(loanOrderDO.getContractId());
            if (Constants.DeductQueryResponseConstants.SUCCESS_CODE == queryTrade.getCode()){
                //后台触发 修改合同
                if (loanOrderDO.getTriggerStyle().equals(PayTriggerStyleEnum.BACK_GROUND.getCode())){
                    borrow.setRepayRetryFlag(Constant.REPAY_RETRY_FLAG_SUCCESS);
                    borrowListMapper.updateByPrimaryKeySelective(borrow);
                }
                settlementUtil.deductSuccessAfter(loanOrderDO);
            }else if (CodeReturn.PROCESSING != queryTrade.getCode()){
                //还款失败
                settlementUtil.deductFailAfter(loanOrderDO,queryTrade.getInfo());
                //修改合同
                borrow.setRepayRetryFlag(Constant.REPAY_RETRY_FLAG_FAIL);
                borrowListMapper.updateByPrimaryKeySelective(borrow);
            }
        }
        return ResponseDo.newSuccessDo();
    }

    @Override
    public ResponseDo<?> searchWithdrawalOrder(String serialNo) {
        if (StringUtils.isNotEmpty(serialNo)){
            LoanOrderDO loanOrderDO = loanOrderDOMapper.selectBySerNo(serialNo);
            ResponseDo<YoutuQuery> queryTrade = qianQiDepositBackService.queryTrade(Constant.CG1047, serialNo);
            log.info("提现主动查询返回参数 queryTrade" +queryTrade+",流水号: "+serialNo);
            LoanOrderDO feeOrder = loanOrderDOMapper.selectSubOrderByPid(loanOrderDO.getId());
            if (Constants.DeductQueryResponseConstants.SUCCESS_CODE == queryTrade.getCode()){
                log.info("提现成功流水号"+serialNo);
                loanOrderDOMapper.updateStatusById(loanOrderDO.getId(), "s", null);
                loanOrderDOMapper.updateStatusById(feeOrder.getId(), "s", null);
            }else if (CodeReturn.PROCESSING != queryTrade.getCode()){
                log.info("提现失败流水号"+serialNo);
                loanOrderDOMapper.updateStatusById(loanOrderDO.getId(), "f", null);
                loanOrderDOMapper.updateStatusById(feeOrder.getId(), "f", null);
            }
        }
        return ResponseDo.newSuccessDo();
    }

    @Override
    public void depositoryOverdue() {
        List<BorrowList> list = borrowListMapper.getBorrowByStatusYoutu();
        if (list != null && list.size() > 0) {
            for (BorrowList bl : list) {
                // TODO 推送用户信息
                RabbitMessage rabbitMessage = new RabbitMessage();
                rabbitMessage.setType(3); //1:注册推送 2：资产风控推送3:逾期
                Map<String, Object> map = new HashMap<>();
                map.put("borrowId", bl.getInvestBorrowId());
                map.put("period",bl.getTermNum());
                rabbitMessage.setData(map); //推送数据
                rabbitTemplate.convertAndSend("baikaDataQueue", JacksonUtil.objWriteStr(rabbitMessage, JsonSerialize.Inclusion.NON_EMPTY));

                log.info("推送用户信息:" + map);
            }
        }
    }


    /**
     *  修改订单状态
     * @param id
     * @param status
     * @param msg
     */
    private void updateOrder(Integer id,String status,String msg){
        loanOrderDOMapper.updateStatusById(id,status, msg);
    }

    /**
     *  组装还款参数
     * @param order 订单
     * @return
     */
    private DepositoryTrade setDepositoryTrade(LoanOrderDO order) {
        Person p = personMapper.selectByPrimaryKey(order.getPerId());
        DepositoryTrade trade = new DepositoryTrade();
        trade.setAmount(String.valueOf(order.getOptAmount()));
        trade.setBorrId(order.getContractId());
        trade.setBorrNum(order.getBorrNum());
        trade.setPerId(p.getId());
        trade.setPhone(p.getPhone());
        return trade;
    }

}
