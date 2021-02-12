package com.yahaha.ad.index.district;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther LeeMZ
 * @Date 2021/2/12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitDistrictObject {

    private Long unitId;
    private String province;
    private String city;

    //<String,Set<Long>>
    //province-city 通过连字符作为一个字符串key值
}
