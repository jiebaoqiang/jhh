package com.jhh.dc.baika.dao;

import com.jhh.dc.baika.entity.loan.BorrowDeductions;

import tk.mybatis.mapper.common.Mapper;

public interface BorrowDeductionsMapper extends Mapper<BorrowDeductions> {
    BorrowDeductions selectByBorrId(Integer borrId);
}
