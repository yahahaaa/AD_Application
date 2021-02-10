package com.yahaha.ad.client;

import com.yahaha.ad.client.vo.AdPlan;
import com.yahaha.ad.client.vo.AdPlanGetRequest;
import com.yahaha.ad.vo.CommonResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SponsorClientHystrix implements SponsorClient {

    /**
     * 服务降级
     * @param request
     * @return
     */
    @Override
    public CommonResponse<List<AdPlan>> getAdPlans(AdPlanGetRequest request) {

        return new CommonResponse<>(1,"eureka-client-ad-sponsor error");
    }
}
