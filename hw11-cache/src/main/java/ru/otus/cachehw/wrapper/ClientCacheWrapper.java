package ru.otus.cachehw.wrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.cachehw.HwCache;
import ru.otus.crm.model.Client;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class ClientCacheWrapper extends CacheWrapper<String, Client> {

    private final HwCache<String, Client> cache;

    @Override
    public Optional<Client> doWithCache(Supplier<Optional<Client>> dbValueSupplier, String key) {
        return super.doWithCache(dbValueSupplier, cache, key);
    }
}
