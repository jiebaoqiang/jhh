package com.jhh.dc.baika.entity.manager;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 2018/5/9.
 */
@Entity
@Table(name = "bank_ban")
@Getter
@Setter
@ToString
public class BankBan implements Serializable{
    private static final long serialVersionUID = -8599725918681752142L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String bankName;

    private String bankCode;

    private String type;
}
