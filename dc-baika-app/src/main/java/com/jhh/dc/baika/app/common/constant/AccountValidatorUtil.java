package com.jhh.dc.baika.app.common.constant;

import java.util.regex.Pattern;

/**
 * 2018/7/9.
 */
public class AccountValidatorUtil {

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6-8])|(17[0,3,5-8])|(18[0-9])|(19[8,9])|(147))\\d{8}$";


    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }


}
