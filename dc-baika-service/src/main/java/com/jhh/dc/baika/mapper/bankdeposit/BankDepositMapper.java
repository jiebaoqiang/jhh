package com.jhh.dc.baika.mapper.bankdeposit;

import com.jhh.dc.baika.entity.app.Person;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BankDepositMapper {

    int updateAccountState(@Param("acctNo") String acctNo,@Param("custNo") String custNo,
                           @Param("phone") String phone, @Param("isOpen") String isOpen);

    int updateAuthRepayState(@Param("custNo") String custNo,@Param("isAuth") String isAuth);

    int updateAuthLoanState(@Param("custNo") String custNo,@Param("isAuth") String isAuth);

    List<Person> findAllPPerson(@Param("code") int code);

}