package ru.otus.crm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.cachehw.wrapper.CacheWrapper;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Manager;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class DbServiceManagerImpl implements DBServiceManager {

    private final TransactionRunner transactionRunner;
    private final DataTemplate<Manager> dataTemplate;
    private final CacheWrapper<String, Manager> cacheWrapper;

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = dataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                return createdManager;
            }
            dataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        return cacheWrapper.doWithCache(() -> transactionRunner.doInTransaction(connection -> {
            var managerOptional = dataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            return managerOptional;
        }), String.valueOf(no));
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = dataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            return managerList;
       });
    }
}
