package org.luban.common;

import java.util.LinkedHashMap;

/**
 * User: krisjin
 * Date: 2016/3/3
 */
public class LRUMap<K, V> extends LinkedHashMap<K, V> {
    private int maxCapacity;

    public LRUMap(int maxCapacity) {
        super(16, 0.75f, true);
        this.maxCapacity = maxCapacity;
    }

    public int size() {
        return super.size();
    }

    public V put(K k, V v) {
        return super.put(k, v);
    }

    public V get(Object k) {
        return super.get(k);
    }

    public V remove(Object k) {
        return super.remove(k);
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return this.size() > this.maxCapacity;
    }

}
