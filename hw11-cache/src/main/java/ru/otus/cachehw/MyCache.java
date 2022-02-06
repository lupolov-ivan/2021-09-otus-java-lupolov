package ru.otus.cachehw;


import lombok.extern.slf4j.Slf4j;
import ru.otus.cachehw.listeners.Actions;
import ru.otus.cachehw.listeners.HwListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Slf4j
public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K,V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        notifyAll(key, value, Actions.PUT);
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        V removed = cache.remove(key);
        notifyAll(key, removed, Actions.REMOVE);
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        notifyAll(key, value, Actions.GET);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyAll(K key, V value, String action) {
        for (HwListener<K, V> listener : listeners) {
            try {
                listener.notify(key, value, action);
            }
            catch (Exception ex) {
                log.error("Unexpected error during notify listener", ex);
            }
        }
    }
}
