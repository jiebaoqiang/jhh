package com.jhh.dc.baika.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BkAssetModel implements Serializable{
  private static final long serialVersionUID = 432176949589871497L;
//  private Integer id;

//  private Integer borrowId;

//  private Integer sysUserId;

  private String assetNo;
  //选填,其他为必填
  private String loanInvitationCode;

  private String channelCode;

//  private Integer assetType;

//  private Integer assetStatus;

//  private Integer guaranteeType;
//
//  private String guaranteeName;
//
//  private Integer guaranteeCompanyId;

//  private Integer assetLevel;

  private String phone;

  private String name;

  private String card;

  private String sex;

  private Integer age;

  private String education;

  private Integer maritalStatus;

  private String userType;

  private String industry;

  private BigDecimal account;

  private Integer timeLimit;

  private Integer timeLimitType;

  private String loanUse;

  private Integer style;

  private String repaymentSource;

  private Long monthlyIncome;

  private String debt;

  private Integer housingConditions;

  private Integer driverLicense;

  private Integer credit;

  private Integer household;

  private Integer creditReport;

  private Integer serialNumber;

  private Integer workCertificate;

  private Integer marriageCertificate;
  //借款利率
  private String filed1;
  //卡卡资产id
  private String filed2;

 // private String filed3;
  //  private Date addTime;
  private String borrowDescribe;
  //json  身份证正面 identifyPositive;身份证反面identifyOpposite;征信 credit
  private String flilList;


  /*
`id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `borrow_id` int(11) DEFAULT NULL COMMENT '借款id',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '借款用户id',
  `asset_no` varchar(30) DEFAULT NULL COMMENT '资产编号',
  `loan_invitation_code` varchar(30) DEFAULT NULL COMMENT '借款邀请码',
  `channel_code` varchar(30) DEFAULT NULL COMMENT '渠道编码',
  `asset_type` int(5) DEFAULT NULL COMMENT '资产类型 1惠学习,2线下DS,3公积金贷,4个人申请 ,5白卡',
  `asset_status` int(5) DEFAULT '1' COMMENT '1待审核,2发标,3驳回 ',
  `guarantee_type` int(2) DEFAULT '0' COMMENT '担保类型 0暂无担保,1第三方担保',
  `guarantee_name` varchar(20) DEFAULT NULL COMMENT '担保企业名称',
  `guarantee_company_id` int(11) DEFAULT NULL COMMENT '担保企业的用户userid',
  `asset_level` int(5) DEFAULT NULL COMMENT '资产等级 1 低, 3 中, 5 高',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `name` varchar(10) DEFAULT NULL COMMENT '借款人真实姓名',
  `card` varchar(30) DEFAULT NULL COMMENT '身份证号',
  `sex` varchar(2) DEFAULT NULL COMMENT '借款人性别 男 女',
  `age` int(3) DEFAULT NULL COMMENT '借款人年龄',
  `education` varchar(10) DEFAULT NULL COMMENT '借款人文化程度',
  `marital_status` int(2) DEFAULT NULL COMMENT '0:已婚，1:未婚,2离异 3丧偶',
  `user_type` varchar(10) DEFAULT NULL COMMENT '自然人,法人,其他组织',
  `industry` varchar(50) DEFAULT NULL COMMENT '所属行业',
  `account` decimal(14,2) NOT NULL DEFAULT '0.00' COMMENT '借款总额',
  `time_limit` int(2) DEFAULT NULL COMMENT '借款期限',
  `time_limit_type` int(2) DEFAULT NULL COMMENT '借款期限类型 1月，2年，3天',
  `loan_use` varchar(50) DEFAULT NULL COMMENT '借款用途',
  `style` int(2) DEFAULT NULL COMMENT '还款方式  0等额本息，1先息后本，2到期本息',
  `repayment_source` varchar(50) DEFAULT NULL COMMENT '还款来源',
  `monthly_income` bigint(10) DEFAULT NULL COMMENT '月收入',
  `debt` varchar(50) DEFAULT NULL COMMENT '债务情况',
  `housing_conditions` int(2) DEFAULT '0' COMMENT '住房条件 0无, 1有',
  `driver_license` int(2) DEFAULT '0' COMMENT '是否购车  0无, 1有',
  `credit` int(2) DEFAULT '0' COMMENT '是否有身份证 0无, 1有',
  `household` int(2) DEFAULT '0' COMMENT '户口本  0无, 1有',
  `credit_report` int(2) DEFAULT '0' COMMENT '征信报告  0无, 1有',
  `serial_number` int(2) DEFAULT '0' COMMENT '用户对公银行流水号   0无, 1有',
  `work_certificate` int(2) DEFAULT '0' COMMENT '工作证明   0无, 1有',
  `marriage_certificate` int(2) DEFAULT '0' COMMENT '婚姻证明  0无, 1有',
  `borrow_describe` text COMMENT '借款描述',
  `filed1` varchar(255) DEFAULT NULL COMMENT 'pc申请的费率',
  `filed2` varchar(255) DEFAULT NULL COMMENT '卡卡的id',
   */

}
