package com.jhh.dc.baika.entity.app_vo;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**借款列表实体类
 * @author xuepengfei
 */
@Getter
@Setter
public class MyBorrow implements Serializable{

    @Id
    private Integer id;

    private String borrNum;

    private String borrStatus;

    private String statusName;

    private String amount;

//    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date askDate;


}
