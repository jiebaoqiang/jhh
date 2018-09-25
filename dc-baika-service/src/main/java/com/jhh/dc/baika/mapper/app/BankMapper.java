package com.jhh.dc.baika.mapper.app;

import java.util.List;

import com.jhh.dc.baika.api.entity.PaymentInfoDo;
import org.apache.ibatis.annotations.Param;

import com.jhh.dc.baika.entity.app.Bank;

public interface BankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Bank record);

    int insertSelective(Bank record);

    Bank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Bank record);

    int updateByPrimaryKey(Bank record);

    
    //更改用户所有银行卡状态
    int updateBankStatus(String status, Integer per_id, String oldStatus);

    //查询根据银行卡号查询该银行卡是否存在（有效卡）
    Bank selectByBankNumAndStatus(@Param("bankNum") String bankNum);
    // 根据银行卡号和用户Id查询当前有效卡
    Bank selectByBankNumEffective(@Param("bankNum") String bankNum,@Param("perId") Integer perId);


    // 查询用户所有有效的银行卡
    List<Bank> selectAllBanks(String per_id);

    //查询用户拥有的主卡
    Bank selectPrimayCardByPerId(String per_id);

    void updateBankStatusWithoutBankNum(@Param("status") String status, @Param("perId") Integer perId, @Param("oldStatus") String oldStatus, @Param("bankNum") String bankNum);

    /**查询用户付款信息*/
    PaymentInfoDo getPaymentInfoDo(@Param("perId") int perId,@Param("bankId") int bankId);
}