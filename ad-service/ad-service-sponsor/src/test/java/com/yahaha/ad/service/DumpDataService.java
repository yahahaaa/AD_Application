package com.yahaha.ad.service;

import com.alibaba.fastjson.JSON;
import com.yahaha.ad.AdSponsorApplication;
import com.yahaha.ad.constant.CommonStatus;
import com.yahaha.ad.dao.AdPlanRepository;
import com.yahaha.ad.dao.AdUnitRepository;
import com.yahaha.ad.dao.CreativeRepository;
import com.yahaha.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.yahaha.ad.dao.unit_condition.AdUnitItRepository;
import com.yahaha.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.yahaha.ad.dao.unit_condition.CreativeUnitRepository;
import com.yahaha.ad.dump.table.*;
import com.yahaha.ad.entity.AdPlan;
import com.yahaha.ad.entity.AdUnit;
import com.yahaha.ad.entity.Creative;
import com.yahaha.ad.entity.unit_condition.AdUnitDistrict;
import com.yahaha.ad.entity.unit_condition.AdUnitIt;
import com.yahaha.ad.entity.unit_condition.AdUnitKeyword;
import com.yahaha.ad.entity.unit_condition.CreativeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther LeeMZ
 * @Date 2021/2/12
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SuppressWarnings("all")
@SpringBootTest(classes = AdSponsorApplication.class,webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DumpDataService {

    @Autowired
    private AdPlanRepository planRepository;
    @Autowired
    private AdUnitRepository unitRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitDistrictRepository districtRepository;
    @Autowired
    private AdUnitItRepository itRepository;
    @Autowired
    private AdUnitKeywordRepository unitKeywordRepository;

    private void dumpAdPlanTable(String fileName){

        List<AdPlan> adPlans = planRepository.findAllByPlanStatus(CommonStatus.VALID.getCode());

        if (CollectionUtils.isEmpty(adPlans))
            return;

        //将entity的值传递到table中
        List<AdPlanTable> planTables = new ArrayList<>();
        adPlans.forEach(p -> planTables.add(new AdPlanTable(p.getId(),p.getUserId(),p.getPlanStatus(),p.getStartDate(),p.getEndDate())));

        writeToLocal(fileName,planTables,"dumpAdPlanTable");
    }

    private void dumpAdUnitTable(String fileName){

        List<AdUnit> adUnits = unitRepository.findAllByUnitStatus(CommonStatus.VALID.getCode());

        if (CollectionUtils.isEmpty(adUnits))
            return;

        List<AdUnitTable> adUnitTables = new ArrayList<>();
        adUnits.forEach(u -> adUnitTables.add(new AdUnitTable(u.getId(),u.getUnitStatus(),u.getPositionType(),u.getPlanId())));

        writeToLocal(fileName,adUnitTables,"dumpAdUnitTable");
    }

    private void dumpAdCreativeTable(String fileName){

        List<Creative> creatives = creativeRepository.findAll();
        if (CollectionUtils.isEmpty(creatives)){
            return;
        }

        List<AdCreativeTable> creativeTables = new ArrayList<>();
        creatives.forEach(c -> creativeTables.add(
                new AdCreativeTable(
                        c.getId(),
                        c.getName(),
                        c.getType(),
                        c.getMaterialType(),
                        c.getHeight(),
                        c.getWidth(),
                        c.getAuditStatus(),
                        c.getUrl())));

        writeToLocal(fileName,creativeTables,"dumpAdCreativeTable");
    }
    
    private void dumpAdCreativeUnitTable(String fileName){

        List<CreativeUnit> creativeUnits = creativeUnitRepository.findAll();
        if (CollectionUtils.isEmpty(creativeUnits))
            return;

        List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
        creativeUnits.forEach(c -> creativeUnitTables.add(new AdCreativeUnitTable(c.getId(),c.getUnitId())));

        writeToLocal(fileName,creativeUnitTables,"dumpAdCreativeUnitTable");

    }

    private void dumpAdUnitDistrictTable(String fileName){
        List<AdUnitDistrict> districts = districtRepository.findAll();
        if (CollectionUtils.isEmpty(districts))
            return;

        List<AdUnitDistrictTable> districtTables = new ArrayList<>();
        districts.forEach(d -> districtTables.add(new AdUnitDistrictTable(d.getUnitId(),d.getProvince(),d.getCity())));

        writeToLocal(fileName,districtTables,"dumpAdUnitDistrictTable");
    }

    private void dumpAdUnitKeywordTable(String fileName){
        List<AdUnitKeyword> unitKeywords = unitKeywordRepository.findAll();
        if (CollectionUtils.isEmpty(unitKeywords))
            return;

        List<AdUnitKeywordTable> unitKeywordTables = new ArrayList<>();
        unitKeywords.forEach(k -> unitKeywordTables.add(new AdUnitKeywordTable(k.getUnitId(),k.getKeyword())));

        writeToLocal(fileName,unitKeywordTables,"dumpAdUnitKeywordTable");

    }

    private void dumpAdUnitItTable(String fileName){
        List<AdUnitIt> its = itRepository.findAll();
        if (CollectionUtils.isEmpty(its))
            return;

        List<AdUnitItTable> unitItTables = new ArrayList<>();
        its.forEach(i -> unitItTables.add(new AdUnitItTable(i.getUnitId(),i.getItTag())));

        writeToLocal(fileName,unitItTables,"dumpAdUnitItTable");
    }
    
    private <T> void writeToLocal(String fileName,List<T> t,String methodName){
        Path path = Paths.get(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            for (T t1 : t) {
                writer.write(JSON.toJSONString(t1));
                writer.newLine();
            }
        }catch (IOException ex){
            log.error(methodName + " error");
        }
    }
}
