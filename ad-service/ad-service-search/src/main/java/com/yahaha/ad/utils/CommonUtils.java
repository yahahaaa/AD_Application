package com.yahaha.ad.utils;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @Auther LeeMZ
 * @Date 2021/2/11
 **/
public class CommonUtils {

    public static <K,V> V getOrCreate(K key, Map<K,V> map, Supplier<V> factory){

        return map.computeIfAbsent(key,k->factory.get());
    }
}
