package com.jhh.dc.baika.entity.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品期数表
 */
@Setter
@Getter
@Table(name = "product_term")
@Entity
@ToString
public class ProductTerm implements Serializable{
    private static final long serialVersionUID = -2327618382473475628L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer term_id;

    private Integer product_id;

    private Integer term;

    private Float penalty_level_1;

    private Float penalty_level_2;

    private Float penalty_level_3;

    private Float penalty_level_4;

    private Float penalty_level_5;

    private Float penalty_interest_leve_1;

    private Float penalty_interest_leve_2;

    private Float penalty_interest_leve_3;

    private Float penalty_interest_leve_4;

    private Float penalty_interest_leve_5;

    private Float interest_rate;
}