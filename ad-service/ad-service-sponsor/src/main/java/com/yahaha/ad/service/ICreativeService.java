package com.yahaha.ad.service;

import com.yahaha.ad.entity.Creative;
import com.yahaha.ad.vo.CreativeRequest;
import com.yahaha.ad.vo.CreativeResponse;

public interface ICreativeService {

    CreativeResponse createCreative(CreativeRequest request);
}
