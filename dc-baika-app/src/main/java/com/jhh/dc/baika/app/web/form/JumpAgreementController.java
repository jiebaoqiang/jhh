package com.jhh.dc.baika.app.web.form;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.app.web.exception.ExceptionPageController;
import com.jhh.dc.baika.api.app.LoanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @auther: wenfucheng
 * @date: 2018/7/30 15:55
 * @description: 跳转到各种协议
 */
@Api(tags = "跳转到各种协议")
@Controller
@RequestMapping("/agreement")
public class JumpAgreementController extends ExceptionPageController {

    @Reference
    private LoanService loanService;

    /**
     * 跳转到销售合同页面
     * @param borrId
     * @return
     */
    @ApiOperation(value = "跳转到销售合同页面")
    @GetMapping("/saleContract/{borrId}")
    public String saleContract(@PathVariable("borrId")Integer borrId, Model model){

        Map<String,Object> contractInfo = loanService.getContractInfoByBorrId(borrId);
        model.addAttribute("contract",contractInfo);

        return contractInfo.get("view").toString();
    }


    /**
     * 跳转我的合同页面
     * @param borrId
     * @return
     */
    @ApiOperation(value = "查看我的合同/协议")
    @GetMapping("/myContract/{borrId}")
    public String myContract(@PathVariable("borrId")Integer borrId, Model model){

        Map<String,String> contractInfo = loanService.getContractImageByBorrId(borrId);
        model.addAttribute("contract",contractInfo);

        return "agreement/contractAgreement";
    }

    /**
     * 跳转到用户注册及隐私保护协议页面
     * @return
     */
    @ApiOperation(value = "跳转到用户注册及隐私保护协议页面")
    @GetMapping("/userAgreement")
    public String userAgreement(){
        return "agreement/userAgreement";
    }
}
