package com.jhh.dc.baika.app.web.form;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.api.app.DetailsService;
import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.api.entity.DetailsDo;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.details.AccountDetails;
import com.jhh.dc.baika.api.entity.details.RepayDetails;
import com.jhh.dc.baika.app.common.constant.AccountValidatorUtil;
import com.jhh.dc.baika.app.common.exception.CommonException;
import com.jhh.dc.baika.app.web.exception.ExceptionPageController;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.utils.RepaymentDetails;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 2018/8/24.
 */
@Controller
@RequestMapping("/form")
@Slf4j
public class JumpDetailsController extends ExceptionPageController {


    @Reference
    private DetailsService detailsService;

    @ApiOperation(value = "我的账户中心", notes = "跳转我的账户中心，存在app交互", hidden = true)
    @RequestMapping("/getAccountCenter/{phone}")
    public String getAccountCenter(@PathVariable("phone") String phone, Map<String, Object> map, HttpServletRequest request) throws CommonException {
        //拦截器存入cookie用
        request.setAttribute("phone", phone);
        if (!AccountValidatorUtil.isMobile(phone)) {
            throw new CommonException(StateCode.PHONE_ERROR, StateCode.PHONE_ERROR_MSG);
        }
        ResponseDo<AccountDetails> details = detailsService.getAccountDetails(phone);
        if (CodeReturn.success == details.getCode()) {
            map.put("details", details.getData());
            return "details/accountCenter";
        } else {
            throw new CommonException(CodeReturn.fail, details.getInfo());

        }

    }

    @ApiOperation(value = "用户借款列表", notes = "用户借款列表页面", hidden = true)
    @RequestMapping("/getMyBorrowList")
    public String getMyBorrowList(@RequestParam Integer perId, @RequestParam String phone, Map<String, Object> map) throws Exception {
        ResponseDo<List<BorrowList>> myBorrowList = detailsService.getMyBorrowList(perId);
        if (CodeReturn.success == myBorrowList.getCode()) {
            map.put("phone", phone);
            map.put("mylist", myBorrowList.getData());
            return "details/myBorrow";
        } else {
            throw new CommonException(CodeReturn.fail, myBorrowList.getInfo());
        }
    }

    @ApiOperation(value = "跳转详情页面",notes = "存在app交互",hidden = true)
    @RequestMapping("/jumpDetails/{phone}/{borrNum}")
    public String jumpDetails(@PathVariable("phone") String phone, @PathVariable("borrNum") String borrNum,
                              Map<String,Object> map, HttpServletRequest request) throws CommonException {
        //拦截器存入cookie用
        request.setAttribute("phone",phone);
        if (!AccountValidatorUtil.isMobile(phone)) {
            throw new CommonException(StateCode.PHONE_ERROR,StateCode.PHONE_ERROR_MSG);
        }
        ResponseDo<DetailsDo> details = detailsService.getDetails(phone,borrNum);
        if (200 == details.getCode()){
            map.put("details",details.getData());
        }else {
            throw new CommonException(201,details.getInfo());
        }
        return "details/details";
    }

    @ApiOperation(value = "还款详情", notes = "还款详情页面", hidden = true)
    @RequestMapping("/getRepayDetails/{phone}/{borrId}")
    public String getRepayDetails(@PathVariable String phone, @PathVariable String borrId, Map<String, Object> map) throws CommonException {
        ResponseDo<RepayDetails> details = detailsService.getRepayDetails(phone,borrId);
        if (CodeReturn.success == details.getCode()) {
            map.put("phone", phone);
            map.put("details", details.getData());
            return "details/repayDetails";
        } else {
            throw new CommonException(CodeReturn.fail, details.getInfo());
        }
    }


}