package com.qi.market.common.db;

/**
 * Created by Qi on 2017/12/1.
 */

public class Pair<K, V> {

    private K key;
    private V value;

    public Pair(K key, V v) {
        this.key = key;
        this.value = value;
    }

    public Pair() {

    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public K getKey() {
        return key;
    }
}
