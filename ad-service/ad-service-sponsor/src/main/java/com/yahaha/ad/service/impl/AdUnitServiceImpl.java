package com.yahaha.ad.service.impl;

import com.netflix.discovery.converters.Auto;
import com.yahaha.ad.constant.Constants;
import com.yahaha.ad.dao.AdPlanRepository;
import com.yahaha.ad.dao.AdUnitRepository;
import com.yahaha.ad.dao.CreativeRepository;
import com.yahaha.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.yahaha.ad.dao.unit_condition.AdUnitItRepository;
import com.yahaha.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.yahaha.ad.dao.unit_condition.CreativeUnitRepository;
import com.yahaha.ad.entity.AdPlan;
import com.yahaha.ad.entity.AdUnit;
import com.yahaha.ad.entity.unit_condition.AdUnitDistrict;
import com.yahaha.ad.entity.unit_condition.AdUnitIt;
import com.yahaha.ad.entity.unit_condition.AdUnitKeyword;
import com.yahaha.ad.entity.unit_condition.CreativeUnit;
import com.yahaha.ad.exception.AdException;
import com.yahaha.ad.service.IAdUnitService;
import com.yahaha.ad.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auther LeeMZ
 * @Date 2021/2/7
 **/
@Service
@SuppressWarnings("all")
public class AdUnitServiceImpl implements IAdUnitService {

    @Autowired
    private AdUnitRepository unitRepository;

    @Autowired
    private AdPlanRepository planRepository;

    @Autowired
    private AdUnitKeywordRepository unitKeywordRepository;

    @Autowired
    private AdUnitItRepository unitItRepository;

    @Autowired
    private AdUnitDistrictRepository unitDistrictRepository;

    @Autowired
    private CreativeRepository creativeRepository;

    @Autowired
    private CreativeUnitRepository creativeUnitRepository;

    @Override
    @Transactional
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {

        //参数校验
        if (!request.createValidate())
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);

        //判断推广计划是否存在
        Optional<AdPlan> plan = planRepository.findById(request.getPlanId());
        if (!plan.isPresent())
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECODE);

        //查询当前推广单元是否存在
        AdUnit adUnit = unitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if (adUnit != null)
            throw new AdException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);

        //保存
        AdUnit newAdUnit = unitRepository.save(new AdUnit(request.getPlanId(), request.getUnitName(),
                request.getPositionType(), request.getBudget()));
        return new AdUnitResponse(newAdUnit.getId(),newAdUnit.getUnitName());
    }

    @Override
    @Transactional
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {

        //检测reqeust中的uninid是否存在
        List<Long> unitIds = request.getUnitKeywords().stream().map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();
        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitKeywords())){
            request.getUnitKeywords().stream().map(e->unitKeywords.add(new AdUnitKeyword(e.getUnitId(),e.getKeyword())));
            ids = unitKeywordRepository.saveAll(unitKeywords).stream().map(AdUnitKeyword::getId).collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(ids);
    }

    @Override
    @Transactional
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {

        //检测request中的unitid是否存在
        List<Long> unitIds = request.getUnitIts().stream().map(AdUnitItRequest.UnitIt::getUnitId).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds))
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);

        List<Long> ids = Collections.emptyList(); //接收返回值集合
        List<AdUnitIt> unitIts = new ArrayList<>(); //数据库字段映射对象集合
        if (!CollectionUtils.isEmpty(request.getUnitIts())){
            request.getUnitIts().stream().map(e->unitIts.add(new AdUnitIt(e.getUnitId(),e.getItTag())));//填充数据库映射对象集合
            ids = unitItRepository.saveAll(unitIts).stream().map(AdUnitIt::getId).collect(Collectors.toList());//调用Dao层保存数据库
        }
        return new AdUnitItResponse(ids);
    }

    @Override
    @Transactional
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {

        //检测request中的unitid是否存在
        List<Long> unitIds = request.getUnitDistricts().stream().map(AdUnitDistrictRequest.UnitDistrict::getUnitId).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds))
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);

        List<Long> ids = Collections.emptyList(); //接收返回值集合
        List<AdUnitDistrict> unitDistricts = new ArrayList<>();// 数据库字段映射对象集合
        if (!CollectionUtils.isEmpty(request.getUnitDistricts())){
            request.getUnitDistricts().stream().map(e->unitDistricts.add(new AdUnitDistrict(e.getUnitId(),e.getProvince(),e.getCity())));//填充数据库映射对象集合
            ids = unitDistrictRepository.saveAll(unitDistricts).stream().map(AdUnitDistrict::getId).collect(Collectors.toList());//调用Dao层保存至数据库
        }
        return new AdUnitDistrictResponse(ids);
    }

    @Override
    @Transactional
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {

        //需要验证关联的创意和推广单元是否都存在
        //首先需要获取两个ids
        List<Long> unit_ids = request.getCreativeUnitItemList().stream().map(CreativeUnitRequest.CreativeUnitItem::getUnitId).collect(Collectors.toList());
        List<Long> creative_ids = request.getCreativeUnitItemList().stream().map(CreativeUnitRequest.CreativeUnitItem::getCreativeId).collect(Collectors.toList());
        if (!(isRelatedUnitExist(unit_ids) && isRelatedCreativeExist(creative_ids))){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<CreativeUnit> creativeUnits = new ArrayList<>();
        request.getCreativeUnitItemList().stream().map(e->creativeUnits.add(new CreativeUnit(e.getCreativeId(),e.getUnitId())));
        List<Long> ids = creativeUnitRepository.saveAll(creativeUnits).stream().map(CreativeUnit::getId).collect(Collectors.toList());

        return new CreativeUnitResponse(ids);
    }

    //根据unitId对应的所有推广单元是否存在
    private boolean isRelatedUnitExist(List<Long> unitIds){

        if (CollectionUtils.isEmpty(unitIds))
            return false;

        return unitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();//由于unitIds可能有重复的id，所有用set去重
    }

    private boolean isRelatedCreativeExist(List<Long> creativeIds){

        if (CollectionUtils.isEmpty(creativeIds))
            return false;

        return creativeRepository.findAllById(creativeIds).size() == new HashSet<>(creativeIds).size();
    }
}
