package com.jetlyn.springbatchtest.batch.readers;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

public class UniversalItemReaderBuilder {


    public static  ItemReader<Map<String,Object>> buildUniversalItemReader(String tableName, DataSource dataSource){

        return new JdbcCursorItemReaderBuilder<Map<String,Object>>()
                .name("universalItemReader")
                .sql("SELECT * FROM " + tableName)
                .fetchSize(10)
                .rowMapper( (resultSet,rownum) ->{
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    Map<String,Object> rowMap = new LinkedHashMap<>();
                    for (int i=1; i< rsmd.getColumnCount()+1; i++){
                        rowMap.put(rsmd.getColumnName(i),resultSet.getObject(i));
                    }
                    return rowMap;
                })
                .dataSource(dataSource)
                .build();

    }
}
