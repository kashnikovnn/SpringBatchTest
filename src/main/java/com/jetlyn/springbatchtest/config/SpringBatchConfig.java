package com.jetlyn.springbatchtest.config;

import com.jetlyn.springbatchtest.batch.readers.CountriesHttpItemReader;
import com.jetlyn.springbatchtest.dataloaders.DataLoader;
import com.jetlyn.springbatchtest.entities.CountryEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("dataLoaderHttpImpl")
    DataLoader dataLoader;


    @Bean
    public Job importCountriesJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }


    // Steps
    @Bean
    public Step stepLoadFromHttp(@Autowired
                                 @Qualifier("countriesHttpItemReader")
                                 ItemReader<CountryEntity> reader,
                                 @Autowired
                                 @Qualifier("countryEntityJdbcWriter")
                                 ItemWriter<CountryEntity> writer) {

        return stepBuilderFactory.get("stepLoadFromHttp")
                .<CountryEntity, CountryEntity> chunk(10)
                .reader(reader)
                .writer(writer)
                .build();
    }


    //Readers-writers
    @Bean
    public ItemReader<CountryEntity> countriesHttpItemReader() {
        return new CountriesHttpItemReader(dataLoader);
    }


    @Bean
    public JdbcBatchItemWriter<CountryEntity> countryEntityCopyJdbcWriter() {
        return new JdbcBatchItemWriterBuilder<CountryEntity>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<CountryEntity>())
                .sql("INSERT INTO COUNTRIES_COPY (CODE, NAME, PHONECODE) VALUES (:code, :name, :phoneCode)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<CountryEntity> countryEntityJdbcWriter() {
        return new JdbcBatchItemWriterBuilder<CountryEntity>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<CountryEntity>())
                .sql("INSERT INTO COUNTRIES (CODE, NAME, PHONECODE) VALUES (:code, :name, :phoneCode)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public ItemReader<CountryEntity> countryEntityItemReader(){
       return new JdbcCursorItemReaderBuilder<CountryEntity>()
               .fetchSize(10)
               .rowMapper( (resultSet,i) ->{
                   CountryEntity countryEntity = new CountryEntity();
                   countryEntity.setCode(resultSet.getString(1));
                   countryEntity.setName(resultSet.getString(2));
                   countryEntity.setPhoneCode(resultSet.getString(3));
                   return countryEntity;
               })
               .dataSource(dataSource)
               .build();

    }



}
