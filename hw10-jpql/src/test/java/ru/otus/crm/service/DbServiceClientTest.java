package ru.otus.crm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
class DbServiceClientTest extends AbstractHibernateTest {

    @Test
    @DisplayName(" корректно сохранять, изменять и загружать клиента")
    void shouldCorrectSaveClient() {
        //given
        var address = new Address(null,"Deribasovskaya street");
        var phone = new Phone(null, "11-111-11");
        var phone2 = new Phone(null, "22-222-2222");
        var client = new Client(null, "Vasya", address, (List.of(phone, phone2)));

        //when
        var savedClient = dbServiceClient.saveClient(client);
        System.out.println(savedClient);

        //then
        var loadedSavedClient = dbServiceClient.getClient(savedClient.getId());
        assertThat(loadedSavedClient).isPresent();
        assertThat(loadedSavedClient.get()).usingRecursiveComparison().isEqualTo(savedClient);

        //when
        var savedClientUpdated = loadedSavedClient.get().clone();
        savedClientUpdated.setName("updatedName");
        dbServiceClient.saveClient(savedClientUpdated);

        //then
        var loadedClient = dbServiceClient.getClient(savedClientUpdated.getId());
        assertThat(loadedClient).isPresent();
        assertThat(loadedClient.get()).usingRecursiveComparison().isEqualTo(savedClientUpdated);
        System.out.println(loadedClient);

        //when
        var clientList = dbServiceClient.findAll();

        //then
        assertThat(clientList.size()).isEqualTo(1);
        assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(loadedClient.get());
    }
}