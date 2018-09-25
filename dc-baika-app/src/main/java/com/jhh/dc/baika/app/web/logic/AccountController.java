package com.jhh.dc.baika.app.web.logic;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositWebService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.loan.AccountService;
import com.jhh.dc.baika.app.common.util.IpUtil;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.bankdeposit.BankDepositCommon;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.NetUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @auther: wenfucheng
 * @date: 2018/9/10 10:01
 * @description:
 */
@Slf4j
@Controller
@RequestMapping("/account")
public class AccountController {

    @Reference
    private AccountService accountService;

    @Reference
    private QianQiDepositWebService qianQiDepositWebService;

    @Value("${callback.baseurl}")
    private String callbackBaseUrl;

    // 开户页面
    @RequestMapping("/openAccountHtml/{perId}")
    public String openAccountHtml(@PathVariable("perId") Integer perId, Model model){
        Map<String,String> map = accountService.getOpenAccountHtmlParam(perId);
        model.addAllAttributes(map);
        return "account/openBank";
    }


    // 开户
    @ResponseBody
    @RequestMapping("/getOpenAccountStatus")
    public ResponseDo<Person> getOpenAccountStatus(HttpServletRequest request){
        String perId = request.getParameter("perId");
        ResponseDo<Person> responseDo = accountService.getOpenAccountStatus(perId);
        return responseDo;
    }

    // 开户
    @ResponseBody
    @RequestMapping("/openAccount")
    public ResponseDo<BankDepositCommon> openAccount(HttpServletRequest request){
        String perId = request.getParameter("perId");
        String phone = request.getParameter("phone");
        // 获取参数
        String contextPath = request.getContextPath();
        //  回调地址
        String webResponseUrl = callbackBaseUrl+contextPath+"/account/openAccountHtml/"+perId;
        ResponseDo<BankDepositCommon> bankDepositCommonResponseDo = qianQiDepositWebService.openAccount(phone, webResponseUrl);
        return bankDepositCommonResponseDo;
    }


    // 授权页面
    @RequestMapping("/authAccountHtml/{perId}")
    public String authAccountHtml(@PathVariable("perId") Integer perId, Model model){
        Map<String,String> map = accountService.getOpenAccountHtmlParam(perId);
        model.addAllAttributes(map);
        return "account/authorize";
    }


    // 授权
    @ResponseBody
    @RequestMapping("/authAccount")
    public ResponseDo<BankDepositCommon> authAccount(HttpServletRequest request){
        ResponseDo<BankDepositCommon> responseDo = null;
        try {
            // 获取参数
            String perId = request.getParameter("perId");
            String authType = request.getParameter("authType");
            String contextPath = request.getContextPath();
            // 回调地址
            String webResponseUrl = callbackBaseUrl+contextPath+"/account/authAccountHtml/"+perId;
            // 调用授权接口
            responseDo = accountService.authAccount(perId,authType,webResponseUrl);
        } catch (Exception e) {
            responseDo = ResponseDo.newFailedDo("授权失败");
            log.error("授权失败",e);
        }
        return responseDo;
    }

    // 获取授权状态
    @ResponseBody
    @RequestMapping("/getAuthStatus")
    public ResponseDo<String> getAuthStatus(Integer perId,Integer authType){
        ResponseDo<String> responseDo = accountService.getAuthStatus(perId,authType);
        return responseDo;
    }

}
