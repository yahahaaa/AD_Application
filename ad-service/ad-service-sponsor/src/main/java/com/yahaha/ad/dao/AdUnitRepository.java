package com.yahaha.ad.dao;

import com.yahaha.ad.entity.AdUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
public interface AdUnitRepository extends JpaRepository<AdUnit,Long> {

    AdUnit findByPlanIdAndUnitName(Long planId,String unitName);

    List<AdUnit> findAllByUnitStatus(Integer unitStatus);

}
