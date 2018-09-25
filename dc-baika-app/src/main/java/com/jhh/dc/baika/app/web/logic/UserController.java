package com.jhh.dc.baika.app.web.logic;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.api.app.LoanService;
import com.jhh.dc.baika.api.app.RiskService;
import com.jhh.dc.baika.api.app.UserService;
import com.jhh.dc.baika.api.entity.ForgetPayPwdVo;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.PersonInfoVO;
import com.jhh.dc.baika.api.sms.SmsService;
import com.jhh.dc.baika.api.entity.LoginVo;
import com.jhh.dc.baika.app.web.exception.ExceptionJsonController;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.app_vo.BorrowListVO;
import com.jhh.dc.baika.entity.manager.Feedback;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

/**
 * 用户模块
 *
 * @author
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends ExceptionJsonController {

    @Reference
    private UserService userService;

    @Reference
    private RiskService riskService;

    @Reference
    private SmsService smsService;
    @Reference
    private LoanService loanService;

    @ApiOperation(value = "用户信息修改", notes = "同步A公司用户信息")
    @ApiImplicitParam(name = "person", value = "用户实体person", required = true)
    @PostMapping("/updatePersonInfo")
    public ResponseDo<?> userUpdateRegister(Person person) {
        return userService.updatePersonInfo(person);

    }

    @ApiOperation(value = "用户登陆", notes = "B公司用户登录" ,hidden = true)
    @RequestMapping("/login")
    public ResponseDo<Person> login(LoginVo vo) {
        return userService.login(vo);

    }

    @ApiOperation(value = "保存或修改用户支付密码")
    @RequestMapping("/setPassword")
    public ResponseDo<?> setPassword(@RequestParam String phone,@RequestParam String paypwd,@RequestParam String confirmPaypwd){
        return userService.userSetPassword(phone,paypwd,confirmPaypwd);
    }

    @ApiOperation(value = "A公司获取我的账单信息")
    @GetMapping("/getBorrowListByPhone/{phone}")
    public List<BorrowListVO> getBorrowListByPhone(@ApiParam(value = "用户手机号", required = true) @PathVariable("phone") String phone){
        return userService.getBorrowListByPhone(phone);
    }

    @ApiOperation(value = "意见反馈")
    @RequestMapping("/feedback")
    public ResponseDo<?> addFeedback(Feedback feedback){
        int a = 1;
        return userService.userFeedBack(feedback);

    }

    @ApiOperation(value = "忘记密码验证")
    @RequestMapping("/forgetPayPwd")
    public ResponseDo<?> forgetPayPwd(ForgetPayPwdVo vo){
        return userService.forgetPayPwd(vo);
    }


    @ApiOperation(value = "A公司获取我的账单信息")
    @GetMapping("/getBorrowListByPersonId/{personId}")
    public List<BorrowListVO> getBorrowListByPersonId(@ApiParam(value = "用户id", required = true) @PathVariable("personId") Integer personId){
        return userService.getBorrowListByPersonId(personId);
    }

    @ApiOperation(value = "保存用户信息失败")
    @PostMapping("/saveUserInfo")
    public ResponseDo saveUserInfo(PersonInfoVO userInfoVO){
        ResponseDo responseDo = ResponseDo.newFailedDo("保存用户信息失败");
        try {
            responseDo = userService.saveUserInfo(userInfoVO);
        } catch (Exception e) {
            log.error("保存用户信息失败",e);
        }
        return responseDo;
    }

    @ResponseBody
    @GetMapping("/getUserJson")
    public String getUserJson() throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/static/config/user_json.json");
        StringWriter writer = new StringWriter();
        IOUtils.copy(in, writer, "UTF-8");
        String jsonString = writer.toString();
        return jsonString;
    }

}