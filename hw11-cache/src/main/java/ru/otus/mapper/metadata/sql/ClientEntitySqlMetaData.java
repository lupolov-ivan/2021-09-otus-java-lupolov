package ru.otus.mapper.metadata.sql;

public class ClientEntitySqlMetaData implements EntitySQLMetaData {

    @Override
    public String getSelectAllSql() {
        return "SELECT id, name FROM client";
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT id, name FROM client WHERE id = ?";
    }

    @Override
    public String getInsertSql() {
        return "INSERT INTO client(name) VALUES(?)";
    }

    @Override
    public String getUpdateSql() {
        return "UPDATE client SET name = ? WHERE id = ?";
    }
}
