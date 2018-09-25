package com.jhh.dc.baika.manage.service.impl;

import com.jhh.dc.baika.manage.service.refund.RefundRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhh.dc.baika.manage.mapper.RefundRecordMapper;

import java.util.List;

@Service
public class RefundRecordServiceImpl implements RefundRecordService {

    private static Logger log = LoggerFactory.getLogger(RefundRecordService.class);

    @Autowired
    RefundRecordMapper refundRecordMapper;

    @Override
    public List getRefundRecord() {
        return refundRecordMapper.getRefundRecord();
    }
}
