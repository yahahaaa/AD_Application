package com.yahaha.ad.dao;

import com.yahaha.ad.entity.AdPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
public interface AdPlanRepository extends JpaRepository<AdPlan,Long> {

    /**
     * 精确查找广告计划
     * @param id
     * @param userId
     * @return
     */
    AdPlan findByIdAndUserId(Long id, Long userId);

    /**
     * 给出广告计划id列表，查询该列表下所有属于userId的广告计划
     * @param ids
     * @param userId
     * @return
     */
    List<AdPlan> findAllByIdInAndUserId (List<Long> ids, Long userId);

    /**
     * 保证对于每个用户而言，用户计划是需要唯一的
     * @param userId
     * @param planName
     * @return
     */
    AdPlan findByUserIdAndPlanName(Long userId,String planName);

    List<AdPlan> findAllByPlanStatus(Integer status);
}
