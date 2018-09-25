package com.jhh.dc.baika.manage.entity;

import java.util.Date;

/**
 * 黑名单 记录日志表
 * RiskBlacklistInfoLog
 * 数据库表：risk_blacklist_info_log
 */
public class RiskBlacklistInfoLog implements Comparable<RiskBlacklistInfoLog>{

    /**
     * 
     * 表字段 : risk_blacklist_info_log.id
     */
    private Long id;

    /**
     * 系统来源
     * 表字段 : risk_blacklist_info_log.sys
     */
    private String sys;

    /**
     * 姓名
     * 表字段 : risk_blacklist_info_log.name
     */
    private String name;

    /**
     * 身份证
     * 表字段 : risk_blacklist_info_log.idcard
     */
    private String idcard;

    /**
     * 手机号
     * 表字段 : risk_blacklist_info_log.phone
     */
    private String phone;

    /**
     * 操作人 工号
     * 表字段 : risk_blacklist_info_log.handler_no
     */
    private String handlerNo;

    /**
     * 操作人 姓名
     * 表字段 : risk_blacklist_info_log.handler_name
     */
    private String handlerName;

    /**
     * 操作类型 0拉黑  1洗白
     * 表字段 : risk_blacklist_info_log.type
     */
    private Integer type;

    /**
     * 原因
     * 表字段 : risk_blacklist_info_log.reason
     */
    private String reason;

    /**
     * 
     * 表字段 : risk_blacklist_info_log.create_time
     */
    private Date createTime;

    /**
     * 获取  字段:risk_blacklist_info_log.id
     *
     * @return risk_blacklist_info_log.id, 
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置  字段:risk_blacklist_info_log.id
     *
     * @param id the value for risk_blacklist_info_log.id, 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 系统来源 字段:risk_blacklist_info_log.sys
     *
     * @return risk_blacklist_info_log.sys, 系统来源
     */
    public String getSys() {
        return sys;
    }

    /**
     * 设置 系统来源 字段:risk_blacklist_info_log.sys
     *
     * @param sys the value for risk_blacklist_info_log.sys, 系统来源
     */
    public void setSys(String sys) {
        this.sys = sys == null ? null : sys.trim();
    }

    /**
     * 获取 姓名 字段:risk_blacklist_info_log.name
     *
     * @return risk_blacklist_info_log.name, 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 姓名 字段:risk_blacklist_info_log.name
     *
     * @param name the value for risk_blacklist_info_log.name, 姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取 身份证 字段:risk_blacklist_info_log.idcard
     *
     * @return risk_blacklist_info_log.idcard, 身份证
     */
    public String getIdcard() {
        return idcard;
    }

    /**
     * 设置 身份证 字段:risk_blacklist_info_log.idcard
     *
     * @param idcard the value for risk_blacklist_info_log.idcard, 身份证
     */
    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    /**
     * 获取 手机号 字段:risk_blacklist_info_log.phone
     *
     * @return risk_blacklist_info_log.phone, 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置 手机号 字段:risk_blacklist_info_log.phone
     *
     * @param phone the value for risk_blacklist_info_log.phone, 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取 操作人 工号 字段:risk_blacklist_info_log.handler_no
     *
     * @return risk_blacklist_info_log.handler_no, 操作人 工号
     */
    public String getHandlerNo() {
        return handlerNo;
    }

    /**
     * 设置 操作人 工号 字段:risk_blacklist_info_log.handler_no
     *
     * @param handlerNo the value for risk_blacklist_info_log.handler_no, 操作人 工号
     */
    public void setHandlerNo(String handlerNo) {
        this.handlerNo = handlerNo == null ? null : handlerNo.trim();
    }

    /**
     * 获取 操作人 姓名 字段:risk_blacklist_info_log.handler_name
     *
     * @return risk_blacklist_info_log.handler_name, 操作人 姓名
     */
    public String getHandlerName() {
        return handlerName;
    }

    /**
     * 设置 操作人 姓名 字段:risk_blacklist_info_log.handler_name
     *
     * @param handlerName the value for risk_blacklist_info_log.handler_name, 操作人 姓名
     */
    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName == null ? null : handlerName.trim();
    }

    /**
     * 获取 操作类型 0拉黑  1洗白 字段:risk_blacklist_info_log.type
     *
     * @return risk_blacklist_info_log.type, 操作类型 0拉黑  1洗白
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置 操作类型 0拉黑  1洗白 字段:risk_blacklist_info_log.type
     *
     * @param type the value for risk_blacklist_info_log.type, 操作类型 0拉黑  1洗白
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取 原因 字段:risk_blacklist_info_log.reason
     *
     * @return risk_blacklist_info_log.reason, 原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置 原因 字段:risk_blacklist_info_log.reason
     *
     * @param reason the value for risk_blacklist_info_log.reason, 原因
     */
    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    /**
     * 获取  字段:risk_blacklist_info_log.create_time
     *
     * @return risk_blacklist_info_log.create_time, 
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置  字段:risk_blacklist_info_log.create_time
     *
     * @param createTime the value for risk_blacklist_info_log.create_time, 
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public RiskBlacklistInfoLog(String sys, String name, String idcard,
			String phone, String handlerNo, String handlerName, Integer type,
			String reason) {
		super();
		this.sys = sys;
		this.name = name;
		this.idcard = idcard;
		this.phone = phone;
		this.handlerNo = handlerNo;
		this.handlerName = handlerName;
		this.type = type;
		this.reason = reason;
	}

	public RiskBlacklistInfoLog() {
		super();
	}


    @Override
    public int compareTo(RiskBlacklistInfoLog o) {
        return this.getCreateTime().compareTo(o.getCreateTime());
    }
}