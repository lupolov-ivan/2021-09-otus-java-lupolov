package ru.otus.mapper.metadata.clazz;

import ru.otus.crm.model.Manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ManagerEntityClassMetaData implements EntityClassMetaData<Manager> {

    @Override
    public String getName() {
        return Manager.class.getSimpleName();
    }

    @Override
    public Constructor<Manager> getConstructor() {
        try {
            return Manager.class.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new ClassMetaDataReadException(e);
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(Manager.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Entity must contain field marked '@Id'"));
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(Manager.class.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(Manager.class.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }
}
