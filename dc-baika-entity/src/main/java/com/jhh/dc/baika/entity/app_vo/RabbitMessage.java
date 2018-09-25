package com.jhh.dc.baika.entity.app_vo;

import java.io.Serializable;

/**
 * @Description: 消息
 * @author: likun
 * @date: 2018年6月21日 下午2:08:43
 */
public class RabbitMessage implements Serializable {

  private static final long serialVersionUID = 2936198759245719750L;

  /**
   * 数据集
   */
  private Object data;

  /**
   * 消息类型
   */
  private Integer type;

  /**
   * 消息标题
   */
  private String title;


  public RabbitMessage() {
  }

  public RabbitMessage(Object data) {
    this.data = data;
  }


  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
