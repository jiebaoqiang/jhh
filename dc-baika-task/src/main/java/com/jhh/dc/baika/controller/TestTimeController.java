package com.jhh.dc.baika.controller;

import com.jhh.dc.baika.api.app.LoanService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.depository.DepositoryCallbackService;
import com.jhh.dc.baika.dao.LoanOrderDOMapper;
import com.jhh.dc.baika.model.LoanOrderDO;
import com.jhh.dc.baika.service.*;
import com.jhh.dc.baika.task.AgentDeductQueryTask;
import com.jhh.dc.baika.task.QianQiBankDepositTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by wanzezhong on 2018/1/25.
 */
@Controller
public class TestTimeController {

    @Autowired
    TimerService timerService;
    @Autowired
    BorrListService borrListService;
    @Autowired
    RobotService robotService;
    @Autowired
    FinanceService financeService;
    @Autowired
    private CommissionSummaryService commissionSummaryService;
    @Autowired
    private BlackService blackService;

    @Autowired
    private LoanOrderDOMapper loanOrderDOMapper;

    @Autowired
    private DepositoryCallbackService depositoryCallbackService;

    @Autowired
    private AgentDeductQueryTask task;

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserNoticeService userNoticeService;

    @Autowired
    private QianQiBankDepositTask qianQiBankDepositTask;

    @RequestMapping("/time/smsAlert")
    @ResponseBody
    public void smsAlert() {
        timerService.smsAlert();
    }

    @RequestMapping("/time/sendFinancePayData")
    @ResponseBody
    public void sendMoneyManagement() {
        financeService.sendPayData();
    }


    @RequestMapping("/time/rejectAudit")
    @ResponseBody
    public void rejectAudit() {
        borrListService.rejectAudit();
    }

    @RequestMapping("/time/submenuTransfer")
    @ResponseBody
    public void submenuTransfer() {
        borrListService.submenuTransfer();
    }

    @RequestMapping("/time/smsOverdue")
    @ResponseBody
    public void smsOverdue() {
        timerService.smsOverdue();
    }

    //更新逾期三天分给杨艳
    @RequestMapping("/time/updateOverdueThree")
    @ResponseBody
    public void updateOverdueThree() {
        borrListService.updateOverdueThree();
    }

    //更新逾期两天分给杨艳
    @RequestMapping("/time/updateOverdueTow")
    @ResponseBody
    public void updateOverdueTow() {
        borrListService.updateOverdueTwo();
    }

    @RequestMapping("/time/sendFinanceRepayData")
    @ResponseBody
    public void sendDataToFinance() {
        financeService.sendRepayData();
    }

    @RequestMapping("/time/batchWithhold")
    @ResponseBody
    public void batchWithhold() {
        borrListService.batchWithhold();
    }

    @RequestMapping("/time/sendDataToBaikelu")
    @ResponseBody
    public void sendDataToBaikelu() {
        robotService.sendDataToBaikelu();
    }


    @RequestMapping("/time/sendDataToBaikeluFirstAudit")
    @ResponseBody
    public void sendDataToBaikeluFirstAudit() {
        borrListService.rcCallPhone();
    }


    //发送昨天催款人催收数据九点
    @RequestMapping("/time/sendCollectorsDataToFinanceNineOclock")
    @ResponseBody
    public void sendCollectorsDataToFinanceNineOclock() {
        financeService.sendCollectorsDataToFinanceNineOclock();
    }

    @RequestMapping("/time/commissionSummary")
    @ResponseBody
    public String commissionSummary() {
        return "执行完成，共更新【" + commissionSummaryService.doBussiness() + "】条记录!";
    }

    //发送今天催款人催收数据五点
    @RequestMapping("/time/sendCollectorsDataToFinanceFiveOclock")
    @ResponseBody
    public void sendCollectorsDataToFinanceFiveOclock() {
        financeService.sendCollectorsDataToFinanceFiveOclock();
    }


    @RequestMapping("/time/batchQueryResult")
    @ResponseBody
    public String batchQueryResult() {
        borrListService.batchQueryResult();
        return "查询预期未还合同状态成功";
    }


    @RequestMapping("/time/black")
    @ResponseBody
    public void blackOverdays() {
        blackService.blackOverdays();
    }

    @RequestMapping("/batchState")
    @ResponseBody
    public String batchState() throws Exception {
        task.batchDeduct();
        return "查询预期未还合同状态成功";
    }
      @RequestMapping("/time/batchNoLogin")
    @ResponseBody
    public String batchNoLogin() throws Exception {
        userNoticeService.loginNotice();
        return "未登录用户百可录打电话跑批成功";
    }

    @RequestMapping("/time/batchNoRegister")
    @ResponseBody
    public String batchNoRegister() throws Exception {
        userNoticeService.registerNotice();
        return "未注册用户百可录打电话跑批成功";
    }

    @RequestMapping("/synchBorrowStatusTask")
    @ResponseBody
    public String SynchBorrowStatusTask() throws Exception {
        loanService.synchBorrowStatusOverdue();
        return "未注册用户百可录打电话跑批成功";
    }

    @RequestMapping("/queryAccountAuthAll")
    @ResponseBody
    public String queryAccountAuthAll() throws Exception {
        qianQiBankDepositTask.task();
        return "哈哈哈";
    }
    @RequestMapping("/depositoryRecharge")
    @ResponseBody
    public void depositoryRecharge(){
        List<LoanOrderDO> loanOrderDOList = loanOrderDOMapper.selectProcessOrderByType(Constants.payOrderType.DEPOSITORY_RECHARGE_TYPE);
        if (loanOrderDOList !=null && loanOrderDOList.size()> 0){
            loanOrderDOList.forEach(v ->  depositoryCallbackService.searchRechargeOrder(v.getSerialNo()));

        }
    }
    @RequestMapping("/depositoryRepay")
    @ResponseBody
    public void depositoryRepay(){
        List<LoanOrderDO> loanOrderDOList = loanOrderDOMapper.selectProcessOrderByType(Constants.payOrderType.DEPOSITORY_REPAY_TYPE);
        if (loanOrderDOList !=null && loanOrderDOList.size()> 0){
            loanOrderDOList.forEach(v ->  depositoryCallbackService.searchRepayOrder(v.getSerialNo()));

        }
    }

    @RequestMapping("/time/depositoryWithdrawal")
    @ResponseBody
    public String depositoryWithdrawal(){
        List<LoanOrderDO> loanOrderDOList = loanOrderDOMapper.selectOrderByType(Constants.payOrderType.DEPOSITORY_WITHDRAWAL_TYPE);
        if (loanOrderDOList !=null && loanOrderDOList.size()> 0){
            loanOrderDOList.forEach(v ->  depositoryCallbackService.searchWithdrawalOrder(v.getSerialNo()));

        }
        return "提现结果主动查询成功";
    }
    @RequestMapping("/depositoryOverdue")
    @ResponseBody
    public String depositoryOverdue(){
        depositoryCallbackService.depositoryOverdue();
        return "成功";
    }
}
