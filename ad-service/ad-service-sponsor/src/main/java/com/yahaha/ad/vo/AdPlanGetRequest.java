package com.yahaha.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 根据userId和planId列表批量获取
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdPlanGetRequest {

    private Long userId;
    private List<Long> ids;

    public boolean validate(){
        return userId != null && !CollectionUtils.isEmpty(ids);
    }
}
