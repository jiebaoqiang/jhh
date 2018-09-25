package com.jhh.dc.baika.api.entity.capital;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 2018/4/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeBatchVo implements Serializable{

    private static final long serialVersionUID = -7711646817176765685L;
    private List<TradeVo> deduct;

    private Integer deductSize;

    private String payChannel;

    private String optPerson;
}
