package ru.otus.mapper.metadata.sql;

public class ManagerEntitySqlMetaData implements EntitySQLMetaData {

    @Override
    public String getSelectAllSql() {
        return "SELECT no, label, param1 FROM manager";
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT no, label, param1 FROM manager WHERE no = ?";
    }

    @Override
    public String getInsertSql() {
        return "INSERT INTO manager(label, param1) VALUES(?,?)";
    }

    @Override
    public String getUpdateSql() {
        return "UPDATE manager SET label = ?, param1 = ? WHERE no = ?";
    }
}
