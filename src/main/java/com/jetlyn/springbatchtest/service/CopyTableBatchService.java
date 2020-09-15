package com.jetlyn.springbatchtest.service;

import com.jetlyn.springbatchtest.batch.readers.UniversalItemReaderBuilder;
import com.jetlyn.springbatchtest.batch.readers.UniversalItemWriterBuilder;
import com.jetlyn.springbatchtest.tools.SqlQueryConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CopyTableBatchService implements CopyTableService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    JobLauncher jobLauncher;

    public void copyTableService(String srcTableName, String dstTableName) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {


        LinkedHashMap queryRes = jdbcTemplate.queryForObject("SELECT * FROM " + srcTableName + " LIMIT 1",
                (resultSet, rownum) -> {
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    LinkedHashMap<String, Object> rowMap = new LinkedHashMap<>();
                    for (int i = 1; i < rsmd.getColumnCount()+1; i++) {
                        rowMap.put(rsmd.getColumnName(i), resultSet.getObject(i));
                    }
                    return rowMap;
                });

        jdbcTemplate.execute(SqlQueryConstructor.createCopyTableQuery(srcTableName,dstTableName));

        ItemReader<Map<String, Object>> universalItemReader =
                UniversalItemReaderBuilder.buildUniversalItemReader(srcTableName, dataSource);

        ItemWriter<Map<String, Object>> universalJdbcWriter =
                UniversalItemWriterBuilder.buildUniversalJdbcWriter(dstTableName, dataSource, queryRes);

        Step universalTableCopyStep = createUniversalTableCopyStep(universalItemReader,universalJdbcWriter);

        Job universalCopyTableJob = createUniversalCopyTableJob(universalTableCopyStep);

        JobParameters jobParameters = new JobParameters();

        jobLauncher.run(universalCopyTableJob, jobParameters);

    }

    private Step createUniversalTableCopyStep(ItemReader<Map<String, Object>> reader,
                                              ItemWriter<Map<String, Object>> writer) {

        return stepBuilderFactory.get("universalCopyStep")
                .<Map<String, Object>, Map<String, Object>>chunk(10)
                .reader(reader)
                .writer(writer)
                .build();


    }

    public Job createUniversalCopyTableJob(Step universalTableCopy) {
        return jobBuilderFactory.get("universalCopyTableJob")
                .start(universalTableCopy)
                .build();
    }
}
