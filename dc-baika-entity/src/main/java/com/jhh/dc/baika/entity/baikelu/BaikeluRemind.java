package com.jhh.dc.baika.entity.baikelu;

import com.jhh.id.annotation.IdGenerator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


//百可录提醒类电话实体类
@Entity
@Table(name = "b_baikelu_remind")
@Getter
@Setter
public class BaikeluRemind implements Serializable {
    @Id
    @IdGenerator(value = "dc_baika_baikelu_remind")
    private Integer id;  //编号

    private String jobRef;//订单编号

    private String phone;//手机号

    private String remindType; //提醒类型

    private String remindDesc;//提醒描述

    private String requestData;//请求数据

    private String responseData;//响应数据

    private Date requestTime;//请求时间

    private Date responseTime;//响应时间

    private String callbackData;//回调数据

    private Date callbackTime;//回调时间

    private Date createTime;//创建时间

}
