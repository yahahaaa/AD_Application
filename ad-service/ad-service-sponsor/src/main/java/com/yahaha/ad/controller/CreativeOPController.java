package com.yahaha.ad.controller;

import com.alibaba.fastjson.JSON;
import com.yahaha.ad.exception.AdException;
import com.yahaha.ad.service.ICreativeService;
import com.yahaha.ad.vo.CreativeRequest;
import com.yahaha.ad.vo.CreativeResponse;
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
public class CreativeOPController {

    @Autowired
    private ICreativeService creativeService;

    @PostMapping("/create/creative")
    public CreativeResponse createCreative(@RequestBody CreativeRequest request) throws AdException {

        log.info("【创建创意单元】--> {}", JSON.toJSONString(request));

        return creativeService.createCreative(request);
    }

}
