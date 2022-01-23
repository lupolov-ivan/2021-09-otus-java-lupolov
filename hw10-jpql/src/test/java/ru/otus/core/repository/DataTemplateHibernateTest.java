package ru.otus.core.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DataTemplateHibernateTest extends AbstractHibernateTest {

    @Test
    @DisplayName(" корректно сохраняет, изменяет и загружает клиента по заданному id")
    void shouldSaveAndFindCorrectClientById() {

        var address = new Address(null,"Deribasovskaya street");
        var phone = new Phone(null, "11-111-11");
        var phone2 = new Phone(null, "22-222-2222");
        var client = new Client(null, "Vasya", address, (List.of(phone, phone2)));

        var savedClient = transactionManager.doInTransaction(session -> {
            clientTemplate.insert(session, client);
            return client;
        });

        assertThat(savedClient.getId()).isNotNull();
        assertThat(savedClient.getName()).isEqualTo(client.getName());
        assertThat(savedClient.getAddress()).isEqualTo(address);
        assertThat(savedClient.getPhones()).hasSize(2)
                .hasSameElementsAs(List.of(phone, phone2));


       transactionManager.doInReadOnlyTransaction(session -> {
           Optional<Client> optionalClient = clientTemplate.findById(session, savedClient.getId());

            assertThat(optionalClient).isPresent().get().usingRecursiveComparison().isEqualTo(savedClient);

            return optionalClient;
        });

        var updatedClient = savedClient.clone();
        updatedClient.setName("updatedName");
        transactionManager.doInTransaction(session -> {
            clientTemplate.update(session, updatedClient);
            return null;
        });

        transactionManager.doInReadOnlyTransaction(session -> {
            Optional<Client> optionalClient = clientTemplate.findById(session, updatedClient.getId());

            assertThat(optionalClient).isPresent().get().usingRecursiveComparison().isEqualTo(updatedClient);

            return optionalClient;
        });


        transactionManager.doInReadOnlyTransaction(session -> {
            List<Client> clientList = clientTemplate.findAll(session);

            assertThat(clientList.size()).isEqualTo(1);
            assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(updatedClient);

            return clientList;
        });


        transactionManager.doInReadOnlyTransaction(session -> {

            List<Client> clientList = clientTemplate.findByEntityField(session, "name", "updatedName");

            assertThat(clientList.size()).isEqualTo(1);
            assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(updatedClient);

            return clientList;
        });
    }

}
