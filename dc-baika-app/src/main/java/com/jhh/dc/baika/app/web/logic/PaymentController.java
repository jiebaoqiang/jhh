package com.jhh.dc.baika.app.web.logic;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.app.web.exception.ExceptionJsonController;
import com.jhh.dc.baika.api.app.LoanService;
import com.jhh.dc.baika.api.channel.AgentChannelService;
import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.capital.AgentDeductRequest;
import com.jhh.dc.baika.api.loan.BankService;
import com.jhh.dc.baika.app.common.exception.CommonException;
import com.jhh.dc.baika.common.constant.PayCenterChannelConstant;
import com.jhh.dc.baika.common.util.RedisConst;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2018/7/24.
 */
@RestController
@RequestMapping("/trade")
@Slf4j
public class PaymentController extends ExceptionJsonController {

    @Autowired
    private JedisCluster jedisCluster;

    @Reference
    private LoanService loanService;

    @Reference
    private BankService bankService;

    @Reference
    private AgentChannelService agentChannelService;

    @ApiOperation(value = "代扣接口", notes = "代扣接口")
    @PostMapping(value = "/agentDeduct")
    @ApiParam(value = "body", name = "代扣请求体")
    public ResponseDo<?> doAgentDeduct(@Validated AgentDeductRequest request, BindingResult result, Map<String,Object> map) throws CommonException {
        log.info("\n=========== 代扣接口 come in ===========");
        log.info("\n前端请求数据:{}", request.toString());
        if (result.hasErrors()) {
            List<BindingResult> bindingResults = new ArrayList<>();
            bindingResults.add(result);
           return ResponseDo.newFailedDo(String.format(StateCode.PARAM_EMPTY_MSG, getResult(bindingResults)));
        }
         return doBusiness(request);

    }

    private ResponseDo<?> doBusiness(Object object) {
        log.info("\n=========== 代扣接口 doBusiness ===========");
        AgentDeductRequest request = (AgentDeductRequest) object;
        if (!PayCenterChannelConstant.PAY_CHANNEL_ZFB.equals(request.getPayChannel())) {
            // 判断验证码是否正确
            String valiCodeKey = RedisConst.VALIDATE_CODE + RedisConst.SEPARATOR + request.getPhone();
            String valiCode = jedisCluster.get(valiCodeKey);
            log.info("redis kye:" + valiCodeKey + ",redis中获取本次验证码为:" + valiCode);
            if (StringUtils.isEmpty(request.getValidateCode())) {
                return ResponseDo.newFailedDo("验证码不能为空!");
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(valiCode)) {
                return ResponseDo.newFailedDo("请先获取验证码!");
            }
            if (!RedisConst.HELI_PAY_MSG_ORDER_NUM.equals(valiCode) && !request.getValidateCode().equals(valiCode)) {
                return ResponseDo.newFailedDo("验证码不正确!");
            }
        }
        // 根据borrId查询对应的催收人信息
        String userSysno = loanService.getCollectionUser(Integer.parseInt(request.getBorrId()));
        if (StringUtils.isNotEmpty(userSysno)) {
            request.setCollectionUser(userSysno);
        }
        // 按需绑定快捷支付
        ResponseDo<String> noteResult = bankService.bindQuickPayDuringPay(request);

        if (noteResult.getData() != null) {
            String payChannel = noteResult.getData();
            request.setPayChannel(payChannel);
        }
        return agentChannelService.deduct(request);
    }
}
