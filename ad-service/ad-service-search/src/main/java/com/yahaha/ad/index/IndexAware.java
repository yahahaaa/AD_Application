package com.yahaha.ad.index;

/**
 * @Auther LeeMZ
 * @Date 2021/2/10
 **/

public interface IndexAware<K,V> {

    V get(K key);

    void add(K key,V value);

    void update(K key, V value);

    void delete(K key, V value);
}
