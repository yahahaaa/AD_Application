package com.yahaha.ad.service.impl;

import com.netflix.discovery.converters.Auto;
import com.yahaha.ad.constant.CommonStatus;
import com.yahaha.ad.constant.Constants;
import com.yahaha.ad.dao.AdPlanRepository;
import com.yahaha.ad.dao.AdUserRepository;
import com.yahaha.ad.entity.AdPlan;
import com.yahaha.ad.entity.AdUser;
import com.yahaha.ad.exception.AdException;
import com.yahaha.ad.service.IAdPlanService;
import com.yahaha.ad.utils.CommonUtils;
import com.yahaha.ad.vo.AdPlanGetRequest;
import com.yahaha.ad.vo.AdPlanRequest;
import com.yahaha.ad.vo.AdPlanResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
@Service
@SuppressWarnings("all")
public class AdPlanServiceImpl implements IAdPlanService {

    @Autowired
    private AdUserRepository userRepository;

    @Autowired
    private AdPlanRepository adPlanRepository;

    @Override
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException {

        //参数校验
        if (!request.createValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        //判断user对象是否存在
        Optional<AdUser> adUser = userRepository.findById(request.getUserId());
        if (!adUser.isPresent()) {
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECODE);
        }

        //判断当前用户是否有重复的推广计划名
        AdPlan adPlan = adPlanRepository.findByUserIdAndPlanName(request.getUserId(), request.getPlanName());
        if (adPlan != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_PLAN_ERROR);
        }

        //保存
        AdPlan newAdPlan = adPlanRepository.save(new AdPlan(request.getUserId(), request.getPlanName(),
                CommonUtils.parseStringDate(request.getStartDate()), CommonUtils.parseStringDate(request.getEndDate())));

        return new AdPlanResponse(newAdPlan.getId(),newAdPlan.getPlanName());
    }

    @Override
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {

        if (!request.validate())
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);

        return adPlanRepository.findAllByIdInAndUserId(request.getIds(),request.getUserId());
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException {

        //参数校验
        if (!request.updateValidate())
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);

        //查询根据id和用户id查询推广计划，若不存在该推广计划，则抛出异常
        AdPlan plan = adPlanRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (plan == null)
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECODE);

        //拷贝推广计划信息
        BeanUtils.copyProperties(request,plan);

        //更新推广计划updateTime，并保存
        plan.setUpdateTime(new Date());
        plan = adPlanRepository.save(plan);
        return new AdPlanResponse(plan.getId(),plan.getPlanName());
    }

    @Override
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws AdException {

        //参数校验
        if (!request.deleteValidate())
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);

        //查询根据id和用户id查询推广计划，若不存在该推广计划，则抛出异常
        AdPlan plan = adPlanRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (plan == null)
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECODE);

        //逻辑删除
        plan.setPlanStatus(CommonStatus.INVALID.getCode());
        plan.setUpdateTime(new Date());

        adPlanRepository.save(plan);
    }
}
