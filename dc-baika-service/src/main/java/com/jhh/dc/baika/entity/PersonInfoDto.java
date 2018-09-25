package com.jhh.dc.baika.entity;

import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.utils.RepaymentDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 2018/4/9.
 */
@Data
@AllArgsConstructor
public class PersonInfoDto {

    private BorrowList borrowList;

    private Person person;

    private RepaymentDetails repaymentDetails;
}
