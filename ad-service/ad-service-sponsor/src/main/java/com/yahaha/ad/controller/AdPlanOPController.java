package com.yahaha.ad.controller;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.regexp.internal.RE;
import com.yahaha.ad.entity.AdPlan;
import com.yahaha.ad.exception.AdException;
import com.yahaha.ad.service.IAdPlanService;
import com.yahaha.ad.vo.AdPlanGetRequest;
import com.yahaha.ad.vo.AdPlanRequest;
import com.yahaha.ad.vo.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther LeeMZ
 * @Date 2021/2/9
 **/
@Slf4j
@RestController
public class AdPlanOPController {

    @Autowired
    private IAdPlanService planService;

    @PostMapping("/create/adPlan")
    public AdPlanResponse createAdPlan(@RequestBody AdPlanRequest request) throws AdException {

        log.info("【创建推广计划】推广计划信息为 -> {}", JSON.toJSONString(request));

        return planService.createAdPlan(request);
    }

    @PostMapping("/get/adPlan")
    public List<AdPlan> getAdPlanByIds(@RequestBody AdPlanGetRequest request) throws AdException{

        log.info("【获取推广计划】，获取人-> {},获取的planId为-> {}",request.getUserId(),request.getIds());

        return planService.getAdPlanByIds(request);
    }

    @PutMapping("/update/adPlan")
    public AdPlanResponse updateAdPlan(@RequestBody AdPlanRequest request) throws AdException{

        log.info("【更新推广计划】更新为--> {}",JSON.toJSONString(request));

        return planService.updateAdPlan(request);
    }

    @DeleteMapping("/delete/adPlan")
    public void deleteAdPlan(@RequestBody AdPlanRequest request) throws AdException{

        log.info("【删除推广计划】--> {}",JSON.toJSONString(request));
        planService.deleteAdPlan(request);
    }
}
