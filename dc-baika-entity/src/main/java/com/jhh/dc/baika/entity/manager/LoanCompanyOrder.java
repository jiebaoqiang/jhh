package com.jhh.dc.baika.entity.manager;

import com.jhh.id.annotation.IdGenerator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity @Table(name = "b_loan_company_order") @Setter @Getter
public class LoanCompanyOrder {
    @Id
    @IdGenerator
    private Integer id;
    private Integer orderId;
    private Integer companyId;
    private String createUser;
    private Date createDate;

}
