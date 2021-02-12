package com.yahaha.ad.index.keyword;

import com.yahaha.ad.client.utils.CommonUtils;
import com.yahaha.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sun.awt.windows.WPrinterJob;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Auther LeeMZ
 * @Date 2021/2/11
 **/
@Component
@Slf4j
public class UnitKeywordIndex implements IndexAware<String,Set<Long>> {

    private static Map<String, Set<Long>> keywordUnitMap;
    private static Map<Long,Set<String>> unitKeywordMap;

    static{
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {

        if (StringUtils.isEmpty(key))
            return Collections.emptySet();

        Set<Long> result = keywordUnitMap.get(key);
        if (result == null)
            return Collections.emptySet();

        return result;
    }

    @Override
    public void add(String key, Set<Long> value) {

        log.info("UnitKeywordIndex, before add: {}",unitKeywordMap);
        //操作keywordUnitMap;
        //如果通过当前key获取不到value值，则创建一个线程安全的set空集合，如果获取到了，则直接返回已经存在的set集合
        Set<Long> unitIdSet = CommonUtils.getOrCreate(key,keywordUnitMap,ConcurrentSkipListSet::new);
        //向集合中添加新的值
        unitIdSet.addAll(value);

        //操作unitKeywordMap;将当前keyword插入以unitId为key的map集合中
        for (Long unitId : value){
            //当前unitId中的keyword集合
            Set<String> keywordSet = CommonUtils.getOrCreate(unitId, unitKeywordMap, ConcurrentSkipListSet::new);
            keywordSet.add(key);
        }

        log.info("UnitKeywordIndex,after add:{}",unitKeywordMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("keyword index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {

        log.info("UnitKeywordIndex,before delete : {}",unitKeywordMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(key,keywordUnitMap,ConcurrentSkipListSet::new);
        unitIds.removeAll(value);

        for (Long unitId : unitIds) {
            Set<String> keywordSet = CommonUtils.getOrCreate(unitId, unitKeywordMap, ConcurrentSkipListSet::new);
            keywordSet.remove(key);
        }

        log.info("UnitKeywordIndex,after delete : {}",unitKeywordMap);
    }

    public boolean match(Long unitId, List<String> keywords){

        //unitKeywordMap包含unitId这个键，且键对应的值也不为空
        if (unitKeywordMap.containsKey(unitId) && CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))){

            Set<String> unitKeywords = unitKeywordMap.get(unitId);
            return CollectionUtils.isSubCollection(keywords,unitKeywords); //当且仅当第一个参数是第二个参数的子集合时返回true
        }
        return false;
    }
}
