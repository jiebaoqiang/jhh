package com.jhh.dc.baika.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.jhh.dc.baika.entity.manager.BankBan;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.mapper.*;
import com.jhh.dc.baika.manage.service.user.UserService;
import com.jhh.dc.baika.manage.utils.Assertion;
import com.jhh.dc.baika.manage.utils.Detect;
import com.jhh.dc.baika.manage.utils.PostAsync;
import com.jhh.dc.baika.api.channel.AgentChannelService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.capital.AgentPayRequest;
import com.jhh.dc.baika.common.util.HttpUtils;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.entity.app.BankVo;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.common.Constants;
import com.jhh.dc.baika.entity.common.ResponseCode;
import com.jhh.dc.baika.entity.enums.BorrowStatusEnum;
import com.jhh.dc.baika.entity.manager.Review;
import com.jhh.dc.baika.manage.service.risk.ReviewService;
import com.jhh.dc.baika.manage.utils.PostListAsync;
import com.jhh.pay.driver.pojo.BankBinVo;
import com.jhh.pay.driver.pojo.BankInfo;
import com.jhh.pay.driver.pojo.QueryResponse;
import com.jhh.pay.driver.service.TradeService;
import com.jinhuhang.risk.dto.QueryResultDto;
import com.jinhuhang.risk.service.impl.blacklist.BlacklistAPIClient;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
@Log4j
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private AgentChannelService agentChannelService;

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private BankInfoMapper bankInfoMapper;
    @Autowired
    private BankBanMapper bankBanMapper;
    @Autowired
    private BlacklistAPIClient riskClient;

    @Value("${A.synchrodata.borrowUrl}")
    private String aSynchrodataBorrowUrl;

    @Value("${A.synchrodata.borrowListUrl}")
    private String aSynchrodataBorrowListUrl;

    public String getaSynchrodataBorrowListUrl() {
        return aSynchrodataBorrowListUrl;
    }

    public void setaSynchrodataBorrowListUrl(String aSynchrodataBorrowListUrl) {
        this.aSynchrodataBorrowListUrl = aSynchrodataBorrowListUrl;
    }

    @Value("${refund.service.fee}")
    private String refundServiceFeeUrl;

    /**
     * 操作审核结果
     * @param needState    符合的状态
     * @param saveStatus   修改的状态
     * @param bl            需要修改合同
     * @param reason       修改原因
     * @param userNum      修改人
     * @param errorrMessage      异常信息
     * @return
     */
    private Response operationReviewResult(String needState, String saveStatus, BorrowList bl, String reason, String userNum, String errorrMessage){
        if(bl != null && saveStatus != null){
            //是否满足合同修改状态
            if(needState.equals(bl.getBorrStatus())){
                //更新合同状态
                bl.setBorrStatus(saveStatus);
                borrowListMapper.updateByPrimaryKeySelective(bl);
                //更新审核结果
                return saveReview(bl.getId(), reason, userNum);
            }
        }
        return new Response().code(ResponseCode.FIAL).msg(errorrMessage);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = java.lang.Exception.class)
    public Response saveManuallyReview(Integer borroId, String reason, String userNum, Integer operationType) throws Exception {
        Assertion.isPositive(borroId, "合同Id不能为空");
        Assertion.isPositive(operationType, "操作类型不能为空");
        Response response = new Response().code(ResponseCode.FIAL).msg("操作失败");

        BorrowList bl = borrowListMapper.selectByPrimaryKey(borroId);
        if (bl != null) {
            if (Constants.OperationType.MANUALLY_PASS.equals(operationType)) {

                response = operationReviewResult(BorrowStatusEnum.SIGNED.getCode(), BorrowStatusEnum.WAIT_PAY.getCode(),
                        bl, reason, userNum,"已签约状态的合同才能审核通过");

                //同步A平台状态
                AsyncExecutor.execute(new PostAsync<>(bl, aSynchrodataBorrowUrl));

            } else if (Constants.OperationType.MANUALLY_REJECT.equals(operationType)) {

                response = operationReviewResult(BorrowStatusEnum.SIGNED.getCode(), BorrowStatusEnum.REJECT_AUTO_AUDIT.getCode(),
                        bl, reason, userNum,"已签约状态的合同才能拒绝");

                //同步A平台状态
                AsyncExecutor.execute(new PostAsync<>(bl, aSynchrodataBorrowUrl));
            }else if (Constants.OperationType.BLACK_REJECT.equals(operationType)) {

                response = operationReviewResult(BorrowStatusEnum.SIGNED.getCode(), BorrowStatusEnum.REJECT_AUTO_AUDIT.getCode(),
                        bl, reason, userNum,"已签约状态的合同才能拒绝并拉黑");

                //拉黑接口
                response = userService.userBlockWhite(bl.getPerId(), userNum, "",
                        reason, Constants.UserBlockWhite.BLACK);
                //同步A平台状态
                AsyncExecutor.execute(new PostAsync<>(bl, aSynchrodataBorrowUrl));
            } else if (Constants.OperationType.WHITE.equals(operationType)) {
                //洗白接口
                response = userService.userBlockWhite(bl.getPerId(), userNum, "",
                        reason, Constants.UserBlockWhite.WHITE);
            }else if (Constants.OperationType.WHITE.equals(operationType)) {
                //拉黑接口
                response = userService.userBlockWhite(bl.getPerId(), userNum, "",
                        reason, Constants.UserBlockWhite.BLACK);

            }
        }
        return response;
    }

    @Override
    public Response saveReview(Integer borroId, String reason, String employNum) {
        Assertion.isPositive(borroId, "合同Id不能为空");
        Assertion.notEmpty(employNum, "操作人不能为空");
        Response response = new Response().code(ResponseCode.FIAL).msg("操作失败");

        Review review = new Review();
        review.setReviewType(Constants.ReviewType.MANUALLY_REVIEW);
        review.setBorrId(borroId);
        review = reviewMapper.selectOne(review);

        if (review != null) {
            if (review.getEmployNum().equals(employNum)) {
                //如果为同一个人操作直接更新
                review.setReason(reason);
                reviewMapper.updateByPrimaryKeySelective(review);
            } else {
                //不同人，更新历史,在插入审核记录
                review.setReviewType(Constants.ReviewType.MANUALLY_REVIEW_HISTORY);
                reviewMapper.updateByPrimaryKeySelective(review);

                review.setId(null);
                review.setReviewType(Constants.ReviewType.MANUALLY_REVIEW);
                review.setReason(reason);
                review.setEmployNum(employNum);
                review.setCreateDate(Calendar.getInstance().getTime());
                reviewMapper.insertSelective(review);
            }
            response.code(ResponseCode.SUCCESS).msg("操作成功");
        } else {
            //没分过单的合同直接入库
            review = new Review();
            review.setBorrId(borroId);
            review.setReviewType(Constants.ReviewType.MANUALLY_REVIEW);
            review.setReason(reason);
            review.setEmployNum(employNum);
            review.setCreateDate(Calendar.getInstance().getTime());
            reviewMapper.insertSelective(review);
            response.code(ResponseCode.SUCCESS).msg("操作成功");
        }
        return response;
    }

    @Override
    public Response cancel(String brroIds, String reason, String userNum) {
        Response response = new Response().code(ResponseCode.FIAL).msg("操作失败");

        if (Detect.notEmpty(brroIds) && Detect.notEmpty(brroIds)) {
            String[] ids = brroIds.split(",");
            //查询出符合状态记录
            List status = new ArrayList();
            status.add(BorrowStatusEnum.SIGNED.getCode());
            status.add(BorrowStatusEnum.WAIT_PAY.getCode());
            status.add(BorrowStatusEnum.PAY_SUCESS.getCode());
            List cancelIds = borrowListMapper.selectIdsByStatus(Arrays.asList(ids), status);
            if(Detect.notEmpty(cancelIds)){
                //查询出已缴费记录
                status = new ArrayList();
                status.add(BorrowStatusEnum.PAY_SUCESS.getCode());
                List pushborrNums = borrowListMapper.selectBorrNumByStatus(Arrays.asList(ids), status);

                //更新成已取消
                Example example = new Example(BorrowList.class);
                example.createCriteria().andIn("id", cancelIds);
                BorrowList bl = new BorrowList();
                bl.setBorrStatus(BorrowStatusEnum.CANCEL.getCode());
                borrowListMapper.updateByExampleSelective(bl, example);

                //更新催收人
                Example reviewExample = new Example(Review.class);
                reviewExample.createCriteria().andIn("borrId",cancelIds);
                Review review = new Review();
                review.setEmployNum(userNum);
                review.setReason(reason);
                reviewMapper.updateByExampleSelective(review, reviewExample);

                // 通知A平台退还咨询费
                if(Detect.notEmpty(pushborrNums)){
                    refundServiceFee(pushborrNums, reason, userNum);
                }


                //同步A平台状态
                AsyncExecutor.execute(new PostListAsync<>(borrowListMapper.selectByExample(example), aSynchrodataBorrowListUrl));

                response.code(ResponseCode.SUCCESS).msg("操作成功");
            }else {
                response.code(ResponseCode.SUCCESS).msg("该状态不允许取消");
            }

        }
        return response;
    }

    public void refundServiceFee(List pushIds, String reason, String userNum){
        try {
            if(Detect.notEmpty(pushIds)){
                Map map = new HashMap();
                map.put("borrNum", pushIds);
                map.put("remark", reason);
                map.put("operator", userNum);
                HttpUtils.sendPost(refundServiceFeeUrl,HttpUtils.toParam(map));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response transfer(String brroIds, String userNum) {
        Response response = new Response().code(ResponseCode.FIAL).msg("操作失败");

        if (Detect.notEmpty(brroIds) && Detect.notEmpty(brroIds)) {
            String[] ids = brroIds.split(",");
            List status = new ArrayList();
            status.add(BorrowStatusEnum.SIGNED.getCode());
            status.add(BorrowStatusEnum.PAY_SUCESS.getCode());
            //只有已签约和已缴费能转件
            List<Integer> transferIds = borrowListMapper.selectIdsByStatus(Arrays.asList(ids), status);
            for (Integer id : transferIds) {
                saveReview(id, "", userNum);
            }
            response.code(ResponseCode.SUCCESS).msg("操作成功");
        }
        return response;
    }

    @Override
    public Response pay(Integer borrId, String userNum, String payChannel) {
        Assertion.isPositive(borrId, "合同号不能为空");
        Assertion.notEmpty(userNum, "审核人不能为空");
        Response response = new Response().code(ResponseCode.FIAL).msg("放款失败");

        BorrowList bl = borrowListMapper.selectByPrimaryKey(borrId);
        Assertion.notNull(bl, "合同不存在");


        try {
            // 判断该用户是否是黑名单用户，如果是黑名单用户，提示“该用户是黑名单用户”，把该用户的状态改成“电审未通过”
            Map<String,String> customerInfo = personMapper.getCardNumAndPhoneByBorrId(borrId);
            QueryResultDto queryResultDto = riskClient.blacklistSingleQuery(customerInfo.get("cardNum"),customerInfo.get("phone"));
            if("0".equals(queryResultDto.getCode())){
                response.setMsg("该用户为黑名单用户, 放款失败");
                borrowListMapper.updateStatusById(borrId, BorrowStatusEnum.REJECT_AUTO_AUDIT.getCode());
                return response;
            }
        } catch (Exception e) {
            log.error("调用黑名单接口失败:" + ExceptionUtils.getFullStackTrace(e));
        }

        //合同状态为已缴费和失败的可以放款
        if (bl.getBorrStatus().equals(BorrowStatusEnum.PAY_SUCESS.getCode()) ||
                bl.getBorrStatus().equals(BorrowStatusEnum.LOAN_FAIL.getCode())) {
            AgentPayRequest request = new AgentPayRequest(bl.getPerId(), bl.getId() + "", 1, payChannel);
            request.setProdType(bl.getProdType());
//            //判断该用户银行卡是否可用
//            BankVo bankVo = bankInfoMapper.selectMainBankByUserId(bl.getPerId());
//            BankInfo bankInfo = assemblingParam(bankVo);
//            QueryResponse<BankBinVo> bankBin = tradeService.getBankBin(bankInfo);
//            log.info("查询用户银行卡卡bin返回结果 bankBin = \n"+bankBin);
//            if (bankBin != null && "SUCCESS".equals(bankBin.getCode())) {
//                Example queryExample = new Example(BankBan.class);
//                Example.Criteria criteria = queryExample.createCriteria();
//                criteria.andEqualTo("bankCode", bankBin.getData().getBankCode());
//                criteria.andIn("type", Arrays.asList("1", "3"));
//                List<BankBan> banBan = bankBanMapper.selectByExample(queryExample);
//                if (banBan != null && banBan.size() > 0) {
//                    response.setCode(201);
//                    response.setMsg(bankBin.getData().getBankName()+"不允许放款，请提示客户换卡");
//                    return response;
//                }
//            } else {
//                response.setCode(201);
//                response.setMsg(bankBin == null ? "验证银行卡失败" : bankBin.getMsg());
//                return response;
//            }
//
            //判断用户是否绑卡
            Response bindBank = userService.getValidBankList(bl.getPerId());
            if(bindBank != null && bindBank.getData() != null){
                boolean isBindBank = false;
                for(Object bank: (List) bindBank.getData()){
                    //主卡是否绑定过快捷支付
                    if(((Map)bank).get("status").equals("主卡") && Detect.notEmpty(((Map)bank).get("quickBinding") + "")){
                        isBindBank = true;
                    }
                }

                if(!isBindBank){
                    response.setCode(201);
                    response.setMsg("该用户未绑定任何银行卡，不能放款！");
                    return response;
                }
            }

            ResponseDo<?> result = agentChannelService.pay(request);
            if (result != null) {
                response.setCode(result.getCode());
                response.setMsg(result.getInfo());
            }
        } else {
            response.msg("系统异常，合同状态不符，请刷新页面！");
        }
        return response;
    }

    private BankInfo assemblingParam(BankVo bankVo) {
        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankCard(bankVo.getBankNum());
        return bankInfo;
    }

    public String getaSynchrodataBorrowUrl() {
        return aSynchrodataBorrowUrl;
    }

    public void setaSynchrodataBorrowUrl(String aSynchrodataBorrowUrl) {
        this.aSynchrodataBorrowUrl = aSynchrodataBorrowUrl;
    }

    public String getRefundServiceFeeUrl() {
        return refundServiceFeeUrl;
    }

    public void setRefundServiceFeeUrl(String refundServiceFeeUrl) {
        this.refundServiceFeeUrl = refundServiceFeeUrl;
    }
}