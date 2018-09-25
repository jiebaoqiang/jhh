package com.jhh.dc.baika.common.util;

import java.util.regex.Pattern;

/**
 * @author xingmin
 */
public class RegexUtil {
    private static Pattern isNumberPattern = Pattern.compile("^-?[0-9]+");

    public static boolean isNumber(String param) {
        return isNumberPattern.matcher(param).matches();
    }
}
