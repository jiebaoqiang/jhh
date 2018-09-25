package com.jhh.dc.baika.api.entity;

import com.jhh.dc.baika.entity.app.Bank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 2018/7/26.
 */
@Data
public class DepositDo implements Serializable{

    private static final long serialVersionUID = -1L;

    private Integer borrId;

    private Integer perId;

    private Float amountSurplus;

    private Bank bank;

    private Float fee;

    private String phone;

    private String borrNum;

}
