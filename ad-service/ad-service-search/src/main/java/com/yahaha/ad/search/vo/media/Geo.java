package com.yahaha.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地理位置信息
 * @Auther LeeMZ
 * @Date 2021/2/20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Geo {

    //经纬度
    private Float latitude;
    private Float longitude;

    private String city;
    private String province;
}
