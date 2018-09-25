package com.jhh.dc.baika.app.web.logic;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositWebService;
import com.jhh.dc.baika.app.common.constant.AccountValidatorUtil;
import com.jhh.dc.baika.app.common.util.IpUtil;
import com.jhh.dc.baika.app.web.exception.ExceptionJsonController;
import com.jhh.dc.baika.api.app.BQSService;
import com.jhh.dc.baika.api.app.RiskService;
import com.jhh.dc.baika.api.app.UserService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.sms.SmsService;
import com.jhh.dc.baika.app.common.constant.Admin;
import com.jhh.dc.baika.app.common.util.ValidateCode;
import com.jhh.dc.baika.common.enums.SmsTemplateEnum;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.entity.app.NoteResult;
import io.github.yedaxia.apidocs.ApiDoc;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * 2018/7/11.
 */
@RestController
@Slf4j
public class SmsController extends ExceptionJsonController {

    @Reference
    private RiskService riskService;

    @Reference
    private BQSService bqsService;

    @Reference
    private UserService userService;

    @Reference
    private SmsService smsService;

    @Reference
    private QianQiDepositWebService qianQiDepositWebService;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private IpUtil ipUtil;

    /**
     * 发送绑卡/付款验证码
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "绑卡短信验证码", hidden = true)
    @RequestMapping("sms/getPayCode")
    @ApiDoc(Admin.class)
    public NoteResult getPayCode(HttpServletRequest request) {
        NoteResult noteResult = new NoteResult();
        String phone = request.getParameter("phone");
        String perId = request.getParameter("perId");
        String event = request.getParameter("event");
        String device = request.getParameter("device");
        String bankNum = request.getParameter("bankNum");
        String smsType = request.getParameter("smsType");
        String tokenKey = request.getParameter("tokenKey");
        // 风控黑名单校验，拒绝业务在风控端处理，目前业务端不需要做
        if (!CodeReturn.SMS_REPAY_TYPE.equals(smsType)) {
            if ("register".equals(event) && !riskService.isBlack(phone, null)) {
                log.info("手机号码{}，获取验证码在风控黑名单中", phone);
                noteResult.setCode(String.valueOf(CodeReturn.baiqishi));
                noteResult.setInfo("您的信用等级较低，请稍后再试！");
                noteResult.setData("r");
                return noteResult;

            }
            // -----------------------------白骑士-----------------------------------------

            if (("register".equals(event) && !(bqsService.runBQS(phone, "", "", "register", tokenKey, device) && bqsService.runBQS(phone, "", "", "sendDynamic", tokenKey, device)))
                    ||
                    ("sendDynamic".equals(event) && !bqsService.runBQS(phone, "", "", "sendDynamic", tokenKey, device))) {

                noteResult.setCode(String.valueOf(CodeReturn.baiqishi));
                noteResult.setInfo("您的信用等级较低，请稍后再试！");
                return noteResult;
            }
        }else {
            //获取Ip
            String ipAddr = IpUtil.getIpAddr(request);
            NoteResult note = ipUtil.checkIpLimit(ipAddr);
            if (!CodeReturn.SUCCESS_CODE.equals(note.getCode())){
                return note;
            }
        }
        try {
            //判断用户是否绑定合利宝或拉卡拉快捷支付,如果没有绑定,获取快捷支付验证码
            noteResult = userService.queryBindAndSendMsg(Integer.parseInt(perId),phone, bankNum);
            if (Constants.CODE_200.equals(noteResult.getCode()) && noteResult.getData() != null) {
                return noteResult;
            }
        } catch (Exception e) {
            log.error("调用查询是否绑卡及发送短信接口失败" + ExceptionUtils.getFullStackTrace(e));
        }

        // --------------------------------白骑士--------------------------------
        // 生成6位数的验证码
        Random random = new Random();

        Map<String, Object> data = new HashMap<>();

        String radomInt = "";

        for (int i = 0; i < 6; i++) {

            radomInt += random.nextInt(10);

        }
        log.info("本地短信生成的验证码==" + radomInt);
        //把验证码存到redis中,并设置两分钟分钟过期时间
        String valiCodeKey = RedisConst.VALIDATE_CODE + RedisConst.SEPARATOR + phone;
        jedisCluster.setex(valiCodeKey, 2 * 60, radomInt);

        log.info("redis key:" + valiCodeKey + ",在redis记录本次快捷支付验证码" + radomInt);

        // 老悠米的短信接口，要加标题模版
        // 2017.4.19更新 短信send第三个参数 0-悠兔 ，1-悠米，2-吾老板
        ResponseDo rspDo;
        rspDo = smsService.sendSms(SmsTemplateEnum.LOAN_COMMON_CODE.getCode(), phone, radomInt);
        if (StateCode.SUCCESS_CODE == rspDo.getCode()) {
            log.info("验证码发送成功！");
            noteResult.setCode(String.valueOf(CodeReturn.success));
            noteResult.setInfo("发送成功");
        } else {
            log.info("验证码发送失败！");
            noteResult.setCode(String.valueOf(CodeReturn.fail));
            noteResult.setInfo("发送失败");
        }
        return noteResult;
    }


    /**
     * 短信验证码发送
     *
     * @return
     */
    @ApiOperation(value = "验证码", hidden = true)
    @ResponseBody
    @RequestMapping(value = "user/sendSms")
    @ApiDoc(Admin.class)
    public ResponseDo<?> sendSms(String phone, String timestamp, String graphiCode) {
        //验证手机号
        if (!AccountValidatorUtil.isMobile(phone)) {
            return new ResponseDo(StateCode.PHONE_ERROR, StateCode.PHONE_ERROR_MSG);
        }
        //验证图形码
        String sessionCode = userService.queryRedisData("h5VerifyCode" + timestamp);
        if (!StringUtils.equalsIgnoreCase(graphiCode, sessionCode)) {
            return new ResponseDo(StateCode.GRAPHICODE_ERROR_CODE, StateCode.GRAPHICODE_ERROR__MSG);
        }

        if(!qianQiDepositWebService.checkIsAgreeRegister(phone)){
            return new ResponseDo(StateCode.UN_ALLOW_REGISTER, StateCode.UN_ALLOW_REGISTER_MSG);
        }
        Random random = new Random();
        String radomInt = "";
        for (int i = 0; i < 6; i++) {
            radomInt += random.nextInt(10);
        }
        log.info("生成的验证码==" + radomInt);

        // 风控黑名单校验，拒绝业务在风控端处理，目前业务端不需要做
        if (!riskService.isBlack(phone, null)) {
            log.info("发送短信手机号{}，在风控黑名单中", phone);
            return new ResponseDo<>(StateCode.BQS_VALIT_ERROR_CODE, StateCode.BQS_VALIT_ERROR_MSG);
        }
        ResponseDo rspDo;
        rspDo = smsService.sendSms(SmsTemplateEnum.LOAN_COMMON_CODE.getCode(), phone, radomInt);
        if (StateCode.SUCCESS_CODE == rspDo.getCode()) {
            userService.setRedisData("h5msgCode" + phone + "", 2 * 60, radomInt);
            return ResponseDo.newSuccessDo();
        } else {
            log.info("验证码发送失败！");
            return new ResponseDo<>(StateCode.SMS_SEND_FAIL_CODE, StateCode.SMS_SEND_FAIL_MSG);
        }
    }

    /**
     * 响应验证码页面
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "user/validateCode")
    @ApiOperation(value = "图形码", hidden = true)
    public String validateCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        String timestamp = request.getParameter("timestamp");
        ValidateCode vCode = new ValidateCode(120, 40, 4, 50);
        String count = userService.setRedisData("h5VerifyCode" + timestamp, 2 * 60, vCode.getCode());
        vCode.write(response.getOutputStream());
        return null;
    }
}
