package com.jhh.dc.baika.entity.app;

import com.jhh.id.annotation.IdGenerator;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "b_jd_card_info")
@Data
public class JdCardInfo implements Serializable {
    @Id
    @IdGenerator(value = "dc_baika_jd_card_info")
    private Integer id;

    private Date startDate;

    private Date endDate;

    private String cardNumber;

    private String password;

    private Date fetchPasswordDate;

    private Integer productId;

    private Integer perId;

    private Integer borrId;

    private Date createDate;

    private Date updateDate;

    private String createUser;

    private String reviewUser;

    private String version;

    private static final long serialVersionUID = 1L;


}