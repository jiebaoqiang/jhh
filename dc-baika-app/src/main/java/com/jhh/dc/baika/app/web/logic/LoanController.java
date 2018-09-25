package com.jhh.dc.baika.app.web.logic;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.app.web.exception.ExceptionJsonController;
import com.jhh.dc.baika.api.app.LoanService;
import com.jhh.dc.baika.api.app.UserService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.app.BorrowList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2018/7/12.
 */
@Api(description = "B公司合同相关")
@RestController
@RequestMapping("/loan")
@Slf4j
public class LoanController extends ExceptionJsonController {

    @Reference
    private UserService userService;

    @Reference
    private LoanService loanService;

    /**
     * 合同签约，状态改为已签约，添加签约时间
     *
     * @param perId  用户ID
     * @param borrId 合同id
     * @return 结果
     */
    @RequestMapping("/signingBorrow/{perId}/{borrId}")
    public ResponseDo<?> signingBorrow(@PathVariable("perId") String perId, @PathVariable("borrId") String borrId,String loanUse) {
      /*  String verify = loanService.verifyTokenId(per_id, token);
        if (!SUCCESS_CODE.equals(verify)) {
            result.setCode(CodeReturn.MD5_WRONG);
            result.setInfo("系统繁忙");
            return JSONObject.toJSONString(result);
        }*/
        return loanService.signingBorrow(borrId,loanUse);

    }

    /**
     *  取消合同
     * @param perId
     * @param borrId
     * @return
     */
    @RequestMapping("/cancelBorrow/{perId}/{borrId}")
    public ResponseDo<?> cancelBorrow(@PathVariable("perId") String perId,@PathVariable("borrId") String borrId){
        return loanService.cancelAskBorrow(perId, borrId);
    }


    @ApiOperation("A公司同步更新B公司合同状态接口")
    @GetMapping("/updateBorrowStatusByBorrowNum/{borrowNum}/{rlStatus}")
    public String updateBorrowStatusByBorrowId(@ApiParam(value = "合同号",required = true) @PathVariable("borrowNum") String borrowNum,
                                                      @ApiParam(value = "合同状态",required = true) @PathVariable("rlStatus") String rlStatus){
        log.info("A公司同步更新B公司合同状态 borrowNum="+borrowNum +",rlStatus="+rlStatus);
        return loanService.updateBorrowStatusByBorrowNum(borrowNum,rlStatus);
    }

}
