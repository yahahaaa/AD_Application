package com.yahaha.ad.controller;

import com.alibaba.fastjson.JSON;
import com.yahaha.ad.annotation.IgnoreResponseAdvice;
import com.yahaha.ad.client.SponsorClient;
import com.yahaha.ad.vo.AdPlan;
import com.yahaha.ad.vo.AdPlanGetRequest;
import com.yahaha.ad.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Auther LeeMZ
 * @Date 2021/2/10
 **/
@RestController
@Slf4j
public class SearchController {

    private final RestTemplate restTemplate;

    @Autowired
    private SponsorClient sponsorClient;

    @Autowired
    public SearchController(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    /**
     * 通过RestTemplate和Ribbon实现负载均衡的Http请求方式服务调用
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @IgnoreResponseAdvice //自己定义的注解，不需要使用统一响应格式
    @PostMapping("/getAdPlansByRibbon")
    public CommonResponse<List<AdPlan>> getAdPlansByRibbon(@RequestBody AdPlanGetRequest request){
        log.info("【广告搜索】-> {}", JSON.toJSONString(request));

        return restTemplate.postForEntity("htto://eureka-client-ad-sponsor/ad-sponsor/get/adPlan",
                request,
                CommonResponse.class).getBody();
    }

    /**
     * 通过 feign 方式调用服务
     * @param request
     * @return
     */
    @PostMapping("/getAdPlans")
    @IgnoreResponseAdvice
    public CommonResponse<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request){

        log.info("【广告搜索】-> {}", JSON.toJSONString(request));

        return sponsorClient.getAdPlans(request);
    }
}
