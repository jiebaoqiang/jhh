package com.jhh.dc.baika.entity.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import com.jhh.dc.baika.entity.app_vo.PhoneInfoVo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 2018/1/16.
 */
@Entity
@Table(name = "phone_info")
@Getter
@Setter
@ToString
public class PhoneInfo implements Serializable {
    private static final long serialVersionUID = -2508048794513059524L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer perId;

    private Integer borrId;

    private String info;

    private String device;

    private String serialNumber;

    private String serialNumberUrl;

    private Date createDate;

    private Date updateDate;

    public PhoneInfo(PhoneInfoVo vo, String path) {
        this.perId = Integer.parseInt(vo.getPer_id());
        this.info = vo.getPhoneInfo();
        this.device = vo.getDevice();
        if (StringUtils.isEmpty(path)) {
            this.serialNumber = vo.getSequence();
        } else {
            this.serialNumberUrl = path;
        }
        this.createDate = new Date();
    }

    public PhoneInfo() {

    }


}
