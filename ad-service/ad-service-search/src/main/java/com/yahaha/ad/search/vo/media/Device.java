package com.yahaha.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备信息
 * @Auther LeeMZ
 * @Date 2021/2/20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    //设备 id
    private String deviceCode;

    //mac 地址
    private String mac;

    //ip
    private String ip;

    //机型编码
    private String model;

    //分辨率尺寸
    private String displaySize;

    //屏幕尺寸
    private String screenSize;

    //设备序列号
    private String serialName;
}
