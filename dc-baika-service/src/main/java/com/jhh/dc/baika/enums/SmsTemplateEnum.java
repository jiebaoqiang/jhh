package com.jhh.dc.baika.enums;

/**
 * 短信模板枚举类
 */
public enum SmsTemplateEnum {
    /**注册短信模板ID*/
    register_sms_template(100001),
    /**找回密码短信模板ID*/
    forgetpassword_sms_template(200001);

    private Integer templateId;

    public Integer getTemplateId() {
        return templateId;
    }

    SmsTemplateEnum(Integer templateId) {
        this.templateId = templateId;
    }

    public static boolean contains(String rulerName) {
        SmsTemplateEnum[] properties = values();
        for (SmsTemplateEnum property : properties) {
            if (property.name().equals(rulerName)) {
                return true;
            }
        }
        return false;
    }

}
