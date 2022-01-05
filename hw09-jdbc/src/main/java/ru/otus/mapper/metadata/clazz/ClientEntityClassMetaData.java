package ru.otus.mapper.metadata.clazz;

import ru.otus.crm.model.Client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ClientEntityClassMetaData implements EntityClassMetaData<Client> {

    @Override
    public String getName() {
        return Client.class.getSimpleName();
    }

    @Override
    public Constructor<Client> getConstructor() {
        try {
            return Client.class.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new ClassMetaDataReadException(e);
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(Client.class.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(Id.class))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Entity must contain field marked '@Id'"));
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(Client.class.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(Client.class.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }
}
