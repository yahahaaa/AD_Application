package com.yahaha.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * 用户创建和更新推广计划
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdPlanRequest {

    private Long id;

    private Long userId;

    private String planName;

    private String startDate;

    private String endDate;

    //创建推广计划参数校验
    public boolean createValidate(){

        return userId != null && !StringUtils.isEmpty(planName)
                && !StringUtils.isEmpty(startDate)
                && !StringUtils.isEmpty(endDate);
    }

    //更新推广计划校验
    public boolean updateValidate(){

        return id != null && userId != null;
    }

    //删除推广计划校验
    public boolean deleteValidate(){
        return id != null && userId != null;
    }
}
