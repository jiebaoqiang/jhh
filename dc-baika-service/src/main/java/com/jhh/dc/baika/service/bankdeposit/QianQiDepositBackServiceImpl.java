package com.jhh.dc.baika.service.bankdeposit;

import com.alibaba.dubbo.config.annotation.Service;
import com.jhh.dc.baika.api.YouTuApi;
import com.jhh.dc.baika.api.annotation.Api;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositBackService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.enums.NodeEnum;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.MapUtils;
import com.jhh.dc.baika.constant.Constant;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.bankdeposit.QianQiBack;
import com.jhh.dc.baika.entity.bankdeposit.RepaymentRequest;
import com.jhh.dc.baika.entity.bankdeposit.YoutuQuery;
import com.jhh.dc.baika.entity.bankdeposit.YoutuResult;
import com.jhh.dc.baika.mapper.app.PersonMapper;
import com.jhh.dc.baika.mapper.bankdeposit.BankDepositMapper;
import com.jhh.dc.baika.mapper.gen.LoanOrderDOMapper;
import com.jhh.dc.baika.mapper.gen.domain.LoanOrderDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QianQiDepositBackServiceImpl implements QianQiDepositBackService{

    private static final String OK = "OK";
    private static final String NO = "NO";


    /**
     *
     开户绑卡CG1044
     客户密码修改CG1048
     回调：custNo
     支付密码重置CG1055
     充值CG1045
     T1提现CG1047
     授权CG1050
     *  1.开户状态
     *
     *  2.授权状态
     *
     *  3.转账
     *
     *  4.开户回调
     *
     *  5.授权回调
     *
     *  6.费率
     */

    @Autowired
    private BankDepositMapper mBankDepositMapper;

    @Autowired
    public LoanOrderDOMapper mLoanOrderDOMapper;

    @Autowired
    public PersonMapper personMapper;

    @Api
    YouTuApi mYouTuApi;

    @Override
    public ResponseDo<String> rate() {
        return null;
    }

    @Override
    public ResponseDo<YoutuQuery> repayment(RepaymentRequest repaymentRequest) {
        log.info("转账请求 borrowId:"+repaymentRequest.getBorrowId());
        try {
            Map<String,String> map = MapUtils.setConditionMap(repaymentRequest);
            Response<YoutuResult<YoutuQuery>> execute = mYouTuApi.repayment(map).execute();
            if(execute.isSuccessful()){
                YoutuResult<YoutuQuery> body = execute.body();

                if(CodeReturn.SUCCESS_CODE.equals(body.getRet())){
                    log.info("转账请求 请求成功"+body.getData());
                    return ResponseDo.newSuccessDo(body.getData());
                }else{
                    return ResponseDo.newFailedDo(body.getData());
                }

            }else {
                return ResponseDo.newFailedDo(Constants.DeductQueryResponseConstants.PROGRESSING,"转账请求失败");
            }
        } catch (IOException e) {
            log.info("转账请求 请求失败"+e.toString());
            e.printStackTrace();
        }
        return ResponseDo.newFailedDo(Constants.DeductQueryResponseConstants.PROGRESSING,"系统繁忙");
    }

    @Override
    public String accountBack(QianQiBack qianQiBack) {
        /**
         * 开户
         */
        log.info("修改用户开户状态 手机号:"+qianQiBack.getPhone());
        if(Constant.CG1044.equals(qianQiBack.getTradeCode())){
            int count = mBankDepositMapper.updateAccountState(qianQiBack.getAcctNo(),qianQiBack.getCustNo()
            ,qianQiBack.getPhone(),changeStatus(qianQiBack.getStatus()));

            if(count > 0){
                Person person = personMapper.getPersonByPhone(qianQiBack.getPhone());
                // 保存节点
                personMapper.updateApplyNodeByPerId(person.getId(), NodeEnum.UN_AUTH.getNode());

                log.info("修改用户开户状态成功");
                return OK;
            }
            log.info("修改用户开户状态失败");
        }

        return NO;
    }

    @Override
    public String authBack(QianQiBack qianQiBack) {
        /**
         * 授权
         */
        log.info("修改用户授权状态 用户号:"+qianQiBack.getCustNo()+"-"+qianQiBack.getPhone()+"  授权类型:"+qianQiBack.getAuthType());
        if(Constant.CG1050.equals(qianQiBack.getTradeCode()) && !StringUtils.isEmpty(qianQiBack.getCustNo())){

            int count = 0;

            if(Constant.AUTH_TYPE_REPAY.equals(qianQiBack.getAuthType())){
                log.info("修改用户还款转账授权状态成功");
                count = mBankDepositMapper.updateAuthRepayState(qianQiBack.getCustNo(),
                        changeStatus(qianQiBack.getStatus()));
            }else if(Constant.AUTH_TYPE_LOAN.equals(qianQiBack.getAuthType())){
                log.info("修改用户放款授权状态成功");
                count = mBankDepositMapper.updateAuthLoanState(qianQiBack.getCustNo(),
                        changeStatus(qianQiBack.getStatus()));
            }

            if(count > 0){
                Person person = personMapper.getPersonByCustNo(qianQiBack.getCustNo());
                if("s".equals(person.getIsLoanAuth()) && "s".equals(person.getIsRepayAuth())){
                    // 保存节点
                    personMapper.updateApplyNodeByPerId(person.getId(), NodeEnum.UN_QUICK_PAY.getNode());
                }
                log.info("修改用户授权状态成功");
                return OK;
            }
            log.info("修改用户授权状态失败");
        }

        return NO;
    }

    /**
     * 查询授权和开户状态
     */
    @Override
    public void queryAccountAuthAll() {
        //开户状态
        List<Person> accountPerson = findAllPerson(Constant.ACCOUNT);
        for (Person person : accountPerson) {
            accountBack(query(Constant.CG1044,person.getPhone(),null));
        }

        //授权 还款
        List<Person> authRepay = findAllPerson(Constant.AUTH_REPAY);
        for (Person person : authRepay) {
            authBack(query(Constant.CG1050,person.getPhone(),Constant.AUTH_TYPE_REPAY));
        }

        //授权 放款
        List<Person> authLoan = findAllPerson(Constant.AUTH_LOAN);
        for (Person person : authLoan) {
            authBack(query(Constant.CG1050,person.getPhone(),Constant.AUTH_TYPE_LOAN));
        }

    }


    /**
     * 查询订单状态
     */
    @Override
    public void queryTradeAll() {

        List<LoanOrderDO> p = mLoanOrderDOMapper.findAllPTrade("p");  //p处理的单子

        /**
         * 充值的单子
         */
        List<LoanOrderDO> rechange = p.stream()
                .filter(loanOrderDO -> loanOrderDO.getType().equals("19"))
                .collect(Collectors.toList());

        for (LoanOrderDO loanOrderDO : rechange) {

        }

        /**
         *  --------------------------------------------------------------------------------------
         */
        /**
         * 提现
         */
        List<LoanOrderDO> withdraw = p.stream()
                .filter(loanOrderDO -> loanOrderDO.getType().equals("21"))
                .collect(Collectors.toList());

        for (LoanOrderDO loanOrderDO : withdraw) {
            queryTrade(Constant.CG1047,loanOrderDO.getSerialNo());
        }
    }

    @Override
    public ResponseDo<YoutuQuery> queryTrade(String type,String bankOrderNo){

        log.info("主动查询订单状态:"+bankOrderNo);

        Map<String,String> map = new HashMap<>();
        map.put("tradeCode",type);
        map.put("baikaOrderNo",bankOrderNo);

        try {
            Response<YoutuResult<YoutuQuery>> execute = mYouTuApi.query(map).execute();
            if(execute.isSuccessful()){
                YoutuResult<YoutuQuery> body = execute.body();

                log.info("主动查询订单状态:"+body.getRet() + "-" +body.getMessage());

                if(CodeReturn.SUCCESS_CODE.equals(body.getRet())){

                    switch (body.getData().getStatus()){
                        case Constant.ACCOUNT_P:
                            return ResponseDo.newFailedDo(Constants.DeductQueryResponseConstants.PROGRESSING,body.getMessage());
                        case Constant.ACCOUNT_S:
                            return ResponseDo.newSuccessDo(body.getData());
                        case Constant.ACCOUNT_F:
                            return ResponseDo.newFailedDo(body.getMessage());
                    }
                }else {
                    return ResponseDo.newFailedDo(body.getMessage());
                }
            }else {
                log.info("主动查询订单状态:异常 请求存管失败");
                return ResponseDo.newFailedDo(Constants.DeductQueryResponseConstants.PROGRESSING,"请求存管失败");
            }
        }catch (Exception e){
            log.info("主动查询订单状态:异常 "+e.toString());
            e.printStackTrace();
        }
        log.info("主动查询订单状态:异常 "+"系统繁忙");
        return ResponseDo.newFailedDo(Constants.DeductQueryResponseConstants.PROGRESSING,"系统繁忙");
    }

    @Override
    public List<Person> findAllPerson(int code) {
        return mBankDepositMapper.findAllPPerson(code);
    }

    @Override
    public QianQiBack query(String type,String phone,String authType){
        QianQiBack qianQiBack = new QianQiBack();
        log.info("主动查询:"+type +" 授权类型:"+ authType + "手机号"+phone);
        Map<String,String> map = new HashMap<>();
        map.put("tradeCode",type);
        map.put("registerPhone",phone);
        if(authType != null){
            map.put("authType",authType);
        }

        try {
            Response<YoutuResult<YoutuQuery>> execute = mYouTuApi.query(map).execute();
            if(execute.isSuccessful()){
                YoutuResult<YoutuQuery> body = execute.body();

                log.info("主动查询:");

                if(CodeReturn.SUCCESS_CODE.equals(body.getRet())){
                    YoutuQuery data = body.getData();

                    if(type.equals(data.getTradeCode())){
                        qianQiBack.setPhone(phone);
                        qianQiBack.setCustNo(data.getCustNo());
                        qianQiBack.setTradeCode(data.getTradeCode());
                        qianQiBack.setAcctNo(data.getAcctNo());
                        qianQiBack.setStatus(data.getStatus());
                        qianQiBack.setAuthType(authType);
                        return qianQiBack;
                    }
                }
            }else{
                log.info("主动查询 请求存管失败 "+execute.message());
            }
        } catch (IOException e) {
            log.info("主动查询 异常 "+e.toString());
            e.printStackTrace();
        }

        return qianQiBack;
    }

    /**
     * 修改状态值
     * @param code
     * @return
     */
    public String changeStatus(String code){
        String status = null;

        if("200".equals(code) || Constant.ACCOUNT_S.equals(code)){
            status = "s";
        }else if("201".equals(code) || Constant.ACCOUNT_F.equals(code)){
            status = "f";
        }else if(Constant.ACCOUNT_P.equals(code)){
            status = "p";
        }

        return status;

    }
}
