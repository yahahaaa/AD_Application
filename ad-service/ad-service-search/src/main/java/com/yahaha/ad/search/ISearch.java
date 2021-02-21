package com.yahaha.ad.search;

import com.yahaha.ad.search.vo.SearchRequest;
import com.yahaha.ad.search.vo.SearchResponse;

/**
 * @Auther LeeMZ
 * @Date 2021/2/20
 **/
public interface ISearch {
    SearchResponse fetchAds(SearchRequest request);
}
