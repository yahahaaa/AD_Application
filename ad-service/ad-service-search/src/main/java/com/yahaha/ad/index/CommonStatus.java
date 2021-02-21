package com.yahaha.ad.index;

import lombok.Getter;

/**
 * @Auther LeeMZ
 * @Date 2021/2/21
 **/
@Getter
public enum  CommonStatus {

    VALID(1,"有效状态"),
    INVALID(2,"无效状态");

    private Integer code;
    private String message;

    CommonStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
