package com.jhh.dc.baika.app.web.logic;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.app.web.exception.ExceptionJsonController;
import com.jhh.dc.baika.api.app.LoanService;
import com.jhh.dc.baika.api.entity.BindCardVo;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.loan.BankService;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.pay.driver.pojo.BankBaseInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 2018/7/10.
 */
@RestController
@RequestMapping("/bindCard")
@Slf4j
public class BindCardController extends ExceptionJsonController {

    @Reference
    private BankService bankService;

    @Reference
    private LoanService loanService;

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 获取银行列表
     *
     * @return
     */
    @ApiOperation(value = "银行列表", notes = "获取支持银行列表")
    @GetMapping("/getBankList")
    public ResponseDo<BankBaseInfo[]> getBankList() {
        return bankService.getBankList();
    }

    /**
     * 银行卡认证
     */
    @ApiOperation(value = "银行卡认证", hidden = true)
    @RequestMapping("/insertBankInfo")
    public ResponseDo<?> insertBankInfo(@Valid BindCardVo vo, BindingResult br) {
        log.info("银行卡认证请求信息 bandCardVo = "+vo);
        if (br.hasErrors()) {
            List<BindingResult> bindingResults = new ArrayList<>();
            bindingResults.add(br);
            Map<String, String> map = getResult(bindingResults);
            log.info("-------------有参数为空" + map);
            return ResponseDo.newFailedDo("参数不能为空" + map.toString());
        }
        ResponseDo<?> result = new ResponseDo(201, "参数错误");

        // 判断验证码是否正确
        String valiCodeKey = RedisConst.VALIDATE_CODE + RedisConst.SEPARATOR + vo.getPhone();
        String valiCode = jedisCluster.get(valiCodeKey);
        if (StringUtils.isEmpty(valiCode)) {
            result.setInfo("请先获取验证码!");
            return result;
        }
        if (!RedisConst.HELI_PAY_MSG_ORDER_NUM.equals(valiCode) && !vo.getValidateCode().equals(valiCode)) {
            result.setInfo("验证码不正确!");
            return result;
        }
        //查看是否存在快捷支付验证码渠道
        String valiCodeChannelKey = RedisConst.VALIDATE_CODE_CHANNEL + RedisConst.SEPARATOR + vo.getPhone();
        valiCodeChannelKey = jedisCluster.get(valiCodeChannelKey);
        String[] valiCodeChannel = StringUtils.isNotEmpty(valiCodeChannelKey)?valiCodeChannelKey.split(","):null;
        if (valiCodeChannel !=null && valiCodeChannel.length > 0) {
            vo.setMsgChannel(valiCodeChannel[0]);
            vo.setOrderNo(valiCodeChannel[1]);
        }
        vo.setStatus("1");
        ResponseDo<?> bindCard = bankService.bindQuickPayDuringBind(vo);
        if (200 == bindCard.getCode()) {
            //将用户合同状态改为待签约
           return loanService.updateBorrowStatus(vo.getPer_id(), CodeReturn.STATUS_WAIT_SIGN);
        } else {
            return bindCard;
        }
    }

}
