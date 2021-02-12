package com.yahaha.ad.index.adplan;

import com.yahaha.ad.client.vo.AdPlan;
import com.yahaha.ad.index.IndexAware;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther LeeMZ
 * @Date 2021/2/10
 **/
@Slf4j
@Component
public class AdPlanIndex implements IndexAware<Long,AdPlanObject> {

    private static Map<Long,AdPlanObject> objectMap;

    static{
        objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdPlanObject get(Long key) {

        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdPlanObject value) {

        log.info("before add：{}",objectMap);
        objectMap.put(key,value);
        log.info("after add：{}",objectMap);

    }

    @Override
    public void update(Long key, AdPlanObject value) {

        log.info("before update:{}",objectMap);
        AdPlanObject oldObject = objectMap.get(key);
        if (null == oldObject)
            objectMap.put(key,value);
        else
            oldObject.update(value);
        log.info("after update:{}",objectMap);
    }

    @Override
    public void delete(Long key, AdPlanObject value) {

        log.info("before delete:{}",objectMap);
        objectMap.remove(key);
        log.info("after delete:{}",objectMap);

    }
}
