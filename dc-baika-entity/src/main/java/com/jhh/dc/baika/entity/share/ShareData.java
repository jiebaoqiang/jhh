package com.jhh.dc.baika.entity.share;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShareData implements Serializable {

    private static final long serialVersionUID = -2948569777314427595L;

    /**
     * 分享链接
     */
    private String url;
    /**
     * 分享标题
     */
    private String title;
    /**
     * 分享副标题
     */
    private String secTitle;
    /**
     * 分享logo小图片
     */
    private String logo;
    /**
     * 分享说明
     */
    private String descriptions;

}
