package com.yahaha.ad.constant;

import lombok.Getter;

/**
 * 创意类型
 */
@Getter
public enum CreativeType {

    IMAGE(1,"图片"),
    VIDEO(2,"文本"),
    TEXT(3,"文本");

    private int code;
    private String desc;

    CreativeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
