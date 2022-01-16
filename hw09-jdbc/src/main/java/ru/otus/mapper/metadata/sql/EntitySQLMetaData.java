package ru.otus.mapper.metadata.sql;

/**
 * Create SQL query
 */
public interface EntitySQLMetaData {

    String getSelectAllSql();

    String getSelectByIdSql();

    String getInsertSql();

    String getUpdateSql();
}
