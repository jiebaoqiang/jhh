package com.jhh.dc.baika.app.web.form;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.app.common.constant.AccountValidatorUtil;
import com.jhh.dc.baika.app.web.exception.ExceptionPageController;
import com.jhh.dc.baika.api.app.LoanService;
import com.jhh.dc.baika.api.app.UserService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.UserNodeDo;
import com.jhh.dc.baika.app.common.exception.CommonException;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.entity.app.Bank;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.app_vo.SignInfo;
import com.jhh.dc.baika.entity.bankdeposit.BankDepositCommon;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 2018/7/23.
 */
@Controller
@RequestMapping("/form")
@Slf4j
public class JumpUserController extends ExceptionPageController {


    @Reference
    private UserService userService;

    @Reference
    private LoanService loanService;

    @Autowired
    private JedisCluster jedisCluster;

    @ApiOperation(value = "A公司申请跳转B公司统一入口",notes = "生成用户,借款合同，页面跳转等所有操作",hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "borrNum",value = "合同编号,暂时无用，为扩展做准备" ),
            @ApiImplicitParam(name = "reviewer",value = "审核人")
    })
    @RequestMapping("/applyBorrow/{phone}/{productId}")
    public String applyBorrow(@PathVariable("phone") String phone, @PathVariable("productId") String productId,
                              String borrNum, String reviewer, HttpServletRequest request) throws CommonException {
        //拦截器存入cookie用
        request.setAttribute("phone",phone);
        if (!AccountValidatorUtil.isMobile(phone)) {
            throw new CommonException(201,"手机号格式错误");
        }
        ResponseDo<Map<String,Object>> result = loanService.applyBorrow(phone, productId,borrNum,reviewer);
        if(200 == result.getCode()){
            Map<String, Object> data = result.getData();
            if(data.get("forwardUrl")!=null){
                return data.get("forwardUrl").toString();
            }else{
                throw new CommonException(201,"当前订单状态有误，请重试");
            }
        }else{
            throw new CommonException(201,result.getInfo());
        }
    }

    @ApiOperation(value = "登陆页面跳转", hidden = true)
    @RequestMapping("/login/{phone}")
    public String loginJump(@PathVariable("phone") String phone, Map<String, Object> map) throws CommonException {
        if (!AccountValidatorUtil.isMobile(phone)) {
            throw new CommonException(201, "手机号有误，请返回上一级");
        }
        map.put("phone",phone);
        return "user/login";
    }

    @ApiOperation(value = "设置支付密码/重置密码",notes = "设置支付密码/重置密码",hidden = true)
    @ApiImplicitParam(name = "returnUrl" , value = "跳转返回页面地址，如为null，则按正常流程跳绑卡页面")
    @RequestMapping("/setPassword/{phone}")
    public String setPassword(@PathVariable("phone") String phone,String returnUrl, Map<String,Object> map) {
        //获取用户信息
        Person person = userService.selectByPhone(phone);
        map.put("person",person);
        map.put("returnUrl",returnUrl);
        return "user/setPayPwd";
    }


    @ApiOperation(value = "登陆跳转银行卡认证页面", hidden = true)
    @RequestMapping("/jumpBankCard/{phone}")
    public String jumpBankCard(@PathVariable("phone") String phone,Map<String, Object> map) {
        log.info("用户跳转银行认证界面 phone = "+phone);
        //获取用户信息
        Person person = userService.selectByPhone(phone);
        map.put("person",person);
        return "user/bankCardCertification";
    }


    @ApiOperation(value = "银行卡认证跳转签约页面",hidden = true)
    @RequestMapping("/jumpSign/{phone}")
    public String jumpSign(@PathVariable("phone") String phone,Map<String, Object> map){
        //获取签约页面信息
        SignInfo signInfo = loanService.getSignInfo(phone);
        map.put("signInfo",signInfo);
        return "trade/contract";
    }

    @ApiOperation(value = "添加银行卡",notes = "添加银行卡",hidden = true)
    @ApiImplicitParam(name = "borrId",value = "还款跳转需要")
    @RequestMapping("/addBankCard/{perId}")
    public String addBankCard(@PathVariable("perId") String perId,String returnUrl,Map<String,Object> map) throws CommonException {
        ResponseDo<Person> person = userService.selectPersonById(perId);
        map.put("person",person.getData());
        map.put("returnUrl",returnUrl);
        return "user/addBankCard";
    }

    @ApiOperation(value = "意见反馈",notes = "用户意见反馈",hidden = true)
    @RequestMapping("/feedback/{perId}")
    public String feedback(@PathVariable("perId") String perId, Map<String,Object> map) {
        map.put("perId",perId);
        return "user/feedback";
    }

    @ApiOperation(value = "修改支付密码页面")
    @ApiImplicitParam(name = "returnUrl",value = "密码修改之后返回地址")
    @RequestMapping("/changePayPwd/{phone}")
    public String changePayPwd(@PathVariable("phone") String phone,Map<String,Object> map) throws Exception{
        log.info("\n=========== 修改密码接口 come in ===========");
        log.info("\n前端请求数据:{}", phone.toString());
        //ResponseDo<BankDepositCommon> responseDo = depositoryPayService.depositoryWithdrawal(withdrawal);
        ResponseDo<BankDepositCommon> responseDo = userService.changePassword(phone);

        if (CodeReturn.success == responseDo.getCode()){
            map.put("withdraw",responseDo.getData());
            return "trade/withdraw";
        }else {
            throw new CommonException(CodeReturn.fail,responseDo.getInfo());
        }
    }

    @ApiOperation(value = "修改密码返回页面")
    //@ApiImplicitParam(name = "returnUrl",value = "密码修改之后返回地址")
    @RequestMapping("/editPwdResult")
    public String editPwdResult(){
        return "user/editPwdResult";
    }

    @ApiOperation(value = "重置支付密码页面")
    @ApiImplicitParam(name = "returnUrl",value = "密码修改之后返回地址")
    @RequestMapping("/resetPwd/{phone}")
    public String resetPwd(@PathVariable("phone") String phone,Map<String,Object> map) throws Exception{
        log.info("\n=========== 修改密码接口 come in ===========");
        log.info("\n前端请求数据:{}", phone.toString());
        ResponseDo<BankDepositCommon> responseDo = userService.resetPassword(phone);

        if (CodeReturn.success == responseDo.getCode()){
            map.put("withdraw",responseDo.getData());
            return "trade/withdraw";
        }else {
            throw new CommonException(CodeReturn.fail,responseDo.getInfo());
        }
    }

    @ApiOperation(value = "重置密码返回页面")
    //@ApiImplicitParam(name = "returnUrl",value = "密码修改之后返回地址")
    @RequestMapping("/resetPwdResult")
    public String resetPwdResult(){
        return "user/resetPwdResult";
    }

    @ApiOperation(value = "忘记支付密码页面")
    @ApiImplicitParam(name = "returnUrl",value = "密码修改之后返回地址")
    @RequestMapping("/forgetPayPwd/{phone}")
    public String forgetPayPwd(@PathVariable("phone") String phone,String returnUrl,Map<String,Object> map){
        map.put("phone",phone);
        map.put("returnUrl",returnUrl);
        return "user/forgetPwd";
    }

    @ApiOperation(value = "银行卡管理")
    @RequestMapping("/bankManagement/{perId}/{phone}")
    public String bankManagement(@PathVariable("perId") String perId,@PathVariable("phone") String phone,Map<String,Object> map){
        ResponseDo<List<Bank>> bankManagement = userService.getBankManagement(perId);
        map.put("bankList",bankManagement.getData());
        map.put("perId",perId);
        map.put("phone",phone);
        return "user/bankManagement";
    }

    @ApiOperation(value = "个人信息填写页面")
    @RequestMapping("/userInfo/{phone}")
    public String userInfo(@PathVariable("phone") String phone, Model model){
        Person person = userService.selectByPhone(phone);
        model.addAttribute("perId",person.getId());
        return "user/userInformationWrite";
    }

}
