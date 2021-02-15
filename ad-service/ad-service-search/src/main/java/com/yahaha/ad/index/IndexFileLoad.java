package com.yahaha.ad.index;

import com.alibaba.fastjson.JSON;
import com.yahaha.ad.dump.DConstant;
import com.yahaha.ad.dump.table.*;
import com.yahaha.ad.handler.AdLevelDataHandler;
import com.yahaha.ad.mysql.constant.OpType;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther LeeMZ
 * @Date 2021/2/13
 **/
@Component
@DependsOn("dataTable")
public class IndexFileLoad {

    @PostConstruct
    public void init() {
        //第二层级
        //读取文件数据，返回List<String>格式，一个对象的内容会存储一行
        List<String> adPlanStrings = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_PLAN));
        //将上面获取到的string类型的list集合转为对象形式作为handler的入参，将索引存入缓存中
        adPlanStrings.stream().forEach(p -> AdLevelDataHandler.handleLevel2(JSON.parseObject(p, AdPlanTable.class), OpType.ADD));

        //第二层级
        List<String> adCreativeStrings = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE));
        adCreativeStrings.stream().forEach(c -> AdLevelDataHandler.handleLevel2(JSON.parseObject(c, AdCreativeTable.class), OpType.ADD));

        //第三层级
        List<String> adUnitStrings = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT));
        adUnitStrings.stream().forEach(u -> AdLevelDataHandler.handleLevel3(JSON.parseObject(u, AdUnitTable.class), OpType.ADD));

        //第三层级
        List<String> adCreativeUnitStrings = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE_UNIT));
        adCreativeUnitStrings.stream().forEach(u -> AdLevelDataHandler.handleLevel3(JSON.parseObject(u, AdCreativeUnitTable.class), OpType.ADD));

        //第四层级
        List<String> adUnitKeywordStrings = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_KEYWORD));
        adUnitKeywordStrings.stream().forEach(k -> AdLevelDataHandler.handleLevel4(JSON.parseObject(k, AdUnitKeywordTable.class), OpType.ADD));

        //第四层级
        List<String> adUnitItStrings = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_IT));
        adUnitItStrings.stream().forEach(i -> AdLevelDataHandler.handleLevel4(JSON.parseObject(i, AdUnitItTable.class), OpType.ADD));

        //第四层级
        List<String> adUnitDistrictStrings = loadDumpData(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_DISTRICT));
        adUnitDistrictStrings.stream().forEach(d -> AdLevelDataHandler.handleLevel4(JSON.parseObject(d,AdUnitDistrictTable.class),OpType.ADD));
    }

    private List<String> loadDumpData(String fileName) {

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            return br.lines().collect(Collectors.toList());
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
