package com.yahaha.ad.service.impl;

import com.yahaha.ad.dao.CreativeRepository;
import com.yahaha.ad.entity.Creative;
import com.yahaha.ad.service.ICreativeService;
import com.yahaha.ad.vo.CreativeRequest;
import com.yahaha.ad.vo.CreativeResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther LeeMZ
 * @Date 2021/2/9
 **/
public class CreativeServiceImpl implements ICreativeService {

    @Autowired
    private CreativeRepository creativeRepository;

    @Override
    public CreativeResponse createCreative(CreativeRequest creativeRequest) {

        Creative creative = creativeRepository.save(creativeRequest.convertToEntity());

        return new CreativeResponse(creative.getId(),creative.getName());

    }
}
