package com.yahaha.ad.client;

import com.yahaha.ad.vo.AdPlan;
import com.yahaha.ad.vo.AdPlanGetRequest;
import com.yahaha.ad.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Auther LeeMZ
 * @Date 2021/2/10
 **/
@FeignClient(value = "eureka-client-ad-sponsor",fallback = SponsorClientHystrix.class)
public interface SponsorClient {

    /**
     * 调用 eureka-client-ad-sponsor 服务
     * @param request
     * @return
     */
    @PostMapping("/ad-sponsor/get/adPlan")
    CommonResponse<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request);
}
