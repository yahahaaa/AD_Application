package com.yahaha.ad.constant;

import lombok.Getter;

/**
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
@Getter
public enum CreativeMaterialType {

    JPG(1,"jpg"),
    BMP(2,"bmp"),

    MP4(3,"mp4"),
    AVI(4,"avi"),

    TXT(5,"txt");

    private Integer code;
    private String desc;

    CreativeMaterialType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
