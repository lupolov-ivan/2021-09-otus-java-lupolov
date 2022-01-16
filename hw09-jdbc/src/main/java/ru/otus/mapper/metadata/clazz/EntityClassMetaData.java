package ru.otus.mapper.metadata.clazz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Parses an object into its component parts
 */
public interface EntityClassMetaData<T> {

    String getName();

    Constructor<T> getConstructor();

    Field getIdField();

    List<Field> getAllFields();

    List<Field> getFieldsWithoutId();
}
