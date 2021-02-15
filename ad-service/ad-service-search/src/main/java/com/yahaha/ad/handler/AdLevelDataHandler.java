package com.yahaha.ad.handler;

import com.alibaba.fastjson.JSON;
import com.yahaha.ad.dump.table.*;
import com.yahaha.ad.index.DataTable;
import com.yahaha.ad.index.IndexAware;
import com.yahaha.ad.index.adplan.AdPlanIndex;
import com.yahaha.ad.index.adplan.AdPlanObject;
import com.yahaha.ad.index.adunit.AdUnitIndex;
import com.yahaha.ad.index.adunit.AdUnitObject;
import com.yahaha.ad.index.creative.CreativeIndex;
import com.yahaha.ad.index.creative.CreativeObject;
import com.yahaha.ad.index.creativeunit.CreativeUnitIndex;
import com.yahaha.ad.index.creativeunit.CreativeUnitObject;
import com.yahaha.ad.index.district.UnitDistrictIndex;
import com.yahaha.ad.index.interest.UnitItIndex;
import com.yahaha.ad.index.keyword.UnitKeywordIndex;
import com.yahaha.ad.mysql.constant.OpType;
import com.yahaha.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 1.索引之间存在着层级的划分，也就是依赖关系的划分
 * 2.加载全量索引其实是增量所以 “添加” 的一种特殊实现
 * @Auther LeeMZ
 * @Date 2021/2/13
 **/
@Slf4j
public class AdLevelDataHandler {

    /**
     * 第二层级
     * 添加特殊的全量索引（不需要依赖其他索引，如推广计划和创意）
     * @param planTable
     * @param type
     */
    public static void handleLevel2(AdPlanTable planTable, OpType type){
        AdPlanObject planObject = new AdPlanObject(
                planTable.getId(),
                planTable.getUserId(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate());

        handleBinlogEvent(DataTable.of(AdPlanIndex.class),planObject.getPlanId(),planObject,OpType.ADD);
    }

    public static void handleLevel2(AdCreativeTable creativeTable, OpType type){
        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl());

        handleBinlogEvent(DataTable.of(CreativeIndex.class),creativeObject.getAdId(),creativeObject,OpType.ADD);
    }

    public static void handleLevel3(AdUnitTable unitTable, OpType type){

        AdPlanObject planObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        //如果推广单元关联的推广计划不存在，则打印错误日志
        if (planObject == null){
            log.error("handleLevel3 found AdPlanObject error: {}",unitTable.getPlanId());
        }

        AdUnitObject unitObject = new AdUnitObject(
                unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlanId(),
                planObject
        );
        handleBinlogEvent(DataTable.of(AdUnitIndex.class),unitTable.getUnitId(),unitObject,OpType.ADD);
    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type){

        if (type == OpType.UPDATE){
            log.error("CreativeUnitIndex not support update");
            return;
        }

        //检验其依赖索引是否已经存在
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        if (unitObject == null || creativeObject == null){
            log.error("CreativeUnitIndex index error: {}", JSON.toJSONString(creativeUnitTable));
            return;
        }

        //构建索引对象
        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(creativeUnitTable.getAdId(),creativeUnitTable.getUnitId());

        handleBinlogEvent(
                DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringContact(
                        creativeUnitObject.getAdId().toString(),
                        creativeUnitObject.getUnitId().toString()
                ),
                creativeUnitObject,
                type
        );
    }

    public static void handleLevel4(AdUnitDistrictTable districtTable, OpType type){

        if (type == OpType.UPDATE){
            log.error("district index can not support update");
            return;
        }

        //检验其依赖索引是否已经存在
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(districtTable.getUnitId());
        if (unitObject == null){
            log.error("AdUnitDistrictTable index error: {}",districtTable.getUnitId());
            return;
        }

        String key = CommonUtils.stringContact(districtTable.getProvince(),districtTable.getCity());

        Set<Long> value = new HashSet<>(
                Collections.singleton(districtTable.getUnitId()) //singleton集合是不可变集合，一旦创建不能更改
        );

        handleBinlogEvent(DataTable.of(UnitDistrictIndex.class),key,value,type);

    }

    public static void handleLevel4(AdUnitKeywordTable keywordTable, OpType type){

        if (type == OpType.UPDATE){
            log.error("AdUnitKeywordIndex can not support update");
            return;
        }

        //判断依赖索引是否存在
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(keywordTable.getUnitId());
        if (unitObject == null){
            log.error("AdUnitKeywordIndex index error: {}",keywordTable.getUnitId());
            return;
        }

        //构建value，value为Set<Long>类型，存储key对应的unitId
        Set<Long> value = new HashSet<>(
                Collections.singleton(keywordTable.getUnitId())
        );

        handleBinlogEvent(DataTable.of(UnitKeywordIndex.class),keywordTable.getKeyword(),value,type);
    }

    public static void handleLevel4(AdUnitItTable itTable, OpType type){

        if (type == OpType.UPDATE){
            log.error("AdUnitItIndex can not support update");
            return;
        }

        //判断依赖索引是否存在
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(itTable.getUnitId());
        if (unitObject == null){
            log.error("AdUnitItTable index error : {}",itTable.getUnitId());
            return;
        }

        //构建value，value为Set<Long>类型，存储key对应的unitId
        Set<Long> value = new HashSet<>(
                Collections.singleton(itTable.getUnitId())
        );

        handleBinlogEvent(DataTable.of(UnitItIndex.class),itTable.getItTag(),value,type);
    }

    /**
     * 操作索引方法
     * @param index
     * @param key
     * @param value
     * @param type
     * @param <K>
     * @param <V>
     */
    private static <K,V> void handleBinlogEvent(IndexAware<K,V> index, K key, V value, OpType type){
        switch (type){
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;
            case DELETE:
                index.delete(key,value);
                break;
            default:
                break;
        }
    }
}
