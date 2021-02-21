package com.yahaha.ad.search.impl;

import com.alibaba.fastjson.JSON;
import com.yahaha.ad.index.CommonStatus;
import com.yahaha.ad.index.DataTable;
import com.yahaha.ad.index.adunit.AdUnitIndex;
import com.yahaha.ad.index.adunit.AdUnitObject;
import com.yahaha.ad.index.creative.CreativeIndex;
import com.yahaha.ad.index.creative.CreativeObject;
import com.yahaha.ad.index.creativeunit.CreativeUnitIndex;
import com.yahaha.ad.index.district.UnitDistrictIndex;
import com.yahaha.ad.index.interest.UnitItIndex;
import com.yahaha.ad.index.keyword.UnitKeywordIndex;
import com.yahaha.ad.search.ISearch;
import com.yahaha.ad.search.vo.SearchRequest;
import com.yahaha.ad.search.vo.SearchResponse;
import com.yahaha.ad.search.vo.feature.DistrictFeature;
import com.yahaha.ad.search.vo.feature.FeatureRelation;
import com.yahaha.ad.search.vo.feature.ItFeature;
import com.yahaha.ad.search.vo.feature.KeywordFeature;
import com.yahaha.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Auther LeeMZ
 * @Date 2021/2/20
 **/
@Slf4j
@Component
public class SearchImpl implements ISearch {

    @Override
    public SearchResponse fetchAds(SearchRequest request) {

        //请求的广告位信息
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlotList();

        //三个Feature
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        ItFeature itFeature = request.getFeatureInfo().getItFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        FeatureRelation relation = request.getFeatureInfo().getRelation();

        //构造响应对象
        SearchResponse response = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();

        for (AdSlot adSlot: adSlots){
            Set<Long> targetUnitIdSet;

            //第一步，根据流量类型获取初始 AdUnit（是开屏还是贴片还是贴片暂停等等其他的广告流量类型）
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());

            //第二部，根据keyword、it和district过滤推广单元
            if (relation == FeatureRelation.AND){
                filterKeywordFeature(adUnitIdSet,keywordFeature);
                filterItFeature(adUnitIdSet,itFeature);
                filterDistrictFeature(adUnitIdSet,districtFeature);

                targetUnitIdSet = adUnitIdSet;
            }else{
                targetUnitIdSet = getORRelationUnitIds(adUnitIdSet,keywordFeature,itFeature,districtFeature);
            }

            //第三步，根据推广单元状态和推广单元绑定的推广计划的状态过滤推广单元
            List<AdUnitObject> unitObjects = DataTable.of(AdUnitIndex.class).fetch(targetUnitIdSet);
            filterAdUnitAndPlanStatus(unitObjects,CommonStatus.VALID);

            //第四步，根据推广单元获取创意单元
            List<Long> adIds = DataTable.of(CreativeUnitIndex.class).selectAds(unitObjects);
            List<CreativeObject> creativeObjects = DataTable.of(CreativeIndex.class).fetch(adIds);

            //第五步，通过 AdSlot 实现对创意的过滤
            filterCreativeByAdSlot(creativeObjects,adSlot.getWidth(),adSlot.getHeight(),adSlot.getType());

            //第六步，一个广告位只返回一个广告创意信息
            adSlot2Ads.put(adSlot.getAdSlotCode(),buildCreativeResponse(creativeObjects));
        }

        log.info("fetchAds: {}->{}", JSON.toJSONString(request),JSON.toJSONString(response));
        return response;
    }

    /**
     * 通过AdUnitObject中的状态以及关联的AdPlan中的状态过滤推广计划
     * @param unitObjects
     * @param commonStatus
     */
    private void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects,
                                           CommonStatus commonStatus){

        if (CollectionUtils.isEmpty(unitObjects))
            return;

        CollectionUtils.filter(
                unitObjects,
                object -> object.getUnitStatus().equals(commonStatus.getCode())
                && object.getAdPlanObject().getPlanStatus().equals(commonStatus.getCode())
        );
    }

    /**
     * 根据 keyword 过滤推广单元
     * @param adUnitIds
     * @param keywordFeature
     */
    private void filterKeywordFeature(Collection<Long> adUnitIds, KeywordFeature keywordFeature){

        if (CollectionUtils.isEmpty(adUnitIds))
            return;

        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())){
            CollectionUtils.filter(
                    adUnitIds,
                    //返回true则保留adUnitId，返回false则舍弃adUnitId
                    adUnitId -> DataTable.of(UnitKeywordIndex.class).match(adUnitId,keywordFeature.getKeywords())
            );
        }
    }

    /**
     * 通过 SearchRequest中传入的slot过滤 创意单元
     * @param creativeList
     * @param width
     * @param height
     * @param type
     */
    private void filterCreativeByAdSlot(List<CreativeObject> creativeList,
                                        Integer width,
                                        Integer height,
                                        List<Integer> type){

        if (CollectionUtils.isEmpty(creativeList))
            return;

        CollectionUtils.filter(
                creativeList,
                creative -> creative.getAuditStatus().equals(CommonStatus.VALID.getCode())
                && creative.getWidth().equals(width)
                && creative.getHeight().equals(height)
                && type.contains(creative.getType())
        );
    }

    private void filterItFeature(Collection<Long> adUnitIds, ItFeature itFeature){

        if (CollectionUtils.isEmpty(adUnitIds))
            return;

        if (CollectionUtils.isNotEmpty(itFeature.getIts())){
            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId -> DataTable.of(UnitItIndex.class).match(adUnitId,itFeature.getIts())
            );
        }
    }

    private void filterDistrictFeature(Collection<Long> adUnitIds, DistrictFeature districtFeature){

        if (CollectionUtils.isEmpty(adUnitIds))
            return;

        if (CollectionUtils.isNotEmpty(districtFeature.getDistricts())){
            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId -> DataTable.of(UnitDistrictIndex.class).match(adUnitId,districtFeature.getDistricts())
            );
        }
    }

    private Set<Long> getORRelationUnitIds(Set<Long> adUnitIdSet,
                                           KeywordFeature keywordFeature,
                                           ItFeature itFeature,
                                           DistrictFeature districtFeature){

        if (CollectionUtils.isEmpty(adUnitIdSet))
            return Collections.emptySet();

        Set<Long> keywordUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> itUnitIdSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordUnitIdSet,keywordFeature);
        filterItFeature(itUnitIdSet,itFeature);
        filterDistrictFeature(districtUnitIdSet,districtFeature);

        //返回并集合
        return new HashSet<>(CollectionUtils.union(CollectionUtils.union(keywordUnitIdSet,itUnitIdSet), districtUnitIdSet));
    }

    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creatives){
        if (CollectionUtils.isEmpty(creatives))
            return Collections.emptyList();

        CreativeObject randomObject = creatives.get(Math.abs(new Random().nextInt()) % creatives.size());

        return Collections.singletonList(
                SearchResponse.convert(randomObject)
        );
    }
}
