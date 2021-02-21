package com.yahaha.ad.search.vo;

import com.yahaha.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther LeeMZ
 * @Date 2021/2/20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {

    public Map<String,List<Creative>> adSlot2Ads = new HashMap<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Creative{

        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;

        //展示检测URL (这里做了简化，检测URL应该定义到Creative的数据库字段中)
        private List<String> showMonitorUrl = Arrays.asList("wwww.imooc.com","www.imooc.com");
        //点击监测URL
        private List<String> clickMonitorUrl = Arrays.asList("wwww.imooc.com","www.imooc.com");
    }

    public static Creative convert(CreativeObject object){

        Creative creative = new Creative();
        creative.setAdId(object.getAdId());
        creative.setAdUrl(object.getAdUrl());
        creative.setWidth(object.getWidth());
        creative.setHeight(object.getHeight());
        creative.setType(object.getType());
        creative.setMaterialType(object.getMaterialType());

        return creative;
    }
}
