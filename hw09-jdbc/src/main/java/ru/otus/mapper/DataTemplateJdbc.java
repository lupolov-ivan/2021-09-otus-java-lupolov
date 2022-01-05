package ru.otus.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.mapper.metadata.clazz.ClassMetaDataReadException;
import ru.otus.mapper.metadata.clazz.EntityClassMetaData;
import ru.otus.mapper.metadata.sql.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

/**
 * Save object to database and read from
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor,
                            EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return mapEntityObject(rs);
                }
                return null;
            }
            catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), emptyList(), rs -> {
            var entities = new ArrayList<T>();
            try {
                while (rs.next()) {
                    entities.add(mapEntityObject(rs));
                }
                return entities;
            }
            catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T entity) {

        List<Object> sqlInsertArgs = extractSqlArgs(entityClassMetaData.getFieldsWithoutId(), entity);
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), sqlInsertArgs);
    }

    @Override
    public void update(Connection connection, T entity) {

        List<Field> fields = new ArrayList<>(entityClassMetaData.getFieldsWithoutId());
        fields.add(entityClassMetaData.getIdField());
        List<Object> sqlUpdateArgs = extractSqlArgs(fields, entity);

        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), sqlUpdateArgs);
    }

    private T mapEntityObject(ResultSet rs) throws SQLException {

        try {
            T entity = entityClassMetaData.getConstructor().newInstance();

            for (Field field : entityClassMetaData.getAllFields()) {
                field.setAccessible(true);
                field.set(entity, rs.getObject(field.getName()));
            }

            return entity;
        }
        catch (ReflectiveOperationException e) {
            throw new ClassMetaDataReadException(e);
        }
    }

    private List<Object> extractSqlArgs(List<Field> fields, Object entity) {

        try {
            var args = new ArrayList<>();

            for (Field field : fields) {
                field.setAccessible(true);
                args.add(field.get(entity));
            }

            return args;
        }
        catch (ReflectiveOperationException e) {
            throw new ClassMetaDataReadException(e);
        }
    }
}
