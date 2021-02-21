package com.yahaha.ad.search.vo;

import com.yahaha.ad.search.vo.feature.DistrictFeature;
import com.yahaha.ad.search.vo.feature.FeatureRelation;
import com.yahaha.ad.search.vo.feature.ItFeature;
import com.yahaha.ad.search.vo.feature.KeywordFeature;
import com.yahaha.ad.search.vo.media.AdSlot;
import com.yahaha.ad.search.vo.media.App;
import com.yahaha.ad.search.vo.media.Device;
import com.yahaha.ad.search.vo.media.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther LeeMZ
 * @Date 2021/2/20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    // 媒体方的请求标识
    private String mediaId;
    // 请求基本信息
    private RequestInfo requestInfo;
    // 请求匹配信息
    private FeatureInfo featureInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestInfo{

        private String requestId;

        private List<AdSlot> adSlotList;
        private App app;
        private Geo geo;
        private Device device;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeatureInfo{

        private KeywordFeature keywordFeature;
        private DistrictFeature districtFeature;
        private ItFeature itFeature;

        private FeatureRelation relation = FeatureRelation.AND;
    }
}
