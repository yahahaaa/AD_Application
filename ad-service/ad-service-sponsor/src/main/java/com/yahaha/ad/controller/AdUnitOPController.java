package com.yahaha.ad.controller;

import com.alibaba.fastjson.JSON;
import com.yahaha.ad.exception.AdException;
import com.yahaha.ad.service.IAdUnitService;
import com.yahaha.ad.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther LeeMZ
 * @Date 2021/2/9
 **/
@Slf4j
@RestController
public class AdUnitOPController {

    @Autowired
    private IAdUnitService adUnitService;


    @PostMapping("/create/adUnit")
    public AdUnitResponse createUnit(@RequestBody AdUnitRequest request) throws AdException {

        log.info("【创建推广单元】--> {}", JSON.toJSONString(request));

        return adUnitService.createUnit(request);
    }

    @PostMapping("/create/unitKeyword")
    public AdUnitKeywordResponse createUnitKeyword(@RequestBody AdUnitKeywordRequest request) throws AdException {

        log.info("【创建推广单元关键词限制单元】--> {}",JSON.toJSONString(request));

        return adUnitService.createUnitKeyword(request);
    }

    @PostMapping("/create/unitIt")
    public AdUnitItResponse createUnitIt(@RequestBody AdUnitItRequest request) throws AdException{

        log.info("【创建推广单元兴趣限制单元】--> {}",JSON.toJSONString(request));

        return adUnitService.createUnitIt(request);
    }

    @PostMapping("/create/unitDistrict")
    public AdUnitDistrictResponse createUnitDistrict(@RequestBody AdUnitDistrictRequest request) throws AdException{

        log.info("【创建推广单元地域限制单元】--> {}",JSON.toJSONString(request));

        return adUnitService.createUnitDistrict(request);
    }

    @PostMapping("/create/creativeUnit")
    public CreativeUnitResponse createCreativeUnit(@RequestBody CreativeUnitRequest request) throws AdException{

        log.info("【创建推广单元与创意单元的关联】--> {}",JSON.toJSONString(request));

        return adUnitService.createCreativeUnit(request);
    }
}
