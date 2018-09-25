package com.jhh.dc.baika.entity.app_vo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 *  手机评估参数
 */
@Setter
@Getter
@ToString
public class PhoneInfoVo implements Serializable{
    private static final long serialVersionUID = 5991581349070279494L;
    @NonNull
    private String per_id;
    @NonNull
    private String phoneInfo;
    @NonNull
    private String device;

    private String sequence;

}
