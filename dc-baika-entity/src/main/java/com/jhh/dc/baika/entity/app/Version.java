package com.jhh.dc.baika.entity.app;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by OnionMac on 2018/1/26.
 */
@Getter
@Setter
public class Version implements Serializable{

    public static final String VERSIONNAME_IOS = "ios";
    public static final String VERSIONNAME_ANDROID = "android";

    private int id;
    private String clientName;
    private String versionName;
    private int versionCode;
    private int haveNewVersion;
    private int forceUpdate;
    private String downloadUrl;
    private Date createDate;
    private Date updateDate;

    public static void main(String[] args){
        System.out.println(isFixedPhone("0211-67895683"));
    }
    public static boolean isFixedPhone(String fixedPhone){
        String reg="(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";
        return Pattern.matches(reg, fixedPhone);
    }

}
