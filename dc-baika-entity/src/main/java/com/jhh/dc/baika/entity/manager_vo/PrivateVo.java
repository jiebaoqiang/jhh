package com.jhh.dc.baika.entity.manager_vo;

import java.io.Serializable;
import java.util.Date;

import com.jhh.dc.baika.entity.manager.PrivateOld;

import lombok.Getter;
import lombok.Setter;

/**
 * 
*描述：
*@author: Wanyan
*@date： 日期：2016年11月10日 时间：下午9:28:15
*@version 1.0
 */
@Getter
@Setter
public class PrivateVo extends PrivateOld implements Serializable {
   private String name;
   private String cardNum;
   private String relativesValue;
   private String societyValue;
   private String imageZ;
   private String imageF;
   
   private String source;
   private String sourceValue;
   private String phone;
   private String address;
   private Date birthday;
   private String blacklist;
   private Date createDate;
   private String contactUrl;
}
