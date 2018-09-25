package com.jhh.dc.baika.entity.bankdeposit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 悠兔理财流标参数推送
 */
@Getter
@Setter
@ToString
public class YoutuFailLoanCallback implements Serializable{
    private Integer baikaBorrowId;  //白卡合同编号

}
