package com.yahaha.ad.index.district;

import com.yahaha.ad.search.vo.feature.DistrictFeature;
import com.yahaha.ad.utils.CommonUtils;
import com.yahaha.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * @Auther LeeMZ
 * @Date 2021/2/12
 **/
@Component
@Slf4j
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {

    private static Map<String,Set<Long>> districtUnitMap;
    private static Map<Long,Set<String>> unitDistrictMap;

    static{
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        return districtUnitMap.get(key);
    }

    @Override
    @SuppressWarnings("all")
    public void add(String key, Set<Long> value) {

        log.info("UnitDistrictIndex, before add: {}", unitDistrictMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(value);

        for (Long unitId : unitIds) {
            Set<String> districtSet = CommonUtils.getOrCreate(unitId, unitDistrictMap, ConcurrentSkipListSet::new);
            districtSet.add(key);
        }

        log.info("UnitDistrictIndex, after add: {}", unitDistrictMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("UnitDistrictIndex can not support update");
    }

    @Override
    @SuppressWarnings("all")
    public void delete(String key, Set<Long> value) {

        log.info("UnitDistrictIndex, before delete : {}",unitDistrictMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(key, districtUnitMap, ConcurrentSkipListSet::new);

        unitIds.removeAll(value);

        for (Long unitId : unitIds) {
            Set<String> districtSet = CommonUtils.getOrCreate(unitId, unitDistrictMap, ConcurrentSkipListSet::new);
            districtSet.remove(key);
        }

        log.info("UnitDistrictIndex, after delete : {}",unitDistrictMap);
    }

    public static boolean match(Long adUnitId, List<DistrictFeature.ProvinceAndCity> districts){

        if (unitDistrictMap.containsKey(adUnitId) &&
                CollectionUtils.isNotEmpty(unitDistrictMap.get(adUnitId))){

            Set<String> unitDistricts = unitDistrictMap.get(adUnitId);
            List<String> targetDistrict = districts.stream().map(d -> CommonUtils.stringContact(d.getProvince(), d.getCity()))
                    .collect(Collectors.toList());

            return CollectionUtils.isSubCollection(targetDistrict,unitDistricts);
        }
        return false;
    }
}
