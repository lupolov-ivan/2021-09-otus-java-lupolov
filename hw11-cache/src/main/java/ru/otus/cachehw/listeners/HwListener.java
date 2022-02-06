package ru.otus.cachehw.listeners;


public interface HwListener<K, V> {
    void notify(K key, V value, String action);
}
