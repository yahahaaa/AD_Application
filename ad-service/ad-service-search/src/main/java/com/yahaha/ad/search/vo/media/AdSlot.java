package com.yahaha.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 广告位信息
 * @Auther LeeMZ
 * @Date 2021/2/20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdSlot {

    // 广告位的编码
    private String adSlotCode;

    // 流量类型
    private Integer positionType;

    // 广告位的宽和高
    private Integer width;
    private Integer height;

    // 广告的类型
    private List<Integer> type;

    // 最低出价
    private Integer minCpm;

}
