package com.jetlyn.springbatchtest.batch.readers;

import com.jetlyn.springbatchtest.tools.SqlQueryConstructor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UniversalItemWriterBuilder {


    public static JdbcBatchItemWriter<Map<String,Object>> buildUniversalJdbcWriter(String outTableName ,
                                                                                   DataSource dataSource,
                                                                                   LinkedHashMap<String,Object> paramsMap) {

        return new JdbcBatchItemWriterBuilder<Map<String,Object>>()
                .itemPreparedStatementSetter((item,preparedStatement)->{
                    AtomicInteger i = new AtomicInteger();
                    i.set(1);
                    item.entrySet().forEach(entry->{
                        try {
                            preparedStatement.setObject(i.getAndIncrement(), entry.getValue());
                        } catch (SQLException throwables) {
                            throw new RuntimeException(throwables);
                        }
                    });
                })
                .sql(SqlQueryConstructor.createInsertQuery(outTableName,paramsMap))
                .dataSource(dataSource)
                .build();
    }

}
