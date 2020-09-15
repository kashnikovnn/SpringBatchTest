package com.jetlyn.springbatchtest.tools;

import java.util.Map;

public class SqlQueryConstructor {

    public static String createInsertQuery(String tableName, Map<String, Object> paramsMap) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO ");
        stringBuilder.append(tableName);
        stringBuilder.append(" (");

        StringBuilder paramsSb = new StringBuilder();
        paramsMap.keySet().forEach(columnName -> {
            stringBuilder.append(columnName + ", ");
            paramsSb.append("?, ");
        });
        paramsSb.delete(paramsSb.length() - 2, paramsSb.length());
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append(") VALUES (");
        stringBuilder.append(paramsSb);
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    public static String createCopyTableQuery(String srcTableName, String dstTableName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE ");
        stringBuilder.append(dstTableName);
        stringBuilder.append(" AS SELECT * FROM ");
        stringBuilder.append(srcTableName);
        stringBuilder.append(" WHERE 1=0");
        return stringBuilder.toString();
    }
}
