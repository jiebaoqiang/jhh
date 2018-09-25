package com.jhh.dc.baika.api.entity.agreement;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.Person;

/**
 * 2018/1/23.
 */
@Data
@NoArgsConstructor
public class BorrowAgreement implements Serializable{

    private static final long serialVersionUID = 8198311078894398497L;
    private String name;

    private String idCard;

    private String phone;

    private String email;

    private String assessAmount;

    private String depositAmount;

    private String payAmount;

    private String bankName;

    private String bankNum;

    private String ransomAmount;

    private String planRental;

    private String planRepay;

    private String serviceAmount;

    private Integer totalTermNum;

    private List<Map<String,Object>> trial;

    public BorrowAgreement(Person person, BorrowList bl, List<Map<String,Object>> info){
        this.name = person.getName();
        this.idCard = person.getCardNum();
        this.phone = person.getPhone();
        this.assessAmount = String.format("%.2f",bl.getBorrAmount());
        this.depositAmount = String.format("%.2f",bl.getDepositAmount());
        this.payAmount = String.format("%.2f",bl.getPayAmount());
        this.bankName = person.getBankName();
        this.bankNum = person.getBankCard();
        this.ransomAmount = String.format("%.2f",bl.getRansomAmount());
        this.planRepay = String.format("%.2f",bl.getPlanRepay());
        this.planRental = String.format("%.2f",bl.getPlanRental());
        this.serviceAmount = String.format("%.2f",bl.getServiceAmount());
        this.totalTermNum = bl.getTotalTermNum();
        info.forEach(v ->{
            if (v.get("payToalAmount") != null){
                v.put("payToalAmount",Arrays.asList(v.get("payToalAmount").toString().split(",")));
            }
            if (v.get("shouldToalAmount")!=null){
                v.put("shouldToalAmount",Arrays.asList(v.get("shouldToalAmount").toString().split(",")));
            }
        });
        this.trial = info;
    }
}
