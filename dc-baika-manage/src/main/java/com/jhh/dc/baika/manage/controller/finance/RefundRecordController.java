package com.jhh.dc.baika.manage.controller.finance;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.jhh.dc.baika.manage.entity.RefundRecordVo;
import com.jhh.dc.baika.manage.service.refund.RefundRecordService;

import java.util.List;

@Controller
@RequestMapping("refundRecord")
@Slf4j
public class RefundRecordController {
    @Autowired
    RefundRecordService refundRecordService;
    /**
     * 退款流水
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public List<RefundRecordVo> getRefundRecord() {
        return refundRecordService.getRefundRecord();
    }
}
