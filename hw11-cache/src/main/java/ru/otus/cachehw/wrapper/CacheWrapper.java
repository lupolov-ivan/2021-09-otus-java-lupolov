package ru.otus.cachehw.wrapper;

import lombok.extern.slf4j.Slf4j;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.listeners.Actions;
import ru.otus.cachehw.listeners.HwListener;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public abstract class CacheWrapper<K,V>  {

    protected Optional<V> doWithCache(Supplier<Optional<V>> dbValueSupplier, HwCache<K,V> cache, K key) {

        var getListener = new HwListener<K,V>() {
            @Override
            public void notify(K key, V value, String action) {
                if (Actions.GET.equals(action)) {
                    log.info("key:{}, value:{}, action: {}", key, value, action);
                }
            }
        };
        cache.addListener(getListener);

        Optional<V> optionalValueFromCache = Optional.ofNullable(cache.get(key));
        cache.removeListener(getListener);

        if (optionalValueFromCache.isPresent()) {
            return optionalValueFromCache;
        }

        Optional<V> optionalValueFromDb = dbValueSupplier.get();

        if (optionalValueFromDb.isPresent()) {
            V value = optionalValueFromDb.get();
            var putListener = new HwListener<K,V>() {
                @Override
                public void notify(K key, V value, String action) {
                    if (Actions.PUT.equals(action)) {
                        log.info("key:{}, value:{}, action: {}", key, value, action);
                    }
                }
            };
            cache.addListener(putListener);
            cache.put(key, value);
            cache.removeListener(putListener);
        }

        return optionalValueFromDb;
    }

    public abstract Optional<V> doWithCache(Supplier<Optional<V>> dbValueSupplier, K key);
}
