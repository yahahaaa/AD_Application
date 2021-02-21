package com.yahaha.ad.index.creativeunit;

import com.yahaha.ad.index.IndexAware;
import com.yahaha.ad.index.adunit.AdUnitObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Auther LeeMZ
 * @Date 2021/2/12
 **/
@Component
@Slf4j
@SuppressWarnings("all")
public class CreativeUnitIndex implements IndexAware<String,CreativeUnitObject> {

    //<adId-unitId , CreativeUnitObject>
    private static Map<String,CreativeUnitObject> objectMap;

    //<adId , Set<unitId>>
    private static Map<Long, Set<Long>> creativeUnitMap;

    //<unitId , Set<adId>>
    private static Map<Long,Set<Long>> unitCreativeMap;

    static {
        objectMap = new ConcurrentHashMap<>();
        creativeUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }

    @Override
    public CreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    @Override
    public void add(String key, CreativeUnitObject value) {

        log.info("creativeUnitIndex, before add : {}",objectMap);
        objectMap.put(key,value);
        Set<Long> unitSet = creativeUnitMap.get(value.getAdId());
        //如果unitSet为空就创建一个set
        if (CollectionUtils.isEmpty(unitSet)){
            unitSet = new ConcurrentSkipListSet<>();
            creativeUnitMap.put(value.getAdId(),unitSet);//需要创建要给空集合，防止空指针异常
        }
        //不为空了，就添加新数据
        unitSet.add(value.getUnitId());

        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());
        if (CollectionUtils.isEmpty(creativeSet)){
            creativeSet = new ConcurrentSkipListSet<>();
            unitCreativeMap.put(value.getUnitId(),creativeSet);
        }
        creativeSet.add(value.getAdId());
        log.info("creativeUnitIndex, after add : {}",objectMap);
    }

    @Override
    public void update(String key, CreativeUnitObject value) {
        log.error("CreativeUnitIndex can not support update");
    }

    @Override
    public void delete(String key, CreativeUnitObject value) {

        log.info("CreativeUnitIndex before delete: {}", objectMap);

        objectMap.remove(key);

        Set<Long> unitSet = creativeUnitMap.get(value.getAdId());
        if (CollectionUtils.isNotEmpty(unitSet)) {
            unitSet.remove(value.getUnitId());
        }

        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());
        if (CollectionUtils.isNotEmpty(creativeSet)) {
            creativeSet.remove(value.getAdId());
        }

        log.info("CreativeUnitIndex after delete: {}", objectMap);
    }

    /**
     * 根据 AdUnitObject 查找对应的创意Id
     * @param unitObjects
     * @return
     */
    public List<Long> selectAds(List<AdUnitObject> unitObjects){

        if (CollectionUtils.isEmpty(unitObjects)){
            return Collections.emptyList();
        }
        List<Long> result = new ArrayList<>();
        for (AdUnitObject unitObject : unitObjects) {
            Set<Long> adIds = unitCreativeMap.get(unitObject.getUnitId());
            if (CollectionUtils.isNotEmpty(adIds)){
                result.addAll(adIds);
            }
        }
        return result;
    }

}
