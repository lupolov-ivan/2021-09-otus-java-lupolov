package ru.otus.cachehw.wrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.cachehw.HwCache;
import ru.otus.crm.model.Manager;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class ManagerCacheWrapper extends CacheWrapper<String, Manager> {

    private final HwCache<String, Manager> cache;

    @Override
    public Optional<Manager> doWithCache(Supplier<Optional<Manager>> dbValueSupplier, String key) {
        return super.doWithCache(dbValueSupplier, cache, key);
    }
}
