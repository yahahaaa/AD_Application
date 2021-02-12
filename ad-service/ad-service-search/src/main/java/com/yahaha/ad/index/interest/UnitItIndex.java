package com.yahaha.ad.index.interest;

import com.yahaha.ad.utils.CommonUtils;
import com.yahaha.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Auther LeeMZ
 * @Date 2021/2/12
 **/
@Component
@Slf4j
public class UnitItIndex implements IndexAware<String, Set<Long>> {

    //反向索引， 通过兴趣tag可以查找拥有该tag的所有推广单元
    private static Map<String,Set<Long>> itUnitMap;
    //正向索引，通过推广单元，查找该推广单元下的所有兴趣tag
    private static Map<Long,Set<String>> unitItMap;

    static {
        itUnitMap = new ConcurrentHashMap<>();
        unitItMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        return itUnitMap.get(key);
    }

    @Override
    @SuppressWarnings("all")
    public void add(String key, Set<Long> value) {

        log.info("UnitItIndex, before add: {}",unitItMap);
        Set<Long> unitIds = CommonUtils.getOrCreate(key, itUnitMap, ConcurrentSkipListSet::new);

        unitIds.addAll(unitIds);

        for (Long unitId : unitIds) {
            Set<String> its = CommonUtils.getOrCreate(unitId, unitItMap, ConcurrentSkipListSet::new);
            its.add(key);
        }

        log.info("UnitItIndex, after add: {}", unitItMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("it index can not support update");
    }

    @Override
    @SuppressWarnings("all")
    public void delete(String key, Set<Long> value) {
        log.info("UnitItIndex, before delete: {}", unitItMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(key, itUnitMap, ConcurrentSkipListSet::new);

        unitIds.removeAll(value);

        for (Long unitId : unitIds) {
            Set<String> itTagSet = CommonUtils.getOrCreate(unitId, unitItMap, ConcurrentSkipListSet::new);

            itTagSet.remove(key);
        }

        log.info("UnitItIndex, after delete: {}", unitItMap);
    }

    public boolean match(Long unitId, List<String> itTags){

        if (unitItMap.containsKey(unitId) && CollectionUtils.isNotEmpty(unitItMap.get(unitId))){

            Set<String> itTagSet = unitItMap.get(unitId);

            return CollectionUtils.isSubCollection(itTags,itTagSet); //判断是否为子集
        }
        return false;
    }
}
